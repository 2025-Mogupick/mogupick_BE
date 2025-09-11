package subscribenlike.mogupick.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.auth.common.exception.AuthErrorCode;
import subscribenlike.mogupick.auth.common.exception.AuthException;
import subscribenlike.mogupick.auth.dto.SelfLoginRequest;
import subscribenlike.mogupick.auth.dto.SocialLoginRequest;
import subscribenlike.mogupick.auth.dto.TokenReissueDto;
import subscribenlike.mogupick.common.utils.GlobalLogger;
import subscribenlike.mogupick.global.jwt.JwtProvider;
import subscribenlike.mogupick.global.jwt.TokenInfo;
import subscribenlike.mogupick.global.oauth.OAuthAttributes;
import subscribenlike.mogupick.global.oauth.client.OAuthClient;
import subscribenlike.mogupick.member.common.exception.MemberErrorCode;
import subscribenlike.mogupick.member.common.exception.MemberException;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.dto.MemberLoginResponse;
import subscribenlike.mogupick.member.dto.SignUpRequest;
import subscribenlike.mogupick.member.dto.SignUpResponse;
import subscribenlike.mogupick.member.repository.MemberRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final Map<String, OAuthClient> clients;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider, List<OAuthClient> clients,
                       AuthenticationManager authenticationManager) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthClient::getProvider, Function.identity())
        );
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void logout(String email) {
        Member member = memberRepository.findByEmailOrThrow(email);
        member.updateRefreshToken(null);
    }

//    @Transactional
//    public TokenInfo reissue(TokenReissueDto tokenReissueDto) {
//        String refreshToken = tokenReissueDto.getRefreshToken();
//        if (!jwtProvider.validateToken(refreshToken)) {
//            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
//        }
//
//        Member member = memberRepository.findByRefreshTokenOrThrow(refreshToken);
//
//        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null,
//                Collections.singleton(authority));
//        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);
//
//        member.updateRefreshToken(tokenInfo.getRefreshToken());
//
//        return tokenInfo;
//    }
//
//    @Transactional
//    public TokenInfo socialLogin(SocialLoginRequest request) {
//        GlobalLogger.info(request.getProvider(), "AccessToken(Oauth): ", request.getAccessToken());
//        OAuthClient client = clients.get(request.getProvider());
//        if (client == null) {
//            throw new AuthException(AuthErrorCode.UNSUPPORTED_SOCIAL_LOGIN);
//        }
//
//        Map<String, Object> userAttributes = client.getOAuthUserAttributes(request.getAccessToken());
//        OAuthAttributes attributes = OAuthAttributes.of(request.getProvider(), userAttributes);
//        Member member = memberRepository.findByEmail(attributes.getEmail())
//                .orElseGet(() -> memberRepository.save(attributes.toEntity()));
//
//        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null,
//                Collections.singleton(authority));
//        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);
//
//        member.updateRefreshToken(tokenInfo.getRefreshToken());
//
//        return tokenInfo;
//    }

//    @Transactional
//    public TokenInfo oAuthLogin(SocialLoginRequest request) {
//        log.info("서비스 레이어 접근");
//        OAuthClient client = clients.get(request.getProvider());
//        if (client == null) {
//            throw new AuthException(AuthErrorCode.UNSUPPORTED_SOCIAL_LOGIN);
//        }
//
//        Map<String, Object> userAttributes = client.getOAuthUserAttributes(request.getAccessToken());
//        OAuthAttributes attributes = OAuthAttributes.of(request.getProvider(), userAttributes);
//        Member member = memberRepository.findByEmail(attributes.getEmail())
//                .orElseGet(() -> memberRepository.save(attributes.toEntity()));
//
//        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null,
//                Collections.singleton(authority));
//        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);
//
//        member.updateRefreshToken(tokenInfo.getRefreshToken());
//
//        return tokenInfo;
//    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        log.info("회원가입 성공");
        if (memberRepository.existsByEmail(request.email())) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = request.toEntity(encodedPassword);
        return SignUpResponse.from(memberRepository.save(member));
    }

    @Transactional
    public MemberLoginResponse login(SelfLoginRequest request) {
        log.info("로그인 시도 : ");
        Member member = memberRepository.findByEmailOrThrow(request.email());

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new RuntimeException("자격 증명에 실패하였습니다.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        member.updateRefreshToken(refreshToken);

        return new MemberLoginResponse(member.getId(), accessToken, refreshToken);
    }
}
