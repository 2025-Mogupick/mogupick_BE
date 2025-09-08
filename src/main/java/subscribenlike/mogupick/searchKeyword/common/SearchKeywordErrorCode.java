package subscribenlike.mogupick.searchKeyword.common;

import lombok.Getter;
import subscribenlike.mogupick.common.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum SearchKeywordErrorCode implements ErrorCode {
    SEARCH_KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 키워드를 찾을 수 없습니다."),
    SEARCH_KEYWORD_OWNER_ERROR(HttpStatus.BAD_REQUEST, "검색어의 주인이 아닙니다.") ;

    SearchKeywordErrorCode(HttpStatus status, String rawMessage) {
        this.status = status;
        this.rawMessage = rawMessage;
    }

    public static final String PREFIX = "[SEARCH_KEYWORD ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
