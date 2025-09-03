package subscribenlike.mogupick.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenReissueDto {
    private String refreshToken;
}
