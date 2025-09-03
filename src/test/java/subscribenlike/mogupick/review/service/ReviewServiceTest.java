package subscribenlike.mogupick.review.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.review.domain.ReviewLike;
import subscribenlike.mogupick.review.model.FetchProductReviewsResponse;
import subscribenlike.mogupick.review.repository.ReviewLikeRepository;
import subscribenlike.mogupick.review.repository.ReviewRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.BrandFixture;
import subscribenlike.mogupick.support.fixture.MemberFixture;
import subscribenlike.mogupick.support.fixture.ProductFixture;
import subscribenlike.mogupick.support.fixture.ReviewFixture;
import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품의_리뷰_목록을_페이징과_통계정보와_함께_조회할_수_있다() {
        // Given: 테스트 데이터 준비
        Member member1 = memberRepository.save(MemberFixture.김회원());
        Member member2 = memberRepository.save(MemberFixture.김모구());
        Brand brand = brandRepository.save(BrandFixture.쿠팡(member1));
        Product product = productRepository.save(ProductFixture.구독상품1(brand));

        // 여러 리뷰 추가
        Review review1 = reviewRepository.save(ReviewFixture.상품리뷰1(member1, product));
        Review review2 = reviewRepository.save(ReviewFixture.상품리뷰2(member2, product));
        Review review3 = reviewRepository.save(ReviewFixture.상품리뷰3(member1, product));

        // 리뷰 좋아요 추가
        ReviewLike like1 = reviewLikeRepository.save(new ReviewLike(review1, member2));
        ReviewLike like2 = reviewLikeRepository.save(new ReviewLike(review2, member1));

        // When: 상품 리뷰 목록 조회 (member1의 관점에서)
        FetchProductReviewsResponse result = reviewService.getProductReviewsWithStats(
                product.getId(), member1.getId(), PageRequest.of(0, 10));
        // Then: 결과 검증
        assertThat(result.productAverageRating()).isEqualTo(4.5); // (4.5 + 5.0 + 4.0) / 3
        assertThat(result.productReviewCount()).isEqualTo(3);
        assertThat(result.reviews()).hasSize(3);

        // 첫 번째 리뷰 검증 (상품리뷰1)
        FetchProductReviewsResponse.ReviewResponse firstReview = result.reviews().get(0);
        assertThat(firstReview.reviewId()).isEqualTo(review1.getId());
        assertThat(firstReview.memberName()).isEqualTo(member1.getName());
        assertThat(firstReview.memberProfileImageUrl()).isEqualTo(member1.getProfileImage());
        assertThat(firstReview.reviewScore()).isEqualTo(4.5);
        assertThat(firstReview.reviewContent()).isEqualTo("정말 좋은 상품입니다!");
        assertThat(firstReview.reviewImageUrls()).isNull(); // 다중 이미지 지원 전이므로 null
        assertThat(firstReview.isLikedByCurrentMember()).isFalse(); // member1이 자신의 리뷰를 좋아요하지 않음
        assertThat(firstReview.likeCount()).isEqualTo(1); // member2가 좋아요
        assertThat(firstReview.timeAgo()).isNotNull();

        // 두 번째 리뷰 검증 (상품리뷰2)
        FetchProductReviewsResponse.ReviewResponse secondReview = result.reviews().get(1);
        assertThat(secondReview.reviewId()).isEqualTo(review2.getId());
        assertThat(secondReview.memberName()).isEqualTo(member2.getName());
        assertThat(secondReview.reviewScore()).isEqualTo(5.0);
        assertThat(secondReview.reviewContent()).isEqualTo("배송이 빨라서 좋았어요");
        assertThat(secondReview.reviewImageUrls()).isNull();
        assertThat(secondReview.isLikedByCurrentMember()).isTrue(); // member1이 이 리뷰를 좋아요함
        assertThat(secondReview.likeCount()).isEqualTo(1);
    }

    @Test
    void 리뷰가_없는_상품의_리뷰_목록을_조회하면_빈_목록을_반환한다() {
        // Given: 리뷰가 없는 상품
        Member member = memberRepository.save(MemberFixture.김회원());
        Brand brand = brandRepository.save(BrandFixture.쿠팡(member));
        Product product = productRepository.save(ProductFixture.구독상품1(brand));

        // When: 상품 리뷰 목록 조회
        FetchProductReviewsResponse result = reviewService.getProductReviewsWithStats(
                product.getId(), member.getId(), PageRequest.of(0, 10));

        // Then: 결과 검증
        assertThat(result.productAverageRating()).isEqualTo(0.0);
        assertThat(result.productReviewCount()).isEqualTo(0);
        assertThat(result.reviews()).isEmpty();
    }

    @Test
    void 페이징을_적용해서_상품_리뷰_목록을_조회할_수_있다() {
        // Given: 많은 리뷰가 있는 상품
        Member member1 = memberRepository.save(MemberFixture.김회원());
        Member member2 = memberRepository.save(MemberFixture.김모구());
        Brand brand = brandRepository.save(BrandFixture.쿠팡(member1));
        Product product = productRepository.save(ProductFixture.구독상품1(brand));

        // 5개의 리뷰 추가
        for (int i = 0; i < 5; i++) {
            Review review = reviewRepository.save(ReviewFixture.상품리뷰1(member1, product));
        }

        // When: 첫 번째 페이지 조회 (size = 3)
        FetchProductReviewsResponse result = reviewService.getProductReviewsWithStats(
                product.getId(), member1.getId(), PageRequest.of(0, 3));

        // Then: 결과 검증
        assertThat(result.productReviewCount()).isEqualTo(5);
        assertThat(result.reviews()).hasSize(3); // 페이지 크기만큼만 반환
    }
}