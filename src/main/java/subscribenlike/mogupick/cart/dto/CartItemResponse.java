package subscribenlike.mogupick.cart.dto;

import subscribenlike.mogupick.cart.domain.CartItem;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        String productName,
        SubscriptionPeriodUnit unit,
        int period,
        String displayText
) {
    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getUnit(),
                item.getPeriod(),
                format(item.getUnit(), item.getPeriod())
        );
    }

    private static String format(SubscriptionPeriodUnit unit, int period) {
        return switch (unit) {
            case DAY -> "매 " + period + "일";
            case WEEK -> "매 " + period + "주";
            case MONTH -> "매 " + period + "개월";
        };
    }
}
