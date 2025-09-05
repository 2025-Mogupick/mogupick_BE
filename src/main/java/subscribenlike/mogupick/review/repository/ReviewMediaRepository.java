package subscribenlike.mogupick.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.review.domain.ReviewMedia;

import java.util.List;

public interface ReviewMediaRepository extends JpaRepository<ReviewMedia, Long> {

    List<ReviewMedia> findByReview(Review review);

    List<ReviewMedia> findByReviewId(Long reviewId);

    void deleteByReview(Review review);

    @Query("SELECT rm.imageUrl FROM ReviewMedia rm WHERE rm.review.id = :reviewId ORDER BY rm.createdAt ASC")
    List<String> findImageUrlsByReviewId(@Param("reviewId") Long reviewId);

    @Query("SELECT rm.imageUrl FROM ReviewMedia rm WHERE rm.review IN :reviews ORDER BY rm.review.id, rm.createdAt ASC")
    List<String> findImageUrlsByReviews(@Param("reviews") List<Review> reviews);
}
