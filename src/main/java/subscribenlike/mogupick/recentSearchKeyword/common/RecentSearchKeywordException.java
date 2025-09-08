package subscribenlike.mogupick.recentSearchKeyword.common;

import subscribenlike.mogupick.common.error.core.BaseException;

public class RecentSearchKeywordException extends BaseException {
    public RecentSearchKeywordException(RecentSearchKeywordErrorCode errorCode) {
        super(errorCode);
    }
}
