package subscribenlike.mogupick.product.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;
import subscribenlike.mogupick.product.domain.ProductOption;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class FetchProductOptionResponse {
    private String id;
    private Long productId;
    private RootCategory rootCategory;
    private SubCategory subCategory;
    private List<String> optionNames;
    private Map<String, String> options;


    public static FetchProductOptionResponse from(ProductOption productOption) {
        return new FetchProductOptionResponse(
                productOption.getId(),
                productOption.getProductId(),
                productOption.getRootCategory(),
                productOption.getSubCategory(),
                productOption.getOptions().keySet().stream().toList(),
                productOption.getOptions()
        );
    }
}
