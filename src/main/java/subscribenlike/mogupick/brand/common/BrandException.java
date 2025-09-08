package subscribenlike.mogupick.brand.common;

import subscribenlike.mogupick.common.error.core.BaseException;

public class BrandException extends BaseException {
    public BrandException(BrandErrorCode message) {
        super(message);
    }
}
