package subscribenlike.mogupick.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import subscribenlike.mogupick.common.utils.GlobalLogger;
import subscribenlike.mogupick.product.common.ProductErrorCode;
import subscribenlike.mogupick.product.common.ProductException;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductViewCount;
import subscribenlike.mogupick.product.model.FetchProductDailyViewStatChangeResponse;
import subscribenlike.mogupick.product.model.FetchProductDailyViewStatsResponse;
import subscribenlike.mogupick.product.model.FetchProductMostDailyViewStatChangeResponse;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.product.repository.ProductViewCountRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductViewCountService {
    private final ProductViewCountRepository productViewCountRepository;
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    private List<FetchProductMostDailyViewStatChangeResponse> currentViewStatChanges;

    private final static DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");

    @Scheduled(cron = "0 12 0 * * ?")
    public void updateMostDailyViewStatChangeProduct() {
        // 현재 변화량 랭킹 리스트 업데이트
        long hourRange = 24;
        List<Product> products = productRepository.findAll();

        // TODO : 변화량 구하기 메서드를 비동기적으로 수행?
        currentViewStatChanges =
                products.stream()
                        .map(product -> getMostDailyViewStatChange(product.getId(), hourRange))
                        .sorted((p1, p2) -> Double.compare(p2.getChange().getGradient(), p1.getChange().getGradient()))
                        .toList();
    }

    public List<FetchProductMostDailyViewStatChangeResponse> getMostDailyViewStatChangeProduct(int limit) {
        if (currentViewStatChanges == null) {
            return new ArrayList<>();
        }

        return currentViewStatChanges.subList(0, limit);
    }

    public FetchProductMostDailyViewStatChangeResponse getMostDailyViewStatChange(Long productId, long hourRange) {
        // 해당 상품의 시간대 별 변화량 중, 가장 큰 변화량 구하기
        List<FetchProductDailyViewStatChangeResponse> changes = getDailyViewStatChange(productId, hourRange);

        FetchProductDailyViewStatChangeResponse mostGradientChange =
                changes.stream()
                        .max((c1, c2) -> Double.compare(c2.getGradient(), c1.getGradient()))
                        .orElseThrow(() -> new ProductException(ProductErrorCode.VIEW_COUNT_STATS_NOT_FOUND));

        Long lastCountOfTime = changes.get(changes.size() - 1).getEndViewCount();

        return FetchProductMostDailyViewStatChangeResponse.of(mostGradientChange, lastCountOfTime);
    }

    public List<FetchProductDailyViewStatChangeResponse> getDailyViewStatChange(Long productId, long hourRange) {
        // 한 상품의 시간대 별 변화량 구하기
        FetchProductDailyViewStatsResponse dailyViewStats = getDailyViewStats(productId, hourRange);

        List<Map.Entry<String, Long>> viewStats =
                dailyViewStats.getViewCountOfTimes().entrySet().stream().toList();

        List<FetchProductDailyViewStatChangeResponse> response = new ArrayList<>();

        for (int i = 1; i < viewStats.size(); i++) {
            Long time = Long.parseLong(viewStats.get(i).getKey());
            Long prevTime = Long.parseLong(viewStats.get(i - 1).getKey());
            Long count = viewStats.get(i).getValue();
            Long prevCount = viewStats.get(i - 1).getValue();

            Double gradient = getGradient(prevCount, count, prevTime, time);

            response.add(FetchProductDailyViewStatChangeResponse.builder()
                    .productId(productId)
                    .startTime(viewStats.get(i - 1).getKey())
                    .endTime(viewStats.get(i).getKey())
                    .startViewCount(prevCount)
                    .endViewCount(count)
                    .gradient(gradient)
                    .viewCountIncreaseRate(getIncreaseRate(count, prevCount))
                    .build());
        }

        return response;
    }

    public void incrementProductViewCount(Long productId) {
        incrementViewCount(productId);
        incrementDailyCount(productId);
    }

    public Long getViewCount(Long productId) {
        String productIdKey = String.valueOf(productId);
        String productViewCountKey = RedisKeyTemplate.PRODUCT_VIEW_COUNT.of(productIdKey);

        if (!redisTemplate.hasKey(productViewCountKey)) {
            createProductViewCountIfNotExists(productId);
            Long loadedCount = productViewCountRepository.getByProductId(productId).getViewCount();

            setIfNotExists(productViewCountKey, loadedCount);

            return loadedCount;
        }

        // 현재 상품의 조회수 불러오기
        return redisTemplate.opsForValue().get(productViewCountKey);
    }

    private void incrementDailyCount(Long productId) {
        String productIdKey = String.valueOf(productId);
        String dailyViewCountKey = RedisKeyTemplate.PRODUCT_DAILY_VIEW_COUNT.of(timeFormat(LocalDateTime.now()), productIdKey);

        setIfNotExists(dailyViewCountKey);

        redisTemplate.opsForValue().increment(dailyViewCountKey, 1);
    }

    private void incrementViewCount(Long productId) {
        String productIdKey = String.valueOf(productId);
        String productViewCountKey =
                RedisKeyTemplate.PRODUCT_VIEW_COUNT.of(productIdKey);

        setIfNotExists(productViewCountKey);

        redisTemplate.opsForValue().increment(productViewCountKey, 1);
    }

    private void setIfNotExists(String key) {
        if (!redisTemplate.hasKey(key)) {
            setProductViewCountIfNotExistsInRedis(key, 0L);
        }
    }

    private void setIfNotExists(String key, Long initialValue) {
        if (!redisTemplate.hasKey(key)) {
            setProductViewCountIfNotExistsInRedis(key, initialValue);
        }
    }

    private void setProductViewCountIfNotExistsInRedis(String productViewCountKey, Long value) {
        redisTemplate.opsForValue()
                .setIfAbsent(productViewCountKey, value);
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
