package subscribenlike.mogupick.product.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.product.domain.ProductOption;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FetchProductMostDailyViewStatChangeResponse {
    FetchProductDailyViewStatChangeResponse change;
    FetchProductResponse product;
    FetchBrandResponse brand;
    ProductOption option;
    Long lastCountOfTime;

    public static FetchProductMostDailyViewStatChangeResponse of(
            FetchProductDailyViewStatChangeResponse change,
            FetchProductResponse product,
            FetchBrandResponse brand,
            ProductOption option,
            Long lastCountOfTime
    ) {
        return new FetchProductMostDailyViewStatChangeResponse(change, product, brand, option, lastCountOfTime);
    }
}
