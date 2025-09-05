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

public class BabyFoodCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public BabyFoodCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 이유식 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.EAT_METHOD,
                CategoryOption.CALORIE,
                CategoryOption.COOK_METHOD,
                CategoryOption.BABY_AGE,
                CategoryOption.STORAGE_METHOD,
                CategoryOption.JUICE_PACKAGING,
                CategoryOption.SPOON_HOLDER,
                CategoryOption.ORGANIC
        );

        // 이유식 서브카테고리 필터 초기화
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
                CategoryOption.EAT_METHOD, Arrays.asList(
                        CategoryOptionFilter.of("즉석섭취음료", "READY_TO_DRINK"),
                        CategoryOptionFilter.of("즉석섭취식품", "READY_TO_CONSUME"),
                        CategoryOptionFilter.of("즉석조리식품", "READY_TO_COOK")
                ),
                CategoryOption.CALORIE, Arrays.asList(
                        CategoryOptionFilter.of("20kcal 이하", "(0,20]"),
                        CategoryOptionFilter.of("20kcal-40kcal", "(20,40]"),
                        CategoryOptionFilter.of("40kcal-100kcal", "(40,100]"),
                        CategoryOptionFilter.of("100kcal-200kcal", "(100,200]"),
                        CategoryOptionFilter.of("200kcal 이상", "(200,)")
                ),
                CategoryOption.COOK_METHOD, Arrays.asList(
                        CategoryOptionFilter.of("전자레인지 가능", "MICROWAVE_AVAILABLE")
                ),
                CategoryOption.BABY_AGE, Arrays.asList(
                        CategoryOptionFilter.of("4~6개월", "4_6_MONTHS"),
                        CategoryOptionFilter.of("6~9개월", "6_9_MONTHS"),
                        CategoryOptionFilter.of("9~12개월", "9_12_MONTHS"),
                        CategoryOptionFilter.of("12개월 이상", "12_PLUS_MONTHS")
                ),
                CategoryOption.STORAGE_METHOD, Arrays.asList(
                        CategoryOptionFilter.of("냉장", "REFRIGERATED"),
                        CategoryOptionFilter.of("실온", "ROOM_TEMPERATURE")
                ),
                CategoryOption.JUICE_PACKAGING, Arrays.asList(
                        CategoryOptionFilter.of("용기 및 통", "CONTAINER"),
                        CategoryOptionFilter.of("파우치", "POUCH"),
                        CategoryOptionFilter.of("튜브형", "TUBE")
                ),
                CategoryOption.SPOON_HOLDER, Arrays.asList(
                        CategoryOptionFilter.of("유", "WITH_SPOON_HOLDER"),
                        CategoryOptionFilter.of("무", "WITHOUT_SPOON_HOLDER")
                ),
                CategoryOption.ORGANIC, Arrays.asList(
                        CategoryOptionFilter.of("인증있음", "CERTIFIED")
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
