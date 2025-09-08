package subscribenlike.mogupick.product.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import subscribenlike.mogupick.category.domain.SubCategory;
import subscribenlike.mogupick.product.common.ProductErrorCode;
import subscribenlike.mogupick.product.common.ProductException;
import subscribenlike.mogupick.product.domain.MemberProductViewCount;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductOption;
import subscribenlike.mogupick.product.model.FetchBrandResponse;
import subscribenlike.mogupick.product.model.FetchProductResponse;
import subscribenlike.mogupick.product.model.FetchReviewResponse;
import subscribenlike.mogupick.product.model.FetchSimilarProductResponse;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.product.repository.MemberProductViewCountRepository;
import subscribenlike.mogupick.product.repository.ProductMediaRepository;
import subscribenlike.mogupick.product.repository.ProductOptionRepository;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.review.repository.ReviewRepository;
import subscribenlike.mogupick.subscription.domain.Subscription;
import subscribenlike.mogupick.subscription.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SimilarProductFetchService {

    private final MemberProductViewCountRepository memberProductViewCountRepository;
    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;
    private final ProductOptionRepository productOptionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ReviewRepository reviewRepository;

    public Page<FetchSimilarProductResponse> fetchSimilarProduct(Long memberId, Pageable pageable) {
        List<Product> mostPurchasedSubCategoryProduct = fetchMostPurchasedSubCategoryProduct(memberId);
        List<Product> mostViewedSubCategoryProduct = fetchMostViewedSubCategoryProduct(memberId);

        List<Product> similarProduct = merge(mostPurchasedSubCategoryProduct, mostViewedSubCategoryProduct);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), similarProduct.size());

        List<FetchSimilarProductResponse> responses = similarProduct.subList(start, end).stream()
                .map(this::createFetchSimilarProductResponse)
                .toList();

        return new PageImpl<>(responses, pageable, similarProduct.size());
    }

    private FetchSimilarProductResponse createFetchSimilarProductResponse(Product product) {
        // 상품 정보 생성
        FetchProductResponse productResponse = FetchProductResponse.of(
                product.getId(),
                getProductImageUrl(product), // 상품 이미지 URL 조회
                product.getName(),
                product.getPrice(),
                product.getCreatedAt()
        );

        // 브랜드 정보 생성
        Brand brand = product.getBrand();
        FetchBrandResponse brandResponse = FetchBrandResponse.of(
                brand.getId(),
                brand.getName()
        );

        // 리뷰 정보 생성
        Double averageRating = reviewRepository.findAverageScoreByProductId(product.getId());
        Long reviewCount = reviewRepository.countByProductId(product.getId());
        FetchReviewResponse reviewResponse = FetchReviewResponse.of(averageRating, reviewCount);

        return FetchSimilarProductResponse.of(productResponse, brandResponse, reviewResponse);
    }

    private String getProductImageUrl(Product product) {
        return productMediaRepository.findFirstImageUrlByProductId(product.getId());
    }

    private static List<Product> merge(List<Product> firstProduct, List<Product> secondProduct) {
        return Stream.concat(
                firstProduct.stream(),
                secondProduct.stream()
        ).collect(Collectors.toMap(
                Product::getId,   // key: productId
                Function.identity(),
                (p1, p2) -> p1    // 중복 발생 시 앞의 것 유지
        )).values().stream().toList();
    }


    public List<Product> fetchMostPurchasedSubCategoryProduct(Long memberId){
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(memberId);

        Map<SubCategory, Long> subCategoryCountMap = new HashMap<>();

        subscriptions.forEach(subscription -> increaseSubCategoryCountByProduct(subCategoryCountMap, subscription.getProduct()));

        SubCategory mostPurchasedSubCategory = getMostCountsBySubCategory(subCategoryCountMap);
        
        List<Long> mostPurchasedSubCategoryProductIds = findProductIdsBySubCategory(mostPurchasedSubCategory);

        return productRepository.findAllByIdIn(mostPurchasedSubCategoryProductIds);
    }

    private List<Product> fetchMostViewedSubCategoryProduct(Long memberId){
        Map<SubCategory, Long> categoryCountMap = new HashMap<>();

        List<MemberProductViewCount> productViewCounts = memberProductViewCountRepository.findByMemberId(memberId);

        productViewCounts.forEach(viewCount->increaseBySubCategoryCountByMemberProductViewCount(categoryCountMap, viewCount));

        SubCategory mostViewCountsBySubCategory = getMostCountsBySubCategory(categoryCountMap);

        List<Long> mostViewCountsSubCategoryProductIds = findProductIdsBySubCategory(mostViewCountsBySubCategory);

        return productRepository.findAllByIdIn(mostViewCountsSubCategoryProductIds);
    }

    private void increaseSubCategoryCountByProduct(Map<SubCategory, Long> categoryCountMap, Product product) {
        ProductOption productOption = productOptionRepository.getByProductId(product.getId());
        SubCategory subCategory = productOption.getSubCategory();
        long increaseCount = 1L;

        increaseCategoryCount(categoryCountMap, subCategory, increaseCount);
    }

    private void increaseBySubCategoryCountByMemberProductViewCount(Map<SubCategory, Long> categoryCountMap, MemberProductViewCount viewCount) {
        ProductOption productOption = productOptionRepository.getByProductId(viewCount.getProduct().getId());
        SubCategory subCategory = productOption.getSubCategory();

        increaseCategoryCount(categoryCountMap, subCategory, viewCount.getViewCount());
    }

    private List<Long> findProductIdsBySubCategory(SubCategory subCategory) {
        return productOptionRepository.findAllBySubCategory(subCategory).stream()
                        .map(ProductOption::getProductId)
                        .toList();
    }

    private static void increaseCategoryCount(Map<SubCategory, Long> categoryCountMap, SubCategory subCategory, long count) {
        categoryCountMap.put(subCategory, categoryCountMap.getOrDefault(subCategory, 0L) + count);
    }

    private static SubCategory getMostCountsBySubCategory(Map<SubCategory, Long> categoryCountMap) {
        return categoryCountMap.entrySet()
                .stream()
                .max((k1, k2) -> Long.compare(k2.getValue(), k1.getValue()))
                .orElseThrow(() -> new ProductException(ProductErrorCode.SIMILAR_PRODUCT_NOT_FOUND))
                .getKey();
    }
}
