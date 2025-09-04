package subscribenlike.mogupick.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import subscribenlike.mogupick.brand.BrandFixture;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;
import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductOption;
import subscribenlike.mogupick.product.model.CreateProductRequest;
import subscribenlike.mogupick.product.model.FetchNewProductsInMonthResponse;
import subscribenlike.mogupick.product.model.ProductWithOptionResponse;
import subscribenlike.mogupick.product.repository.ProductOptionRepository;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.product.service.ProductService;
import subscribenlike.mogupick.review.ReviewFixture;
import subscribenlike.mogupick.review.repository.ReviewRepository;
import subscribenlike.mogupick.review.domain.Review;
import subscribenlike.mogupick.support.annotation.ServiceTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @MockBean
    private ProductOptionRepository productOptionRepository;

    @Test
    void 이번_달_새로나온_구독_상품들을_조회할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        Review review = ReviewFixture.좋은리뷰(member, product);
        reviewRepository.save(review);

        // When
        Page<FetchNewProductsInMonthResponse> result = productService.findAllNewProductsInMonth(LocalDateTime.now().getMonthValue(), PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).product().productId()).isEqualTo(product.getId());
        assertThat(result.getContent().get(0).product().productName()).isEqualTo("쿠팡 구독 서비스");
        assertThat(result.getContent().get(0).brand().brandName()).isEqualTo("쿠팡");
        assertThat(result.getContent().get(0).review().rating()).isEqualTo(4.5);
        assertThat(result.getContent().get(0).review().reviewCount()).isEqualTo(1);
    }

    @Test
    void 이번_달_새로나온_구독_상품이_없을_경우_빈_리스트를_반환한다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        // When - 다른 달로 조회
        Page<FetchNewProductsInMonthResponse> result = productService.findAllNewProductsInMonth(12, PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).isEmpty();
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

        // When
        Page<FetchNewProductsInMonthResponse> result = productService.findAllNewProductsInMonth(LocalDateTime.now().getMonthValue(), PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("product.productName")
                .containsExactlyInAnyOrder("쿠팡 구독 서비스", "네이버 구독 서비스");
        assertThat(result.getContent()).extracting("brand.brandName")
                .containsExactlyInAnyOrder("쿠팡", "네이버");
    }

    @Test
    void 리뷰가_없는_상품도_조회할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        // When
        Page<FetchNewProductsInMonthResponse> result = productService.findAllNewProductsInMonth(LocalDateTime.now().getMonthValue(), PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).product().productId()).isEqualTo(product.getId());
        assertThat(result.getContent().get(0).review().rating()).isEqualTo(0.0);
        assertThat(result.getContent().get(0).review().reviewCount()).isEqualTo(0);
    }

    @Test
    void 상품을_생성할_수_있다() throws IOException {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        MockMultipartFile image = new MockMultipartFile(
                "image", 
                "test-image.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
        );

        CreateProductRequest request = new CreateProductRequest(
                RootCategory.CONVENIENCE_FOOD,
                SubCategory.FROZEN,
                brand.getId(),
                "테스트 상품",
                "테스트 상품 설명",
                "한국",
                15000,
                Map.of("PRICE", "15000원", "RATING", "4.5"),
                List.of(image)
        );

        // Mock ProductOptionRepository
        Product product = ProductFixture.쿠팡구독(brand);
        ProductOption mockProductOption = ProductOptionFixture.간편식옵션(product);
        when(productOptionRepository.save(any(ProductOption.class))).thenReturn(mockProductOption);

        // When
        productService.createProduct(request);

        // Then
        assertThat(productRepository.count()).isEqualTo(1);
    }

    @Test
    void 상품ID로_상품과_옵션을_조회할_수_있다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        Product product = ProductFixture.쿠팡구독(brand);
        productRepository.save(product);

        ProductOption productOption = ProductOptionFixture.간편식옵션(product);
        when(productOptionRepository.getByProductId(product.getId())).thenReturn(productOption);

        // When
        ProductWithOptionResponse result = productService.findProductWithOptionById(product.getId());

        // Then
        assertThat(result.getProduct().getId()).isEqualTo(product.getId());
        assertThat(result.getProduct().getName()).isEqualTo("쿠팡 구독 서비스");
        assertThat(result.getOption().getProductId()).isEqualTo(product.getId());
        assertThat(result.getOption().getRootCategory()).isEqualTo(RootCategory.CONVENIENCE_FOOD);
    }

    @Test
    void 존재하지_않는_상품ID로_조회할_경우_예외가_발생한다() {
        // Given
        Long nonExistentProductId = 999L;
        when(productOptionRepository.getByProductId(nonExistentProductId))
                .thenThrow(new subscribenlike.mogupick.product.common.ProductException(subscribenlike.mogupick.product.common.ProductErrorCode.PRODUCT_NOT_FOUND));

        // When & Then
        assertThatThrownBy(() -> productService.findProductWithOptionById(nonExistentProductId))
                .isInstanceOf(subscribenlike.mogupick.product.common.ProductException.class);
    }

    @Test
    void 루트카테고리로_상품과_옵션_목록을_조회할_수_있다() {
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

        ProductOption option1 = ProductOptionFixture.간편식옵션(product1);
        ProductOption option2 = ProductOptionFixture.간편식옵션(product2);
        when(productOptionRepository.findAllByRootCategory(RootCategory.CONVENIENCE_FOOD))
                .thenReturn(List.of(option1, option2));

        // When
        Page<ProductWithOptionResponse> result = productService.findProductWithOptionByRootCategory(RootCategory.CONVENIENCE_FOOD, PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("product.name")
                .containsExactlyInAnyOrder("쿠팡 구독 서비스", "네이버 구독 서비스");
        assertThat(result.getContent()).extracting("option.rootCategory")
                .containsOnly(RootCategory.CONVENIENCE_FOOD);
    }

    @Test
    void 루트카테고리에_해당하는_상품이_없을_경우_빈_리스트를_반환한다() {
        // Given
        RootCategory emptyCategory = RootCategory.HEALTH_SUPPLEMENTS;
        when(productOptionRepository.findAllByRootCategory(emptyCategory)).thenReturn(List.of());

        // When
        Page<ProductWithOptionResponse> result = productService.findProductWithOptionByRootCategory(emptyCategory, PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void 상품_생성_시_브랜드가_존재하지_않으면_예외가_발생한다() {
        // Given
        Long nonExistentBrandId = 999L;
        MockMultipartFile image = new MockMultipartFile(
                "image", 
                "test-image.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
        );

        CreateProductRequest request = new CreateProductRequest(
                RootCategory.CONVENIENCE_FOOD,
                SubCategory.FROZEN,
                nonExistentBrandId,
                "테스트 상품",
                "테스트 상품 설명",
                "한국",
                15000,
                Map.of("PRICE", "15000원", "RATING", "4.5"),
                List.of(image)
        );

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(org.springframework.dao.InvalidDataAccessApiUsageException.class);
    }

    @Test
    void 상품_생성_시_카테고리_옵션이_유효하지_않으면_예외가_발생한다() {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        MockMultipartFile image = new MockMultipartFile(
                "image", 
                "test-image.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
        );

        CreateProductRequest request = new CreateProductRequest(
                RootCategory.CONVENIENCE_FOOD,
                SubCategory.FROZEN,
                brand.getId(),
                "테스트 상품",
                "테스트 상품 설명",
                "한국",
                15000,
                Map.of("INVALID_OPTION", "잘못된 옵션"), // 유효하지 않은 옵션
                List.of(image)
        );

        // Mock CategoryService에서 예외 발생
        when(productOptionRepository.save(any(ProductOption.class)))
                .thenThrow(new subscribenlike.mogupick.category.common.exception.CategoryException(
                        subscribenlike.mogupick.category.common.exception.CategoryErrorCode.INVALID_INPUT_VALUE));

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(subscribenlike.mogupick.category.common.exception.CategoryException.class);
    }

    @Test
    void 상품_생성_시_이미지가_null이어도_상품은_생성된다() throws IOException {
        // Given
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);

        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);

        CreateProductRequest request = new CreateProductRequest(
                RootCategory.CONVENIENCE_FOOD,
                SubCategory.FROZEN,
                brand.getId(),
                "테스트 상품",
                "테스트 상품 설명",
                "한국",
                15000,
                Map.of("PRICE", "15000원", "RATING", "4.5"),
                null // 이미지가 null
        );

        // Mock ProductOptionRepository
        Product product = ProductFixture.쿠팡구독(brand);
        ProductOption mockProductOption = ProductOptionFixture.간편식옵션(product);
        when(productOptionRepository.save(any(ProductOption.class))).thenReturn(mockProductOption);

        // When
        productService.createProduct(request);

        // Then
        assertThat(productRepository.count()).isEqualTo(1);
    }
}
