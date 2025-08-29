package subscribenlike.mogupick.product.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.product.service.ProductService;
import subscribenlike.mogupick.product.common.ProductSuccessCode;
import subscribenlike.mogupick.product.model.FetchNewProductsInMonthResponse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "이번 달 새로나온 상품 조회", description = "이번 달 새로나온 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 목록 조회 성공"
            )
    })
    @GetMapping("/new")
    public ResponseEntity<?> getNewSubscriptionProducts(){
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        List<FetchNewProductsInMonthResponse> response =
                productService.findAllNewProductsInMonth(now.getMonthValue());

        return ResponseEntity
                .status(200)
                .body(SuccessResponse.from(ProductSuccessCode.NEW_PRODUCTS_IN_MONTH_FETCHED, response)) ;
    }

}
