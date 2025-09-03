package subscribenlike.mogupick.searchKeyword.dto;

import subscribenlike.mogupick.searchKeyword.domain.SearchKeyword;

public record SearchKeywordResponse(
        Long id,
        String content,
        int searchedCount
) {
    public static SearchKeywordResponse from(SearchKeyword searchKeyword) {
        return new SearchKeywordResponse(searchKeyword.getId(), searchKeyword.getContent(),
                searchKeyword.getSearchedCount());
    }
}
