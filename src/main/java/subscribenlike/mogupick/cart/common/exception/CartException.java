package subscribenlike.mogupick.cart.common.exception;


import subscribenlike.mogupick.common.error.core.BaseException;

public class CartException extends BaseException {
    public CartException(CartErrorCode errorCode) {
        super(errorCode);
    }

    public CartException(CartErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
