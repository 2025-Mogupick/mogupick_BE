package subscribenlike.mogupick.product.model;

import lombok.Builder;

@Builder
public record FetchProductDetailResponse(
        Long productId,
        String productName,
        String productImageUrl,
        Integer price,
        Long brandId,
        String brandName,
        Double averageRating,
        Long reviewCount
) {
}
