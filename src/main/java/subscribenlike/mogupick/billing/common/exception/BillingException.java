package subscribenlike.mogupick.billing.common.exception;

import subscribenlike.mogupick.cart.common.exception.CartErrorCode;
import subscribenlike.mogupick.common.error.core.BaseException;

public class BillingException extends BaseException {
    public BillingException(BillingErrorCode errorCode) {
        super(errorCode);
    }

    public BillingException(BillingErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
