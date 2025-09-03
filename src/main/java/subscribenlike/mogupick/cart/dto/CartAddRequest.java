package subscribenlike.mogupick.cart.dto;

import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;

public record CartAddRequest(
        Long memberId,
        Long productId,
        SubscriptionPeriodUnit unit,
        int period
) {
    public static CartAddRequest of(Long memberId, Long productId, SubscriptionPeriodUnit unit, int period) {
        return new CartAddRequest(memberId, productId, unit, period);
    }
}
