package subscribenlike.mogupick.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.auth.dto.TokenReissueDto;
import subscribenlike.mogupick.auth.service.AuthService;
import subscribenlike.mogupick.global.dto.GlobalResponse;
import subscribenlike.mogupick.global.jwt.TokenInfo;

@Tag(name = "Auth", description = "인증/인가 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그아웃", description = "사용자의 Access Token을 만료 처리하고 Refresh Token을 삭제하여 로그아웃합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공"
            )
    })
    @PostMapping("/logout")
    public GlobalResponse<Void> logout(Authentication authentication) {
        String email = authentication.getName();

        authService.logout(email);

        return GlobalResponse.success(null);
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
        return GlobalResponse.success(tokenInfo);
    }
}