package subscribenlike.mogupick.review.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewSuccessCode {

    REVIEW_CREATED(HttpStatus.CREATED, "리뷰가 성공적으로 작성되었습니다.");

    private final HttpStatus status;
    private final String message;
}
