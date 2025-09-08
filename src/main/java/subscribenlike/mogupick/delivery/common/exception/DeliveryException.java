package subscribenlike.mogupick.delivery.common.exception;

import subscribenlike.mogupick.common.error.core.BaseException;

public class DeliveryException extends BaseException {
    public DeliveryException(DeliveryErrorCode errorCode) {
        super(errorCode);
    }
}