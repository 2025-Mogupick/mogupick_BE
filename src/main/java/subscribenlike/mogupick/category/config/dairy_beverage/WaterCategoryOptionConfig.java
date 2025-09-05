package subscribenlike.mogupick.category.config.dairy_beverage;

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

public class WaterCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public WaterCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 생수 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.TOTAL_QUANTITY,
                CategoryOption.CONTAINER_TYPE
        );

        // 생수 서브카테고리 필터 초기화
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
                        CategoryOptionFilter.of("6개 이하", "(0,6]"),
                        CategoryOptionFilter.of("6~12개", "(6,12]"),
                        CategoryOptionFilter.of("12~18개", "(12,18]"),
                        CategoryOptionFilter.of("18~24개", "(18,24]"),
                        CategoryOptionFilter.of("24~40개", "(24,40]"),
                        CategoryOptionFilter.of("40개 이상", "(40,)")
                ),
                CategoryOption.CONTAINER_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("플라스틱병", "PLASTIC_BOTTLE"),
                        CategoryOptionFilter.of("유리병", "GLASS_BOTTLE"),
                        CategoryOptionFilter.of("캔", "CAN"),
                        CategoryOptionFilter.of("파우치", "POUCH"),
                        CategoryOptionFilter.of("종이팩", "CARDBOARD_PACK"),
                        CategoryOptionFilter.of("컵", "CUP"),
                        CategoryOptionFilter.of("스틱 및 포", "STICK_POWDER"),
                        CategoryOptionFilter.of("튜브", "TUBE")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.DAIRY_BEVERAGE;
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
