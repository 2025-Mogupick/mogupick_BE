package subscribenlike.mogupick.cart.dto;

import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;

import java.time.LocalDate;

public record CartItemOptionUpdateRequest(
        Long memberId,
        Long subscriptionOptionId,
        LocalDate firstDeliveryDate
) {
    public static CartItemOptionUpdateRequest of(Long memberId, Long subscriptionOptionId, LocalDate firstDeliveryDate) {
        return new CartItemOptionUpdateRequest(memberId, subscriptionOptionId, firstDeliveryDate);
    }
}
