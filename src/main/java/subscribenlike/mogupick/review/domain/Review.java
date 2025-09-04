package subscribenlike.mogupick.review.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.subscription.domain.Subscription;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private double score;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

    public Review(String content, double score, Member member, Product product) {
        this.content = content;
        this.score = score;
        this.member = member;
        this.product = product;
    }
}
