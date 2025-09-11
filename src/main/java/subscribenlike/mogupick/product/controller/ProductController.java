package subscribenlike.mogupick.product.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.auth.domain.PrincipalDetails;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.common.model.PaginatedResponse;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.product.common.ProductSuccessCode;
import subscribenlike.mogupick.product.model.*;
import subscribenlike.mogupick.product.service.BeginnerFriendlyProductFetchService;
import subscribenlike.mogupick.product.service.ConstantlyPopularProductFetchService;
import subscribenlike.mogupick.product.service.SimilarProductFetchService;
import subscribenlike.mogupick.product.service.ProductService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final SimilarProductFetchService similarProductFetchService;
    private final ConstantlyPopularProductFetchService constantlyPopularProductFetchService;
    private final BeginnerFriendlyProductFetchService beginnerFriendlyProductFetchService;

    @Operation(summary = "이번 달 새로나온 상품 조회", description = "이번 달 새로나온 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 목록 조회 성공"
            )
    })
    @GetMapping("/new")
    public ResponseEntity<SuccessResponse<PaginatedResponse<FetchNewProductsInMonthResponse>>> getNewSubscriptionProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        Pageable pageable = PageRequest.of(page, size);
        Page<FetchNewProductsInMonthResponse> response =
                productService.findAllNewProductsInMonth(now.getMonthValue(), pageable);

        return ResponseEntity
                .status(ProductSuccessCode.NEW_PRODUCTS_IN_MONTH_FETCHED.getStatus())
                .body(SuccessResponse.from(ProductSuccessCode.NEW_PRODUCTS_IN_MONTH_FETCHED, PaginatedResponse.from(response)));
    }

    @Operation(summary = "내 또래 상품 베스트 리뷰 조회", description = "내 또래 상품 베스트 리뷰를 상위 10개 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 목록 조회 성공"
            )
    })
    @GetMapping("/peer-best-reviews")
    public ResponseEntity<SuccessResponse<PaginatedResponse<FetchPeerBestReviewsResponse>>> fetchPeerBestReviews(
            @AuthenticationPrincipal PrincipalDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FetchPeerBestReviewsResponse> response =
                productService.fetchPeerBestReview(userDetails.getId(), pageable);

        return ResponseEntity
                .status(ProductSuccessCode.PEER_BEST_REVIEW_FETCHED.getStatus())
                .body(SuccessResponse.from(ProductSuccessCode.PEER_BEST_REVIEW_FETCHED, PaginatedResponse.from(response)));
    }

    @Operation(summary = "루트 카테고리에 대한 상품 목록 조회", description = "하나의 루트 카테고리에 대한 상품 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 조회 성공"
            )
    })
    @GetMapping("/category")
    public ResponseEntity<SuccessResponse<PaginatedResponse<FetchProductWithOptionResponse>>> getProductsByCategory(
            @RequestParam RootCategory rootCategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FetchProductWithOptionResponse> response =
                productService.findProductWithOptionByRootCategoryAsFetchResponse(rootCategory, pageable);

        return ResponseEntity
                .status(ProductSuccessCode.PRODUCT_GROUP_BY_ROOT_CATEGORY_FETCHED.getStatus())
                .body(SuccessResponse.from(ProductSuccessCode.PRODUCT_GROUP_BY_ROOT_CATEGORY_FETCHED, PaginatedResponse.from(response)));
    }


    @Operation(summary = "상품 등록(생성)하기", description = "상품을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "상품 등록 성공"
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Void>> createProduct(
            @ModelAttribute CreateProductRequest request) throws IOException {

        productService.createProduct(request);

        return ResponseEntity
                .status(ProductSuccessCode.PRODUCT_CREATED.getStatus())
                .body(SuccessResponse.from(ProductSuccessCode.PRODUCT_CREATED));
    }

    @Operation(summary = "멤버의 최근 본 상품 목록 조회", description = "멤버가 최근에 본 상품 목록을 조회합니다. lastViewedAt 기준 내림차순으로 정렬됩니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "최근 본 상품 목록 조회 성공"
            )
    })
    @GetMapping("/recently-viewed")
    public ResponseEntity<SuccessResponse<Page<FetchRecentlyViewProductResponse>>> getRecentlyViewedProducts(
            @AuthenticationPrincipal PrincipalDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<FetchRecentlyViewProductResponse> response =
                productService.fetchRecentlyViewedProducts(userDetails.getId(), pageable);

        return ResponseEntity
                .status(ProductSuccessCode.RECENTLY_VIEWED_PRODUCTS_FETCHED.getStatus())
                .body(SuccessResponse.from(ProductSuccessCode.RECENTLY_VIEWED_PRODUCTS_FETCHED, response));
    }

    @Operation(summary = "상품 상세 조회", description = "상품의 상세 정보를 조회합니다. 상품명, 브랜드명, 브랜드ID, 평균 평점, 리뷰 수, 가격 정보를 포함합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 상세 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "상품을 찾을 수 없음"
            )
    })
    @GetMapping("/{productId}/detail")
    public ResponseEntity<SuccessResponse<FetchProductDetailResponse>> getProductDetail(@PathVariable Long productId) {
        FetchProductDetailResponse response = productService.findProductDetailById(productId);

        return ResponseEntity
                .status(ProductSuccessCode.PRODUCT_DETAIL_FETCHED.getStatus())
                .body(SuccessResponse.from(ProductSuccessCode.PRODUCT_DETAIL_FETCHED, response));
    }

    @Operation(summary = "유사 상품 목록 조회", description = "회원의 구매 및 조회 패턴을 기반으로 유사한 상품 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "유사 상품 목록 조회 성공"
            )
    })
    @GetMapping("/similar")
    public ResponseEntity<SuccessResponse<PaginatedResponse<FetchSimilarProductResponse>>> getSimilarProducts(
            @AuthenticationPrincipal PrincipalDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FetchSimilarProductResponse> response =
                similarProductFetchService.fetchSimilarProduct(userDetails.getId(), pageable);

        return ResponseEntity
                .status(ProductSuccessCode.SIMILAR_PRODUCTS_FETCHED.getStatus())
                .body(SuccessResponse.from(ProductSuccessCode.SIMILAR_PRODUCTS_FETCHED, PaginatedResponse.from(response)));
    }

    @Operation(summary = "꾸준히 사랑받는 상품 목록 조회", description = "꾸준히 사랑받는 상품 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "꾸준히 사랑받는 상품 목록 조회 성공"
            )
    })
    @GetMapping("/constantly-popular")
    public ResponseEntity<SuccessResponse<PaginatedResponse<FetchConstantlyPopularProductResponse>>> getConstantlyPopularProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FetchConstantlyPopularProductResponse> response =
                constantlyPopularProductFetchService.fetchConstantlyPopularProducts(pageable);

        return ResponseEntity
                .status(ProductSuccessCode.CONSTANTLY_POPULAR_PRODUCTS_FETCHED.getStatus())
                .body(SuccessResponse.from(ProductSuccessCode.CONSTANTLY_POPULAR_PRODUCTS_FETCHED, PaginatedResponse.from(response)));
    }

    @Operation(summary = "입문용 상품 목록 조회", description = "처음 시작하기 좋은 입문용 상품 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "입문용 상품 목록 조회 성공"
            )
    })
    @GetMapping("/beginner-friendly")
    public ResponseEntity<SuccessResponse<PaginatedResponse<FetchBeginnerFriendlyProductResponse>>> getBeginnerFriendlyProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FetchBeginnerFriendlyProductResponse> response =
                beginnerFriendlyProductFetchService.fetchBeginnerFriendlyProducts(pageable);

        return ResponseEntity
                .status(ProductSuccessCode.BEGINNER_FRIENDLY_PRODUCTS_FETCHED.getStatus())
                .body(SuccessResponse.from(ProductSuccessCode.BEGINNER_FRIENDLY_PRODUCTS_FETCHED, PaginatedResponse.from(response)));
    }

}
