package subscribenlike.mogupick.subscription.common.exception;

import subscribenlike.mogupick.common.error.core.BaseException;

public class SubscriptionException extends BaseException {
    public SubscriptionException(SubscriptionErrorCode errorCode) {
        super(errorCode);
    }

    public SubscriptionException(SubscriptionErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
