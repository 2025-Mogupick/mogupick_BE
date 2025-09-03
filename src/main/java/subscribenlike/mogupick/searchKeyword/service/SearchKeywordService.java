package subscribenlike.mogupick.searchKeyword.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.recentSearchKeyword.domain.RecentSearchKeyword;
import subscribenlike.mogupick.recentSearchKeyword.repository.RecentSearchKeywordRepository;
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
    public static final int START_RANK_NUMBER = 0;
    public static final int END_RANK_NUMBER = 9;
    public static final int KEYWORD_RANK_SIZE = 10;

    private final SearchKeywordRepository searchKeywordRepository;
    private final RecentSearchKeywordRepository recentSearchKeywordRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final StringRedisTemplate redis;
    private final MemberRepository memberRepository;

    @Transactional
    public List<SearchProductResponse> findByKeyword(String email, SearchKeywordRequest searchKeywordRequest) {
        SearchKeyword searchKeyword = new SearchKeyword(searchKeywordRequest.content());
        searchKeywordRepository.findByNormalizedContent(searchKeyword.getNormalizedContent())
                .ifPresentOrElse(
                        SearchKeyword::increase,
                        () -> searchKeywordRepository.save(searchKeyword)
                );
        recordSearchRiseToday(searchKeyword.getNormalizedContent());
        List<Product> productList = productRepository.findByNameContainingIgnoreCase(
                searchKeyword.getNormalizedContent());
        if (email != null){
            Member member = memberRepository.findByEmailOrThrow(email);
            updateRecentSearchKeyword(member, searchKeyword.getNormalizedContent());
        }
        return createProductResponse(productList);
    }

    private List<SearchProductResponse> createProductResponse(List<Product> productList) {
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

    @Transactional
    public void recordSearchRiseToday(String rawKeyword) {
        String norm = SearchKeyword.normalize(rawKeyword);
        if (norm.isEmpty()) {
            return;
        }

        String key = todayRiseKey();
        redis.opsForZSet().incrementScore(key, norm, 1D);

        redis.expire(key, java.time.Duration.ofDays(3));
    }

    private String todayRiseKey() {
        return "zk:rise:" + java.time.LocalDate.now();
    }

    private String yesterdayRiseKey() {
        return "zk:rise:" + java.time.LocalDate.now().minusDays(1);
    }

    public List<SearchKeywordResponse> findRelatedKeyword(String keyword) {
        String normalized = SearchKeyword.normalize(keyword);

        return searchKeywordRepository
                .findTop5ByNormalizedContentContainingIgnoreCaseOrderBySearchedCountDesc(normalized)
                .stream()
                .map(SearchKeywordResponse::from)
                .toList();
    }

    public List<SearchKeywordResponse> findTop10RisingToday() {
        Set<ZSetOperations.TypedTuple<String>> todayTuples =
                redis.opsForZSet().reverseRangeWithScores(todayRiseKey(), START_RANK_NUMBER, END_RANK_NUMBER);
        List<String> orderedNorms = getNormalizedKeyword(todayTuples);

        if (orderedNorms.size() < KEYWORD_RANK_SIZE) {
            Set<ZSetOperations.TypedTuple<String>> yestTuples =
                    redis.opsForZSet().reverseRangeWithScores(yesterdayRiseKey(), START_RANK_NUMBER, END_RANK_NUMBER);

            if (yestTuples != null && !yestTuples.isEmpty()) {
                Set<String> existing = new HashSet<>(orderedNorms);
                for (ZSetOperations.TypedTuple<String> t : yestTuples) {
                    String norm = t.getValue();
                    if (norm == null) {
                        continue;
                    }
                    if (existing.add(norm)) {
                        orderedNorms.add(norm);
                        if (orderedNorms.size() == KEYWORD_RANK_SIZE) {
                            break;
                        }
                    }
                }
            }
        }
        List<SearchKeyword> keywords = getSearchKeywords(orderedNorms);
        Map<String, SearchKeyword> byNorm = keywords.stream()
                .collect(Collectors.toMap(SearchKeyword::getNormalizedContent, e -> e));

        List<SearchKeywordResponse> result = new ArrayList<>();
        for (String norm : orderedNorms) {
            SearchKeyword e = byNorm.get(norm);
            if (e != null) {
                result.add(SearchKeywordResponse.from(e));
            }
            if (result.size() == 10) {
                break;
            }
        }
        return result;
    }

    private List<SearchKeyword> getSearchKeywords(List<String> orderedNorms) {
        List<SearchKeyword> keywords = new ArrayList<>();
        for (String keyword : orderedNorms) {
            keywords.add(searchKeywordRepository.findByNormalizedContentOrThrow(keyword));
        }
        return keywords;
    }

    private List<String> getNormalizedKeyword(Set<TypedTuple<String>> todayTuples) {
        List<String> orderedNorms = new ArrayList<>();
        if (todayTuples != null) {
            todayTuples.stream()
                    .map(TypedTuple::getValue)
                    .filter(Objects::nonNull)
                    .forEach(orderedNorms::add);
        }
        return orderedNorms;
    }

    private void updateRecentSearchKeyword(Member member, String normalizedContent) {
        RecentSearchKeyword existing = recentSearchKeywordRepository.findByNormalizedContentAndMember(normalizedContent, member);

        if (existing != null) {
            recentSearchKeywordRepository.delete(existing);
            recentSearchKeywordRepository.save(new RecentSearchKeyword(existing.getContent(), existing.getNormalizedContent(), member));
        } else {
            RecentSearchKeyword recentSearchKeyword = new RecentSearchKeyword(normalizedContent, normalizedContent, member);
            recentSearchKeywordRepository.save(recentSearchKeyword);
        }
    }
}
