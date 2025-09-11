package subscribenlike.mogupick.subscription.batch.dto;

import subscribenlike.mogupick.subscription.domain.Subscription;

public record ChargeCommand(
        Subscription subscription,
        String orderId,
        String orderName,
        String customerKey,
        int amount
) {}
