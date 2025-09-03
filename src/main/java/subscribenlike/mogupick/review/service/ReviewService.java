package subscribenlike.mogupick.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.review.model.CreateReviewRequest;
import subscribenlike.mogupick.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createReview(CreateReviewRequest request) {
        Member member = memberRepository.findOrThrow(request.getMemberId());
        Product product = productRepository.getById(request.getProductId());

        // TODO: 이미지 업로드 기능 구현 시 변경
        String reviewImageUrl = "https://via.placeholder.com/150";

        if (request.getReviewImage() != null && !request.getReviewImage().isEmpty()) {
            // TODO: 실제 이미지 업로드 로직 구현
            reviewImageUrl = uploadReviewImage(request.getReviewImage());
        }

        Review review = new Review(
                request.getContent(),
                request.getScore(),
                reviewImageUrl,
                member,
                product
        );

        reviewRepository.save(review);
    }

    private String uploadReviewImage(MultipartFile image) {
        // TODO: 이미지 업로드 구현
        // S3나 다른 스토리지에 업로드하고 URL 반환
        return "https://via.placeholder.com/150";
    }
}
