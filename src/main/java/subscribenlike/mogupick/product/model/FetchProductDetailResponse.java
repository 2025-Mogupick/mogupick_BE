package subscribenlike.mogupick.product.model;

import lombok.Builder;

import java.util.List;

@Builder
public record FetchProductDetailResponse(
        Long productId,
        String productName,
        List<String> productImageUrls,
        Integer price,
        Long brandId,
        String brandName,
        Double averageRating,
        Long reviewCount
) {
}
