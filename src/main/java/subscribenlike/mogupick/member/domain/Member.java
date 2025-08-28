package subscribenlike.mogupick.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
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

    private String email;

    private String password;

    private String phoneNumber;

    private LocalDate birthDate;

    private boolean isAccepted;

    private MemberRole role;

    //todo social타입을 관리할건지? 논의가 필요해 보입니다!


    public Member(Long id, String name, String email, String password, String phoneNumber, LocalDate birthDate,
                  boolean isAccepted, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.isAccepted = isAccepted;
        this.role = role;
    }

    public Member(String name, String email, String password, String phoneNumber, LocalDate birthDate,
                  boolean isAccepted,
                  MemberRole role) {
        this(null, name, email, password, phoneNumber, birthDate, isAccepted, role);
    }

    public boolean isMember() {
        return role == MemberRole.MEMBER;
    }
}
