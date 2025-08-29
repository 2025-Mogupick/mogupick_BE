package subscribenlike.mogupick.member;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
