package subscribenlike.mogupick.brand;

import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.member.domain.Member;

public enum BrandFixture {
    쿠팡("쿠팡"),
    네이버("네이버"),
    카카오("카카오"),
    모구픽("모구픽");

    private final String name;

    BrandFixture(String name) {
        this.name = name;
    }

    public static Brand 쿠팡(Member member) {
        return new Brand(쿠팡.name, member);
    }

    public static Brand 네이버(Member member) {
        return new Brand(네이버.name,  member);
    }

    public static Brand 카카오(Member member) {
        return new Brand(카카오.name,  member);
    }

    public static Brand 모구픽(Member member) {
        return new Brand(모구픽.name,  member);
    }
}
