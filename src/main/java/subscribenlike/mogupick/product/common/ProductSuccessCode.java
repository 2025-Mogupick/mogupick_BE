package subscribenlike.mogupick.product.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ProductSuccessCode implements SuccessCode {
    NEW_PRODUCTS_IN_MONTH_FETCHED(HttpStatus.OK, "이번 달 새로나온 구독 상품 리스트를 조회하였습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}