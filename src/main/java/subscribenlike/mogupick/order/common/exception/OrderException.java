package subscribenlike.mogupick.order.common.exception;

import subscribenlike.mogupick.common.error.core.BaseException;

public class OrderException extends BaseException {
    public OrderException(OrderErrorCode errorCode) {
        super(errorCode);
    }
}
