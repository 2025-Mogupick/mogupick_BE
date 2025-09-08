package subscribenlike.mogupick.domain.like.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.like.repository.ProductLikeRepository;
import subscribenlike.mogupick.like.service.LikeService;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.ProductFixture;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.BrandFixture;
import subscribenlike.mogupick.support.fixture.MemberFixture;

@ServiceTest
class LikeServiceTest {
    @Autowired
    LikeService likeService;
    @Autowired
    ProductLikeRepository productLikeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BrandRepository brandRepository;

    @Test
    void 좋아요를_생성할_수_있다() {
        Member member = MemberFixture.김모구();
        memberRepository.save(member);
        Brand brand = BrandFixture.네이버(member);
        brandRepository.save(brand);
        Product product = ProductFixture.모구픽구독(brand);
        productRepository.save(product);

        likeService.updateProductLike(product.getId(), member.getId());
        assertThat(productLikeRepository.findAll()).hasSize(1);
    }

    @Test
    void 이미_좋아요를_한_상품일_경우_좋아요를_삭제한다() {
        Member member = MemberFixture.김모구();
        memberRepository.save(member);
        Brand brand = BrandFixture.네이버(member);
        brandRepository.save(brand);
        Product product = ProductFixture.모구픽구독(brand);
        productRepository.save(product);

        likeService.updateProductLike(product.getId(), member.getId());
        likeService.updateProductLike(product.getId(), member.getId());
        assertThat(productLikeRepository.findAll()).hasSize(0);
    }
}
