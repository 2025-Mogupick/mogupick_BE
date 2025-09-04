package subscribenlike.mogupick.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.brand.BrandFixture;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.model.query.ProductsInMonthQueryResult;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.review.ReviewFixture;
import subscribenlike.mogupick.review.repository.ReviewRepository;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.support.annotation.ServiceTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class ProductQuerydslRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void 이번_달_생성된_상품들을_조회할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        Review review = ReviewFixture.좋은리뷰(member, product);
        reviewRepository.save(review);

        int currentMonth = LocalDateTime.now().getMonthValue();
        System.out.println("Current month: " + currentMonth);
        System.out.println("Product created at: " + product.getCreatedAt());

        // When
        List<ProductsInMonthQueryResult> result = productRepository.findAllProductsInMonth(currentMonth);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(product.getId());
        assertThat(result.get(0).getProductName()).isEqualTo("쿠팡 구독 서비스");
        assertThat(result.get(0).getProductPrice()).isEqualTo(29900);
        assertThat(result.get(0).getBrandId()).isEqualTo(brand.getId());
        assertThat(result.get(0).getBrandName()).isEqualTo("쿠팡");
        assertThat(result.get(0).getRating()).isEqualTo(4.5);
        assertThat(result.get(0).getReviewCount()).isEqualTo(1);
    }

    @Test
    void 다른_달에_생성된_상품은_조회되지_않는다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        // When - 다른 달로 조회
        List<ProductsInMonthQueryResult> result = productRepository.findAllProductsInMonth(12);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void 이번_달_생성된_상품_여러개를_조회할_수_있다() {
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
        List<ProductsInMonthQueryResult> result = productRepository.findAllProductsInMonth(LocalDateTime.now().getMonthValue());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("productName")
                .containsExactlyInAnyOrder("쿠팡 구독 서비스", "네이버 구독 서비스");
        assertThat(result).extracting("brandName")
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
        List<ProductsInMonthQueryResult> result = productRepository.findAllProductsInMonth(LocalDateTime.now().getMonthValue());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(product.getId());
        assertThat(result.get(0).getRating()).isNull();
        assertThat(result.get(0).getReviewCount()).isEqualTo(0);
    }

    @Test
    void 여러_리뷰가_있는_상품의_평균_평점을_계산할_수_있다() {
        // Given
        Member member1 = ProductTestMemberFixture.김회원();
        Member member2 = ProductTestMemberFixture.김모구();
        memberRepository.save(member1);
        memberRepository.save(member2);

        Brand brand = BrandFixture.쿠팡(member1);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        Review review1 = ReviewFixture.좋은리뷰(member1, product); // 4.5점
        Review review2 = ReviewFixture.보통리뷰(member2, product); // 3.0점
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        // When
        List<ProductsInMonthQueryResult> result = productRepository.findAllProductsInMonth(LocalDateTime.now().getMonthValue());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRating()).isEqualTo(3.75); // (4.5 + 3.0) / 2
        assertThat(result.get(0).getReviewCount()).isEqualTo(2);
    }

    @Test
    void 간단한_상품_조회_테스트() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        // When - 모든 상품 조회 (월 필터 없이)
        List<Product> allProducts = productRepository.findAll();

        // Then
        assertThat(allProducts).hasSize(1);
        assertThat(allProducts.get(0).getName()).isEqualTo("쿠팡 구독 서비스");
    }
}
