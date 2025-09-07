package subscribenlike.mogupick.subscription;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import subscribenlike.mogupick.auth.service.AuthService;
import subscribenlike.mogupick.billing.util.AesGcm;
import subscribenlike.mogupick.common.utils.S3Service;
import subscribenlike.mogupick.global.oauth.client.GoogleApiClient;
import subscribenlike.mogupick.global.oauth.client.KakaoApiClient;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.subscription.domain.Subscription;
import subscribenlike.mogupick.subscription.domain.SubscriptionStatus;
import subscribenlike.mogupick.subscription.repository.SubscriptionRepository;
import subscribenlike.mogupick.subscription.service.SubscriptionService;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;
import subscribenlike.mogupick.subscriptionOption.repository.SubscriptionOptionRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.BrandFixture;
import subscribenlike.mogupick.support.fixture.MemberFixture;
import subscribenlike.mogupick.support.fixture.ProductFixture;
import subscribenlike.mogupick.brand.repository.BrandRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class SubscriptionServiceTest {

    @Autowired private SubscriptionService subscriptionService;
    @Autowired private SubscriptionRepository subscriptionRepository;
    @Autowired private SubscriptionOptionRepository optionRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BrandRepository brandRepository;
    @Autowired private subscribenlike.mogupick.product.repository.ProductRepository productRepository;

    @MockitoBean private AuthService authService;
    @MockitoBean private S3Service s3Service;
    @MockitoBean private GoogleApiClient googleApiClient;
    @MockitoBean private KakaoApiClient kakaoApiClient;
    @MockitoBean private WebClient tossWebClient;
    @MockitoBean private AesGcm aesGcm;

    private Member member;
    private Product product;
    private SubscriptionOption option;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.김회원());
        var brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);
        product = productRepository.save(ProductFixture.구독상품1(brand));
        option = optionRepository.save(new SubscriptionOption(SubscriptionPeriodUnit.MONTH, 1, "매 1개월"));

        subscriptionRepository.deleteAll();
    }

    @Test
    void 구독_생성후_리스트에_포함되는지_확인() {
        Subscription subscription = Subscription.create(member, product, option, LocalDate.now().plusDays(3), 10);
        subscriptionRepository.save(subscription);

        var list = subscriptionService.getList(member.getId(), null);

        assertThat(list).extracting("subscriptionId").contains(subscription.getId());
    }

    @Test
    void 구독_상세조회_정상처리() {
        Subscription sub = Subscription.create(member, product, option, LocalDate.now().plusDays(3), 10);
        subscriptionRepository.save(sub);

        var detail = subscriptionService.getDetail(sub.getId());

        assertThat(detail.subscriptionId()).isEqualTo(sub.getId());
        assertThat(detail.productName()).isEqualTo(product.getName());
    }

    @Test
    void 구독_해지_상태변경() {
        Subscription sub = Subscription.create(member, product, option, LocalDate.now().plusDays(3), 10);
        subscriptionRepository.save(sub);

        var result = subscriptionService.cancel(sub.getId());

        assertThat(result.status()).isEqualTo(SubscriptionStatus.ENDED.name());
    }
}
