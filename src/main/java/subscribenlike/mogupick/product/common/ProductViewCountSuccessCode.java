package subscribenlike.mogupick.product.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ProductViewCountSuccessCode implements SuccessCode {
    PRODUCT_VIEW_COUNT_INCREMENTED(HttpStatus.OK, "상품의 조회수를 성공적으로 증가시켰습니다."),
    MOST_DAILY_VIEW_STAT_CHANGE_PRODUCTS_FETCHED(HttpStatus.OK, "주목받는 상품 목록을 성공적으로 조회했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}