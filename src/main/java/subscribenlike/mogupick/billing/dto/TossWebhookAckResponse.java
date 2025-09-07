package subscribenlike.mogupick.billing.dto;

public record TossWebhookAckResponse(
        boolean processed,
        String eventType,
        String paymentKey,
        String orderId
) {
    public static TossWebhookAckResponse processed(String eventType, String paymentKey, String orderId) {
        return new TossWebhookAckResponse(true, eventType, paymentKey, orderId);
    }
    public static TossWebhookAckResponse ignored(String eventType) {
        return new TossWebhookAckResponse(false, eventType, null, null);
    }
}
