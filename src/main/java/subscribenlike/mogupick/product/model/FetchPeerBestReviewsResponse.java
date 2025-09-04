package subscribenlike.mogupick.product.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.product.model.query.FetchPeerBestReviewsQueryResult;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FetchPeerBestReviewsResponse {
    private ProductResponse product;   // 상품 정보
    private MemberResponse member;     // 회원 정보
    private ReviewResponse review;     // 리뷰 정보

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class ProductResponse{
        private Long productId;          // 상품 id
        private String productName;      // 상품명
        private Integer price;        // 가격
        private String brandName;        // 브랜드명
        private String productImageUrl;  // 상품 대표 이미지
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class MemberResponse{
        private Integer memberAge;       // 회원 나이
        private String memberProfileImageUrl;   // 회원 이미지
        private String memberName;       // 회원 이름
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class ReviewResponse{
        private Long likeCount;       // 좋아요 수
        private Long reviewCount;     // 리뷰 수
        private String reviewImageUrl;   // 리뷰 이미지
        private LocalDateTime reviewCreatedAt; // 리뷰 작성일
    }

    public static FetchPeerBestReviewsResponse from(
            FetchPeerBestReviewsQueryResult queryResult) {
        return new FetchPeerBestReviewsResponse(
                ProductResponse.of(
                        queryResult.getProductId(),
                        queryResult.getProductName(),
                        queryResult.getPrice(),
                        queryResult.getBrandName(),
                        queryResult.getProductImageUrl()
                ),
                MemberResponse.of(
                        queryResult.getMemberBirthYear(),
                        queryResult.getMemberProfileImageUrl(),
                        queryResult.getMemberName()
                ),
                ReviewResponse.of(
                        queryResult.getLikeCount(),
                        queryResult.getReviewCount(),
                        queryResult.getReviewImageUrl(),
                        queryResult.getReviewCreatedAt()
                )
        );
    }
}
