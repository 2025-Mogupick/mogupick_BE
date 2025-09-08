package subscribenlike.mogupick.like.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.like.domain.ProductLike;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    Optional<ProductLike> findByMemberIdAndProductId(Long memberId, Long productId);
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
