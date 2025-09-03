package subscribenlike.mogupick.review.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum ReviewSuccessCode implements SuccessCode {

    PRODUCT_REVIEWS_FETCHED(HttpStatus.OK, "상품의 리뷰 목록을 조회하였습니다.");

    private final HttpStatus status;
    private final String message;
}
