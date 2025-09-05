package subscribenlike.mogupick.category.config.snack;

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

public class ChocolateCandyGumCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public ChocolateCandyGumCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 초콜릿/사탕/껌 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT,
                CategoryOption.SUGAR_CONTENT,
                CategoryOption.PACKAGING_TYPE,
                CategoryOption.INDIVIDUAL_PACKAGING,
                CategoryOption.CONVENIENCE_FOOD_TYPE,
                CategoryOption.FLAVOR_SCENT
        );

        // 초콜릿/사탕/껌 서브카테고리 필터 초기화
        this.filters = Map.of(
                CategoryOption.PRICE, Arrays.asList(
                        CategoryOptionFilter.of("10,000원 미만", "(0,10000)"),
                        CategoryOptionFilter.of("10,000원 이상-20,000원 미만", "[10000,20000)"),
                        CategoryOptionFilter.of("20,000원 이상-30,000원 미만", "[20000,30000)"),
                        CategoryOptionFilter.of("30,000원 이상-40,000원 미만", "[30000,40000)"),
                        CategoryOptionFilter.of("40,000원 이상", "[40000,)"),
                        CategoryOptionFilter.of("범위", "RANGE")
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
                CategoryOption.SUGAR_CONTENT, Arrays.asList(
                        CategoryOptionFilter.of("무설탕", "NO_SUGAR"),
                        CategoryOptionFilter.of("저설탕", "LOW_SUGAR"),
                        CategoryOptionFilter.of("설탕첨가", "SUGAR_ADDED"),
                        CategoryOptionFilter.of("당첨가", "SWEETENER_ADDED")
                ),
                CategoryOption.PACKAGING_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("봉지 및 지퍼백", "BAG_ZIPLOCK"),
                        CategoryOptionFilter.of("상자", "BOX"),
                        CategoryOptionFilter.of("용기 및 통", "CONTAINER"),
                        CategoryOptionFilter.of("컵", "CUP")
                ),
                CategoryOption.INDIVIDUAL_PACKAGING, Arrays.asList(
                        CategoryOptionFilter.of("개별포장", "INDIVIDUAL_PACKAGING")
                ),
                CategoryOption.CONVENIENCE_FOOD_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("초콜릿", "CHOCOLATE"),
                        CategoryOptionFilter.of("사탕", "CANDY"),
                        CategoryOptionFilter.of("젤리", "JELLY"),
                        CategoryOptionFilter.of("껌", "GUM"),
                        CategoryOptionFilter.of("기타", "OTHER")
                ),
                CategoryOption.FLAVOR_SCENT, Arrays.asList(
                        CategoryOptionFilter.of("과일", "FRUIT"),
                        CategoryOptionFilter.of("민트", "MINT"),
                        CategoryOptionFilter.of("커피", "COFFEE"),
                        CategoryOptionFilter.of("초코", "CHOCOLATE"),
                        CategoryOptionFilter.of("카라멜", "CARAMEL"),
                        CategoryOptionFilter.of("우유", "MILK"),
                        CategoryOptionFilter.of("요거트", "YOGURT"),
                        CategoryOptionFilter.of("견과", "NUT"),
                        CategoryOptionFilter.of("허브", "HERB"),
                        CategoryOptionFilter.of("기타", "OTHER")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.SNACK;
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
