package subscribenlike.mogupick.deliveryAddress.common.exception;

import subscribenlike.mogupick.common.error.core.BaseException;

public class DeliveryAddressException extends BaseException {
    public DeliveryAddressException(DeliveryAddressErrorCode errorCode) {
        super(errorCode);
    }

    public DeliveryAddressException(DeliveryAddressErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
