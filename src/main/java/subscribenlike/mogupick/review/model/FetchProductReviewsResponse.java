package subscribenlike.mogupick.review.model;

import lombok.Builder;

import java.util.List;

@Builder
public record FetchProductReviewsResponse(
        Double productAverageRating,
        Long productReviewCount,
        List<ReviewResponse> reviews
) {
    @Builder
    public record ReviewResponse(
            Long reviewId,
            String memberName,
            String memberProfileImageUrl,
            Double reviewScore,
            String reviewContent,
            List<String> reviewImageUrls,
            Boolean isLikedByCurrentMember,
            Long likeCount,
            String timeAgo
    ) {
    }
}
