package subscribenlike.mogupick.billing.dto;

public record RegisterPaymentMethodRequest(
        String authKey,
        String customerKey
) {}
