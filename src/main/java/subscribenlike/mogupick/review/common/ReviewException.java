package subscribenlike.mogupick.review.common;


import subscribenlike.mogupick.common.error.core.BaseException;

public class ReviewException extends BaseException {
    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode);
    }
}
