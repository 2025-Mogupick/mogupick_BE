package subscribenlike.mogupick.billing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BillingKeyCredential extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String customerKey; // 내부 고객 식별자

    @Column(nullable = false)
    private String billingKeyEnc; // 암호화된 빌링키

    @Column(nullable = false)
    private String keyAlias; // 마스킹 별칭

    private BillingKeyCredential(String customerKey, String billingKeyEnc, String keyAlias) {
        this.customerKey = customerKey;
        this.billingKeyEnc = billingKeyEnc;
        this.keyAlias = keyAlias;
    }

    public static BillingKeyCredential of(String customerKey, String billingKeyEnc, String keyAlias) {
        return new BillingKeyCredential(customerKey, billingKeyEnc, keyAlias);
    }

    public void rotate(String newEnc, String newAlias) {
        this.billingKeyEnc = newEnc;
        this.keyAlias = newAlias;
    }
}
