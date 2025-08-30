package subscribenlike.mogupick.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subscribenlike.mogupick.product.model.FetchBrandResponse;
import subscribenlike.mogupick.product.model.FetchNewProductsInMonthResponse;
import subscribenlike.mogupick.product.model.FetchProductResponse;
import subscribenlike.mogupick.product.model.FetchReviewResponse;
import subscribenlike.mogupick.product.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository subscriptionRepository;

    public List<FetchNewProductsInMonthResponse> findAllNewProductsInMonth(int month) {
        return subscriptionRepository.findAllProductsInMonth(month).stream()
                .map(product -> FetchNewProductsInMonthResponse.of(
                                FetchProductResponse.of(
                                        product.getProductId(),
                                        product.getProductImageUrl(),
                                        product.getProductName(),
                                        product.getProductPrice(),
                                        product.getCreatedAt()
                                ),
                                FetchBrandResponse.of(
                                        product.getBrandId(),
                                        product.getBrandName()
                                ),
                                FetchReviewResponse.of(
                                        product.getRating() != null ? product.getRating() : 0.0,
                                        product.getReviewCount() != null ? product.getReviewCount().intValue() : 0
                                )
                        )
                )
                .toList();
    }
}
