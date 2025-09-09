package subscribenlike.mogupick.cart.dto;

import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;

import java.time.LocalDate;

public record CartAddRequest(
        Long productId,
        Long subscriptionOptionId,
        LocalDate firstDeliveryDate
) {
    public static CartAddRequest of( Long productId, Long subscriptionOptionId, LocalDate firstDeliveryDate) {
        return new CartAddRequest(productId, subscriptionOptionId, firstDeliveryDate);
    }
}
