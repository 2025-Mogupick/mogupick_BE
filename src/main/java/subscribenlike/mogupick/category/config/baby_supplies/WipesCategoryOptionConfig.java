package subscribenlike.mogupick.category.config.baby_supplies;

import subscribenlike.mogupick.category.common.exception.CategoryErrorCode;
import subscribenlike.mogupick.category.common.exception.CategoryException;
import subscribenlike.mogupick.category.config.CategoryOptionConfig;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WipesCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public WipesCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 물티슈 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.SHEET_COUNT,
                CategoryOption.TISSUE_TYPE,
                CategoryOption.EMBOSSING
        );

        // 물티슈 서브카테고리 필터 초기화
        this.filters = Map.of(
                CategoryOption.PRICE, Arrays.asList(
                        CategoryOptionFilter.of("10,000원 미만", "(0,10000)"),
                        CategoryOptionFilter.of("10,000원 이상-20,000원 미만", "[10000,20000)"),
                        CategoryOptionFilter.of("20,000원 이상-30,000원 미만", "[20000,30000)"),
                        CategoryOptionFilter.of("30,000원 이상-40,000원 미만", "[30000,40000)"),
                        CategoryOptionFilter.of("40,000원 이상", "[40000,)") 
                ),
                CategoryOption.RATING, Arrays.asList(
                        CategoryOptionFilter.of("1개", "1"),
                        CategoryOptionFilter.of("2개", "2"),
                        CategoryOptionFilter.of("3개", "3"),
                        CategoryOptionFilter.of("4개", "4"),
                        CategoryOptionFilter.of("5개", "5")
                ),
                CategoryOption.SHEET_COUNT, Arrays.asList(
                        CategoryOptionFilter.of("50매 이하", "(0,50]"),
                        CategoryOptionFilter.of("50~100매", "(50,100]"),
                        CategoryOptionFilter.of("100~200매", "(100,200]"),
                        CategoryOptionFilter.of("200~300매", "(200,300]")
                ),
                CategoryOption.TISSUE_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("캡형", "CAP_TYPE"),
                        CategoryOptionFilter.of("리필형", "REFILL_TYPE"),
                        CategoryOptionFilter.of("휴대용", "PORTABLE"),
                        CategoryOptionFilter.of("낱개포장", "INDIVIDUAL_PACK")
                ),
                CategoryOption.EMBOSSING, Arrays.asList(
                        CategoryOptionFilter.of("유", "WITH_EMBOSSING"),
                        CategoryOptionFilter.of("무", "WITHOUT_EMBOSSING")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.BABY_SUPPLIES;
    }

    @Override
    public List<CategoryOption> getCategoryOptions() {
        return options;
    }

    @Override
    public Map<CategoryOption, List<CategoryOptionFilter>> getCategoryFilters() {
        return filters;
    }

    @Override
    public List<CategoryOption> getCategoryOptions(SubCategory subCategory) {
        throw new CategoryException(CategoryErrorCode.SUB_CATEGORY_INVALID_INPUT_VALUE, List.of(subCategory.name()));
    }

    @Override
    public Map<CategoryOption, List<CategoryOptionFilter>> getCategoryFilters(SubCategory subCategory) {
        throw new CategoryException(CategoryErrorCode.SUB_CATEGORY_INVALID_INPUT_VALUE, List.of(subCategory.name()));
    }

    @Override
    public boolean hasSubCategory(SubCategory subCategory) {
        throw new CategoryException(CategoryErrorCode.SUB_CATEGORY_INVALID_INPUT_VALUE, List.of(subCategory.name()));
    }
}
