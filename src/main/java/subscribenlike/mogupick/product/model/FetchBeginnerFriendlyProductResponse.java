package subscribenlike.mogupick.product.model;

public record FetchBeginnerFriendlyProductResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review
) {

    public static FetchBeginnerFriendlyProductResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review) {
        return new FetchBeginnerFriendlyProductResponse(product, brand, review);
    }
}
