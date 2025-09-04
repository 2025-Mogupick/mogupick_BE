package subscribenlike.mogupick.product.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.ProductFixture;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.model.query.FetchPeerBestReviewsQueryResult;
import subscribenlike.mogupick.review.ReviewFixture;
import subscribenlike.mogupick.review.ReviewLikeFixture;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.review.domain.ReviewLike;
import subscribenlike.mogupick.review.repository.ReviewLikeRepository;
import subscribenlike.mogupick.review.repository.ReviewRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.BrandFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Test
    @DisplayName("내 또래 베스트 리뷰 네이티브 쿼리를 실행할 수 있다")
    void 내_또래_베스트_리뷰_네이티브_쿼리를_실행할_수_있다() {
        // Given: 테스트 데이터 생성 및 저장
        Member member1 = ProductTestMemberFixture.김회원(); // 1990년생
        memberRepository.save(member1);
        
        Member member2 = ProductTestMemberFixture.김모구(); // 1985년생
        memberRepository.save(member2);
        
        Member member3 = ProductTestMemberFixture.관리자(); // 1980년생 (범위 밖)
        memberRepository.save(member3);

        Brand brand1 = BrandFixture.쿠팡(member1);
        brandRepository.save(brand1);
        
        Brand brand2 = BrandFixture.네이버(member2);
        brandRepository.save(brand2);

        Product product1 = ProductFixture.쿠팡구독(brand1);
        productRepository.save(product1);
        
        Product product2 = ProductFixture.네이버구독(brand2);
        productRepository.save(product2);

        Review review1 = ReviewFixture.최고리뷰(member1, product1);
        reviewRepository.save(review1);
        
        Review review2 = ReviewFixture.좋은리뷰(member2, product2);
        reviewRepository.save(review2);
        
        Review review3 = ReviewFixture.보통리뷰(member3, product1); // 범위 밖
        reviewRepository.save(review3);

        // 리뷰 좋아요 추가
        ReviewLike reviewLike1 = ReviewLikeFixture.기본좋아요(review1, member2); // member2가 review1에 좋아요
        reviewLikeRepository.save(reviewLike1);
        
        ReviewLike reviewLike2 = ReviewLikeFixture.기본좋아요(review2, member1); // member1이 review2에 좋아요
        reviewLikeRepository.save(reviewLike2);
        
        ReviewLike reviewLike3 = ReviewLikeFixture.기본좋아요(review1, member3); // member3이 review1에 좋아요 (범위 밖)
        reviewLikeRepository.save(reviewLike3);

        int fromYear = 1985;
        int toYear = 1995;
        int limit = 10;
        
        // When: 네이티브 쿼리 실행
        List<FetchPeerBestReviewsQueryResult> result = productRepository.fetchPeerBestReviewNative(fromYear, toYear, limit);
        
        // Then: 결과 검증
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2); // 1985년생과 1990년생의 리뷰만 포함
        
        // 첫 번째 결과 검증 (좋아요 수가 높은 순으로 정렬)
        FetchPeerBestReviewsQueryResult firstResult = result.get(0);
        assertThat(firstResult.getProductId()).isEqualTo(product1.getId());
        assertThat(firstResult.getProductName()).isEqualTo("쿠팡 구독 서비스");
        assertThat(firstResult.getPrice()).isEqualTo(29900);
        assertThat(firstResult.getBrandName()).isEqualTo("쿠팡");
        assertThat(firstResult.getMemberBirthYear()).isEqualTo(1990);
        assertThat(firstResult.getMemberProfileImageUrl()).isEqualTo("https://example.com/profile1.jpg");
        assertThat(firstResult.getMemberName()).isEqualTo("김회원");
        assertThat(firstResult.getLikeCount()).isEqualTo(2); // member2와 member3이 좋아요 (범위 밖 member3도 포함)
        assertThat(firstResult.getReviewCount()).isEqualTo(2); // product1에 대한 리뷰 수
        assertThat(firstResult.getReviewCreatedAt()).isNotNull();
        
        // 두 번째 결과 검증
        FetchPeerBestReviewsQueryResult secondResult = result.get(1);
        assertThat(secondResult.getProductId()).isEqualTo(product2.getId());
        assertThat(secondResult.getProductName()).isEqualTo("네이버 구독 서비스");
        assertThat(secondResult.getPrice()).isEqualTo(19900);
        assertThat(secondResult.getBrandName()).isEqualTo("네이버");
        assertThat(secondResult.getMemberBirthYear()).isEqualTo(1985);
        assertThat(secondResult.getMemberProfileImageUrl()).isEqualTo("https://example.com/profile2.jpg");
        assertThat(secondResult.getMemberName()).isEqualTo("김모구");
        assertThat(secondResult.getLikeCount()).isEqualTo(1); // member1이 좋아요
        assertThat(secondResult.getReviewCount()).isEqualTo(1); // product2에 대한 리뷰 수
        assertThat(secondResult.getReviewCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("나이 범위가 유효하지 않을 경우 빈 리스트를 반환한다")
    void 나이_범위가_유효하지_않을_경우_빈_리스트를_반환한다() {
        // Given: 테스트 데이터 생성
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);
        
        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);
        
        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);
        
        Review review = ReviewFixture.좋은리뷰(member, product);
        reviewRepository.save(review);

        int fromYear = 2000;
        int toYear = 1990; // fromYear > toYear (잘못된 범위)
        int limit = 10;
        
        // When: 잘못된 범위로 쿼리 실행
        List<FetchPeerBestReviewsQueryResult> result = productRepository.fetchPeerBestReviewNative(fromYear, toYear, limit);
        
        // Then: 빈 리스트 반환
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("limit이 0일 경우 빈 리스트를 반환한다")
    void limit이_0일_경우_빈_리스트를_반환한다() {
        // Given: 테스트 데이터 생성
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);
        
        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);
        
        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);
        
        Review review = ReviewFixture.좋은리뷰(member, product);
        reviewRepository.save(review);

        int fromYear = 1990;
        int toYear = 2000;
        int limit = 0;
        
        // When: limit 0으로 쿼리 실행
        List<FetchPeerBestReviewsQueryResult> result = productRepository.fetchPeerBestReviewNative(fromYear, toYear, limit);
        
        // Then: 빈 리스트 반환
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("특정 나이 범위에 해당하는 리뷰만 조회된다")
    void 특정_나이_범위에_해당하는_리뷰만_조회된다() {
        // Given: 다양한 나이대의 회원과 리뷰 생성
        Member member1 = ProductTestMemberFixture.김회원(); // 1990년생
        memberRepository.save(member1);
        
        Member member2 = ProductTestMemberFixture.김모구(); // 1985년생
        memberRepository.save(member2);
        
        Member member3 = ProductTestMemberFixture.관리자(); // 1980년생
        memberRepository.save(member3);

        Brand brand1 = BrandFixture.쿠팡(member1);
        brandRepository.save(brand1);
        
        Brand brand2 = BrandFixture.네이버(member2);
        brandRepository.save(brand2);
        
        Brand brand3 = BrandFixture.카카오(member3);
        brandRepository.save(brand3);

        Product product1 = ProductFixture.쿠팡구독(brand1);
        productRepository.save(product1);
        
        Product product2 = ProductFixture.네이버구독(brand2);
        productRepository.save(product2);
        
        Product product3 = ProductFixture.카카오구독(brand3);
        productRepository.save(product3);

        Review review1 = ReviewFixture.좋은리뷰(member1, product1);
        reviewRepository.save(review1);
        
        Review review2 = ReviewFixture.좋은리뷰(member2, product2);
        reviewRepository.save(review2);
        
        Review review3 = ReviewFixture.좋은리뷰(member3, product3);
        reviewRepository.save(review3);

        // When: 1985-1990년 범위로 조회
        List<FetchPeerBestReviewsQueryResult> result = productRepository.fetchPeerBestReviewNative(1985, 1990, 10);
        
        // Then: 1985년생과 1990년생의 리뷰만 포함
        assertThat(result).hasSize(2);
        assertThat(result.stream().mapToInt(FetchPeerBestReviewsQueryResult::getMemberBirthYear))
                .containsExactlyInAnyOrder(1985, 1990);
        
        // 1980년생의 리뷰는 포함되지 않아야 함
        assertThat(result.stream().mapToInt(FetchPeerBestReviewsQueryResult::getMemberBirthYear))
                .doesNotContain(1980);
    }

    @Test
    @DisplayName("좋아요 수 순으로 정렬되어 조회된다")
    void 좋아요_수_순으로_정렬되어_조회된다() {
        // Given: 테스트 데이터 생성
        Member member1 = ProductTestMemberFixture.김회원();
        memberRepository.save(member1);
        
        Member member2 = ProductTestMemberFixture.김모구();
        memberRepository.save(member2);
        
        Member member3 = ProductTestMemberFixture.관리자();
        memberRepository.save(member3);

        Brand brand1 = BrandFixture.쿠팡(member1);
        brandRepository.save(brand1);
        
        Brand brand2 = BrandFixture.네이버(member2);
        brandRepository.save(brand2);

        Product product1 = ProductFixture.쿠팡구독(brand1);
        productRepository.save(product1);
        
        Product product2 = ProductFixture.네이버구독(brand2);
        productRepository.save(product2);

        Review review1 = ReviewFixture.보통리뷰(member1, product1); // 3.0점
        reviewRepository.save(review1);
        
        Review review2 = ReviewFixture.최고리뷰(member2, product2); // 5.0점
        reviewRepository.save(review2);
        
        // product1에 대한 추가 리뷰 제거 (테스트 단순화)

        // 리뷰 좋아요 추가 (좋아요 수 차이를 만들어서 정렬 테스트)
        ReviewLike reviewLike1 = ReviewLikeFixture.기본좋아요(review1, member2); // review1에 좋아요 1개
        reviewLikeRepository.save(reviewLike1);
        
        ReviewLike reviewLike2 = ReviewLikeFixture.기본좋아요(review2, member1); // review2에 좋아요 1개
        reviewLikeRepository.save(reviewLike2);
        
        ReviewLike reviewLike3 = ReviewLikeFixture.기본좋아요(review2, member3); // review2에 추가 좋아요 (범위 밖)
        reviewLikeRepository.save(reviewLike3);

        int fromYear = 1985;
        int toYear = 1995;
        int limit = 10;
        
        // When: 네이티브 쿼리 실행
        List<FetchPeerBestReviewsQueryResult> result = productRepository.fetchPeerBestReviewNative(fromYear, toYear, limit);
        
        // Then: 좋아요 수 순으로 정렬되어야 함
        assertThat(result).hasSize(2);
        
        // review2가 좋아요 2개, review1이 좋아요 1개로 review2가 먼저 나와야 함
        assertThat(result.get(0).getLikeCount()).isGreaterThanOrEqualTo(result.get(1).getLikeCount());
        
        // 좋아요 수가 많은 순으로 정렬 확인
        assertThat(result.get(0).getLikeCount()).isEqualTo(2); // review2 (좋아요 2개)
        assertThat(result.get(1).getLikeCount()).isEqualTo(1); // review1 (좋아요 1개)
        
        // 모든 필드 검증
        FetchPeerBestReviewsQueryResult firstResult = result.get(0);
        assertThat(firstResult.getProductId()).isEqualTo(product2.getId());
        assertThat(firstResult.getProductName()).isEqualTo("네이버 구독 서비스");
        assertThat(firstResult.getPrice()).isEqualTo(19900);
        assertThat(firstResult.getBrandName()).isEqualTo("네이버");
        assertThat(firstResult.getMemberBirthYear()).isEqualTo(1985);
        assertThat(firstResult.getMemberProfileImageUrl()).isEqualTo("https://example.com/profile2.jpg");
        assertThat(firstResult.getMemberName()).isEqualTo("김모구");
        assertThat(firstResult.getReviewCreatedAt()).isNotNull();
        assertThat(firstResult.getReviewCount()).isEqualTo(1);
        
        FetchPeerBestReviewsQueryResult secondResult = result.get(1);
        assertThat(secondResult.getProductId()).isEqualTo(product1.getId());
        assertThat(secondResult.getProductName()).isEqualTo("쿠팡 구독 서비스");
        assertThat(secondResult.getPrice()).isEqualTo(29900);
        assertThat(secondResult.getBrandName()).isEqualTo("쿠팡");
        assertThat(secondResult.getMemberBirthYear()).isEqualTo(1990);
        assertThat(secondResult.getMemberProfileImageUrl()).isEqualTo("https://example.com/profile1.jpg");
        assertThat(secondResult.getMemberName()).isEqualTo("김회원");
        assertThat(secondResult.getReviewCreatedAt()).isNotNull();
        assertThat(secondResult.getReviewCount()).isEqualTo(1); // product1에 대한 리뷰 1개
    }
}
