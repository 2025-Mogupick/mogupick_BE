package subscribenlike.mogupick.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.auth.dto.TokenReissueDto;
import subscribenlike.mogupick.auth.service.AuthService;
import subscribenlike.mogupick.global.dto.ApiResponse;
import subscribenlike.mogupick.global.jwt.TokenInfo;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // TODO: 추후 자체 회원가입, 이메일/비밀번호 로그인, 토큰 재발급 API 구현 예정

    @PostMapping("/logout")
    public ApiResponse<Void> logout(Authentication authentication) {
        String email = authentication.getName();

        authService.logout(email);

        return ApiResponse.success(null);
    }

    @PostMapping("/reissue")
    public ApiResponse<TokenInfo> reissue(@RequestBody TokenReissueDto tokenReissueDto) {
        TokenInfo tokenInfo = authService.reissue(tokenReissueDto);
        return ApiResponse.success(tokenInfo);
    }
}