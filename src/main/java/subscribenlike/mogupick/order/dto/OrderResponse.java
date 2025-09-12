package subscribenlike.mogupick.order.dto;

import subscribenlike.mogupick.order.domain.Order;

import java.util.List;

public record OrderResponse(
        String orderId,
        int payableAmount,
        String orderName,
        String status,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order o) {
        return new OrderResponse(
                o.getOrderId(), o.getPayableAmount(), o.getOrderName(), o.getStatus().name(),
                o.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
