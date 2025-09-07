package subscribenlike.mogupick.product.model;

public record FetchSimilarProductResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review
) {

    public static FetchSimilarProductResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review) {
        return new FetchSimilarProductResponse(product, brand, review);
    }
}


