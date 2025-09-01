package subscribenlike.mogupick.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
  
    default Member findOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }
}
