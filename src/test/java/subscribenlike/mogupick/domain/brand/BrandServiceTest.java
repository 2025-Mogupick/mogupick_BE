package subscribenlike.mogupick.domain.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.dto.BrandCreateRequest;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.brand.service.BrandService;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.BrandFixture;
import subscribenlike.mogupick.support.fixture.MemberFixture;

@ServiceTest
class BrandServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    BrandService brandService;

    @Test
    void 브랜드_저장할_수_있다() {
        Member member = MemberFixture.김회원();
        memberRepository.save(member);
        Brand brand = BrandFixture.쿠팡(member);

        brandRepository.save(brand);
        assertThat(brandRepository.count()).isEqualTo(1);
    }

    @Test
    void 권한이_사용자일_경우_브랜드_등록_실패() {
        Member member = MemberFixture.김모구();
        memberRepository.save(member);
        BrandCreateRequest brandCreateRequest = new BrandCreateRequest("모구픽");
        assertThatThrownBy(() -> brandService.save(member.getId(), brandCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 브랜드의_주인이_아니면_삭제할_수_없다() {
        Member owner = MemberFixture.김회원();
        memberRepository.save(owner);
        Brand brand = BrandFixture.쿠팡(owner);
        brandRepository.save(brand);
        Member member = MemberFixture.김모구();
        memberRepository.save(member);

        assertThatThrownBy(() -> brandService.delete(member.getId(), brand.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
