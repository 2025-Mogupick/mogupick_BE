package subscribenlike.mogupick.brand.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
public enum BrandErrorCode implements ErrorCode {
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 브랜드입니다.");

    BrandErrorCode(HttpStatus status, String rawMessage) {
        this.status = status;
        this.rawMessage = rawMessage;
    }

    public static final String PREFIX = "[BRAND ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
