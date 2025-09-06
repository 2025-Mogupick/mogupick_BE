package subscribenlike.mogupick.auth.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.auth.common.exception.AuthErrorCode;
import subscribenlike.mogupick.auth.common.exception.AuthException;
import subscribenlike.mogupick.auth.dto.SocialLoginRequest;
import subscribenlike.mogupick.auth.dto.TokenReissueDto;
import subscribenlike.mogupick.global.jwt.JwtProvider;
import subscribenlike.mogupick.global.jwt.TokenInfo;
import subscribenlike.mogupick.global.oauth.OAuthAttributes;
import subscribenlike.mogupick.global.oauth.client.OAuthClient;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final Map<String, OAuthClient> clients;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider, List<OAuthClient> clients) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthClient::getProvider, Function.identity())
        );
    }

    @Transactional
    public void logout(String email) {
        Member member = memberRepository.findByEmailOrThrow(email);
        member.updateRefreshToken(null);
    }

    @Transactional
    public TokenInfo reissue(TokenReissueDto tokenReissueDto) {
        String refreshToken = tokenReissueDto.getRefreshToken();
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_FOR_TOKEN));

        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null, Collections.singleton(authority));
        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

        member.updateRefreshToken(tokenInfo.getRefreshToken());

        return tokenInfo;
    }

    @Transactional
    public TokenInfo socialLogin(SocialLoginRequest request) {
        OAuthClient client = clients.get(request.getProvider());
        if (client == null) {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_SOCIAL_LOGIN);
        }

        Map<String, Object> userAttributes = client.getOAuthUserAttributes(request.getAccessToken());

        OAuthAttributes attributes = OAuthAttributes.of(request.getProvider(), userAttributes);

        Member member = memberRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> memberRepository.save(attributes.toEntity()));

        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null, Collections.singleton(authority));
        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

        member.updateRefreshToken(tokenInfo.getRefreshToken());

        return tokenInfo;
    }
}
