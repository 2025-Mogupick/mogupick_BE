package subscribenlike.mogupick.member.dto;

import java.time.LocalDate;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.domain.MemberRole;

public record SignUpRequest(
        String email,
        String password,
        String name,
        LocalDate birthDate,
        String phoneNumber
) {
    public Member toEntity(String encryptedPassword) {
        return new Member(name, email, encryptedPassword, phoneNumber, birthDate, false, MemberRole.MEMBER);
    }
}
