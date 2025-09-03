package subscribenlike.mogupick.recentSearchKeyword.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.recentSearchKeyword.domain.RecentSearchKeyword;

public interface RecentSearchKeywordRepository extends JpaRepository<RecentSearchKeyword, Long> {
    RecentSearchKeyword findByNormalizedContentAndMember(String normalizedContent, Member member);

    List<RecentSearchKeyword> findTop5ByMemberOrderByCreatedAtDesc(Member member);

    default RecentSearchKeyword findOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
    }
}
