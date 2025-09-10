package subscribenlike.mogupick.product.model;

import subscribenlike.mogupick.product.domain.ProductOption;

public record FetchBeginnerFriendlyProductResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review,
        FetchProductOptionResponse option
) {

    public static FetchBeginnerFriendlyProductResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review, FetchProductOptionResponse option) {
        return new FetchBeginnerFriendlyProductResponse(product, brand, review, option);
    }
}
