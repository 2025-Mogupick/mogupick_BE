package subscribenlike.mogupick.auth.common.exception;

import subscribenlike.mogupick.common.error.core.BaseException;

public class AuthException extends BaseException {
    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }
}