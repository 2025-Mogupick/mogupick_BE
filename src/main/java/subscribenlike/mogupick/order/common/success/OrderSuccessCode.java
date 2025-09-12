package subscribenlike.mogupick.order.common.success;

import lombok.*;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderSuccessCode implements SuccessCode {
    ORDER_CREATED(HttpStatus.CREATED, "주문이 생성되었습니다."),
    ORDER_FETCHED(HttpStatus.OK, "주문을 조회하였습니다.");

    private final HttpStatus status;
    private final String message;
}
