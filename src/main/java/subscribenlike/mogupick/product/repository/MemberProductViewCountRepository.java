package subscribenlike.mogupick.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.product.domain.MemberProductViewCount;

import java.util.Optional;

public interface MemberProductViewCountRepository extends JpaRepository<MemberProductViewCount, Long>, MemberProductViewCountQuerydslRepository {

    Optional<MemberProductViewCount> findByMemberIdAndProductId(Long memberId, Long productId);

    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
