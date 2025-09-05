package subscribenlike.mogupick.member.common.exception;

import subscribenlike.mogupick.common.error.core.BaseException;

public class MemberException extends BaseException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}