package subscribenlike.mogupick.product.model.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductsInMonthQueryResult {
    private Long productId;
    private String productName;
    private Integer productPrice;
    private LocalDateTime createdAt;
    private Long brandId;
    private String brandName;
    private Double rating;
    private Long reviewCount;
    private String productImageUrl;
}
