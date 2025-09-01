package subscribenlike.mogupick.product.model.query;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class FetchPeerBestReviewsQueryResult {
    private Long productId;          // 상품 id
    private String brandName;        // 브랜드명
    private String productName;      // 상품명
    private Integer price;        // 가격
    private Integer memberBirthYear;       // 회원 나이
    private String memberProfileImageUrl;   // 회원 이미지
    private String memberName;       // 회원 이름
    private String reviewImageUrl;   // 리뷰 이미지
    private LocalDateTime reviewCreatedAt; // 리뷰 작성일
    private Integer likeCount;       // 좋아요 수
    private Integer reviewCount;     // 리뷰 수
}
