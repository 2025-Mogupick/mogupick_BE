package subscribenlike.mogupick.product.model;

import subscribenlike.mogupick.product.domain.ProductOption;

public record FetchProductWithOptionResponse(
        FetchProductResponse product,
        FetchBrandResponse brand,
        FetchReviewResponse review,
        FetchProductOptionResponse option
) {

    public static FetchProductWithOptionResponse of(FetchProductResponse product,
                                                   FetchBrandResponse brand,
                                                   FetchReviewResponse review,
                                                    FetchProductOptionResponse option) {
        return new FetchProductWithOptionResponse(product, brand, review, option);
    }
}
