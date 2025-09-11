package subscribenlike.mogupick.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import subscribenlike.mogupick.common.utils.GlobalLogger;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private static final PathPatternRequestMatcher.Builder prefix =
            PathPatternRequestMatcher.withDefaults().basePath("/api/v1");

    private final List<RequestMatcher> excluded = List.of(
            prefix.matcher("/auth/social-login"),
            prefix.matcher("/auth/login/OAuth2")
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excluded.stream().anyMatch(m -> m.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 경로 로깅
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullPath = queryString != null ? requestURI + "?" + queryString : requestURI;

        GlobalLogger.info("요청:", method, fullPath, "IP: ", request.getRemoteAddr());
        // 토큰 추출
        String token = resolveToken(request);

        // 토큰이 존재하고, 유효성 검사 후
        if (token != null && jwtProvider.validateToken(token)) {
            // 인증 정보 얻기
            Authentication authentication = jwtProvider.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        GlobalLogger.info("Token :", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
