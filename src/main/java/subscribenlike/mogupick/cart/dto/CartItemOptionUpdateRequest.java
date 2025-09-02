package subscribenlike.mogupick.cart.dto;

import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;

public record CartItemOptionUpdateRequest(
        Long memberId,
        SubscriptionPeriodUnit unit,
        int period
) {
    public static CartItemOptionUpdateRequest of(Long memberId, SubscriptionPeriodUnit unit, int period) {
        return new CartItemOptionUpdateRequest(memberId, unit, period);
    }
}
