package subscribenlike.mogupick.member.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phoneNumber;

    private LocalDate birthDate;

    private boolean isAccepted;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String refreshToken;

    private String provider;

    @Column(unique = true)
    private String nickname;

    @Builder
    public Member(Long id, String name, String email, String password, String phoneNumber, LocalDate birthDate,
                  boolean isAccepted, MemberRole role, String provider, String nickname) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.isAccepted = isAccepted;
        this.role = role;
        this.provider = provider;
        this.nickname = nickname;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isMember() {
        return role == MemberRole.MEMBER;
    }
}
