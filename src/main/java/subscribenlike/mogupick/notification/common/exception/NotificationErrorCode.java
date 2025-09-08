package subscribenlike.mogupick.notification.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NotificationErrorCode implements ErrorCode {

    // 404 Not Found
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 알림을 찾을 수 없습니다."),

    // 403 Forbidden
    FORBIDDEN_READ_NOTIFICATION(HttpStatus.FORBIDDEN, "해당 알림을 읽을 권한이 없습니다."),

    ;

    public static final String PREFIX = "[NOTIFICATION ERROR] ";

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