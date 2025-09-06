package subscribenlike.mogupick.category.config.health_supplements;

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

public class HealthTeaPowderCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public HealthTeaPowderCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 건강즙/음료 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT,
                CategoryOption.JUICE_TYPE,
                CategoryOption.JUICE_PACKAGING,
                CategoryOption.JUICE_METHOD,
                CategoryOption.ORGANIC
        );

        // 건강즙/음료 서브카테고리 필터 초기화
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
                CategoryOption.WEIGHT, Arrays.asList(
                        CategoryOptionFilter.of("200g 이하", "(0,200]"),
                        CategoryOptionFilter.of("200g-500g", "(200,500]"),
                        CategoryOptionFilter.of("500g-1kg", "(500,1000]"),
                        CategoryOptionFilter.of("1kg-2kg", "(1000,2000]"),
                        CategoryOptionFilter.of("2kg-3kg", "(2000,3000]"),
                        CategoryOptionFilter.of("3kg 이상", "(3000,)")
                ),
                CategoryOption.JUICE_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("양배추즙", "CABBAGE_JUICE"),
                        CategoryOptionFilter.of("사과즙", "APPLE_JUICE"),
                        CategoryOptionFilter.of("배즙", "PEAR_JUICE"),
                        CategoryOptionFilter.of("도라지즙", "DORAJI_JUICE"),
                        CategoryOptionFilter.of("석류즙", "POMEGRANATE_JUICE"),
                        CategoryOptionFilter.of("기타", "OTHER")
                ),
                CategoryOption.JUICE_PACKAGING, Arrays.asList(
                        CategoryOptionFilter.of("용기", "CONTAINER"),
                        CategoryOptionFilter.of("병", "BOTTLE"),
                        CategoryOptionFilter.of("튜브", "TUBE"),
                        CategoryOptionFilter.of("일화용", "SINGLE_USE"),
                        CategoryOptionFilter.of("봉지 및 팩", "BAG_PACK")
                ),
                CategoryOption.JUICE_METHOD, Arrays.asList(
                        CategoryOptionFilter.of("착즙", "JUICED")
                ),
                CategoryOption.ORGANIC, Arrays.asList(
                        CategoryOptionFilter.of("인증있음", "CERTIFIED")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.HEALTH_SUPPLEMENTS;
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
