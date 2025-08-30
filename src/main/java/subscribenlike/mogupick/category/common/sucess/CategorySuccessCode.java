package subscribenlike.mogupick.category.common.sucess;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.common.success.SuccessCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CategorySuccessCode implements SuccessCode {
    ROOT_CATEGORIES_FETCHED(HttpStatus.OK, "루트 카테고리 리스트 조회에 성공하였습니다."),
    CATEGORY_OPTIONS_AND_FILTERS_FETCHED(HttpStatus.OK, "카테고리 옵션 및 필터 조회에 성공하였습니다.");

    private final HttpStatus status;
    private final String message;
}
