package subscribenlike.mogupick.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;
import subscribenlike.mogupick.member.domain.Member;

@Entity
@Table(name = "review_likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    public ReviewLike(Review review, Member member) {
        this.review = review;
        this.member = member;
    }
}
