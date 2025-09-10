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

        GlobalLogger.info("요청:",method, fullPath, "IP: ", request.getRemoteAddr());
        String token = resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            GlobalLogger.info("요청 인증 헤더:", request.getHeader("Authorization"));
            return bearerToken.substring(7);
        }
        return null;
    }
}