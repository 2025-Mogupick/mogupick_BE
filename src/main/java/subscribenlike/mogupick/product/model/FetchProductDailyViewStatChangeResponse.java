package subscribenlike.mogupick.product.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FetchProductDailyViewStatChangeResponse {
    Long productId;
    Double gradient;
    String startTime;
    String endTime;
    Long startViewCount;
    Long endViewCount;
    Double viewCountIncreaseRate;
}
