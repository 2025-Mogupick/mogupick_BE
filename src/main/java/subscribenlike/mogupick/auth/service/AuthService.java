package subscribenlike.mogupick.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.auth.dto.TokenReissueDto;
import subscribenlike.mogupick.global.jwt.JwtProvider;
import subscribenlike.mogupick.global.jwt.TokenInfo;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;

import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void logout(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        member.updateRefreshToken(null);
    }

    @Transactional
    public TokenInfo reissue(TokenReissueDto tokenReissueDto) {
        String refreshToken = tokenReissueDto.getRefreshToken();
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
        }

        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("해당 Refresh Token을 가진 사용자를 찾을 수 없습니다."));

        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null, Collections.singleton(authority));
        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

        member.updateRefreshToken(tokenInfo.getRefreshToken());

        return tokenInfo;
    }
}