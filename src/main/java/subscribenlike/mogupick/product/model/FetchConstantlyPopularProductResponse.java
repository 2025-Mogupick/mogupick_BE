package subscribenlike.mogupick.product.model;

public record FetchConstantlyPopularProductResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review
) {

    public static FetchConstantlyPopularProductResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review) {
        return new FetchConstantlyPopularProductResponse(product, brand, review);
    }
}
