package subscribenlike.mogupick.cart.dto;

import subscribenlike.mogupick.cart.domain.CartItem;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;

import java.time.LocalDate;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        String productName,
        Long subscriptionOptionId,
        String displayText,
        LocalDate firstDeliveryDate,
        int price,
        String imageUrl
) {
    public static CartItemResponse from(CartItem item, String imageUrl) {
        SubscriptionOption option = item.getOption();
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                option.getId(),
                option.getDisplayText(),
                item.getFirstDeliveryDate(),
                item.getPriceSnapshot(),
                imageUrl
        );
    }
}
