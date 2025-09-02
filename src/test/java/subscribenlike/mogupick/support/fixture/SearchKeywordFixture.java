package subscribenlike.mogupick.support.fixture;

import lombok.Getter;
import subscribenlike.mogupick.searchKeyword.domain.SearchKeyword;

@Getter
public enum SearchKeywordFixture {
    생수("생수", SearchKeyword.normalize("생수"), 10),
    생수한묶음("생수 한묶음", SearchKeyword.normalize("생수 한묶음"), 25),
    생수박스("생수 박스", SearchKeyword.normalize("생수 박스"), 18),
    생수대용량("생수 대용량", SearchKeyword.normalize("생수 대용량"), 12),
    미니생수("미니 생수", SearchKeyword.normalize("미니 생수"), 8),
    해외생수("해외 생수", SearchKeyword.normalize("해외 생수"), 5);

    private final String content;
    private final String normalizedContent;
    private final int searchedCount;

    SearchKeywordFixture(String content, String normalizedContent, int searchedCount) {
        this.content = content;
        this.normalizedContent = normalizedContent;
        this.searchedCount = searchedCount;
    }

    public static SearchKeyword 생수() {
        return new SearchKeyword(null, 생수.content, 생수.searchedCount);
    }

    public static SearchKeyword 생수한묶음() {
        return new SearchKeyword(null, 생수한묶음.content, 생수한묶음.searchedCount);
    }

    public static SearchKeyword 생수박스() {
        return new SearchKeyword(null, 생수박스.content, 생수박스.searchedCount);
    }

    public static SearchKeyword 생수대용량() {
        return new SearchKeyword(null, 생수대용량.content, 생수대용량.searchedCount);
    }

    public static SearchKeyword 미니생수() {
        return new SearchKeyword(null, 미니생수.content, 미니생수.searchedCount);
    }

    public static SearchKeyword 해외생수() {
        return new SearchKeyword(null, 해외생수.content, 해외생수.searchedCount);
    }
}
