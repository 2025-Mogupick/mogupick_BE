package subscribenlike.mogupick.support.fixture;

import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.member.domain.Member;

public enum BrandFixture {
    쿠팡("쿠팡");
    private String brandName;

    BrandFixture(String brandName) {
        this.brandName = brandName;
    }

    public static Brand 쿠팡(Member member) {
        return new Brand(쿠팡.brandName, member);
    }
}
