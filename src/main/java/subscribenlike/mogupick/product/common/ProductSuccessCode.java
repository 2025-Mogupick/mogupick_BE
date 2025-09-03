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
    PEER_BEST_REVIEW_FETCHED(HttpStatus.OK, "내 또래 상품 베스트 리뷰 리스트를 조회하였습니다."),
    PRODUCT_GROUP_BY_ROOT_CATEGORY_FETCHED(HttpStatus.OK, "루트 카테고리의 상품 목록을 조회하였습니다."),
    PRODUCT_CREATED(HttpStatus.CREATED, "상품이 성공적으로 등록되었습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}