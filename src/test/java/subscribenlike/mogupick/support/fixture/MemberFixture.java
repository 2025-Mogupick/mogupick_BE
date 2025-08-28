package subscribenlike.mogupick.support.fixture;

import java.time.LocalDate;
import lombok.Getter;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.domain.MemberRole;

@Getter
public enum MemberFixture {
    김회원("김회원", "test@gmail.com", "password", "010-xxxx-xxxx", LocalDate.of(1995, 5, 10), true, MemberRole.SELLER),
    김모구("김모구", "test@gmail.com", "password", "010-xxxx-xxxx", LocalDate.of(1995, 5, 10), true, MemberRole.MEMBER);
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate birthDate;
    private boolean isAccepted;
    private MemberRole role;

    MemberFixture(String name, String email, String password, String phoneNumber, LocalDate birthDate,
                  boolean isAccepted, MemberRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.isAccepted = isAccepted;
        this.role = role;
    }

    public static Member 김회원() {
        return new Member(김회원.name, 김회원.email, 김회원.password, 김회원.phoneNumber, 김회원.birthDate, 김회원.isAccepted, 김회원.role);
    }
    public static Member 김모구() {
        return new Member(김모구.name, 김모구.email, 김모구.password, 김모구.phoneNumber, 김모구.birthDate, 김모구.isAccepted, 김모구.role);
    }
}
