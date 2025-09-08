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

public class DiaperCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public DiaperCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 기저귀 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.TOTAL_QUANTITY,
                CategoryOption.DIAPER_WEIGHT,
                CategoryOption.DIAPER_SIZE_STAGE,
                CategoryOption.DIAPER_TYPE,
                CategoryOption.SWIMMING_DIAPER,
                CategoryOption.GENDER_TARGET
        );

        // 기저귀 서브카테고리 필터 초기화
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
                CategoryOption.TOTAL_QUANTITY, Arrays.asList(
                        CategoryOptionFilter.of("40매 이하", "(0,40]"),
                        CategoryOptionFilter.of("40~80매", "(40,80]"),
                        CategoryOptionFilter.of("80~120매", "(80,120]"),
                        CategoryOptionFilter.of("120~200매", "(120,200]"),
                        CategoryOptionFilter.of("200매 이상", "(200,)")
                ),
                CategoryOption.DIAPER_WEIGHT, Arrays.asList(
                        CategoryOptionFilter.of("4kg 이하", "(0,4]"),
                        CategoryOptionFilter.of("4~8kg", "(4,8]"),
                        CategoryOptionFilter.of("8~12kg", "(8,12]"),
                        CategoryOptionFilter.of("12~16kg", "(12,16]"),
                        CategoryOptionFilter.of("16kg 이상", "(16,)")
                ),
                CategoryOption.DIAPER_SIZE_STAGE, Arrays.asList(
                        CategoryOptionFilter.of("0~1단계(신생아용)", "NEWBORN_0_1"),
                        CategoryOptionFilter.of("2단계 소형", "SMALL_2"),
                        CategoryOptionFilter.of("3단계 중형", "MEDIUM_3"),
                        CategoryOptionFilter.of("4단계 대형", "LARGE_4"),
                        CategoryOptionFilter.of("5단계 특대형", "EXTRA_LARGE_5"),
                        CategoryOptionFilter.of("6단계 점보형", "JUMBO_6")
                ),
                CategoryOption.DIAPER_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("팬티형", "PANTY"),
                        CategoryOptionFilter.of("밴드형", "BAND"),
                        CategoryOptionFilter.of("일자형", "STRAIGHT")
                ),
                CategoryOption.SWIMMING_DIAPER, Arrays.asList(
                        CategoryOptionFilter.of("수영장용", "SWIMMING_POOL")
                ),
                CategoryOption.GENDER_TARGET, Arrays.asList(
                        CategoryOptionFilter.of("여아", "FEMALE"),
                        CategoryOptionFilter.of("남아", "MALE"),
                        CategoryOptionFilter.of("공용", "UNISEX")
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
