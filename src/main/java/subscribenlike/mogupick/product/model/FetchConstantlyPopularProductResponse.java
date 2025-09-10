package subscribenlike.mogupick.product.model;

import subscribenlike.mogupick.product.domain.ProductOption;

public record FetchConstantlyPopularProductResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review,
        ProductOption option

) {

    public static FetchConstantlyPopularProductResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review, ProductOption option) {
        return new FetchConstantlyPopularProductResponse(product, brand, review, option);
    }
}
