package subscribenlike.mogupick.product.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

import java.util.Map;

@Getter
@Document(collection = "productOptions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {
    @Id
    private String id;

    private Long productId;

    private RootCategory rootCategory;

    private SubCategory subCategory;

    private Map<String, String> options;

    private ProductOption(Long productId, RootCategory rootCategory, SubCategory subCategory, Map<String, String> options) {
        this.productId = productId;
        this.rootCategory = rootCategory;
        this.subCategory = subCategory;
        this.options = options;
    }

    public static ProductOption of(Product product, RootCategory rootCategory, SubCategory subCategory, Map<String, String> options) {
        return new ProductOption(product.getId(), rootCategory, subCategory, options);
    }
}
