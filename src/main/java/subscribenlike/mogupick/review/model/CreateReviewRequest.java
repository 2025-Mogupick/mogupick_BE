package subscribenlike.mogupick.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateReviewRequest {
    private Long memberId;
    private Long productId;
    private String content;
    private double score;
    private List<MultipartFile> images;
}
