package subscribenlike.mogupick.category.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CategoryErrorCode implements ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다. 입력값 : %s"),
    SUB_CATEGORY_INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "SubCategory에서 유효하지 않은 입력 값입니다. 입력값 : %s"),

    ;

    public static final String PREFIX = "[CATEGORY ERROR] ";

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
