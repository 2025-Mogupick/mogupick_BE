package subscribenlike.mogupick.billing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentState extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String orderId; // 멱등 키

    @Column(nullable = false)
    private String customerKey;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String paymentKey; // 승인 성공 시 저장

    private String lastError; // 실패 사유

    private PaymentState(String orderId, String customerKey, int amount, PaymentStatus status) {
        this.orderId = orderId;
        this.customerKey = customerKey;
        this.amount = amount;
        this.status = status;
    }

    public static PaymentState requested(String orderId, String customerKey, int amount) {
        return new PaymentState(orderId, customerKey, amount, PaymentStatus.REQUESTED);
    }

    public void markBillingKeyIssued() {
        this.status = PaymentStatus.BILLING_KEY_ISSUED;
    }

    public void markApproved(String paymentKey) {
        this.status = PaymentStatus.APPROVED;
        this.paymentKey = paymentKey;
    }

    public void markFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.lastError = reason;
    }
}
