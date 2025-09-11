package subscribenlike.mogupick.subscriptionOption.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SubscriptionOptionSuccessCode implements SuccessCode {
    OPTION_FETCHED(HttpStatus.OK, "옵션을 조회하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
