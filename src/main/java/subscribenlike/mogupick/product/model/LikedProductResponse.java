package subscribenlike.mogupick.product.model;

import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductMedia;

public record LikedProductResponse(
        Long productId,
        String brandName,
        String productName,
        String imageUrl,
        int price,
        double starRate,
        int reviewCount
) {
    public static LikedProductResponse of(Product product, String imageUrl,
                                          double score, int reviewCount) {
        return new LikedProductResponse(product.getId(), product.getBrandName(), product.getName(),
                imageUrl, product.getPrice(), score, reviewCount);
    }
}
