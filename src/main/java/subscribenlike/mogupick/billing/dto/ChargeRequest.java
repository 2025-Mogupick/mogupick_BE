package subscribenlike.mogupick.billing.dto;

public record ChargeRequest(
        String orderId,
        String customerKey,
        String orderName,
        int amount
) {
}
