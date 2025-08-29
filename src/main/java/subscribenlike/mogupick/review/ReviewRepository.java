package subscribenlike.mogupick.review;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
