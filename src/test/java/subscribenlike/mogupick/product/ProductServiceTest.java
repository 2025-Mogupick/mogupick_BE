package subscribenlike.mogupick.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.brand.BrandFixture;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.model.FetchNewProductsInMonthResponse;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.product.service.ProductService;
import subscribenlike.mogupick.review.ReviewFixture;
import subscribenlike.mogupick.review.repository.ReviewRepository;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.support.annotation.ServiceTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void 이번_달_새로나온_구독_상품들을_조회할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        Review review = ReviewFixture.좋은리뷰(member, product);
        reviewRepository.save(review);

        // When
        List<FetchNewProductsInMonthResponse> result = productService.findAllNewProductsInMonth(LocalDateTime.now().getMonthValue());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).product().productId()).isEqualTo(product.getId());
        assertThat(result.get(0).product().productName()).isEqualTo("쿠팡 구독 서비스");
        assertThat(result.get(0).brand().brandName()).isEqualTo("쿠팡");
        assertThat(result.get(0).review().rating()).isEqualTo(4.5);
        assertThat(result.get(0).review().reviewCount()).isEqualTo(1);
    }

    @Test
    void 이번_달_새로나온_구독_상품이_없을_경우_빈_리스트를_반환한다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        // When - 다른 달로 조회
        List<FetchNewProductsInMonthResponse> result = productService.findAllNewProductsInMonth(12);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void 이번_달_새로나온_구독_상품_여러개를_조회할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand1 = BrandFixture.쿠팡(member);
        Brand brand2 = BrandFixture.네이버(member);
        brandRepository.save(brand1);
        brandRepository.save(brand2);

        Product product1 = ProductFixture.쿠팡구독(brand1);
        Product product2 = ProductFixture.네이버구독(brand2);
        productRepository.save(product1);
        productRepository.save(product2);

        Review review1 = ReviewFixture.좋은리뷰(member, product1);
        Review review2 = ReviewFixture.보통리뷰(member, product2);
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        // When
        List<FetchNewProductsInMonthResponse> result = productService.findAllNewProductsInMonth(LocalDateTime.now().getMonthValue());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("product.productName")
                .containsExactlyInAnyOrder("쿠팡 구독 서비스", "네이버 구독 서비스");
        assertThat(result).extracting("brand.brandName")
                .containsExactlyInAnyOrder("쿠팡", "네이버");
    }

    @Test
    void 리뷰가_없는_상품도_조회할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        // When
        List<FetchNewProductsInMonthResponse> result = productService.findAllNewProductsInMonth(LocalDateTime.now().getMonthValue());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).product().productId()).isEqualTo(product.getId());
        assertThat(result.get(0).review().rating()).isEqualTo(0.0);
        assertThat(result.get(0).review().reviewCount()).isEqualTo(0);
    }
}
