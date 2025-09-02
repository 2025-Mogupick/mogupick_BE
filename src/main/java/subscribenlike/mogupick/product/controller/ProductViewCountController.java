package subscribenlike.mogupick.product.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.product.common.ProductViewCountSuccessCode;
import subscribenlike.mogupick.product.model.FetchProductMostDailyViewStatChangeResponse;
import subscribenlike.mogupick.product.service.ProductViewCountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products/view-count")
public class ProductViewCountController {

    private final ProductViewCountService productViewCountService;

    @Operation(summary = "상품 조회수 증가", description = "상품의 조회수를 증가 시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 조회수 증가 성공"
            )
    })
    @PutMapping("/{productId}/increment")
    public ResponseEntity<?> updateProductViewCount(Long productId) {
        productViewCountService.incrementProductViewCount(productId);
        return ResponseEntity
                .status(ProductViewCountSuccessCode.PRODUCT_VIEW_COUNT_INCREMENTED.getStatus())
                .body(SuccessResponse.from(ProductViewCountSuccessCode.PRODUCT_VIEW_COUNT_INCREMENTED));
    }

    @Operation(summary = "주목받는 상품 목록 조회", description = "주목받는 상품 목록 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주목받는 상품 목록 조회 성공"
            )
    })
    @GetMapping("/most-daily-view-stat-change")
    public ResponseEntity<?> fetchMostDailyViewStatChange(@RequestParam int size) {
        List<FetchProductMostDailyViewStatChangeResponse> response =
                productViewCountService.getMostDailyViewStatChangeProduct(size);

        return ResponseEntity
                .status(ProductViewCountSuccessCode.MOST_DAILY_VIEW_STAT_CHANGE_PRODUCTS_FETCHED.getStatus())
                .body(SuccessResponse.from(ProductViewCountSuccessCode.MOST_DAILY_VIEW_STAT_CHANGE_PRODUCTS_FETCHED, response));
    }
}
