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

public class BabyFormulaCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public BabyFormulaCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 분유 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.BABY_WEIGHT,
                CategoryOption.BABY_AGE,
                CategoryOption.BABY_STAGE,
                CategoryOption.BABY_FORM,
                CategoryOption.BABY_FOOD_TYPE,
                CategoryOption.ORGANIC
        );

        // 분유 서브카테고리 필터 초기화
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
                CategoryOption.BABY_WEIGHT, Arrays.asList(
                        CategoryOptionFilter.of("400g 이하", "(0,400]"),
                        CategoryOptionFilter.of("400~600g", "(400,600]"),
                        CategoryOptionFilter.of("600~800g", "(600,800]"),
                        CategoryOptionFilter.of("800g 이상", "(800,)")
                ),
                CategoryOption.BABY_AGE, Arrays.asList(
                        CategoryOptionFilter.of("미숙아", "PREMATURE"),
                        CategoryOptionFilter.of("0~6개월", "0_6_MONTHS"),
                        CategoryOptionFilter.of("6~12개월", "6_12_MONTHS"),
                        CategoryOptionFilter.of("12~24개월", "12_24_MONTHS")
                ),
                CategoryOption.BABY_STAGE, Arrays.asList(
                        CategoryOptionFilter.of("1", "1"),
                        CategoryOptionFilter.of("2", "2"),
                        CategoryOptionFilter.of("3", "3"),
                        CategoryOptionFilter.of("4", "4")
                ),
                CategoryOption.BABY_FORM, Arrays.asList(
                        CategoryOptionFilter.of("통", "CAN"),
                        CategoryOptionFilter.of("액상", "LIQUID"),
                        CategoryOptionFilter.of("스틱", "STICK")
                ),
                CategoryOption.BABY_FOOD_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("일반", "REGULAR"),
                        CategoryOptionFilter.of("산양", "GOAT"),
                        CategoryOptionFilter.of("소이", "SOY"),
                        CategoryOptionFilter.of("대두", "SOYBEAN"),
                        CategoryOptionFilter.of("유당불내증", "LACTOSE_INTOLERANT"),
                        CategoryOptionFilter.of("알레르기", "ALLERGY"),
                        CategoryOptionFilter.of("설사", "DIARRHEA"),
                        CategoryOptionFilter.of("구토", "VOMITING"),
                        CategoryOptionFilter.of("저체중", "LOW_WEIGHT"),
                        CategoryOptionFilter.of("미숙아", "PREMATURE"),
                        CategoryOptionFilter.of("무전분(배앓이)", "NO_STARCH_COLIC"),
                        CategoryOptionFilter.of("킨더밀쉬", "KINDER_MILCH"),
                        CategoryOptionFilter.of("기타", "OTHER")
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
