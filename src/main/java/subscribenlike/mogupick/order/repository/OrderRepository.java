package subscribenlike.mogupick.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.order.common.exception.OrderErrorCode;
import subscribenlike.mogupick.order.common.exception.OrderException;
import subscribenlike.mogupick.order.domain.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(String orderId);

    default Order findByOrderIdOrThrow(String orderId) {
        return findByOrderId(orderId).orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
    }
}
