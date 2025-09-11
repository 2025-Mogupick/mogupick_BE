package subscribenlike.mogupick.subscriptionOption.dto;

import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;

public record SubscriptionOptionResponse(Long id, SubscriptionPeriodUnit unit, int period, String displayText) {
    public static SubscriptionOptionResponse from(SubscriptionOption o) {
        return new SubscriptionOptionResponse(o.getId(), o.getUnit(), o.getPeriod(), o.getDisplayText());
    }
}

