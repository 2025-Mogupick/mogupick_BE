package subscribenlike.mogupick.product.model;

import subscribenlike.mogupick.product.domain.ProductOption;

import java.time.LocalDateTime;

public record FetchRecentlyViewProductResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review,
        FetchProductOptionResponse option,
        long viewCount,
        LocalDateTime lastViewedAt
) {

    public static FetchRecentlyViewProductResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review, FetchProductOptionResponse option, long viewCount, LocalDateTime lastViewedAt) {
        return new FetchRecentlyViewProductResponse(product, brand, review, option, viewCount, lastViewedAt);
    }
}


