package subscribenlike.mogupick.auth.dto;

public record SelfLoginRequest(
        String email,
        String password
) {
}
