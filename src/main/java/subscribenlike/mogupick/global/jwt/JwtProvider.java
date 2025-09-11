package subscribenlike.mogupick.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import subscribenlike.mogupick.auth.service.PrincipalDetailService;
import subscribenlike.mogupick.global.oauth.OAuthAttributes;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final MemberRepository memberRepository;
    private final long accessTokenValidityInMilliseconds = 1000 * 60 * 30; // Access Token: 30분
    private final long refreshTokenValidityInMilliseconds = 1000 * 60 * 60 * 24 * 7; // Refresh Token: 7일
    private final PrincipalDetailService principalDetailService;
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60;
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7;

//////    public TokenInfo generateToken(Authentication authentication) {
//////        String authorities = authentication.getAuthorities().stream()
//////                .map(GrantedAuthority::getAuthority)
//////                .collect(Collectors.joining(","));
//////
//////        long now = (new Date()).getTime();
//////
//////        String email;
//////        if (authentication.getPrincipal() instanceof OAuth2User) {
//////            String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
//////            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//////            OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
//////            email = attributes.getEmail();
//////        }
//////        else if (authentication.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {
//////            email = (String) authentication.getPrincipal();
//////        }
//////        else {
//////            email = authentication.getName();
//////        }
//////
//////        if (email == null) {
//////            throw new RuntimeException("사용자 이메일 정보를 가져올 수 없습니다.");
//////        }
//////
//////        Member member = memberRepository.findByEmailOrThrow(email);
//////
//////        // Access Token 생성
//////        Date accessTokenExpiresIn = new Date(now + accessTokenValidityInMilliseconds);
//////        String accessToken = Jwts.builder()
//////                .setSubject(email)
//////                .claim("auth", authorities)
//////                .claim("memberId", member.getId())
//////                .setExpiration(accessTokenExpiresIn)
//////                .signWith(key, SignatureAlgorithm.HS256)
//////                .compact();
//////
//////        // Refresh Token 생성
//////        String refreshToken = Jwts.builder()
//////                .setExpiration(new Date(now + refreshTokenValidityInMilliseconds))
//////                .signWith(key, SignatureAlgorithm.HS256)
//////                .compact();
//////
//////        return TokenInfo.builder()
//////                .grantType("Bearer")
//////                .accessToken(accessToken)
//////                .refreshToken(refreshToken)
//////                .build();
//////    }
//////
//////    public Authentication getAuthentication(String accessToken) {
//////        Claims claims = parseClaims(accessToken);
//////
//////        if (claims.get("auth") == null) {
//////            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//////        }
//////
//////        Collection<? extends GrantedAuthority> authorities =
//////                Arrays.stream(claims.get("auth").toString().split(","))
//////                        .map(SimpleGrantedAuthority::new)
//////                        .collect(Collectors.toList());
//////
//////        Long memberId = claims.get("memberId", Long.class);
//////        UserDetails principal = new CustomUserDetails(memberId, claims.getSubject(), "", authorities);
//////
//////        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//////    }
////
////    public boolean validateToken(String token) {
////        try {
////            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
////            return true;
////        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
////            log.info("Invalid JWT Token", e);
////        } catch (ExpiredJwtException e) {
////            log.info("Expired JWT Token", e);
////        } catch (UnsupportedJwtException e) {
////            log.info("Unsupported JWT Token", e);
////        } catch (IllegalArgumentException e) {
////            log.info("JWT claims string is empty.", e);
////        }
////        return false;
////    }
//
//    private Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }

    // Access Token 생성
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_VALIDITY);
    }

    // Refresh Token 생성
    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, REFRESH_TOKEN_VALIDITY);
    }

    private String generateToken(Authentication authentication, long validityDuration) {
        String username = authentication.getName();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityDuration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 토큰에서 사용자 이메일(또는 사용자 고유 ID) 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        UserDetails userDetails = principalDetailService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
