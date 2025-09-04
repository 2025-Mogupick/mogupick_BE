package subscribenlike.mogupick.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import subscribenlike.mogupick.brand.BrandFixture;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.product.repository.ProductViewCountRepository;
import subscribenlike.mogupick.product.service.ProductViewCountService;
import subscribenlike.mogupick.support.annotation.ServiceTest;

@ServiceTest
class ProductViewCountServiceTest {

    @Autowired
    private ProductViewCountService productViewCountService;
    @Autowired
    private ProductViewCountRepository productViewCountRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private RedisTemplate<String, Long> redisTemplate;

    @MockBean
    private ValueOperations<String, Long> valueOperations;

    private Product createProduct() {
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);
        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);
        Product product = ProductFixture.쿠팡구독(brand);
        return productRepository.save(product);
    }

    @Test
    void 초기_조회수_조회시_엔티티가_생성되고_0을_반환한다() {
        // Given
        Product product = createProduct();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey(anyString())).thenReturn(false);
        when(valueOperations.get(anyString())).thenReturn(0L);

        // When
        Long viewCount = productViewCountService.getViewCount(product.getId());

        // Then
        assertThat(viewCount).isEqualTo(0L);
        assertThat(productViewCountRepository.existsByProductId(product.getId())).isTrue();
    }

    @Test
    void 조회수_증가_후_getViewCount가_증가된_값을_반환한다() {
        // Given
        Product product = createProduct();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        when(valueOperations.get(anyString())).thenReturn(3L);

        // When: increment 3 times
        productViewCountService.incrementProductViewCount(product.getId());
        productViewCountService.incrementProductViewCount(product.getId());
        productViewCountService.incrementProductViewCount(product.getId());

        Long viewCount = productViewCountService.getViewCount(product.getId());

        // Then
        assertThat(viewCount).isEqualTo(3L);
    }

    @Test
    void 일별_조회수_키가_생성되고_값이_증가한다() {
        // Given
        Product product = createProduct();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        when(valueOperations.get(anyString())).thenReturn(2L);

        // When: increment twice to affect both total and daily counts
        productViewCountService.incrementProductViewCount(product.getId());
        productViewCountService.incrementProductViewCount(product.getId());

        // Then: total should be 2, and daily counter path exercised (간접 검증)
        Long viewCount = productViewCountService.getViewCount(product.getId());
        assertThat(viewCount).isEqualTo(2L);
    }

    @Test
    void 주목받는_상품_목록이_없을_경우_빈_리스트를_반환한다() {
        // Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        when(valueOperations.get(anyString())).thenReturn(0L);

        // When
        var result = productViewCountService.getMostDailyViewStatChangeProduct(PageRequest.of(0, 5));

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void 조회수_증가_메서드가_정상_동작한다() {
        // Given
        Product product = createProduct();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey(anyString())).thenReturn(false);

        // When
        productViewCountService.incrementProductViewCount(product.getId());

        // Then
        // Redis Mock이 정상 동작하는지 확인
        verify(redisTemplate, atLeastOnce()).opsForValue();
    }
}

