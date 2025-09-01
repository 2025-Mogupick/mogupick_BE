package subscribenlike.mogupick.member;

import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.domain.MemberRole;

import java.time.LocalDate;

public enum ProductTestMemberFixture {
    김회원("김회원", "kim@test.com", "password123", "010-1234-5678", LocalDate.of(1990, 1, 1), true, MemberRole.MEMBER),
    김모구("김모구", "mogupick@test.com", "password123", "010-9876-5432", LocalDate.of(1985, 5, 15), true, MemberRole.SELLER),
    관리자("관리자", "admin@test.com", "admin123", "010-1111-2222", LocalDate.of(1980, 10, 10), true, MemberRole.ADMIN);

    private final String name;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final LocalDate birthDate;
    private final boolean isAccepted;
    private final MemberRole role;

    ProductTestMemberFixture(String name, String email, String password, String phoneNumber, LocalDate birthDate, boolean isAccepted, MemberRole role) {
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

    public static Member 관리자() {
        return Member.builder()
                .name(관리자.name)
                .email(관리자.email)
                .password(관리자.password)
                .phoneNumber(관리자.phoneNumber)
                .birthDate(관리자.birthDate)
                .isAccepted(관리자.isAccepted)
                .role(관리자.role)
                .build();
    }
}
