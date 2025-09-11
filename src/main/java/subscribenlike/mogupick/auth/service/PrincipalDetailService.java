package subscribenlike.mogupick.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import subscribenlike.mogupick.auth.domain.PrincipalDetails;
import subscribenlike.mogupick.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new PrincipalDetails(memberRepository.findByEmailOrThrow(email));
    }
}
