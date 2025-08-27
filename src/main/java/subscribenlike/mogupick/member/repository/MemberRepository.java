package subscribenlike.mogupick.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    default Member findOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }
}
