package subscribenlike.mogupick.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.global.dto.GlobalResponse;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.member.common.success.MemberSuccessCode;
import subscribenlike.mogupick.member.dto.MemberResponse;
import subscribenlike.mogupick.member.dto.MemberUpdateRequest;
import subscribenlike.mogupick.member.service.MemberService;

@Tag(name = "Member", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public GlobalResponse<MemberResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        MemberResponse memberInfo = memberService.getMemberInfo(email);
        return GlobalResponse.from(MemberSuccessCode.GET_MY_INFO_SUCCESS, memberInfo);
    }

    @PatchMapping("/me")
    public GlobalResponse<Void> updateMyNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MemberUpdateRequest request) {
        String email = userDetails.getUsername();
        memberService.updateNickname(email, request);
        return GlobalResponse.from(MemberSuccessCode.UPDATE_NICKNAME_SUCCESS);
    }
}