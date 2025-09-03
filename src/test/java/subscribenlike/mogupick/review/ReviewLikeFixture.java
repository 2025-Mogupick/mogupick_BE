package subscribenlike.mogupick.review;

import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.review.domain.ReviewLike;

public enum ReviewLikeFixture {
    기본좋아요;

    public static ReviewLike 기본좋아요(Review review, Member member) {
        return new ReviewLike(review, member);
    }
}
