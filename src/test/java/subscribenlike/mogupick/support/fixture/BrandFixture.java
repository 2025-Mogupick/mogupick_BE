package subscribenlike.mogupick.support.fixture;

import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.member.domain.Member;

public enum BrandFixture {
    쿠팡("쿠팡"),
    네이버("네이버"),
    카카오("카카오");
    
    private String brandName;

    BrandFixture(String brandName) {
        this.brandName = brandName;
    }

    public static Brand 쿠팡(Member member) {
        return new Brand(쿠팡.brandName, member);
    }
    
    public static Brand 네이버(Member member) {
        return new Brand(네이버.brandName, member);
    }
    
    public static Brand 카카오(Member member) {
        return new Brand(카카오.brandName, member);
    }
}
