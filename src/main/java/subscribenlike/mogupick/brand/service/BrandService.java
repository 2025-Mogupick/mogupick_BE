package subscribenlike.mogupick.brand.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.dto.BrandCreateRequest;
import subscribenlike.mogupick.brand.dto.BrandResponse;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BrandResponse save(Long memberId, BrandCreateRequest brandCreateRequest) {
        Member member = memberRepository.findOrThrow(memberId);
        validateRole(member);
        Brand brand = brandCreateRequest.toEntity(member);
        return BrandResponse.from(brandRepository.save(brand));
    }

    private void validateRole(Member member) {
        if (!member.isSeller()) {
            throw new IllegalArgumentException("Member is not seller");
        }
    }

    public void delete(Long memberId, Long brandId) {
        Brand brand = brandRepository.findOrThrow(brandId);
        Member member = memberRepository.findOrThrow(memberId);
        validateIsOwner(brand, member);
        brandRepository.delete(brand);
    }

    private void validateIsOwner(Brand brand, Member member) {
        if (!Objects.equals(brand.getMember(), member)) {
            throw new IllegalArgumentException("Member is not own");
        }
    }
}
