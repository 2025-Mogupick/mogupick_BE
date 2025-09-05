package subscribenlike.mogupick.review.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class CreateReviewRequest {
    private Long memberId;
    private Long productId;
    private String content;
    private double score;
    private MultipartFile reviewImage;

    public CreateReviewRequest(Long memberId, Long productId, String content, double score, MultipartFile reviewImage) {
        this.memberId = memberId;
        this.productId = productId;
        this.content = content;
        this.score = score;
        this.reviewImage = reviewImage;
    }
}
