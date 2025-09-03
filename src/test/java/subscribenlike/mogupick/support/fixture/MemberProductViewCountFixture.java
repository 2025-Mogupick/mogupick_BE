package subscribenlike.mogupick.support.fixture;

import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.product.domain.MemberProductViewCount;
import subscribenlike.mogupick.product.domain.Product;

public enum MemberProductViewCountFixture {
    회원상품조회1(5),
    회원상품조회2(10),
    회원상품조회3(3);

    private int viewCount;

    MemberProductViewCountFixture(int viewCount) {
        this.viewCount = viewCount;
    }

    public static MemberProductViewCount 회원상품조회1(Product product, Member member) {
        MemberProductViewCount viewCount = new MemberProductViewCount(product, member);
        for (int i = 0; i < 회원상품조회1.viewCount; i++) {
            viewCount.increase();
        }
        return viewCount;
    }

    public static MemberProductViewCount 회원상품조회2(Product product, Member member) {
        MemberProductViewCount viewCount = new MemberProductViewCount(product, member);
        for (int i = 0; i < 회원상품조회2.viewCount; i++) {
            viewCount.increase();
        }
        return viewCount;
    }

    public static MemberProductViewCount 회원상품조회3(Product product, Member member) {
        MemberProductViewCount viewCount = new MemberProductViewCount(product, member);
        for (int i = 0; i < 회원상품조회3.viewCount; i++) {
            viewCount.increase();
        }
        return viewCount;
    }
}
