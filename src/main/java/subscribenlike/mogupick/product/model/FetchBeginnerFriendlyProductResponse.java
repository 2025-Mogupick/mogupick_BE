package subscribenlike.mogupick.product.model;

import subscribenlike.mogupick.product.domain.ProductOption;

public record FetchBeginnerFriendlyProductResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review,
        ProductOption option
) {

    public static FetchBeginnerFriendlyProductResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review, ProductOption option) {
        return new FetchBeginnerFriendlyProductResponse(product, brand, review, option);
    }
}
