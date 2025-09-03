package subscribenlike.mogupick.product.model;

public record FetchBrandResponse(
        Long brandId,
        String brandName
) {

    public static FetchBrandResponse of(Long brandId, String brandName) {
        return new FetchBrandResponse(brandId, brandName);
    }
}
