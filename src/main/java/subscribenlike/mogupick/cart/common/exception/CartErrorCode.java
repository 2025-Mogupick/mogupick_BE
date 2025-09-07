package subscribenlike.mogupick.cart.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CartErrorCode implements ErrorCode {
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "카트를 찾을 수 없습니다."),
    PERIOD_INVALID(HttpStatus.BAD_REQUEST, "구독 주기는 1 이상이어야 합니다."),
    UNIT_INVALID(HttpStatus.BAD_REQUEST, "단위를 선택해야합니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에 상품이 없습니다."),
    SUBSCRIPTION_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "구독 옵션을 찾을 수 없습니다."),
    FIRST_DELIVERY_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "첫 배송 희망일을 선택해야 합니다.")

    ;

    public static final String PREFIX = "[CART ERROR] ";

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
