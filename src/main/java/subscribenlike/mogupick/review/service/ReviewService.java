package subscribenlike.mogupick.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.review.domain.ReviewLike;
import subscribenlike.mogupick.review.model.FetchProductReviewsResponse;
import subscribenlike.mogupick.review.repository.ReviewLikeRepository;
import subscribenlike.mogupick.review.repository.ReviewRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public Page<FetchProductReviewsResponse.ReviewResponse> getProductReviews(Long productId, Long currentMemberId, Pageable pageable) {
        Product product = productRepository.getById(productId);

        List<Review> reviews = reviewRepository.findByProductId(productId);

        // 리뷰 ID 리스트 추출
        List<Long> reviewIds = reviews.stream()
                .map(Review::getId)
                .toList();

        // 각 리뷰의 좋아요 수 맵 생성
        Map<Long, Long> likeCountMap = reviewLikeRepository.findByReviewIdIn(reviewIds).stream()
                .collect(Collectors.groupingBy(
                        reviewLike -> reviewLike.getReview().getId(),
                        Collectors.counting()
                ));

        // 현재 회원이 좋아요한 리뷰 ID 리스트
        List<Long> likedReviewIds = reviewLikeRepository.findByMemberId(currentMemberId).stream()
                .map(reviewLike -> reviewLike.getReview().getId())
                .toList();

        // 페이지네이션 적용
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), reviews.size());
        List<Review> paginatedReviews = reviews.subList(start, end);

        List<FetchProductReviewsResponse.ReviewResponse> reviewResponses = paginatedReviews.stream()
                .map(review -> createReviewResponse(review, likeCountMap, likedReviewIds))
                .toList();

        return new PageImpl<>(reviewResponses, pageable, reviews.size());
    }

    private FetchProductReviewsResponse.ReviewResponse createReviewResponse(
            Review review,
            Map<Long, Long> likeCountMap,
            List<Long> likedReviewIds) {

        Long likeCount = likeCountMap.getOrDefault(review.getId(), 0L);
        Boolean isLiked = likedReviewIds.contains(review.getId());
        String timeAgo = calculateTimeAgo(review.getCreatedAt());

        return FetchProductReviewsResponse.ReviewResponse.builder()
                .reviewId(review.getId())
                .memberName(review.getMember().getName())
                .memberProfileImageUrl(review.getMember().getProfileImage())
                .reviewScore(review.getScore())
                .reviewContent(review.getContent())
                .reviewImageUrl(review.getReviewImage())
                .isLikedByCurrentMember(isLiked)
                .likeCount(likeCount)
                .timeAgo(timeAgo)
                .build();
    }

    private String calculateTimeAgo(LocalDateTime createdAt) {
        Duration duration = Duration.between(createdAt, LocalDateTime.now());

        long hours = duration.toHours();
        if (hours < 1) {
            return "방금 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else {
            long days = duration.toDays();
            return days + "일 전";
        }
    }

    public FetchProductReviewsResponse getProductReviewsWithStats(Long productId, Long currentMemberId, Pageable pageable) {
        // 상품의 평균 평점과 리뷰 수 계산
        List<Review> allReviews = reviewRepository.findByProductId(productId);

        Double averageRating = allReviews.stream()
                .mapToDouble(Review::getScore)
                .average()
                .orElse(0.0);

        Long reviewCount = (long) allReviews.size();

        // 리뷰 목록 조회 (페이지네이션 적용)
        Page<FetchProductReviewsResponse.ReviewResponse> reviewPage = getProductReviews(productId, currentMemberId, pageable);

        return FetchProductReviewsResponse.builder()
                .productAverageRating(averageRating)
                .productReviewCount(reviewCount)
                .reviews(reviewPage.getContent())
                .build();
    }
}
