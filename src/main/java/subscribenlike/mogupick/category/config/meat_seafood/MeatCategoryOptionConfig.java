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

public class MeatCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public MeatCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 정육 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT,
                CategoryOption.TYPE,
                CategoryOption.ORIGIN,
                CategoryOption.STORAGE_METHOD,
                CategoryOption.CONVENIENCE_FOOD,
                CategoryOption.PROCESSING_METHOD,
                CategoryOption.ORGANIC,
                CategoryOption.CUT_TYPE
        );

        // 정육 서브카테고리 필터 초기화
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
                CategoryOption.TYPE, Arrays.asList(
                        CategoryOptionFilter.of("소", "BEEF"),
                        CategoryOptionFilter.of("돼지", "PORK"),
                        CategoryOptionFilter.of("닭", "CHICKEN"),
                        CategoryOptionFilter.of("오리", "DUCK"),
                        CategoryOptionFilter.of("양", "LAMB"),
                        CategoryOptionFilter.of("말", "HORSE"),
                        CategoryOptionFilter.of("기타육고기", "OTHER_MEAT")
                ),
                CategoryOption.ORIGIN, Arrays.asList(
                        CategoryOptionFilter.of("국내산", "DOMESTIC"),
                        CategoryOptionFilter.of("수입산", "IMPORTED")
                ),
                CategoryOption.STORAGE_METHOD, Arrays.asList(
                        CategoryOptionFilter.of("냉장", "REFRIGERATED"),
                        CategoryOptionFilter.of("냉동", "FROZEN"),
                        CategoryOptionFilter.of("실온", "ROOM_TEMPERATURE")
                ),
                CategoryOption.CONVENIENCE_FOOD, Arrays.asList(
                        CategoryOptionFilter.of("즉석섭취", "READY_TO_EAT"),
                        CategoryOptionFilter.of("즉석조리", "READY_TO_COOK"),
                        CategoryOptionFilter.of("밀키트", "MEAL_KIT")
                ),
                CategoryOption.PROCESSING_METHOD, Arrays.asList(
                        CategoryOptionFilter.of("일반", "NORMAL"),
                        CategoryOptionFilter.of("시즈닝", "SEASONED"),
                        CategoryOptionFilter.of("양념", "MARINATED")
                ),
                CategoryOption.ORGANIC, Arrays.asList(
                        CategoryOptionFilter.of("인증 있음", "CERTIFIED")
                ),
                CategoryOption.CUT_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("슬라이스", "SLICED"),
                        CategoryOptionFilter.of("토막", "CHUNK"),
                        CategoryOptionFilter.of("기타", "OTHER")
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
