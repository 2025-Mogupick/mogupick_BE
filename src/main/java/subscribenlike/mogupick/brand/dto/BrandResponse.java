package subscribenlike.mogupick.brand.dto;

import subscribenlike.mogupick.brand.domain.Brand;

public record BrandResponse(
        Long brandId,
        String brandName
) {
    public static BrandResponse from(Brand brand) {
        return new BrandResponse(brand.getId(), brand.getName());
    }
}
