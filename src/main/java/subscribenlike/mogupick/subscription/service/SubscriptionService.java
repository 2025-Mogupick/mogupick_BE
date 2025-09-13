package subscribenlike.mogupick.subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.billing.common.exception.BillingErrorCode;
import subscribenlike.mogupick.billing.common.exception.BillingException;
import subscribenlike.mogupick.billing.domain.PaymentState;
import subscribenlike.mogupick.billing.domain.PaymentStatus;
import subscribenlike.mogupick.billing.repository.PaymentStateRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.order.domain.Order;
import subscribenlike.mogupick.order.domain.OrderItem;
import subscribenlike.mogupick.order.repository.OrderRepository;
import subscribenlike.mogupick.order.service.OrderService;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductMediaRepository;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.subscription.common.exception.SubscriptionErrorCode;
import subscribenlike.mogupick.subscription.common.exception.SubscriptionException;
import subscribenlike.mogupick.subscription.domain.Subscription;
import subscribenlike.mogupick.subscription.domain.SubscriptionStatus;
import subscribenlike.mogupick.subscription.dto.*;
import subscribenlike.mogupick.subscription.repository.SubscriptionRepository;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;
import subscribenlike.mogupick.subscriptionOption.repository.SubscriptionOptionRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionOptionRepository subscriptionOptionRepository;
    private final MemberRepository memberRepository;
    private final PaymentStateRepository paymentStateRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getList(Long memberId, SubscriptionStatus status) {
        List<Subscription> list = (status == null)
                ? subscriptionRepository.findByMemberId(memberId)
                : subscriptionRepository.findByMemberIdAndStatus(memberId, status);

        List<Long> productIds = list.stream()
                .map(s -> s.getProduct().getId())
                .distinct()
                .toList();

        Map<Long, String> imageMap = findFirstImageUrlMap(productIds);

        return list.stream()
                .map(sub -> SubscriptionResponse.from(sub, imageMap.get(sub.getProduct().getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse getDetail(Long subscriptionId) {
        Subscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND));
        String imageUrl = findFirstImageUrl(sub.getProduct().getId());
        return SubscriptionResponse.from(sub, imageUrl);
    }

    @Transactional(readOnly = true)
    public SubscriptionCalendarResponse getCalendar(Long memberId, YearMonth yearMonth) {
        List<Subscription> subs = subscriptionRepository.findByMemberId(memberId);
        Map<LocalDate, List<CalendarSubscriptionResponse>> dateMap = new HashMap<>();

        for (Subscription s : subs) {
            if (s.getStatus().isActive()) {
                List<LocalDate> dates = getSchedulesInMonth(s, yearMonth);
                for (LocalDate date : dates) {
                    dateMap.computeIfAbsent(date, k -> new ArrayList<>())
                            .add(new CalendarSubscriptionResponse(
                                    s.getId(),
                                    s.getProduct().getName(),
                                    s.getProduct().getPrice()
                            ));
                }
            }
        }

        int totalAmount = dateMap.values().stream()
                .flatMap(List::stream)
                .mapToInt(CalendarSubscriptionResponse::amount)
                .sum();

        List<CalendarDayResponse> days = dateMap.entrySet().stream()
                .map(e -> new CalendarDayResponse(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(CalendarDayResponse::date))
                .toList();

        return new SubscriptionCalendarResponse(totalAmount, days);
    }

    @Transactional
    public void createFromPayment(String paymentKey, String orderId) {
        if (subscriptionRepository.existsByPaymentKey(paymentKey)) {
            throw new SubscriptionException(SubscriptionErrorCode.PAYMENT_KEY_DUPLICATE);
        }
        PaymentState state = paymentStateRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BillingException(BillingErrorCode.PAYMENT_STATE_NOT_FOUND));

        if (state.getStatus() != PaymentStatus.APPROVED) {
            throw new BillingException(BillingErrorCode.PAYMENT_NOT_APPROVED);
        }
        Order order = orderRepository.findByOrderIdOrThrow(orderId);
        Member member = order.getMember();

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new SubscriptionException(SubscriptionErrorCode.PRODUCT_NOT_FOUND));

            SubscriptionOption option = item.getOption();
            LocalDate firstDeliveryDate = item.getFirstDeliveryDate();

            Subscription subscription = Subscription.create(
                    member, product, option, firstDeliveryDate, paymentKey
            );
            subscriptionRepository.save(subscription);
        }
        orderService.markPaid(orderId);
    }

    @Transactional
    public SubscriptionResponse cancel(Long subscriptionId) {
        Subscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND));

        if (sub.getStatus().isTerminated()) {
            throw new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_ALREADY_CANCELLED);
        }

        sub.cancel();
        String imageUrl = findFirstImageUrl(sub.getProduct().getId());
        return SubscriptionResponse.from(sub, imageUrl);
    }

    @Transactional
    public SubscriptionResponse changeOption(Long subscriptionId, SubscriptionOptionUpdateRequest request) {
        Subscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND));

        SubscriptionOption newOption = subscriptionOptionRepository.findById(request.subscriptionOptionId())
                .orElseThrow(() -> new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_OPTION_NOT_FOUND));

        if (request.firstDeliveryDate() == null) {
            throw new SubscriptionException(SubscriptionErrorCode.FIRST_DELIVERY_DATE_REQUIRED);
        }

        sub.changeOption(newOption, request.firstDeliveryDate());
        String imageUrl = findFirstImageUrl(sub.getProduct().getId());
        return SubscriptionResponse.from(sub, imageUrl);
    }

    private String findFirstImageUrl(Long productId) {
        if (productId == null) return null;
        List<Object[]> rows = productMediaRepository.findFirstImageUrlsByProductIds(List.of(productId));
        return (rows == null || rows.isEmpty()) ? null : (String) rows.get(0)[1];
    }

    private Map<Long, String> findFirstImageUrlMap(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) return Collections.emptyMap();

        List<Object[]> rows = productMediaRepository.findFirstImageUrlsByProductIds(productIds);
        return rows.stream().collect(Collectors.toMap(
                r -> ((Number) r[0]).longValue(),
                r -> (String) r[1],
                (a, b) -> a
        ));
    }

    private List<LocalDate> getSchedulesInMonth(Subscription subscription, YearMonth yearMonth) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        LocalDate current = subscription.getNextBillingDate();

        if (current != null && !current.isBefore(start) && !current.isAfter(end)) {
            dates.add(current);
        }

        return dates;
    }
}
