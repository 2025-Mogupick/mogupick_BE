package subscribenlike.mogupick.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.global.dto.GlobalResponse;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.member.dto.MemberResponse;
import subscribenlike.mogupick.member.dto.MemberUpdateRequest;
import subscribenlike.mogupick.member.service.MemberService;

@Tag(name = "Member", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
class MemberController {

    private final MemberService memberService;

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "내 정보 조회 성공"
            ),
    })
    @GetMapping("/me")
    public GlobalResponse<MemberResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        MemberResponse memberInfo = memberService.getMemberInfo(email);
        return GlobalResponse.success(memberInfo);
    }

    @Operation(summary = "내 정보 수정 (닉네임)", description = "현재 로그인된 사용자의 닉네임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "닉네임 수정 성공"
            ),
    })
    @PatchMapping("/me")
    public GlobalResponse<Void> updateMyNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MemberUpdateRequest request) {
        Long memberId = userDetails.getMemberId();
        String email = userDetails.getUsername();
        memberService.updateNickname(email, request);
        return GlobalResponse.success(null);
    }
}