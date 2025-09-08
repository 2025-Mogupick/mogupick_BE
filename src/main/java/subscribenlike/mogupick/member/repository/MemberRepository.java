package subscribenlike.mogupick.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.auth.common.exception.AuthErrorCode;
import subscribenlike.mogupick.auth.common.exception.AuthException;
import subscribenlike.mogupick.member.common.exception.MemberErrorCode;
import subscribenlike.mogupick.member.common.exception.MemberException;
import subscribenlike.mogupick.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByRefreshToken(String refreshToken);

    default Member findByEmailOrThrow(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    default Member findByRefreshTokenOrThrow(String refreshToken) {
        return findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_FOR_TOKEN));
    }
}