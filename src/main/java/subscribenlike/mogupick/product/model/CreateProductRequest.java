package subscribenlike.mogupick.product.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductOption;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CreateProductRequest {
    private RootCategory rootCategory;
    private SubCategory subCategory;
    private Long brandId;
    private String name;
    private int price;
    private Map<String, String> options;
    private List<MultipartFile> productImages;
    private List<MultipartFile> productDescriptionImages;

    public Product toProduct(Brand brand) {
        return new Product(
                this.name,
                this.price,
                brand
        );
    }

    public ProductOption toProductOption(Product product) {
        return ProductOption.of(
                product,
                this.rootCategory,
                this.subCategory,
                this.options
        );
    }

    private void validate(){
        // 루트 카테고리에 대한 옵션의 키 값이 유효한지 확인
        if (options != null) {
            options.keySet().forEach(CategoryOption::validate);
        }
    }
}
