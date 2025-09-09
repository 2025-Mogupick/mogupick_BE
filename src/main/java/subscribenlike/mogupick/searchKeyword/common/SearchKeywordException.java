package subscribenlike.mogupick.searchKeyword.common;

import subscribenlike.mogupick.common.error.core.BaseException;

public class SearchKeywordException extends BaseException {
    public SearchKeywordException(SearchKeywordErrorCode errorCode) {
        super(errorCode);
    }
}
