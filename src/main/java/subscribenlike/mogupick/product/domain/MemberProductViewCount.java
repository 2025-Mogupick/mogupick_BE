package subscribenlike.mogupick.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;
import subscribenlike.mogupick.member.domain.Member;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Entity
@Table(name = "member_product_view_count")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProductViewCount extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long viewCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDateTime lastViewedAt;

    public MemberProductViewCount(Product product, Member member) {
        this.product = product;
        this.member  = member;
        this.viewCount = 0L;
        this.lastViewedAt = LocalDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId());
    }

    public static MemberProductViewCount of(Product product, Member member) {
        return new MemberProductViewCount(product,member);
    }

    public void increase() {
        this.viewCount += 1L;
    }

    public void updateViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}


