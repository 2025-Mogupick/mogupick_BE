package subscribenlike.mogupick.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.like.service.LikeService;
import subscribenlike.mogupick.product.model.LikedProductResponse;
import subscribenlike.mogupick.product.service.ProductService;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final ProductService productService;

    @Operation(summary = "상품 좋아요 기능", description = "상품에 대해 좋아요를 누르거나 이미 좋아요가 되있으면 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 좋아요 기능 성공"
            )
    })
    @PostMapping("/product/{productId}")
    public ResponseEntity<Void> updateProductLike(
            @PathVariable("productId") Long productId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        likeService.updateProductLike(productId, user.getMemberId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "브랜드 좋아요 기능", description = "브랜드에 대해 좋아요를 누르거나 이미 좋아요가 되있으면 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "브랜드 좋아요 기능 성공"
            )
    })
    @PostMapping("/brand/{brandId}")
    public ResponseEntity<Void> updateBrandLike(
            @PathVariable("brandId") Long brandId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        likeService.updateBrandLike(brandId, user.getMemberId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "좋아요한 상품 조회 기능", description = "좋아요를 누른 모든 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요한 상품목록 조회 성공"
            )
    })
    @GetMapping("/my/products")
    public ResponseEntity<List<LikedProductResponse>> getMyProducts(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(productService.getMyLikedProducts(user.getMemberId()));
    }
}
