package subscribenlike.mogupick.product;

import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductOption;

import java.util.HashMap;
import java.util.Map;

public enum ProductOptionFixture {
    간편식옵션(RootCategory.CONVENIENCE_FOOD, SubCategory.FROZEN, Map.of("PRICE", "10000원 미만", "RATING", "4.5", "WEIGHT", "200g")),
    신선식품옵션(RootCategory.FRESH_FOOD, SubCategory.VEGETABLE, Map.of("PRICE", "20000원 미만", "RATING", "4.0", "WEIGHT", "500g")),
    정육수산물옵션(RootCategory.MEAT_SEAFOOD, SubCategory.MEAT, Map.of("PRICE", "30000원 미만", "RATING", "4.8", "WEIGHT", "1kg")),
    유제품음료옵션(RootCategory.DAIRY_BEVERAGE, SubCategory.DAIRY, Map.of("PRICE", "5000원 미만", "RATING", "4.2", "WEIGHT", "1L"));

    private final RootCategory rootCategory;
    private final SubCategory subCategory;
    private final Map<String, String> options;

    ProductOptionFixture(RootCategory rootCategory, SubCategory subCategory, Map<String, String> options) {
        this.rootCategory = rootCategory;
        this.subCategory = subCategory;
        this.options = options;
    }

    public static ProductOption 간편식옵션(Product product) {
        return ProductOption.of(product, 간편식옵션.rootCategory, 간편식옵션.subCategory, 간편식옵션.options);
    }

    public static ProductOption 신선식품옵션(Product product) {
        return ProductOption.of(product, 신선식품옵션.rootCategory, 신선식품옵션.subCategory, 신선식품옵션.options);
    }

    public static ProductOption 정육수산물옵션(Product product) {
        return ProductOption.of(product, 정육수산물옵션.rootCategory, 정육수산물옵션.subCategory, 정육수산물옵션.options);
    }

    public static ProductOption 유제품음료옵션(Product product) {
        return ProductOption.of(product, 유제품음료옵션.rootCategory, 유제품음료옵션.subCategory, 유제품음료옵션.options);
    }
}
