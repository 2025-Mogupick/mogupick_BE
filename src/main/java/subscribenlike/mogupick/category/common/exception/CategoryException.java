package subscribenlike.mogupick.category.common.exception;


import subscribenlike.mogupick.common.error.core.BaseException;

public class CategoryException extends BaseException {
    public CategoryException(CategoryErrorCode errorCode) {
        super(errorCode);
    }

    public CategoryException(CategoryErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}