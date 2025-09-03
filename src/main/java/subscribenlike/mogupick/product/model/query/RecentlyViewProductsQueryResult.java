package subscribenlike.mogupick.product.model.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecentlyViewProductsQueryResult {
    private Long productId;
    private String productImageUrl;
    private String productName;
    private Integer productPrice;
    private Long brandId;
    private String brandName;
    private Double rating;
    private Long reviewCount;
    private Long viewCount;
    private LocalDateTime lastViewedAt;
}
