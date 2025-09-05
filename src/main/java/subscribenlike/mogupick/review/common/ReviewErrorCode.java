package subscribenlike.mogupick.review.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReviewErrorCode implements ErrorCode {
    REVIEW_LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 리뷰에 좋아요를 눌렀습니다."),
    REVIEW_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰에 대한 좋아요가 존재하지 않습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

    ;

    public static final String PREFIX = "[REVIEW ERROR] ";

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
