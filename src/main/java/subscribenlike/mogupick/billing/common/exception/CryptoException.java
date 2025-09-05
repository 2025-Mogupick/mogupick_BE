package subscribenlike.mogupick.billing.common.exception;

import subscribenlike.mogupick.common.error.core.BaseException;

public class CryptoException extends BaseException {

    public CryptoException(CryptoErrorCode errorCode) {
        super(errorCode);
    }

    public CryptoException(CryptoErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
