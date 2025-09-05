package subscribenlike.mogupick.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.auth.common.success.AuthSuccessCode;
import subscribenlike.mogupick.auth.dto.SocialLoginRequest;
import subscribenlike.mogupick.auth.dto.TokenReissueDto;
import subscribenlike.mogupick.auth.service.AuthService;
import subscribenlike.mogupick.global.dto.GlobalResponse;
import subscribenlike.mogupick.global.jwt.TokenInfo;
import subscribenlike.mogupick.global.security.CustomUserDetails;

@Tag(name = "Auth", description = "인증/인가 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "소셜 로그인", description = "소셜 플랫폼의 Access Token으로 로그인 처리 후 서비스 JWT를 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    @PostMapping("/social-login")
    public GlobalResponse<TokenInfo> socialLogin(@RequestBody SocialLoginRequest request) {
        TokenInfo tokenInfo = authService.socialLogin(request);
        return GlobalResponse.from(AuthSuccessCode.LOGIN_SUCCESS, tokenInfo);
    }

    @Operation(summary = "로그아웃", description = "사용자의 Access Token을 만료 처리하고 Refresh Token을 삭제하여 로그아웃합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공"
            )
    })
    @PostMapping("/logout")
    public GlobalResponse<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        authService.logout(email);
        return GlobalResponse.from(AuthSuccessCode.LOGOUT_SUCCESS);
    }

    @Operation(summary = "토큰 재발급", description = "만료된 Access Token과 Refresh Token을 사용하여 새로운 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공"
            )
    })
    @PostMapping("/reissue")
    public GlobalResponse<TokenInfo> reissue(@RequestBody TokenReissueDto tokenReissueDto) {
        TokenInfo tokenInfo = authService.reissue(tokenReissueDto);
        return GlobalResponse.from(AuthSuccessCode.REISSUE_SUCCESS, tokenInfo);
    }
}