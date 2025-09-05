package subscribenlike.mogupick.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.review.common.ReviewSuccessCode;
import subscribenlike.mogupick.review.model.CreateReviewRequest;
import subscribenlike.mogupick.review.model.FetchProductReviewsResponse;
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
    public ResponseEntity<SuccessResponse<Void>> createReview(@ModelAttribute CreateReviewRequest request) throws IOException {
        reviewService.createReview(request);

        return ResponseEntity
                .status(ReviewSuccessCode.REVIEW_CREATED.getStatus())
                .body(SuccessResponse.from(ReviewSuccessCode.REVIEW_CREATED));
    }

    @Operation(summary = "상품 리뷰 목록 조회", description = "특정 상품의 리뷰 목록을 조회합니다. 페이지네이션과 함께 상품의 평균 평점, 리뷰 수 등의 정보를 포함합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 리뷰 목록 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "상품을 찾을 수 없음"
            )
    })
    @GetMapping("/products/{productId}")
    public ResponseEntity<SuccessResponse<FetchProductReviewsResponse>> getProductReviews(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        FetchProductReviewsResponse response = reviewService.getProductReviewsWithStats(productId, userDetails.getMemberId(), pageable);

        return ResponseEntity
                .status(200)
                .body(SuccessResponse.from(ReviewSuccessCode.PRODUCT_REVIEWS_FETCHED, response));
    }
}
