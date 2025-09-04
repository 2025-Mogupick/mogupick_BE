package subscribenlike.mogupick.product.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FetchProductMostDailyViewStatChangeResponse {
    FetchProductDailyViewStatChangeResponse change;
    Long lastCountOfTime;

    public static FetchProductMostDailyViewStatChangeResponse of(
            FetchProductDailyViewStatChangeResponse change,
            Long lastCountOfTime) {
        return new FetchProductMostDailyViewStatChangeResponse(change, lastCountOfTime);
    }
}
