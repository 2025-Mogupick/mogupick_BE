package subscribenlike.mogupick.review.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum ReviewSuccessCode implements SuccessCode {

    REVIEW_CREATED(HttpStatus.CREATED, "리뷰가 성공적으로 작성되었습니다."),
    PRODUCT_REVIEWS_FETCHED(HttpStatus.OK, "상품의 리뷰 목록을 조회하였습니다."),
    REVIEW_LIKE_ADDED(HttpStatus.CREATED, "리뷰에 좋아요를 추가하였습니다."),
    REVIEW_LIKE_REMOVED(HttpStatus.OK, "리뷰 좋아요를 제거하였습니다.");

    private final HttpStatus status;
    private final String message;
}
