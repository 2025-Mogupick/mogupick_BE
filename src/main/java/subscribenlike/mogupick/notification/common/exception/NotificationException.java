package subscribenlike.mogupick.notification.common.exception;

import subscribenlike.mogupick.common.error.core.BaseException;

public class NotificationException extends BaseException {
    public NotificationException(NotificationErrorCode errorCode) {
        super(errorCode);
    }
}