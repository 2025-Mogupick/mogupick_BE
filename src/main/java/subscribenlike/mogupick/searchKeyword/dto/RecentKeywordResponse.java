package subscribenlike.mogupick.searchKeyword.dto;

import subscribenlike.mogupick.recentSearchKeyword.domain.RecentSearchKeyword;

public record RecentKeywordResponse(
        Long id,
        String content,
        String normalizedContent
) {
    public static RecentKeywordResponse from(RecentSearchKeyword keyword) {
        return new RecentKeywordResponse(keyword.getId(), keyword.getContent(), keyword.getNormalizedContent());
    }
}
