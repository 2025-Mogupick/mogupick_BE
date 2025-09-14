package subscribenlike.mogupick.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.common.utils.GlobalLogger;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.product.common.ProductErrorCode;
import subscribenlike.mogupick.product.common.ProductException;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductOption;
import subscribenlike.mogupick.product.domain.ProductViewCount;
import subscribenlike.mogupick.product.model.*;
import subscribenlike.mogupick.product.repository.*;
import subscribenlike.mogupick.product.domain.MemberProductViewCount;
import subscribenlike.mogupick.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ProductViewCountService {
    private final ProductViewCountRepository productViewCountRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductMediaRepository productMediaRepository;
    private final MemberProductViewCountRepository memberProductViewCountRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    private List<FetchProductMostDailyViewStatChangeResponse> currentViewStatChanges;

    private final static DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");
    private static final long HOUR_RANGE = 24;

    @Scheduled(cron = "0 12 0 * * ?")
    public void updateMostDailyViewStatChangeProduct() {
        // 현재 변화량 랭킹 리스트 업데이트
        List<Product> products = productRepository.findAll();

        // TODO : 변화량 구하기 메서드를 비동기적으로 수행?
        currentViewStatChanges =
                products.stream()
                        .map(product -> getMostDailyViewStatChange(product, HOUR_RANGE))
                        .sorted((p1, p2) -> Double.compare(p2.getChange().getGradient(), p1.getChange().getGradient()))
                        .toList();
    }

    public Page<FetchProductMostDailyViewStatChangeResponse> getMostDailyViewStatChangeProduct(Pageable pageable) {
        if (currentViewStatChanges == null) {
            updateMostDailyViewStatChangeProduct();
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), currentViewStatChanges.size());
        List<FetchProductMostDailyViewStatChangeResponse> pageContent = currentViewStatChanges.subList(start, end);

        return new PageImpl<>(pageContent, pageable, currentViewStatChanges.size());
    }

    public FetchProductMostDailyViewStatChangeResponse getMostDailyViewStatChange(Product product, long hourRange) {
        // 해당 상품의 시간대 별 변화량 중, 가장 큰 변화량 구하기
        List<FetchProductDailyViewStatChangeResponse> changes = getDailyViewStatChange(product.getId(), hourRange);

        FetchProductDailyViewStatChangeResponse mostGradientChange =
                changes.stream()
                        .max((c1, c2) -> Double.compare(c2.getGradient(), c1.getGradient()))
                        .orElseThrow(() -> new ProductException(ProductErrorCode.VIEW_COUNT_STATS_NOT_FOUND));

        Long lastCountOfTime = changes.get(changes.size() - 1).getEndViewCount();
        ProductOption option = productOptionRepository.getByProductId(product.getId());

        String imageUrl = productMediaRepository.findFirstImageUrlByProductId(product.getId());

        FetchProductResponse productResponse = FetchProductResponse.of(
                product.getId(),
                imageUrl,
                product.getName(),
                product.getPrice(),
                product.getCreatedAt()
        );

        FetchBrandResponse brandResponse = FetchBrandResponse.of(
                product.getBrand().getId(),
                product.getBrand().getName()
        );

        FetchProductOptionResponse optionResponse = FetchProductOptionResponse.from(option);
        return FetchProductMostDailyViewStatChangeResponse.of(mostGradientChange, productResponse, brandResponse, optionResponse, lastCountOfTime);
    }

    public List<FetchProductDailyViewStatChangeResponse> getDailyViewStatChange(Long productId, long hourRange) {
        // 한 상품의 시간대 별 변화량 구하기
        FetchProductDailyViewStatsResponse dailyViewStats = getDailyViewStats(productId, hourRange);

        List<Map.Entry<String, Long>> viewStats =
                dailyViewStats.getViewCountOfTimes().entrySet().stream().toList();

        List<FetchProductDailyViewStatChangeResponse> response = new ArrayList<>();

        for (int i = 1; i < viewStats.size(); i++) {
            response.add(createProductDailViewStatChange(productId, viewStats, i));
        }

        return response;
    }

    private FetchProductDailyViewStatChangeResponse createProductDailViewStatChange(Long productId, List<Map.Entry<String, Long>> viewStats, int i) {
        Long time = Long.parseLong(viewStats.get(i).getKey());
        Long prevTime = Long.parseLong(viewStats.get(i - 1).getKey());
        Long count = viewStats.get(i).getValue();
        Long prevCount = viewStats.get(i - 1).getValue();

        Double gradient = getGradient(prevCount, count, prevTime, time);

        return FetchProductDailyViewStatChangeResponse.builder()
                .productId(productId)
                .startTime(viewStats.get(i - 1).getKey())
                .endTime(viewStats.get(i).getKey())
                .startViewCount(prevCount)
                .endViewCount(count)
                .gradient(gradient)
                .viewCountIncreaseRate(getIncreaseRate(count, prevCount))
                .build();
    }

    public void incrementProductViewCount(Long productId) {
        incrementViewCount(productId);
        incrementDailyCount(productId);
    }

    @Transactional
    public void incrementProductViewCount(Long productId, Long memberId) {
        incrementViewCount(productId);
        incrementDailyCount(productId);
        incrementMemberProductViewCount(productId, memberId);
    }

    private void incrementMemberProductViewCount(Long productId, Long memberId) {
        Member member = memberRepository.findOrThrow(memberId);
        Product product = productRepository.getById(productId);

        MemberProductViewCount memberProductViewCount = memberProductViewCountRepository
                .findByMemberIdAndProductId(memberId, productId)
                .orElseGet(() -> createMemberProductViewCount(product, member));

        memberProductViewCount.increase();
        memberProductViewCountRepository.save(memberProductViewCount);
    }

    private MemberProductViewCount createMemberProductViewCount(Product product, Member member) {
        return memberProductViewCountRepository.save(MemberProductViewCount.of(product, member));
    }

    public Long getViewCount(Long productId) {
        String productIdKey = String.valueOf(productId);
        String productViewCountKey = RedisKeyTemplate.PRODUCT_VIEW_COUNT.of(productIdKey);

        Long currentCount = redisTemplate.opsForValue().get(productViewCountKey);
        
        if (currentCount == null) {
            // Redis에 키가 없으면 DB에서 초기값 로드하고 Redis에 설정
            createProductViewCountIfNotExists(productId);
            Long loadedCount = productViewCountRepository.getByProductId(productId).getViewCount();
            
            // setIfAbsent를 사용하여 원자적으로 초기값 설정 (다른 스레드가 이미 설정했으면 무시)
            redisTemplate.opsForValue().setIfAbsent(productViewCountKey, loadedCount);
            
            // 실제 Redis에서 값을 다시 읽어서 반환 (다른 스레드가 설정한 값일 수도 있음)
            return redisTemplate.opsForValue().get(productViewCountKey);
        }

        return currentCount;
    }

    private void incrementDailyCount(Long productId) {
        String productIdKey = String.valueOf(productId);
        String dailyViewCountKey = RedisKeyTemplate.PRODUCT_DAILY_VIEW_COUNT.of(timeFormat(LocalDateTime.now()), productIdKey);

        // Redis increment는 키가 없으면 자동으로 0으로 초기화하고 증가시킴 (원자적 연산)
        redisTemplate.opsForValue().increment(dailyViewCountKey, 1);
    }

    private void incrementViewCount(Long productId) {
        String productIdKey = String.valueOf(productId);
        String productViewCountKey =
                RedisKeyTemplate.PRODUCT_VIEW_COUNT.of(productIdKey);

        // Redis increment는 키가 없으면 자동으로 0으로 초기화하고 증가시킴 (원자적 연산)
        redisTemplate.opsForValue().increment(productViewCountKey, 1);
    }


    private void createProductViewCountIfNotExists(Long productId) {
        // 뷰 카운트 객체가 존재하지 않으면 생성하기
        GlobalLogger.info("Create ProductViewCount if not exists. productId: %d".formatted(productId));
        if (!productViewCountRepository.existsByProductId(productId)) {
            productViewCountRepository.save(ProductViewCount.of(
                    productRepository.getById(productId)
            ));
        }
    }

    private static double getIncreaseRate(Long count, Long prevCount) {
        return 100.0 * (count - prevCount) / (prevCount == 0 ? 1 : prevCount);
    }

    private Double getGradient(Long y1, Long y2, Long x1, Long x2) {
        if (x2 - x1 == 0) throw new ArithmeticException("x1(%s)과 x2(%s)는 같은 값일 수 없습니다. ".formatted(x1, x2));
        return (double) (y2 - y1) / (x2 - x1);
    }

    private FetchProductDailyViewStatsResponse getDailyViewStats(Long productId, long hourRange) {
        // 현재 시간에서 hourRange 이전까지의 데이터 불러오기
        List<LocalDateTime> timeRange = generateTimeRange(hourRange);

        // 하나의 상품에 대한 조회수 리스트 불러오기
        Map<String, Long> countOfTimes = getCountOfTimes(productId, timeRange);

        return FetchProductDailyViewStatsResponse.of(productId, countOfTimes);
    }

    private Map<String, Long> getCountOfTimes(Long productId, List<LocalDateTime> timeRange) {
        List<String> productDailyViewCountKeys = getProductDailyViewCountKeys(productId, timeRange);
        List<Long> counts = redisTemplate.opsForValue().multiGet(productDailyViewCountKeys).stream()
                .map(count -> count != null ? count : 0L)
                .toList();

        return IntStream.range(0, productDailyViewCountKeys.size())
                .boxed()
                .collect(Collectors.toMap(
                        idx -> timeFormat(timeRange.get(idx)),
                        counts::get
                ));
    }

    private List<String> getProductDailyViewCountKeys(Long productId, List<LocalDateTime> timeRange) {
        return timeRange.stream().map(time ->
                        RedisKeyTemplate.PRODUCT_DAILY_VIEW_COUNT.of(timeFormat(time), String.valueOf(productId)))
                .toList();
    }

    private List<LocalDateTime> generateTimeRange(long hourRange) {
        List<LocalDateTime> times = new ArrayList<>();

        LocalDateTime from = LocalDateTime.now().minusHours(hourRange);
        LocalDateTime to = LocalDateTime.now();

        LocalDateTime current = from;

        while (current.isBefore(to)) {
            times.add(current);
            current = current.plusHours(1);
        }

        return times;
    }

    private static String timeFormat(LocalDateTime time) {
        return time.format(DEFAULT_TIME_FORMATTER);
    }
}
