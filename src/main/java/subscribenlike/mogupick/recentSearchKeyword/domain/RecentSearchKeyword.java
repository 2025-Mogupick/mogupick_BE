package subscribenlike.mogupick.recentSearchKeyword.domain;

import jakarta.persistence.Column;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentSearchKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "normalized_content", nullable = false, length = 200, unique = true)
    private String normalizedContent;

    @ManyToOne
    private Member member;

    public RecentSearchKeyword(String content, String normalizedContent, Member member) {
        this.content = content;
        this.normalizedContent = normalizedContent;
        this.member = member;
    }
}
