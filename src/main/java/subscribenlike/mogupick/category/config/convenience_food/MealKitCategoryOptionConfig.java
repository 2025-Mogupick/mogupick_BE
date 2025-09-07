package subscribenlike.mogupick.category.config.convenience_food;

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

public class MealKitCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public MealKitCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 밀키트 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT,
                CategoryOption.EAT_METHOD,
                CategoryOption.CALORIE,
                CategoryOption.COOK_METHOD,
                CategoryOption.QUANTITY,
                CategoryOption.CONVENIENCE_FOOD_TYPE,
                CategoryOption.PORTION_SIZE
        );

        // 밀키트 서브카테고리 필터 초기화
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
                CategoryOption.EAT_METHOD, Arrays.asList(
                        CategoryOptionFilter.of("즉석완조리식품", "READY_TO_EAT"),
                        CategoryOptionFilter.of("즉석섭취식품", "READY_TO_CONSUME"),
                        CategoryOptionFilter.of("즉석반조리식품", "SEMI_READY")
                ),
                CategoryOption.CALORIE, Arrays.asList(
                        CategoryOptionFilter.of("100kcal 이하", "(0,100]"),
                        CategoryOptionFilter.of("100kcal-200kcal", "(100,200]"),
                        CategoryOptionFilter.of("200kcal-300kcal", "(200,300]"),
                        CategoryOptionFilter.of("300kcal-500kcal", "(300,500]"),
                        CategoryOptionFilter.of("500kcal 이상", "(500,)")
                ),
                CategoryOption.COOK_METHOD, Arrays.asList(
                        CategoryOptionFilter.of("에어프라이어", "AIR_FRYER"),
                        CategoryOptionFilter.of("에어프라이어,전자렌지", "AIR_FRYER_MICROWAVE"),
                        CategoryOptionFilter.of("전자렌지", "MICROWAVE")
                ),
                CategoryOption.QUANTITY, Arrays.asList(
                        CategoryOptionFilter.of("6개 이하", "(0,6]"),
                        CategoryOptionFilter.of("6개-10개", "(6,10]"),
                        CategoryOptionFilter.of("10개-20개", "(10,20]"),
                        CategoryOptionFilter.of("20개-30개", "(20,30]"),
                        CategoryOptionFilter.of("50개 이상", "[50,)")
                ),
                CategoryOption.CONVENIENCE_FOOD_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("스테이크", "STEAK"),
                        CategoryOptionFilter.of("파스타", "PASTA"),
                        CategoryOptionFilter.of("감바스", "GAMBAS"),
                        CategoryOptionFilter.of("국", "SOUP"),
                        CategoryOptionFilter.of("탕", "HOT_POT"),
                        CategoryOptionFilter.of("찌개", "STEW"),
                        CategoryOptionFilter.of("중식", "CHINESE"),
                        CategoryOptionFilter.of("분식", "STREET_FOOD"),
                        CategoryOptionFilter.of("아시안", "ASIAN"),
                        CategoryOptionFilter.of("볶음", "STIR_FRY"),
                        CategoryOptionFilter.of("조림", "BRAISED"),
                        CategoryOptionFilter.of("찜", "STEAMED"),
                        CategoryOptionFilter.of("덮밥 및 비빔밥", "BIBIMBAP"),
                        CategoryOptionFilter.of("기타", "OTHER")
                ),
                CategoryOption.PORTION_SIZE, Arrays.asList(
                        CategoryOptionFilter.of("1~2인분", "1_2_SERVINGS"),
                        CategoryOptionFilter.of("2~3인분", "2_3_SERVINGS"),
                        CategoryOptionFilter.of("3인분 이상", "3_PLUS_SERVINGS")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.CONVENIENCE_FOOD;
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
