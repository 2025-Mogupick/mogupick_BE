package subscribenlike.mogupick.product.model;

import subscribenlike.mogupick.product.domain.ProductOption;

public record FetchNewProductsInMonthResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review,
        FetchProductOptionResponse option
) {

    public static FetchNewProductsInMonthResponse of(FetchProductResponse product, FetchBrandResponse brand, FetchReviewResponse review, FetchProductOptionResponse option) {
        return new FetchNewProductsInMonthResponse(product, brand, review, option);
    }
}


