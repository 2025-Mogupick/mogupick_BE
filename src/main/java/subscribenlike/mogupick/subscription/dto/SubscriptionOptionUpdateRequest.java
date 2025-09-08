package subscribenlike.mogupick.subscription.dto;

import java.time.LocalDate;

public record SubscriptionOptionUpdateRequest(
        Long subscriptionOptionId,
        LocalDate firstDeliveryDate
) {}
