package subscribenlike.mogupick.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subscribenlike.mogupick.brand.BrandFixture;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductOption;
import subscribenlike.mogupick.product.repository.ProductOptionRepository;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.review.ReviewFixture;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.review.repository.ReviewRepository;
import subscribenlike.mogupick.support.annotation.ServiceTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ServiceTest
class ProductControllerTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @MockBean
    private ProductOptionRepository productOptionRepository;

    @LocalServerPort
    private int port;

    @Test
    void 이번_달_새로나온_구독_상품_조회_API를_호출할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        Review review = ReviewFixture.좋은리뷰(member, product);
        reviewRepository.save(review);

        // When & Then
        given()
                .port(port)
                .when()
                .get("/api/v1/products/new")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(200))
                .body("message", equalTo("이번 달 새로나온 구독 상품 리스트를 조회하였습니다."))
                .body("data", hasSize(1))
                .body("data[0].product.productId", equalTo(product.getId().intValue()))
                .body("data[0].product.productName", equalTo("쿠팡 구독 서비스"))
                .body("data[0].brand.brandName", equalTo("쿠팡"))
                .body("data[0].review.rating", equalTo(4.5f))
                .body("data[0].review.reviewCount", equalTo(1));
    }

    @Test
    void 이번_달_새로나온_구독_상품이_없을_경우_빈_리스트를_반환한다() {
        // Given - 데이터 없음

        // When & Then
        given()
                .port(port)
                .when()
                .get("/api/v1/products/new")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(200))
                .body("message", equalTo("이번 달 새로나온 구독 상품 리스트를 조회하였습니다."))
                .body("data", hasSize(0));
    }

    @Test
    void 이번_달_새로나온_구독_상품_여러개를_조회할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand1 = BrandFixture.쿠팡(member);
        Brand brand2 = BrandFixture.네이버(member);
        brandRepository.save(brand1);
        brandRepository.save(brand2);

        Product product1 = ProductFixture.쿠팡구독(brand1);
        Product product2 = ProductFixture.네이버구독(brand2);
        productRepository.save(product1);
        productRepository.save(product2);

        Review review1 = ReviewFixture.좋은리뷰(member, product1);
        Review review2 = ReviewFixture.보통리뷰(member, product2);
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        // When & Then
        given()
                .port(port)
                .when()
                .get("/api/v1/products/new")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(200))
                .body("message", equalTo("이번 달 새로나온 구독 상품 리스트를 조회하였습니다."))
                .body("data", hasSize(2))
                .body("data.product.productName", hasItems("쿠팡 구독 서비스", "네이버 구독 서비스"))
                .body("data.brand.brandName", hasItems("쿠팡", "네이버"));
    }

    @Test
    void 루트카테고리로_상품_목록_조회_API를_호출할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        ProductOption productOption = ProductOptionFixture.간편식옵션(product);
        when(productOptionRepository.findAllByRootCategory(RootCategory.CONVENIENCE_FOOD))
                .thenReturn(List.of(productOption));

        // When & Then
        given()
                .port(port)
                .param("rootCategory", "CONVENIENCE_FOOD")
                .when()
                .get("/api/v1/products/category")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(200))
                .body("message", equalTo("루트 카테고리의 상품 목록을 조회하였습니다."))
                .body("data", hasSize(1))
                .body("data[0].product.name", equalTo("쿠팡 구독 서비스"))
                .body("data[0].option.rootCategory", equalTo("CONVENIENCE_FOOD"));
    }

    @Test
    void 루트카테고리에_해당하는_상품이_없을_경우_빈_리스트를_반환한다() {
        // Given
        when(productOptionRepository.findAllByRootCategory(RootCategory.HEALTH_SUPPLEMENTS))
                .thenReturn(List.of());

        // When & Then
        given()
                .port(port)
                .param("rootCategory", "HEALTH_SUPPLEMENTS")
                .when()
                .get("/api/v1/products/category")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(200))
                .body("message", equalTo("루트 카테고리의 상품 목록을 조회하였습니다."))
                .body("data", hasSize(0));
    }

    @Test
    void 상품_등록_API를_호출할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        ProductOption mockProductOption = ProductOptionFixture.간편식옵션(ProductFixture.쿠팡구독(brand));
        when(productOptionRepository.save(any(ProductOption.class))).thenReturn(mockProductOption);

        // When & Then
        given()
                .port(port)
                .contentType("multipart/form-data")
                .multiPart("rootCategory", "CONVENIENCE_FOOD")
                .multiPart("subCategory", "FROZEN")
                .multiPart("brandId", brand.getId())
                .multiPart("name", "테스트 상품")
                .multiPart("description", "테스트 상품 설명")
                .multiPart("origin", "한국")
                .multiPart("price", 15000)
                .multiPart("options[PRICE]", "15000원")
                .multiPart("options[RATING]", "4.5")
                .multiPart("image", "test-image.jpg", "test image content".getBytes(), "image/jpeg")
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("status", equalTo(201))
                .body("message", equalTo("상품이 성공적으로 등록되었습니다."));
    }

    @Test
    void 상품_등록_API_호출_시_필수_파라미터가_누락되면_에러가_발생한다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        // When & Then - brandId 누락
        given()
                .port(port)
                .contentType("multipart/form-data")
                .multiPart("rootCategory", "CONVENIENCE_FOOD")
                .multiPart("subCategory", "FROZEN")
                .multiPart("name", "테스트 상품")
                .multiPart("description", "테스트 상품 설명")
                .multiPart("origin", "한국")
                .multiPart("price", 15000)
                .multiPart("options[PRICE]", "15000원")
                .multiPart("options[RATING]", "4.5")
                .multiPart("image", "test-image.jpg", "test image content".getBytes(), "image/jpeg")
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 상품_등록_API_호출_시_이미지가_누락되면_에러가_발생한다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        // When & Then - 이미지 누락
        given()
                .port(port)
                .contentType("multipart/form-data")
                .multiPart("rootCategory", "CONVENIENCE_FOOD")
                .multiPart("subCategory", "FROZEN")
                .multiPart("brandId", brand.getId())
                .multiPart("name", "테스트 상품")
                .multiPart("description", "테스트 상품 설명")
                .multiPart("origin", "한국")
                .multiPart("price", 15000)
                .multiPart("options[PRICE]", "15000원")
                .multiPart("options[RATING]", "4.5")
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 상품_등록_API_호출_시_잘못된_카테고리_값이_전달되면_에러가_발생한다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        // When & Then - 잘못된 카테고리 값
        given()
                .port(port)
                .contentType("multipart/form-data")
                .multiPart("rootCategory", "INVALID_CATEGORY")
                .multiPart("subCategory", "FROZEN")
                .multiPart("brandId", brand.getId())
                .multiPart("name", "테스트 상품")
                .multiPart("description", "테스트 상품 설명")
                .multiPart("origin", "한국")
                .multiPart("price", 15000)
                .multiPart("options[PRICE]", "15000원")
                .multiPart("options[RATING]", "4.5")
                .multiPart("image", "test-image.jpg", "test image content".getBytes(), "image/jpeg")
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
