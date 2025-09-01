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
        return Member.builder()
                .name(김회원.name)
                .email(김회원.email)
                .password(김회원.password)
                .phoneNumber(김회원.phoneNumber)
                .birthDate(김회원.birthDate)
                .isAccepted(김회원.isAccepted)
                .role(김회원.role)
                .build();
    }
    public static Member 김모구() {
        return Member.builder()
                .name(김모구.name)
                .email(김모구.email)
                .password(김모구.password)
                .phoneNumber(김모구.phoneNumber)
                .birthDate(김모구.birthDate)
                .isAccepted(김모구.isAccepted)
                .role(김모구.role)
                .build();
    }
}
