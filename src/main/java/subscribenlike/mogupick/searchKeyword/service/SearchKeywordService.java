package subscribenlike.mogupick.searchKeyword.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.review.repository.ReviewRepository;
import subscribenlike.mogupick.searchKeyword.domain.SearchKeyword;
import subscribenlike.mogupick.searchKeyword.dto.SearchKeywordRequest;
import subscribenlike.mogupick.searchKeyword.dto.SearchKeywordResponse;
import subscribenlike.mogupick.searchKeyword.dto.SearchProductResponse;
import subscribenlike.mogupick.searchKeyword.repository.SearchKeywordRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchKeywordService {
    private final SearchKeywordRepository searchKeywordRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public List<SearchProductResponse> findByKeyword(SearchKeywordRequest searchKeywordRequest) {
        SearchKeyword searchKeyword = new SearchKeyword(searchKeywordRequest.content());
        searchKeywordRepository.findByNormalizedContent(searchKeyword.getNormalizedContent())
                .ifPresentOrElse(
                        SearchKeyword::increase,
                        () -> searchKeywordRepository.save(searchKeyword)
                );
        List<Product> productList = productRepository.findByNameContainingIgnoreCase(
                searchKeyword.getNormalizedContent());
        return productList.stream()
                .map(product -> {
                    List<Review> reviews = findReviewByProductId(product.getId());
                    return SearchProductResponse.of(product, calculateRate(reviews), reviews.size());
                })
                .toList();
    }

    private List<Review> findReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    //todo 리뷰기능 정리되면 수정예정 임시사항
    private double calculateRate(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToDouble(Review::getScore)
                .average()
                .orElse(0.0);
    }

    public List<SearchKeywordResponse> findRelatedKeyword(String keyword) {
        String normalized = SearchKeyword.normalize(keyword);

        return searchKeywordRepository
                .findTop5ByNormalizedContentContainingIgnoreCaseOrderBySearchedCountDesc(normalized)
                .stream()
                .map(SearchKeywordResponse::from)
                .toList();
    }
}
