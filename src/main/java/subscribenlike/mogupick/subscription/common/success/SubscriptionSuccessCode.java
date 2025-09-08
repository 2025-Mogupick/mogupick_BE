package subscribenlike.mogupick.subscription.common.success;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SubscriptionSuccessCode implements SuccessCode {
    SUBSCRIPTION_LIST_FETCHED(HttpStatus.OK, "구독 리스트를 조회하였습니다."),
    SUBSCRIPTION_DETAIL_FETCHED(HttpStatus.OK, "구독 상세를 조회하였습니다."),
    SUBSCRIPTION_CALENDAR_FETCHED(HttpStatus.OK, "구독 캘린더를 조회하였습니다."),
    SUBSCRIPTION_CREATED(HttpStatus.OK, "구독이 생성되었습니다."),
    SUBSCRIPTION_CANCELLED(HttpStatus.OK, "구독이 해지되었습니다."),
    SUBSCRIPTION_OPTION_UPDATED(HttpStatus.OK, "구독 옵션이 변경되었습니다.");

    private final HttpStatus status;
    private final String message;
}
