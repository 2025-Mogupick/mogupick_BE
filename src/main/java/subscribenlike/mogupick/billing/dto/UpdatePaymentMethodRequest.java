package subscribenlike.mogupick.billing.dto;

public record UpdatePaymentMethodRequest(
        String authKey,
        String customerKey
) {
}
