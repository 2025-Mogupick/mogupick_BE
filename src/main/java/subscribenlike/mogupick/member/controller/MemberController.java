package subscribenlike.mogupick.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.global.dto.ApiResponse;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.member.dto.MemberResponse;
import subscribenlike.mogupick.member.dto.MemberUpdateRequest;
import subscribenlike.mogupick.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ApiResponse<MemberResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername(); 
        MemberResponse memberInfo = memberService.getMemberInfo(email);
        return ApiResponse.success(memberInfo);
    }

    @PatchMapping("/me")
    public ApiResponse<Void> updateMyNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MemberUpdateRequest request) {
        Long memberId = userDetails.getMemberId();
        String email = userDetails.getUsername();
        memberService.updateNickname(email, request);
        return ApiResponse.success(null);
    }
}