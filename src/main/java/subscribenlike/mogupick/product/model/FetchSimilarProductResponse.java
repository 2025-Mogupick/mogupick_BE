package subscribenlike.mogupick.product.model;

import subscribenlike.mogupick.product.domain.ProductOption;

public record FetchSimilarProductResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review,
        FetchProductOptionResponse option
) {

    public static FetchSimilarProductResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review, FetchProductOptionResponse option) {
        return new FetchSimilarProductResponse(product, brand, review, option);
    }
}


