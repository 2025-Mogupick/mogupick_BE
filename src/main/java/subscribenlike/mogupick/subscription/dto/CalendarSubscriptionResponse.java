package subscribenlike.mogupick.subscription.dto;

public record CalendarSubscriptionResponse(
        Long subscriptionId,
        String productName,
        int amount
) {}
