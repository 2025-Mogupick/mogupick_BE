package subscribenlike.mogupick.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.category.CategoryService;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductOption;
import subscribenlike.mogupick.product.model.*;
import subscribenlike.mogupick.product.model.query.ProductsInMonthQueryResult;
import subscribenlike.mogupick.product.repository.ProductOptionRepository;
import subscribenlike.mogupick.product.repository.ProductRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final BrandRepository brandRepository;

    private final CategoryService categoryService;

    public List<FetchNewProductsInMonthResponse> findAllNewProductsInMonth(int month) {
        return productRepository.findAllProductsInMonth(month).stream()
                .map(ProductService::createFetchNewProductsInMonthResponse)
                .toList();
    }

    @Transactional
    public void createProduct(CreateProductRequest request, MultipartFile image) {
        Brand brand = brandRepository.findOrThrow(request.getBrandId());
        String imageUrl = "https://via.placeholder.com/150"; // TODO: 이미지 업로드 기능 구현 시 변경

        Product product = createProduct(request, brand, imageUrl);
        createProductOption(request, product);
    }

    public ProductWithOptionResponse findProductWithOptionById(Long productId){
        Product product = productRepository.getById(productId);
        ProductOption productOption = productOptionRepository.getByProductId(productId);

        return ProductWithOptionResponse.of(product, productOption);
    }

    public List<ProductWithOptionResponse> findProductWithOptionByRootCategory(RootCategory rootCategory){
        // TODO : 페이지네이션 필요시 구현
        List<ProductOption> productOptions = productOptionRepository.findAllByRootCategory(rootCategory);
        List<Long> productIds = productOptions.stream()
                .map(ProductOption::getProductId)
                .toList();

        Map<Long, Product> products = productRepository.findAllByIdInMaps(productIds);

        return productOptions.stream()
                .map(option-> createProductWithOptionResponse(products, option))
                .toList();
    }

    private static ProductWithOptionResponse createProductWithOptionResponse(Map<Long, Product> products, ProductOption option) {
        return ProductWithOptionResponse.of(products.get(option.getProductId()), option);
    }

    private Product createProduct(CreateProductRequest request, Brand brand, String imageUrl) {
        return productRepository.save(request.toProduct(brand, imageUrl));
    }

    private ProductOption createProductOption(CreateProductRequest request, Product product) {
        categoryService.validateCategoryOptionFromMap(request.getRootCategory(), request.getOptions());
        return productOptionRepository.save(request.toProductOption(product));
    }

    private static FetchNewProductsInMonthResponse createFetchNewProductsInMonthResponse(ProductsInMonthQueryResult product) {
        return FetchNewProductsInMonthResponse.of(
                FetchProductResponse.of(
                        product.getProductId(),
                        product.getProductImageUrl(),
                        product.getProductName(),
                        product.getProductPrice(),
                        product.getCreatedAt()
                ),
                FetchBrandResponse.of(
                        product.getBrandId(),
                        product.getBrandName()
                ),
                FetchReviewResponse.of(
                        product.getRating(),
                        product.getReviewCount()
                )
        );
    }
}
