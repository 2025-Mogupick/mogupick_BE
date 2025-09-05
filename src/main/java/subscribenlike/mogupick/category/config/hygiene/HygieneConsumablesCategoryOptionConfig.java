package subscribenlike.mogupick.category.config.hygiene;

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

public class HygieneConsumablesCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public HygieneConsumablesCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 생리대/성인용 기저귀 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.TOTAL_QUANTITY,
                CategoryOption.SANITARY_PAD_TYPE,
                CategoryOption.DIAPER_SIZE
        );

        // 생리대/성인용 기저귀 서브카테고리 필터 초기화
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
                CategoryOption.TOTAL_QUANTITY, Arrays.asList(
                        CategoryOptionFilter.of("3개 이하", "(0,3]"),
                        CategoryOptionFilter.of("3~6개", "(3,6]"),
                        CategoryOptionFilter.of("6~9개", "(6,9]"),
                        CategoryOptionFilter.of("9개 이상", "[9,)")
                ),
                CategoryOption.SANITARY_PAD_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("일자형 생리대", "STRAIGHT_PAD"),
                        CategoryOptionFilter.of("날개형 생리대", "WING_PAD"),
                        CategoryOptionFilter.of("체내형(탐폰)", "TAMPON"),
                        CategoryOptionFilter.of("일반형 오버나이트", "REGULAR_OVERNIGHT"),
                        CategoryOptionFilter.of("팬티형 오버나이트", "PANTY_OVERNIGHT"),
                        CategoryOptionFilter.of("면생리대", "COTTON_PAD")
                ),
                CategoryOption.DIAPER_SIZE, Arrays.asList(
                        CategoryOptionFilter.of("소", "SMALL"),
                        CategoryOptionFilter.of("중", "MEDIUM"),
                        CategoryOptionFilter.of("대", "LARGE"),
                        CategoryOptionFilter.of("특대", "EXTRA_LARGE")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.HYGIENE;
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
