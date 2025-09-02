package subscribenlike.mogupick.cart.common.success;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CartSuccessCode implements SuccessCode {
    CART_FETCHED(HttpStatus.OK, "장바구니를 조회하였습니다."),
    CART_ITEM_ADDED(HttpStatus.OK, "장바구니 담기 성공"),
    CART_ITEM_OPTION_UPDATED(HttpStatus.OK, "장바구니 옵션 변경 성공"),
    CART_ITEM_REMOVED(HttpStatus.OK, "장바구니 아이템 삭제 성공"),

    ;

    private final HttpStatus status;
    private final String message;
}
