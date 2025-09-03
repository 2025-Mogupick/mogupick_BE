package subscribenlike.mogupick.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribenlike.mogupick.review.domain.ReviewLike;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    
    /**
     * 특정 리뷰에 대한 좋아요 수를 조회합니다.
     */
    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review.id = :reviewId")
    long countByReviewId(@Param("reviewId") Long reviewId);
    
    /**
     * 특정 리뷰에 대한 모든 좋아요를 조회합니다.
     */
    List<ReviewLike> findByReviewId(Long reviewId);
    
    /**
     * 특정 회원이 특정 리뷰에 좋아요를 눌렀는지 확인합니다.
     */
    Optional<ReviewLike> findByReviewIdAndMemberId(Long reviewId, Long memberId);
    
    /**
     * 특정 회원이 좋아요한 모든 리뷰를 조회합니다.
     */
    List<ReviewLike> findByMemberId(Long memberId);

    /**
     * 여러 리뷰 ID에 대한 좋아요 목록을 조회합니다.
     */
    List<ReviewLike> findByReviewIdIn(List<Long> reviewIds);
}
