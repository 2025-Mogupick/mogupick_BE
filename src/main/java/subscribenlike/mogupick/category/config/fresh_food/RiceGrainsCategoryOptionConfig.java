package subscribenlike.mogupick.category.config.fresh_food;

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

public class RiceGrainsCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public RiceGrainsCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 쌀/잡곡 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT,
                CategoryOption.TYPE,
                CategoryOption.GRADE,
                CategoryOption.ORIGIN,
                CategoryOption.PACKAGING
        );

        // 쌀/잡곡 서브카테고리 필터 초기화
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
                        CategoryOptionFilter.of("백미", "WHITE_RICE"),
                        CategoryOptionFilter.of("찹쌀", "GLUTINOUS_RICE"),
                        CategoryOptionFilter.of("현미", "BROWN_RICE"),
                        CategoryOptionFilter.of("흑미", "BLACK_RICE"),
                        CategoryOptionFilter.of("기타쌀", "OTHER_RICE")
                ),
                CategoryOption.GRADE, Arrays.asList(
                        CategoryOptionFilter.of("특", "SPECIAL"),
                        CategoryOptionFilter.of("상", "PREMIUM"),
                        CategoryOptionFilter.of("보통", "STANDARD")
                ),
                CategoryOption.ORIGIN, Arrays.asList(
                        CategoryOptionFilter.of("강원도", "GANGWON"),
                        CategoryOptionFilter.of("경기도", "GYEONGGI"),
                        CategoryOptionFilter.of("강화도", "GANGHWA"),
                        CategoryOptionFilter.of("경상도", "GYEONGSANG"),
                        CategoryOptionFilter.of("전라도", "JEOLLA"),
                        CategoryOptionFilter.of("충청도", "CHUNGCHEONG")
                ),
                CategoryOption.PACKAGING, Arrays.asList(
                        CategoryOptionFilter.of("봉지 및 지퍼백", "BAG_ZIPLOCK"),
                        CategoryOptionFilter.of("상자", "BOX"),
                        CategoryOptionFilter.of("용기 및 통", "CONTAINER"),
                        CategoryOptionFilter.of("1회분", "PORTION")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.FRESH_FOOD;
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
