package subscribenlike.mogupick.product.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor(access= AccessLevel.PRIVATE)
public class FetchProductDailyViewStatsResponse {
    Long productId;
    Map<String, Long> viewCountOfTimes;

    public static FetchProductDailyViewStatsResponse of(Long productId, Map<String, Long> viewCountOfTimes) {
        return new FetchProductDailyViewStatsResponse(productId, viewCountOfTimes);
    }
}
