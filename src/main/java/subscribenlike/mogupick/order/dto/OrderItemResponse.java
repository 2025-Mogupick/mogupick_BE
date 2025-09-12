package subscribenlike.mogupick.order.dto;

import subscribenlike.mogupick.order.domain.OrderItem;

import java.time.LocalDate;

public record OrderItemResponse(
        Long orderItemId,
        String productName,
        String brandName,
        int price,
        String optionDisplayText,
        LocalDate firstDeliveryDate
) {
    public static OrderItemResponse from(OrderItem i) {
        return new OrderItemResponse(
                i.getId(), i.getProductName(), i.getBrandName(), i.getPrice(),
                i.getOption() != null ? i.getOption().getDisplayText() : null, i.getFirstDeliveryDate()
        );
    }
}
