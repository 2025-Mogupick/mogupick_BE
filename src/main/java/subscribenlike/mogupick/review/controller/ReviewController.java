package subscribenlike.mogupick.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.review.common.ReviewSuccessCode;
import subscribenlike.mogupick.review.model.CreateReviewRequest;
import subscribenlike.mogupick.review.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "상품 리뷰 작성", description = "상품에 대한 리뷰를 작성합니다. 이미지, 댓글, 평점을 입력할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "리뷰 작성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "상품 또는 회원을 찾을 수 없음"
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReview(@ModelAttribute CreateReviewRequest request) {
        reviewService.createReview(request);

        return ResponseEntity
                .status(ReviewSuccessCode.REVIEW_CREATED.getStatus())
                .body(SuccessResponse.from(ReviewSuccessCode.REVIEW_CREATED));
    }
}
