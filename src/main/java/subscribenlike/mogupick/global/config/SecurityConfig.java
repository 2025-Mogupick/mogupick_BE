package subscribenlike.mogupick.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import subscribenlike.mogupick.global.jwt.JwtAuthenticationFilter;
import subscribenlike.mogupick.global.jwt.JwtProvider;
import subscribenlike.mogupick.global.oauth.CustomOAuth2UserService;
import subscribenlike.mogupick.global.oauth.OAuth2LoginFailureHandler;
import subscribenlike.mogupick.global.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtProvider jwtProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    public SecurityConfig(@Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigurationSource,
                          JwtProvider jwtProvider,
                          CustomOAuth2UserService customOAuth2UserService,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
                          OAuth2LoginFailureHandler oAuth2LoginFailureHandler) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.jwtProvider = jwtProvider;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.oAuth2LoginFailureHandler = oAuth2LoginFailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 개발 편의상 일단 모두 허용

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 사용자 정보 처리
                        )
                        .successHandler(oAuth2LoginSuccessHandler) // 로그인 성공 시 JWT 발급
                        .failureHandler(oAuth2LoginFailureHandler) // 로그인 실패 시 처리
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}