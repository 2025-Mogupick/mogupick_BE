package subscribenlike.mogupick.review.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribenlike.mogupick.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    long countByProductId(Long productId);

    @Query("SELECT AVG(r.score) FROM Review r WHERE r.product.id = :productId")
    Double findAverageScoreByProductId(@Param("productId") Long productId);
}
