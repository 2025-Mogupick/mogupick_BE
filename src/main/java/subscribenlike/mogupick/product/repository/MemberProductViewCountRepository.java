package subscribenlike.mogupick.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.product.domain.MemberProductViewCount;
import subscribenlike.mogupick.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface MemberProductViewCountRepository extends JpaRepository<MemberProductViewCount, Long>, MemberProductViewCountQuerydslRepository {

    Optional<MemberProductViewCount> findByMemberIdAndProductId(Long memberId, Long productId);

    List<MemberProductViewCount> findByMemberId(Long memberId);

    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
