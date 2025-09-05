package subscribenlike.mogupick.billing.dto;

public record FirstChargeRequest(
        String orderId,
        String authKey,
        String customerKey,
        String orderName,
        int amount
) {
}
