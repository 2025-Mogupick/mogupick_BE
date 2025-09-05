package subscribenlike.mogupick.billing.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.error.core.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BillingErrorCode implements ErrorCode {
    NO_BILLING_KEY(HttpStatus.NOT_FOUND, "등록된 빌링키가 없습니다."),             // 고객 빌링키 없음
    INVALID_AUTH_KEY(HttpStatus.BAD_REQUEST, "유효하지 않은 인증키(authKey)입니다."), // Toss authKey 검증 실패
    PAYMENT_APPROVAL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 승인에 실패했습니다."),  // 승인 실패
    BILLING_KEY_ISSUE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "빌링키 발급에 실패했습니다."), // 빌링키 발급 실패
    PAYMENT_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 상태를 찾을 수 없습니다."),
    ;

    public static final String PREFIX = "[BILLING ERROR] ";

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
