package subscribenlike.mogupick.product.model;

public record FetchNewProductsInMonthResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review
) {

    public static FetchNewProductsInMonthResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review) {
        return new FetchNewProductsInMonthResponse(product, brand, review);
    }
}


