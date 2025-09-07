package subscribenlike.mogupick.subscription.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

    @OneToOne
    private SubscriptionOption option;

    private LocalDate nextBillingDate;

    private LocalDate firstDeliveryDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private LocalDate endedAt;

    private int progressRound;

    private String paymentKey;

    public static Subscription create(Member member, Product product,
                                      SubscriptionOption option, LocalDate firstDeliveryDate,
                                       String paymentKey) {
        Subscription s = new Subscription();
        s.member = member;
        s.product = product;
        s.option = option;
        s.firstDeliveryDate = firstDeliveryDate;
        s.nextBillingDate = s.calculateNextBillingDate(firstDeliveryDate, option);
        s.progressRound = 1;
        s.status = SubscriptionStatus.ONGOING;
        s.paymentKey = paymentKey;
        return s;
    }

    public static Subscription create(Member member, Product product,
                                      SubscriptionOption option, LocalDate firstDeliveryDate,
                                      int totalRounds) {
        return create(member, product, option, firstDeliveryDate, null);
    }

    public LocalDate calculateNextBillingDate(LocalDate baseDate, SubscriptionOption option) {
        switch (option.getUnit()) {
            case DAY -> { return baseDate.plusDays(option.getPeriod()); }
            case WEEK -> { return baseDate.plusWeeks(option.getPeriod()); }
            case MONTH -> { return baseDate.plusMonths(option.getPeriod()); }
            default -> throw new IllegalArgumentException("Unknown unit");
        }
    }

    public void changeOption(SubscriptionOption newOption, LocalDate newFirstDeliveryDate) {
        this.option = newOption;
        this.firstDeliveryDate = newFirstDeliveryDate;
        this.nextBillingDate = calculateNextBillingDate(newFirstDeliveryDate, newOption);
    }

    public void proceedNextRound() {
        this.progressRound += 1;
        this.nextBillingDate = calculateNextBillingDate(this.nextBillingDate, this.option);
    }

    public void cancel() {
        this.status = SubscriptionStatus.ENDED;
        this.endedAt = LocalDate.now();
    }
}
