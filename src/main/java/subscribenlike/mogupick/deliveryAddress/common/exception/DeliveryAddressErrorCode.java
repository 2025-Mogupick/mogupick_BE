package subscribenlike.mogupick.deliveryAddress.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeliveryAddressErrorCode implements ErrorCode {
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "배송지 정보를 찾을 수 없습니다."),
    ADDRESS_UNAUTHORIZED(HttpStatus.FORBIDDEN, "배송지 수정/삭제 권한이 없습니다."),
    ADDRESS_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 배송지 요청입니다.");

    public static final String PREFIX = "[DELIVERY ADDRESS ERROR] ";

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
