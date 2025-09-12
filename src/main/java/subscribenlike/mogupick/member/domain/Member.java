package subscribenlike.mogupick.member.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

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

    private String profileImage;

    @Column(unique = true)
    private String customerKey;

    @PrePersist
    private void prePersist() {
        ensureCustomerKey(); // 신규 생성 시 자동 발급
    }

    @Builder
    public Member(Long id, String name, String email, String password, String phoneNumber, LocalDate birthDate,
                  boolean isAccepted, MemberRole role, String provider, String nickname, String profileImage) {

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
        this.profileImage = profileImage;
    }

    public Member(String name, String email, String password, String phoneNumber, LocalDate birthDate,
                  boolean isAccepted, MemberRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.isAccepted = isAccepted;
        this.role = role;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String nickname) {
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
    }

    public void ensureCustomerKey() {
        if (this.customerKey == null) {
            this.customerKey = UUID.randomUUID().toString();
        }
    }
  
    public boolean isMember() {
        return role == MemberRole.MEMBER;
    }
}
