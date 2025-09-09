package subscribenlike.mogupick.searchKeyword.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.searchKeyword.common.SearchKeywordErrorCode;
import subscribenlike.mogupick.searchKeyword.common.SearchKeywordException;
import subscribenlike.mogupick.searchKeyword.domain.SearchKeyword;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
    Optional<SearchKeyword> findByNormalizedContent(String normalizedContent);

    List<SearchKeyword> findTop6ByNormalizedContentContainingIgnoreCaseOrderBySearchedCountDesc(String keyword);

    default SearchKeyword findByNormalizedContentOrThrow(String normalizedContent) {
        return findByNormalizedContent(normalizedContent)
                .orElseThrow(() -> new SearchKeywordException(SearchKeywordErrorCode.SEARCH_KEYWORD_NOT_FOUND));
    }
}
