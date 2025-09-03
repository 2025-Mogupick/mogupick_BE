package subscribenlike.mogupick.member.dto;

import lombok.Getter;
import subscribenlike.mogupick.member.domain.Member;

@Getter
public class MemberResponse {

    private final String email;
    private final String name;
    private final String nickname;
    private final String provider;

    public MemberResponse(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.provider = member.getProvider();
    }
}