package subscribenlike.mogupick.billing.common.success;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BillingSuccessCode implements SuccessCode {
    FIRST_CHARGE_SUCCESS(HttpStatus.OK, "최초 결제 성공"),
    RECURRING_CHARGE_SUCCESS(HttpStatus.OK, "재결제 성공"),
    PAYMENT_STATUS_FETCHED(HttpStatus.OK, "결제 상태 조회 성공"),

    ;

    private final HttpStatus status;
    private final String message;
}
