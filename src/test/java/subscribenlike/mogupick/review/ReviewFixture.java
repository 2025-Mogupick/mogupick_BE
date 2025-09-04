package subscribenlike.mogupick.review;

import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.review.domain.Review;

public enum ReviewFixture {
    좋은리뷰("정말 좋은 서비스입니다! 추천합니다.", 4.5),
    보통리뷰("나쁘지 않습니다. 만족합니다.", 3.0),
    나쁜리뷰("기대에 미치지 못합니다.", 2.0),
    최고리뷰("최고의 서비스입니다! 완벽합니다.", 5.0);

    private final String content;
    private final double score;

    ReviewFixture(String content, double score) {
        this.content = content;
        this.score = score;
    }

    public static Review 좋은리뷰(Member member, Product product) {
        return new Review(좋은리뷰.content, 좋은리뷰.score, member, product);
    }

    public static Review 보통리뷰(Member member, Product product) {
        return new Review(보통리뷰.content, 보통리뷰.score, member, product);
    }

    public static Review 나쁜리뷰(Member member, Product product) {
        return new Review(나쁜리뷰.content, 나쁜리뷰.score, member, product);
    }

    public static Review 최고리뷰(Member member, Product product) {
        return new Review(최고리뷰.content, 최고리뷰.score, member, product);
    }
}
