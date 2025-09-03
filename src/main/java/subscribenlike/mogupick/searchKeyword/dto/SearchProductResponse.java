package subscribenlike.mogupick.searchKeyword.dto;

import subscribenlike.mogupick.product.domain.Product;

public record SearchProductResponse(
        Long id,
        String name,
        int price,
        String imageUrl,
        String brandName,
        double rating,
        int reviewCount
) {
    public static SearchProductResponse of(Product product, double rating, int reviewCount) {
        return new SearchProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(),
                product.getBrandName(), rating, reviewCount);
    }
}
