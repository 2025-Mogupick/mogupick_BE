package subscribenlike.mogupick.product.model;

import java.time.LocalDateTime;

public record FetchProductResponse(
        Long productId,
        String productImageUrl,
        String productName,
        int productPrice,
        LocalDateTime createdAt
) {

    public static FetchProductResponse of(Long productId, String productImageUrl, String productName, int productPrice, LocalDateTime createdAt) {
        return new FetchProductResponse(productId, productImageUrl, productName, productPrice, createdAt);
    }
}
