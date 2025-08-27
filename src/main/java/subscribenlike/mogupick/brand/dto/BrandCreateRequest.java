package subscribenlike.mogupick.brand.dto;

import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.member.domain.Member;

public record BrandCreateRequest(
        String brandName
) {
    public Brand toEntity(Member member) {
        return new Brand(brandName, member);
    }
}
