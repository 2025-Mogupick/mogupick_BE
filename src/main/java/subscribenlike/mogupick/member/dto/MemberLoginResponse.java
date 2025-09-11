package subscribenlike.mogupick.member.dto;

public record MemberLoginResponse(
        Long memberId,
        String accessToken,
        String refreshToken
) {
}
