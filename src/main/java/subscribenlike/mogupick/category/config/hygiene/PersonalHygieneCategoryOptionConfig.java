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

public class PersonalHygieneCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public PersonalHygieneCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 개인 위생 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.TOTAL_QUANTITY,
                CategoryOption.WEIGHT,
                CategoryOption.HYGIENE_TYPE,
                CategoryOption.SCENT
        );

        // 개인 위생 서브카테고리 필터 초기화
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
                        CategoryOptionFilter.of("3개 이하", "(0,3]"),
                        CategoryOptionFilter.of("3~6개", "(3,6]"),
                        CategoryOptionFilter.of("6~9개", "(6,9]"),
                        CategoryOptionFilter.of("9개 이상", "[9,)")
                ),
                CategoryOption.WEIGHT, Arrays.asList(
                        CategoryOptionFilter.of("200g 이하", "(0,200]"),
                        CategoryOptionFilter.of("200g-500g", "(200,500]"),
                        CategoryOptionFilter.of("500g-1kg", "(500,1000]"),
                        CategoryOptionFilter.of("1kg-2kg", "(1000,2000]"),
                        CategoryOptionFilter.of("2kg-3kg", "(2000,3000]"),
                        CategoryOptionFilter.of("3kg 이상", "(3000,)")
                ),
                CategoryOption.HYGIENE_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("샴푸", "SHAMPOO"),
                        CategoryOptionFilter.of("린스", "CONDITIONER"),
                        CategoryOptionFilter.of("컨디셔너", "TREATMENT"),
                        CategoryOptionFilter.of("바디", "BODY"),
                        CategoryOptionFilter.of("세안", "FACE_WASH"),
                        CategoryOptionFilter.of("면도", "SHAVING"),
                        CategoryOptionFilter.of("구강", "ORAL"),
                        CategoryOptionFilter.of("화장지", "TOILET_PAPER"),
                        CategoryOptionFilter.of("물티슈", "WET_TISSUE")
                ),
                CategoryOption.SCENT, Arrays.asList(
                        CategoryOptionFilter.of("무", "NONE"),
                        CategoryOptionFilter.of("머스크", "MUSK"),
                        CategoryOptionFilter.of("베이비파우더", "BABY_POWDER"),
                        CategoryOptionFilter.of("시어버터", "SHEA_BUTTER"),
                        CategoryOptionFilter.of("과일", "FRUIT"),
                        CategoryOptionFilter.of("꽃", "FLOWER"),
                        CategoryOptionFilter.of("허브", "HERB"),
                        CategoryOptionFilter.of("기타", "OTHER")
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
