package subscribenlike.mogupick.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.model.FetchBrandResponse;
import subscribenlike.mogupick.product.model.FetchConstantlyPopularProductResponse;
import subscribenlike.mogupick.product.model.FetchProductResponse;
import subscribenlike.mogupick.product.model.FetchReviewResponse;
import subscribenlike.mogupick.product.repository.ProductMediaRepository;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.review.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConstantlyPopularProductFetchService {

    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;
    private final ReviewRepository reviewRepository;

    public Page<FetchConstantlyPopularProductResponse> fetchConstantlyPopularProducts(Pageable pageable) {
        // 현재는 productRepository.findAll()만 사용하여 모든 상품 조회
        List<Product> allProducts = productRepository.findAll();

        // 페이징 적용
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allProducts.size());
        List<Product> pagedProducts = allProducts.subList(start, end);

        List<FetchConstantlyPopularProductResponse> responses = pagedProducts.stream()
                .map(this::createFetchConstantlyPopularProductResponse)
                .toList();

        return new PageImpl<>(responses, pageable, allProducts.size());
    }

    private FetchConstantlyPopularProductResponse createFetchConstantlyPopularProductResponse(Product product) {
        // 상품 정보 생성
        FetchProductResponse productResponse = FetchProductResponse.of(
                product.getId(),
                getProductImageUrl(product),
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

        return FetchConstantlyPopularProductResponse.of(productResponse, brandResponse, reviewResponse);
    }

    private String getProductImageUrl(Product product) {
        return productMediaRepository.findFirstImageUrlByProductId(product.getId());
    }
}
