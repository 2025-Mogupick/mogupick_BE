package subscribenlike.mogupick.subscriptionOption.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubscriptionPeriodUnit unit;

    private int period;

    private String displayText;

    public SubscriptionOption(SubscriptionPeriodUnit unit, int period, String displayText) {
        this.unit = unit;
        this.period = period;
        this.displayText = displayText;
    }

    public static SubscriptionOption of(SubscriptionPeriodUnit unit, int period, String displayText) {
        return new SubscriptionOption(unit, period, displayText);
    }
}
