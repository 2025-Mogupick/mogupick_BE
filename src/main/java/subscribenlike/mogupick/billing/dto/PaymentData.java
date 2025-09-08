package subscribenlike.mogupick.billing.dto;

public record PaymentData(
        String paymentKey,
        String orderId,
        Long memberId,
        String status
) {}
