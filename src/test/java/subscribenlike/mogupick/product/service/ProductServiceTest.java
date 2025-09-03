package subscribenlike.mogupick.product.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import subscribenlike.mogupick.brand.BrandFixture;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.ProductFixture;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.model.FetchPeerBestReviewsResponse;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.review.ReviewFixture;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.review.repository.ReviewRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("존재하지 않는 회원 ID로 조회할 경우 예외가 발생한다")
    void 존재하지_않는_회원_ID로_조회할_경우_예외가_발생한다() {
        // Given
        Long nonExistentMemberId = 999L;
        
        // When & Then
        assertThatThrownBy(() -> productService.fetchPeerBestReview(nonExistentMemberId, 10))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("not found");
    }

    @Test
    @DisplayName("회원을 저장할 수 있다")
    void 회원을_저장할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        
        // When
        Member savedMember = memberRepository.save(member);
        
        // Then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getName()).isEqualTo("김회원");
        assertThat(savedMember.getEmail()).isEqualTo("kim@test.com");
        assertThat(savedMember.getBirthDate().getYear()).isEqualTo(1990);
    }

    @Test
    @DisplayName("브랜드를 저장할 수 있다")
    void 브랜드를_저장할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        member = memberRepository.save(member);
        
        Brand brand = BrandFixture.쿠팡(member);
        
        // When
        Brand savedBrand = brandRepository.save(brand);
        
        // Then
        assertThat(savedBrand.getId()).isNotNull();
        assertThat(savedBrand.getName()).isEqualTo("쿠팡");
        assertThat(savedBrand.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("상품을 저장할 수 있다")
    void 상품을_저장할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        member = memberRepository.save(member);
        
        Brand brand = BrandFixture.쿠팡(member);
        brand = brandRepository.save(brand);
        
        Product product = ProductFixture.쿠팡구독(brand);
        
        // When
        Product savedProduct = productRepository.save(product);
        
        // Then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("쿠팡 구독 서비스");
        assertThat(savedProduct.getPrice()).isEqualTo(29900);
        assertThat(savedProduct.getBrand().getId()).isEqualTo(brand.getId());
    }

    @Test
    @DisplayName("리뷰를 저장할 수 있다")
    void 리뷰를_저장할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        member = memberRepository.save(member);
        
        Brand brand = BrandFixture.쿠팡(member);
        brand = brandRepository.save(brand);
        
        Product product = ProductFixture.쿠팡구독(brand);
        product = productRepository.save(product);
        
        Review review = ReviewFixture.좋은리뷰(member, product);
        
        // When
        Review savedReview = reviewRepository.save(review);
        
        // Then
        assertThat(savedReview.getId()).isNotNull();
        assertThat(savedReview.getContent()).isEqualTo("정말 좋은 서비스입니다! 추천합니다.");
        assertThat(savedReview.getScore()).isEqualTo(4.5);
        assertThat(savedReview.getMember().getId()).isEqualTo(member.getId());
        assertThat(savedReview.getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("내 또래 베스트 리뷰를 조회할 수 있다")
    void 내_또래_베스트_리뷰를_조회할_수_있다() {
        // Given: 테스트 데이터 생성 및 저장
        Member member1 = ProductTestMemberFixture.김회원(); // 1990년생
        member1 = memberRepository.save(member1);
        
        Member member2 = ProductTestMemberFixture.김모구(); // 1985년생 (또래 범위 내)
        member2 = memberRepository.save(member2);
        
        Member member3 = ProductTestMemberFixture.관리자(); // 1980년생 (또래 범위 밖)
        member3 = memberRepository.save(member3);

        Brand brand1 = BrandFixture.쿠팡(member1);
        brandRepository.save(brand1);
        
        Brand brand2 = BrandFixture.네이버(member2);
        brandRepository.save(brand2);

        Product product1 = ProductFixture.쿠팡구독(brand1);
        productRepository.save(product1);
        
        Product product2 = ProductFixture.네이버구독(brand2);
        productRepository.save(product2);

        // 리뷰 생성 (좋아요 수가 높은 순으로 정렬되도록)
        Review review1 = ReviewFixture.최고리뷰(member1, product1); // 5.0점
        reviewRepository.save(review1);
        
        Review review2 = ReviewFixture.좋은리뷰(member2, product2); // 4.5점
        reviewRepository.save(review2);
        
        Review review3 = ReviewFixture.보통리뷰(member3, product1); // 3.0점 (또래 범위 밖)
        reviewRepository.save(review3);

        // When: 내 또래 베스트 리뷰 조회 (김회원 기준, 1990년생)
        List<FetchPeerBestReviewsResponse> result = productService.fetchPeerBestReview(member1.getId(), 10);

        // Then: 결과 검증
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2); // 1990년생과 1985년생의 리뷰만 포함
        
        // 첫 번째 결과는 좋아요 수가 높은 순으로 정렬되어야 함
        FetchPeerBestReviewsResponse firstResult = result.get(0);
        assertThat(firstResult.getProduct().getProductId()).isEqualTo(product1.getId());
        assertThat(firstResult.getProduct().getProductName()).isEqualTo("쿠팡 구독 서비스");
        assertThat(firstResult.getProduct().getPrice()).isEqualTo(29900);
        assertThat(firstResult.getProduct().getBrandName()).isEqualTo("쿠팡");
        
        assertThat(firstResult.getMember().getMemberName()).isEqualTo("김회원");
        assertThat(firstResult.getMember().getMemberAge()).isEqualTo(1990);
        // 프로필 이미지는 Fixture에서 설정한 값
        assertThat(firstResult.getMember().getMemberProfileImageUrl()).isEqualTo("https://example.com/profile1.jpg");
        
        assertThat(firstResult.getReview().getLikeCount()).isEqualTo(0); // 기본값
        assertThat(firstResult.getReview().getReviewCount()).isEqualTo(2); // product1에 대한 리뷰 수
        assertThat(firstResult.getReview().getReviewCreatedAt()).isNotNull();
        
        // 두 번째 결과 검증
        FetchPeerBestReviewsResponse secondResult = result.get(1);
        assertThat(secondResult.getProduct().getProductId()).isEqualTo(product2.getId());
        assertThat(secondResult.getProduct().getProductName()).isEqualTo("네이버 구독 서비스");
        assertThat(secondResult.getProduct().getBrandName()).isEqualTo("네이버");
        assertThat(secondResult.getMember().getMemberName()).isEqualTo("김모구");
        assertThat(secondResult.getMember().getMemberAge()).isEqualTo(1985);
    }
}
