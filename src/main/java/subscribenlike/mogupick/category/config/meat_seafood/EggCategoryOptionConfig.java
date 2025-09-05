package subscribenlike.mogupick.category.config.meat_seafood;

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

public class EggCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public EggCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 계란/알류/가공란 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT,
                CategoryOption.TOTAL_COUNT,
                CategoryOption.STORAGE_METHOD,
                CategoryOption.TYPE,
                CategoryOption.CHARACTERISTICS,
                CategoryOption.ORGANIC,
                CategoryOption.FARMING_ENVIRONMENT
        );

        // 계란/알류/가공란 서브카테고리 필터 초기화
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
                CategoryOption.TOTAL_COUNT, Arrays.asList(
                        CategoryOptionFilter.of("6구 이하", "(0,6]"),
                        CategoryOptionFilter.of("6~10구", "(6,10]"),
                        CategoryOptionFilter.of("10~20구", "(10,20]"),
                        CategoryOptionFilter.of("20~30구", "(20,30]"),
                        CategoryOptionFilter.of("30구 이상", "(30,)")
                ),
                CategoryOption.STORAGE_METHOD, Arrays.asList(
                        CategoryOptionFilter.of("냉장", "REFRIGERATED"),
                        CategoryOptionFilter.of("냉동", "FROZEN"),
                        CategoryOptionFilter.of("실온", "ROOM_TEMPERATURE")
                ),
                CategoryOption.TYPE, Arrays.asList(
                        CategoryOptionFilter.of("구운 및 훈제", "BAKED_SMOKED"),
                        CategoryOptionFilter.of("날계란", "RAW_EGG"),
                        CategoryOptionFilter.of("반숙란", "SOFT_BOILED"),
                        CategoryOptionFilter.of("메추리알", "QUAIL_EGG"),
                        CategoryOptionFilter.of("기타", "OTHER")
                ),
                CategoryOption.CHARACTERISTICS, Arrays.asList(
                        CategoryOptionFilter.of("동물복지", "ANIMAL_WELFARE"),
                        CategoryOptionFilter.of("무항생제", "ANTIBIOTIC_FREE"),
                        CategoryOptionFilter.of("유정란", "FERTILE_EGG")
                ),
                CategoryOption.ORGANIC, Arrays.asList(
                        CategoryOptionFilter.of("인증 있음", "CERTIFIED")
                ),
                CategoryOption.FARMING_ENVIRONMENT, Arrays.asList(
                        CategoryOptionFilter.of("1", "1"),
                        CategoryOptionFilter.of("2", "2"),
                        CategoryOptionFilter.of("3", "3")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.MEAT_SEAFOOD;
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
