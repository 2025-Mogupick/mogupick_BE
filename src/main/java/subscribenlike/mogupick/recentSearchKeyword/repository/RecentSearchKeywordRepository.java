package subscribenlike.mogupick.recentSearchKeyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.recentSearchKeyword.domain.RecentSearchKeyword;

public interface RecentSearchKeywordRepository extends JpaRepository<RecentSearchKeyword, Long> {
    RecentSearchKeyword findByNormalizedContentAndMember(String normalizedContent, Member member);
}
