package subscribenlike.mogupick.like.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.like.domain.BrandLike;
import subscribenlike.mogupick.like.domain.ProductLike;

public interface BrandLikeRepository extends JpaRepository<BrandLike, Long> {
    Optional<BrandLike> findByMemberIdAndBrandId(Long memberId, Long brandId);
}
