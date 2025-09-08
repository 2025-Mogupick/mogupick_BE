package subscribenlike.mogupick.product.model;

import java.time.LocalDateTime;

public record FetchRecentlyViewProductResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review,
        long viewCount,
        LocalDateTime lastViewedAt
) {

    public static FetchRecentlyViewProductResponse of(FetchProductResponse product,
                                                      FetchBrandResponse brand,
                                                      FetchReviewResponse review,
                                                      long viewCount, LocalDateTime lastViewedAt) {
        return new FetchRecentlyViewProductResponse(product, brand, review, viewCount, lastViewedAt);
    }
}


