package subscribenlike.mogupick.subscription.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SubscriptionErrorCode implements ErrorCode {
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "구독을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    SUBSCRIPTION_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "구독 옵션을 찾을 수 없습니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."),
    PAYMENT_KEY_DUPLICATE(HttpStatus.CONFLICT, "이미 처리된 결제입니다."),
    SUBSCRIPTION_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "이미 해지된 구독입니다."),
    FIRST_DELIVERY_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "첫 배송 희망일을 선택해야 합니다.");

    public static final String PREFIX = "[SUBSCRIPTION ERROR] ";

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
