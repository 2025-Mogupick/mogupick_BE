package subscribenlike.mogupick.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.cart.domain.CartItem;
import subscribenlike.mogupick.cart.repository.CartItemRepository;
import subscribenlike.mogupick.deliveryAddress.domain.DeliveryAddress;
import subscribenlike.mogupick.deliveryAddress.repository.DeliveryAddressRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.order.domain.*;
import subscribenlike.mogupick.order.dto.CreateOrderRequest;
import subscribenlike.mogupick.order.dto.OrderResponse;
import subscribenlike.mogupick.order.repository.OrderRepository;
import subscribenlike.mogupick.product.domain.Product;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;

    private static final SecureRandom RND = new SecureRandom();

    @Transactional
    public OrderResponse create(Long memberId, CreateOrderRequest req) {
        Member member = memberRepository.findOrThrow(memberId);

        DeliveryAddress addr = deliveryAddressRepository.findById(req.addressId())
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        AddressSnapshot addressSnapshot = AddressSnapshot.of(
                addr.getReceiver(), addr.getContact(),
                addr.getBaseAddress(), addr.getDetailAddress()
        );

        List<CartItem> items = cartItemRepository.findAllById(req.cartItemIds());
        if (items.isEmpty()) throw new IllegalArgumentException("No cart items selected");

        String orderId = generateOrderId(memberId);
        String orderName = buildOrderName(items);
        int amount = items.stream().mapToInt(i -> i.getProduct().getPrice()).sum();

        Order order = Order.create(orderId, member, addressSnapshot, amount, orderName);

        for (CartItem ci : items) {
            Product p = ci.getProduct();
            LocalDate firstDelivery = ci.getFirstDeliveryDate();
            OrderItem oi = OrderItem.from(p, ci.getOption(), firstDelivery);
            order.addItem(oi);
        }

        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse get(String orderId) {
        return OrderResponse.from(orderRepository.findByOrderIdOrThrow(orderId));
    }

    @Transactional(readOnly = true)
    public Order getEntityByOrderId(String orderId) {
        return orderRepository.findByOrderIdOrThrow(orderId);
    }

    @Transactional
    public void markPaid(String orderId) {
        var order = orderRepository.findByOrderIdOrThrow(orderId);
        order.markPaid();
    }

    private String buildOrderName(List<CartItem> items) {
        if (items.isEmpty()) return "주문";
        String brand = items.get(0).getProduct().getBrandName();
        int others = Math.max(0, items.size() - 1);
        return (others > 0) ? brand + " 외 " + others + "건" : brand + " " + items.get(0).getProduct().getName();
    }

    private String generateOrderId(Long memberId) {
        String ymd = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String rand = Integer.toString(RND.nextInt(36 * 36 * 36 * 36), 36).toUpperCase(Locale.ROOT);
        return "ORD-" + ymd + "-" + memberId + "-" + rand;
    }
}
