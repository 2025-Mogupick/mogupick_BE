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

public class DairyProductsCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public DairyProductsCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 유제품 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.VOLUME,
                CategoryOption.CONTAINER_TYPE,
                CategoryOption.STERILIZATION_TYPE,
                CategoryOption.FLAVOR
        );

        // 유제품 서브카테고리 필터 초기화
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
                CategoryOption.VOLUME, Arrays.asList(
                        CategoryOptionFilter.of("125ml 이하", "(0,125]"),
                        CategoryOptionFilter.of("125-200ml", "(125,200]"),
                        CategoryOptionFilter.of("200-500ml", "(200,500]"),
                        CategoryOptionFilter.of("500ml-1L", "(500,1000]"),
                        CategoryOptionFilter.of("1-2L", "(1000,2000]"),
                        CategoryOptionFilter.of("2L 이상", "(2000,)")
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
                ),
                CategoryOption.STERILIZATION_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("냉장우유", "REFRIGERATED_MILK"),
                        CategoryOptionFilter.of("멸균우유", "STERILIZED_MILK")
                ),
                CategoryOption.FLAVOR, Arrays.asList(
                        CategoryOptionFilter.of("흰우유", "WHITE_MILK"),
                        CategoryOptionFilter.of("딸기우유", "STRAWBERRY_MILK"),
                        CategoryOptionFilter.of("바나나우유", "BANANA_MILK"),
                        CategoryOptionFilter.of("초코우유", "CHOCOLATE_MILK"),
                        CategoryOptionFilter.of("커피우유", "COFFEE_MILK"),
                        CategoryOptionFilter.of("두유", "SOY_MILK"),
                        CategoryOptionFilter.of("요거트", "YOGURT"),
                        CategoryOptionFilter.of("생크림", "CREAM"),
                        CategoryOptionFilter.of("버터", "BUTTER"),
                        CategoryOptionFilter.of("치즈", "CHEESE"),
                        CategoryOptionFilter.of("기타", "OTHER")
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
