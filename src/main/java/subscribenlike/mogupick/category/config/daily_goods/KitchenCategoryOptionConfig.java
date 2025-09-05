package subscribenlike.mogupick.category.config.daily_goods;

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

public class KitchenCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public KitchenCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 청소/주방용품 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.TOTAL_QUANTITY,
                CategoryOption.WEIGHT,
                CategoryOption.CLEANING_TYPE,
                CategoryOption.FORMULATION
        );

        // 청소/주방용품 서브카테고리 필터 초기화
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
                CategoryOption.WEIGHT, Arrays.asList(
                        CategoryOptionFilter.of("200g 이하", "(0,200]"),
                        CategoryOptionFilter.of("200g-500g", "(200,500]"),
                        CategoryOptionFilter.of("500g-1kg", "(500,1000]"),
                        CategoryOptionFilter.of("1kg-2kg", "(1000,2000]"),
                        CategoryOptionFilter.of("2kg-3kg", "(2000,3000]"),
                        CategoryOptionFilter.of("3kg 이상", "(3000,)")
                ),
                CategoryOption.CLEANING_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("욕실", "BATHROOM"),
                        CategoryOptionFilter.of("곰팡이", "MOLD"),
                        CategoryOptionFilter.of("락스", "BLEACH"),
                        CategoryOptionFilter.of("배수구", "DRAIN"),
                        CategoryOptionFilter.of("세탁조", "WASHER_TUB"),
                        CategoryOptionFilter.of("주방 및 기름때", "KITCHEN_OIL"),
                        CategoryOptionFilter.of("유리", "GLASS"),
                        CategoryOptionFilter.of("에어컨", "AIR_CONDITIONER"),
                        CategoryOptionFilter.of("변기", "TOILET"),
                        CategoryOptionFilter.of("싱크대", "SINK")
                ),
                CategoryOption.FORMULATION, Arrays.asList(
                        CategoryOptionFilter.of("액체", "LIQUID"),
                        CategoryOptionFilter.of("고농축", "CONCENTRATED"),
                        CategoryOptionFilter.of("캡슐", "CAPSULE"),
                        CategoryOptionFilter.of("시트", "SHEET"),
                        CategoryOptionFilter.of("가루", "POWDER"),
                        CategoryOptionFilter.of("스프레이", "SPRAY"),
                        CategoryOptionFilter.of("젤", "GEL"),
                        CategoryOptionFilter.of("거품", "FOAM"),
                        CategoryOptionFilter.of("고체", "SOLID"),
                        CategoryOptionFilter.of("크림", "CREAM")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.DAILY_GOODS;
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
