package subscribenlike.mogupick.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.category.CategoryService;
import subscribenlike.mogupick.category.domain.RootCategory;
import org.springframework.web.multipart.MultipartFile;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductMedia;
import subscribenlike.mogupick.product.domain.ProductOption;
import subscribenlike.mogupick.product.model.*;
import subscribenlike.mogupick.product.model.query.FetchPeerBestReviewsQueryResult;
import subscribenlike.mogupick.product.model.query.ProductsInMonthQueryResult;
import subscribenlike.mogupick.product.model.query.RecentlyViewProductsQueryResult;
import subscribenlike.mogupick.product.repository.*;
import subscribenlike.mogupick.review.repository.ReviewRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductViewCountRepository productViewCountRepository;
    private final MemberProductViewCountRepository memberProductViewCountRepository;
    private final ReviewRepository reviewRepository;
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;

    private final CategoryService categoryService;

    private final static int PEER_STANDARD_AGE = 5;

    public FetchProductDetailResponse findProductDetailById(Long productId) {
        Product product = productRepository.getById(productId);

        // 상품 이미지 URL 리스트 조회
        List<String> productImageUrls = productMediaRepository.findImageUrlsByProductId(productId);

        // 리뷰 평균 평점과 리뷰 수 조회
        Double averageRating = reviewRepository.findByProductId(productId).stream()
                .mapToDouble(review -> review.getScore())
                .average()
                .orElse(0.0);

        Long reviewCount = reviewRepository.countByProductId(productId);

        return FetchProductDetailResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productImageUrls(productImageUrls)
                .price(product.getPrice())
                .brandId(product.getBrand().getId())
                .brandName(product.getBrandName())
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .build();
    }

    public Page<FetchNewProductsInMonthResponse> findAllNewProductsInMonth(int month, Pageable pageable) {
        List<FetchNewProductsInMonthResponse> content = productRepository.findAllProductsInMonth(month).stream()
                .map(this::createFetchNewProductsInMonthResponse)
                .toList();

        // 현재는 전체 데이터를 가져와서 페이지네이션 적용 (추후 쿼리 레벨에서 최적화 가능)
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), content.size());
        List<FetchNewProductsInMonthResponse> pageContent = content.subList(start, end);

        return new PageImpl<>(pageContent, pageable, content.size());
    }

    public Page<FetchPeerBestReviewsResponse> fetchPeerBestReview(Long memberId, Pageable pageable) {
        Member member = memberRepository.findOrThrow(memberId);

        int myBirthDateYear = member.getBirthDate().getYear();

        int fromYear = myBirthDateYear - PEER_STANDARD_AGE;
        int toYear = myBirthDateYear + PEER_STANDARD_AGE;

        List<FetchPeerBestReviewsQueryResult> allResults =
                productRepository.fetchPeerBestReviewNative(fromYear, toYear, Integer.MAX_VALUE);

        List<FetchPeerBestReviewsResponse> content = allResults.stream()
                .map(FetchPeerBestReviewsResponse::from)
                .toList();

        // 현재는 전체 데이터를 가져와서 페이지네이션 적용 (추후 쿼리 레벨에서 최적화 가능)
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), content.size());
        List<FetchPeerBestReviewsResponse> pageContent = content.subList(start, end);

        return new PageImpl<>(pageContent, pageable, content.size());
    }

    @Transactional
    public void createProduct(CreateProductRequest request) throws IOException {
        Brand brand = brandRepository.findOrThrow(request.getBrandId());

        // 상품 생성
        Product product = createProduct(request, brand);

        // 다중 이미지 업로드 및 ProductMedia 생성
        uploadAndSaveProductImages(request.getImage(), product);

        createProductOption(request, product);
    }

    public ProductWithOptionResponse findProductWithOptionById(Long productId) {
        Product product = productRepository.getById(productId);
        ProductOption productOption = productOptionRepository.getByProductId(productId);

        return ProductWithOptionResponse.of(product, productOption);
    }

    public Page<ProductWithOptionResponse> findProductWithOptionByRootCategory(RootCategory rootCategory, Pageable pageable) {
        List<ProductOption> productOptions = productOptionRepository.findAllByRootCategory(rootCategory);
        List<Long> productIds = productOptions.stream()
                .map(ProductOption::getProductId)
                .toList();

        Map<Long, Product> products = productRepository.findAllByIdInMaps(productIds);

        List<ProductWithOptionResponse> content = productOptions.stream()
                .map(option -> createProductWithOptionResponse(products, option))
                .toList();

        // 현재는 전체 데이터를 가져와서 페이지네이션 적용 (추후 쿼리 레벨에서 최적화 가능)
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), content.size());
        List<ProductWithOptionResponse> pageContent = content.subList(start, end);

        return new PageImpl<>(pageContent, pageable, content.size());
    }


    public Page<RecentlyViewProductsQueryResult> fetchRecentlyViewedProducts(Long memberId, Pageable pageable) {
        return memberProductViewCountRepository.findRecentlyViewedProductsByMemberId(pageable, memberId);
    }

    private static ProductWithOptionResponse createProductWithOptionResponse(Map<Long, Product> products, ProductOption option) {
        return ProductWithOptionResponse.of(products.get(option.getProductId()), option);
    }

    private void uploadAndSaveProductImages(List<MultipartFile> images, Product product) throws IOException {
        if (images == null || images.isEmpty()) {
            return; // 이미지가 없는 경우 아무것도 하지 않음
        }

        List<ProductMedia> productMedias = images.stream()
                .filter(image -> !image.isEmpty())
                .map(image -> {
                    // TODO: 테스트용 더미 URL 생성, S3Bucket 적용
                    String imageUrl = "https://test-bucket.s3.amazonaws.com/" + image.getOriginalFilename();
                    return ProductMedia.builder()
                            .imageUrl(imageUrl)
                            .product(product)
                            .build();
                })
                .toList();

        if (!productMedias.isEmpty()) {
            productMediaRepository.saveAll(productMedias);
        }
    }

    private Product createProduct(CreateProductRequest request, Brand brand) {
        return productRepository.save(request.toProduct(brand));
    }

    private ProductOption createProductOption(CreateProductRequest request, Product product) {
        categoryService.validateCategoryOptionFromMap(request.getRootCategory(), request.getOptions());
        return productOptionRepository.save(request.toProductOption(product));
    }

    private FetchNewProductsInMonthResponse createFetchNewProductsInMonthResponse(ProductsInMonthQueryResult product) {
        // 상품 대표 이미지 URL 조회 (쿼리에서 가져온 값이 있으면 사용, 없으면 별도 조회)
        String productImageUrl = product.getProductImageUrl();
        if (productImageUrl == null) {
            productImageUrl = productMediaRepository.findFirstImageUrlByProductId(product.getProductId());
        }

        return FetchNewProductsInMonthResponse.of(
                FetchProductResponse.of(
                        product.getProductId(),
                        productImageUrl,
                        product.getProductName(),
                        product.getProductPrice(),
                        product.getCreatedAt()
                ),
                FetchBrandResponse.of(
                        product.getBrandId(),
                        product.getBrandName()
                ),
                FetchReviewResponse.of(
                        product.getRating(),
                        product.getReviewCount()
                )
        );
    }
}
