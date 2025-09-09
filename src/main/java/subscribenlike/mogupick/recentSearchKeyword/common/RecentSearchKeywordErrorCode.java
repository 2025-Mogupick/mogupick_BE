package subscribenlike.mogupick.recentSearchKeyword.common;

import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

public enum RecentSearchKeywordErrorCode implements ErrorCode {
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 키워드입니다."),;
    public static final String PREFIX = "[RecentSearchKeyword ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    RecentSearchKeywordErrorCode(HttpStatus status, String rawMessage) {
        this.status = status;
        this.rawMessage = rawMessage;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
