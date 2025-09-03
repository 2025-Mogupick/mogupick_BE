package subscribenlike.mogupick.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ApiResponse<MemberResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        MemberResponse memberInfo = memberService.getMemberInfo(email);
        return ApiResponse.success(memberInfo);
    }

    @PatchMapping("/me")
    public ApiResponse<Void> updateMyNickname(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody MemberUpdateRequest request) {
        String email = userDetails.getUsername();
        memberService.updateNickname(email, request);
        return ApiResponse.success(null);
    }
}