package subscribenlike.mogupick.product.common;


import subscribenlike.mogupick.common.error.core.BaseException;

public class ProductException extends BaseException {
    public ProductException(ProductErrorCode errorCode) {
        super(errorCode);
    }
}