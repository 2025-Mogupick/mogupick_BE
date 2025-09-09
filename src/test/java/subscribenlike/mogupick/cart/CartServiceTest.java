package subscribenlike.mogupick.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import subscribenlike.mogupick.auth.service.AuthService;
import subscribenlike.mogupick.billing.util.AesGcm;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.cart.common.exception.CartException;
import subscribenlike.mogupick.cart.domain.Cart;
import subscribenlike.mogupick.cart.dto.CartAddRequest;
import subscribenlike.mogupick.cart.dto.CartItemOptionUpdateRequest;
import subscribenlike.mogupick.cart.dto.CartResponse;
import subscribenlike.mogupick.cart.repository.CartRepository;
import subscribenlike.mogupick.cart.service.CartService;
import subscribenlike.mogupick.common.utils.S3Service;
import subscribenlike.mogupick.global.oauth.client.GoogleApiClient;
import subscribenlike.mogupick.global.oauth.client.KakaoApiClient;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;
import subscribenlike.mogupick.subscriptionOption.repository.SubscriptionOptionRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.BrandFixture;
import subscribenlike.mogupick.support.fixture.CartFixture;
import subscribenlike.mogupick.support.fixture.MemberFixture;
import subscribenlike.mogupick.support.fixture.ProductFixture;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class CartServiceTest {

    @Autowired private CartService cartService;
    @Autowired private CartRepository cartRepository;
    @Autowired private SubscriptionOptionRepository subscriptionOptionRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BrandRepository brandRepository;
    @Autowired private ProductRepository productRepository;

    @MockitoBean private AuthService authService;
    @MockitoBean private S3Service s3Service;
    @MockitoBean private GoogleApiClient googleApiClient;
    @MockitoBean private KakaoApiClient kakaoApiClient;
    @MockitoBean private WebClient tossWebClient;
    @MockitoBean private AesGcm aesGcm;

    private Member member;
    private Product product;
    private SubscriptionOption option1;
    private SubscriptionOption option2;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.김회원());

        var brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        product = productRepository.save(ProductFixture.구독상품1(brand));
        option1 = subscriptionOptionRepository.save(new SubscriptionOption(SubscriptionPeriodUnit.MONTH, 1, "매 1개월"));
        option2 = subscriptionOptionRepository.save(new SubscriptionOption(SubscriptionPeriodUnit.WEEK, 2, "매 2주"));
    }

    @Test
    void 장바구니에_상품_하나_담기_및_배송일_포함() {
        LocalDate firstDeliveryDate = LocalDate.now().plusDays(7);
        CartAddRequest request = new CartAddRequest(
                 product.getId(), option1.getId(), firstDeliveryDate);

        CartResponse response = cartService.add(member.getId(), request);

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).subscriptionOptionId()).isEqualTo(option1.getId());
        assertThat(response.items().get(0).firstDeliveryDate()).isEqualTo(firstDeliveryDate);
    }

    @Test
    void 옵션변경_및_중복_병합_배송일_포함() {
        LocalDate date1 = LocalDate.now().plusDays(3);
        LocalDate date2 = LocalDate.now().plusDays(10);

        Cart cart = CartFixture.두아이템담은카트(member, product, option1, date1, option2, date2);
        cartRepository.save(cart);

        Long targetId = cart.getItems().get(0).getId();

        CartItemOptionUpdateRequest request =
                new CartItemOptionUpdateRequest(option2.getId(), date2);

        CartResponse updated = cartService.updateItemOption(member.getId(), targetId, request);

        assertThat(updated.items()).hasSize(1);
        assertThat(updated.items().get(0).subscriptionOptionId()).isEqualTo(option2.getId());
        assertThat(updated.items().get(0).firstDeliveryDate()).isEqualTo(date2);
    }

    @Test
    void 배송희망일_누락시_예외() {
        Cart cart = CartFixture.상품하나담은카트(member, product, option1, LocalDate.now().plusDays(7));
        cartRepository.save(cart);
        Long cartItemId = cart.getItems().get(0).getId();

        CartItemOptionUpdateRequest nullDateReq = new CartItemOptionUpdateRequest(option1.getId(), null);

        assertThatThrownBy(() -> cartService.updateItemOption(cart.getMember().getId(), cartItemId, nullDateReq))
                .isInstanceOf(CartException.class)
                .hasMessageContaining("첫 배송 희망일을 선택해야 합니다.");
    }
}
