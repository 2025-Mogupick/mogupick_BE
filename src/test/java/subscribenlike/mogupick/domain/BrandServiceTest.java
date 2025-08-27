package subscribenlike.mogupick.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
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

    @Test
    void 브랜드_저장할_수_있다() {
        Member member = MemberFixture.김회원();
        memberRepository.save(member);
        Brand brand = BrandFixture.쿠팡(member);

        brandRepository.save(brand);
        assertThat(brandRepository.count()).isEqualTo(1);
    }
}
