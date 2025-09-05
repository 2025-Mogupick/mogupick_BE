package subscribenlike.mogupick.product.model.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RecentlyViewProductsQueryResult {
    private Long productId;
    private List<String> productImageUrls;
    private String productName;
    private Integer productPrice;
    private Long brandId;
    private String brandName;
    private Double rating;
    private Long reviewCount;
    private Long viewCount;
    private LocalDateTime lastViewedAt;
}
