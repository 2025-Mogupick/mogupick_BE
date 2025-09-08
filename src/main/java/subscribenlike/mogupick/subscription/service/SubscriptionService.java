package subscribenlike.mogupick.subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.billing.common.exception.BillingErrorCode;
import subscribenlike.mogupick.billing.common.exception.BillingException;
import subscribenlike.mogupick.billing.domain.PaymentState;
import subscribenlike.mogupick.billing.domain.PaymentStatus;
import subscribenlike.mogupick.billing.repository.PaymentStateRepository;
import subscribenlike.mogupick.cart.common.exception.CartErrorCode;
import subscribenlike.mogupick.cart.common.exception.CartException;
import subscribenlike.mogupick.cart.domain.Cart;
import subscribenlike.mogupick.cart.domain.CartItem;
import subscribenlike.mogupick.cart.repository.CartRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
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

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionOptionRepository subscriptionOptionRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PaymentStateRepository paymentStateRepository;

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getList(Long memberId, SubscriptionStatus status) {
        List<Subscription> list = (status == null)
                ? subscriptionRepository.findByMemberId(memberId)
                : subscriptionRepository.findByMemberIdAndStatus(memberId, status);
        return list.stream().map(SubscriptionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse getDetail(Long subscriptionId) {
        Subscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND));
        return SubscriptionResponse.from(sub);
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
    public void createFromPayment(Long memberId, String paymentKey, String orderId) {
        if (subscriptionRepository.existsByPaymentKey(paymentKey)) {
            throw new SubscriptionException(SubscriptionErrorCode.PAYMENT_KEY_DUPLICATE);
        }
        PaymentState state = paymentStateRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BillingException(BillingErrorCode.PAYMENT_STATE_NOT_FOUND));

        if (state.getStatus() != PaymentStatus.APPROVED) {
            throw new BillingException(BillingErrorCode.PAYMENT_NOT_APPROVED);
        }
        Member member = memberRepository.findOrThrow(memberId);

        Cart cart = cartRepository.findOrThrow(member);

        if (cart.getItems().isEmpty()) {
            throw new CartException(CartErrorCode.CART_EMPTY);
        }

        for (CartItem item : cart.getItems()) {
            Subscription subscription = Subscription.create(
                    member,
                    item.getProduct(),
                    item.getOption(),
                    item.getFirstDeliveryDate(),
                    paymentKey
            );
            subscriptionRepository.save(subscription);
        }
    }

    @Transactional
    public SubscriptionResponse cancel(Long subscriptionId) {
        Subscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND));

        if (sub.getStatus().isTerminated()) {
            throw new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_ALREADY_CANCELLED);
        }

        sub.cancel();
        return SubscriptionResponse.from(sub);
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
        return SubscriptionResponse.from(sub);
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
