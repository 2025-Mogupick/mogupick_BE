package subscribenlike.mogupick.searchKeyword.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.searchKeyword.domain.SearchKeyword;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
    Optional<SearchKeyword> findByNormalizedContent(String normalizedContent);
}
