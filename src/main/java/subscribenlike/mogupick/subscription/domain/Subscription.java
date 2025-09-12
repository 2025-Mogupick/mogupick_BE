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

    @ManyToOne
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
        s.nextBillingDate = firstDeliveryDate.minusDays(3);
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

    public LocalDate calculateNextDeliveryDate(LocalDate baseDate, SubscriptionOption option) {
        return switch (option.getUnit()) {
            case DAY -> baseDate.plusDays(option.getPeriod());
            case WEEK -> baseDate.plusWeeks(option.getPeriod());
            case MONTH -> baseDate.plusMonths(option.getPeriod());
        };
    }

    public void changeOption(SubscriptionOption newOption, LocalDate newFirstDeliveryDate) {
        this.option = newOption;
        this.firstDeliveryDate = newFirstDeliveryDate;
        this.nextBillingDate = calculateNextDeliveryDate(newFirstDeliveryDate, newOption);
    }

    private LocalDate getCurrentDeliveryDate() {
        return this.nextBillingDate.plusDays(3);
    }

    public void proceedNextRound() {
        this.progressRound += 1;
        // 다음 배송일 = (직전 배송일) + 주기
        LocalDate nextDelivery = calculateNextDeliveryDate(getCurrentDeliveryDate(), this.option);
        this.nextBillingDate = nextDelivery.minusDays(3);
    }

    public void cancel() {
        this.status = SubscriptionStatus.ENDED;
        this.endedAt = LocalDate.now();
    }
}
