package subscribenlike.mogupick.notification.common.success;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NotificationSuccessCode implements SuccessCode {

    // 200 OK
    GET_NOTIFICATIONS_SUCCESS(HttpStatus.OK, "알림 목록 조회에 성공했습니다."),
    READ_NOTIFICATION_SUCCESS(HttpStatus.OK, "알림 읽음 처리에 성공했습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}