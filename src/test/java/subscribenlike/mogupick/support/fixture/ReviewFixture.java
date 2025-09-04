package subscribenlike.mogupick.support.fixture;

import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.review.domain.Review;

public enum ReviewFixture {
    상품리뷰1("정말 좋은 상품입니다!", 4.5),
    상품리뷰2("배송이 빨라서 좋았어요", 5.0),
    상품리뷰3("품질이 우수합니다", 4.0);

    private String content;
    private double score;

    ReviewFixture(String content, double score) {
        this.content = content;
        this.score = score;
    }

    public static Review 상품리뷰1(Member member, Product product) {
        return new Review(
                상품리뷰1.content,
                상품리뷰1.score,
                member,
                product
        );
    }

    public static Review 상품리뷰2(Member member, Product product) {
        return new Review(
                상품리뷰2.content,
                상품리뷰2.score,
                member,
                product
        );
    }

    public static Review 상품리뷰3(Member member, Product product) {
        return new Review(
                상품리뷰3.content,
                상품리뷰3.score,
                member,
                product
        );
    }
}
