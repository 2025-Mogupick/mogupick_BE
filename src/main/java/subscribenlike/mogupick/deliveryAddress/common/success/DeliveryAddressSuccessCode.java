package subscribenlike.mogupick.deliveryAddress.common.success;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeliveryAddressSuccessCode implements SuccessCode {
    ADDRESS_REGISTERED(HttpStatus.OK, "배송지가 등록되었습니다."),
    ADDRESS_UPDATED(HttpStatus.OK, "배송지 정보가 수정되었습니다."),
    ADDRESS_DELETED(HttpStatus.OK, "배송지가 삭제되었습니다."),
    ADDRESS_LIST_FETCHED(HttpStatus.OK, "배송지 목록을 조회하였습니다.");

    private final HttpStatus status;
    private final String message;
}
