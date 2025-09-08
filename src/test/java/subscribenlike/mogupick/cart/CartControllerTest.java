package subscribenlike.mogupick.cart;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import subscribenlike.mogupick.auth.service.AuthService;
import subscribenlike.mogupick.billing.util.AesGcm;
import subscribenlike.mogupick.brand.BrandFixture;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.cart.repository.CartRepository;
import subscribenlike.mogupick.cart.service.CartService;
import subscribenlike.mogupick.common.utils.S3Service;
import subscribenlike.mogupick.global.oauth.client.GoogleApiClient;
import subscribenlike.mogupick.global.oauth.client.KakaoApiClient;
import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.ProductFixture;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;
import subscribenlike.mogupick.subscriptionOption.repository.SubscriptionOptionRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;

import java.time.LocalDate;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ServiceTest
class CartControllerTest {

    @LocalServerPort
    int port;

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

    private Member savedMember() {
        return memberRepository.save(ProductTestMemberFixture.김회원());
    }

    private Product savedProduct(Member brandOwner) {
        Brand brand = BrandFixture.쿠팡(brandOwner);
        brandRepository.save(brand);
        return productRepository.save(ProductFixture.쿠팡구독(brand));
    }

    private SubscriptionOption savedOption(SubscriptionPeriodUnit unit, int period, String displayText) {
        return subscriptionOptionRepository.save(new SubscriptionOption(unit, period, displayText));
    }

    @Test
    void 장바구니_조회_API를_호출할_수_있다() {
        Member member = savedMember();

        given()
                .port(port)
                .when()
                .get("/api/v1/carts/{memberId}", member.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(200))
                .body("data.cartId", notNullValue())
                .body("data.memberId", equalTo(member.getId().intValue()))
                .body("data.items", hasSize(0));
    }

    @Test
    void 장바구니_담기_API를_호출할_수_있다() {
        Member member = savedMember();
        Product product = savedProduct(member);
        SubscriptionOption option = savedOption(SubscriptionPeriodUnit.MONTH, 3, "매 3개월");
        LocalDate firstDeliveryDate = LocalDate.now().plusDays(7);

        Map<String, Object> body = Map.of(
                "memberId", member.getId(),
                "productId", product.getId(),
                "subscriptionOptionId", option.getId(),
                "firstDeliveryDate", firstDeliveryDate.toString()
        );

        given()
                .port(port)
                .contentType("application/json")
                .body(body)
                .when()
                .post("/api/v1/carts")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(200))
                .body("data.memberId", equalTo(member.getId().intValue()))
                .body("data.items", hasSize(1))
                .body("data.items[0].productId", equalTo(product.getId().intValue()))
                .body("data.items[0].subscriptionOptionId", equalTo(option.getId().intValue()))
                .body("data.items[0].firstDeliveryDate", equalTo(firstDeliveryDate.toString()))
                .body("data.items[0].displayText", equalTo("매 3개월"));
    }
}
