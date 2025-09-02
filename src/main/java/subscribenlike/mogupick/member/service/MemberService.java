package subscribenlike.mogupick.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.dto.MemberResponse;
import subscribenlike.mogupick.member.dto.MemberUpdateRequest;
import subscribenlike.mogupick.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMemberInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        return new MemberResponse(member);
    }

    @Transactional
    public void updateNickname(String email, MemberUpdateRequest request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        member.updateNickname(request.getNickname());
    }
}