package subscribenlike.mogupick.delivery.common.success;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeliverySuccessCode implements SuccessCode {

    DELIVERY_STARTED_SUCCESS(HttpStatus.OK, "배송 시작 처리에 성공했습니다."),
    DELIVERY_COMPLETED_SUCCESS(HttpStatus.OK, "배송 완료 처리에 성공했습니다.");

    private final HttpStatus status;
    private final String message;
}