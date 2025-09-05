package subscribenlike.mogupick.global.oauth;

public record OAuthUserInfo(
        String provider,
        String email,
        String nickname
) {
}