package subscribenlike.mogupick.category.config;

import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PetSuppliesCategoryOptionConfig implements CategoryOptionConfig {

    private final List<CategoryOption> options;
    private final Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public PetSuppliesCategoryOptionConfig() {
        this.options = Arrays.asList();
        this.filters = Map.of();
        init();
    }

    private void init() {
        // TODO: 옵션 초기화
        /*
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.WEIGHT
        );
        */

        // TODO: 필터 초기화
        /*
        this.filters = Map.of(
                CategoryOption.PRICE, Arrays.asList(
                        CategoryOptionFilter.of("5,000원 미만", "(0,5000)"),
                        CategoryOptionFilter.of("5,000원 이상-10,000원 미만", "[5000,10000)"),
                        CategoryOptionFilter.of("10,000원 이상-20,000원 미만", "[10000,20000)"),
                        CategoryOptionFilter.of("20,000원 이상-30,000원 미만", "[20000,30000)"),
                        CategoryOptionFilter.of("30,000원 이상", "[30000,)")
                ),
                CategoryOption.WEIGHT, Arrays.asList(
                        CategoryOptionFilter.of("100g 이하", "(0,100]"),
                        CategoryOptionFilter.of("100g-500g", "(100,500]"),
                        CategoryOptionFilter.of("500g-1kg", "(500,1000]"),
                        CategoryOptionFilter.of("1kg 이상", "(1000,)")
                )
        );
        */
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.PET_SUPPLIES;
    }

    @Override
    public List<CategoryOption> getCategoryOptions() {
        return options;
    }

    @Override
    public Map<CategoryOption, List<CategoryOptionFilter>> getCategoryFilters() {
        return filters;
    }
}
