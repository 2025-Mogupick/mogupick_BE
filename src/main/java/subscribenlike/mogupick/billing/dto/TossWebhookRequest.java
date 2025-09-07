package subscribenlike.mogupick.billing.dto;

public record TossWebhookRequest(
        String eventType,
        PaymentData data
) {}
