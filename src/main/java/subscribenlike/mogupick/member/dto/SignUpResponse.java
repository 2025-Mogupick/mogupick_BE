package subscribenlike.mogupick.member.dto;

import subscribenlike.mogupick.member.domain.Member;

public record SignUpResponse(
        Long id
) {
    public static SignUpResponse from(Member member) {
        return new SignUpResponse(member.getId());
    }
}
