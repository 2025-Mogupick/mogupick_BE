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
        return new Member(김회원.name, 김회원.email, 김회원.password, 김회원.phoneNumber, 김회원.birthDate, 김회원.isAccepted, 김회원.role);
    }

    public static Member 김모구() {
        return new Member(김모구.name, 김모구.email, 김모구.password, 김모구.phoneNumber, 김모구.birthDate, 김모구.isAccepted, 김모구.role);
    }

    public static Member 관리자() {
        return new Member(관리자.name, 관리자.email, 관리자.password, 관리자.phoneNumber, 관리자.birthDate, 관리자.isAccepted, 관리자.role);
    }
}
