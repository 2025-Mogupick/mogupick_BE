package subscribenlike.mogupick.billing.dto;

import subscribenlike.mogupick.billing.domain.PaymentState;
import subscribenlike.mogupick.billing.domain.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentStateResponse(
        String orderId,
        String maskedCustomerKey,
        int amount,
        PaymentStatus status,
        String paymentKey,
        String lastError,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PaymentStateResponse from(PaymentState state) {
        return new PaymentStateResponse(
                state.getOrderId(),
                mask(state.getCustomerKey()),
                state.getAmount(),
                state.getStatus(),
                state.getPaymentKey(),
                state.getLastError(),
                state.getCreatedAt(),
                state.getUpdatedAt()
        );
    }

    private static String mask(String str) {
        if (str == null || str.length() < 4) {
            return "***";
        }
        return str.substring(0, 2) + "***";
    }
}
