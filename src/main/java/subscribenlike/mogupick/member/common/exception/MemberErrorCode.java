package subscribenlike.mogupick.member.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberErrorCode implements ErrorCode {

    // 404 Not Found
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    INVALID_OWNER_ERROR(HttpStatus.BAD_REQUEST, "브랜드의 주인이 아닙니다"),
    INVALID_SELLER_ROLE(HttpStatus.BAD_REQUEST, "판매자 권한이 아닙니다"),

    ;

    public static final String PREFIX = "[MEMBER ERROR] ";

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
