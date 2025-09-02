package subscribenlike.mogupick.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.global.dto.ApiResponse;
import subscribenlike.mogupick.member.dto.MemberResponse;
import subscribenlike.mogupick.member.dto.MemberUpdateRequest;
import subscribenlike.mogupick.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ApiResponse<MemberResponse> getMyInfo(Authentication authentication) {
        String email = authentication.getName();
        MemberResponse memberInfo = memberService.getMemberInfo(email);
        return ApiResponse.success(memberInfo);
    }

    @PatchMapping("/me")
    public ApiResponse<Void> updateMyNickname(
            Authentication authentication,
            @Valid @RequestBody MemberUpdateRequest request) {
        String email = authentication.getName();
        memberService.updateNickname(email, request);
        return ApiResponse.success(null);
    }
}