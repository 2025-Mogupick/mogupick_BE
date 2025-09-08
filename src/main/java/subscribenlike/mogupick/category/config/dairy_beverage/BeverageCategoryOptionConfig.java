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

public class BeverageCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public BeverageCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 음료 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.TOTAL_QUANTITY,
                CategoryOption.CONTAINER_TYPE,
                CategoryOption.SUGAR_CONTENT,
                CategoryOption.CAFFEINE_CONTENT,
                CategoryOption.FLAVOR_SCENT,
                CategoryOption.TRADITIONAL_TEA_TYPE
        );

        // 음료 서브카테고리 필터 초기화
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
                ),
                CategoryOption.SUGAR_CONTENT, Arrays.asList(
                        CategoryOptionFilter.of("무설탕", "NO_SUGAR"),
                        CategoryOptionFilter.of("저설탕", "LOW_SUGAR"),
                        CategoryOptionFilter.of("설탕첨가", "SUGAR_ADDED"),
                        CategoryOptionFilter.of("당첨가", "SWEETENER_ADDED")
                ),
                CategoryOption.CAFFEINE_CONTENT, Arrays.asList(
                        CategoryOptionFilter.of("카페인", "CAFFEINE"),
                        CategoryOptionFilter.of("디카페인", "DECAFFEINATED")
                ),
                CategoryOption.FLAVOR_SCENT, Arrays.asList(
                        CategoryOptionFilter.of("아메리카노 및 블랙", "AMERICANO_BLACK"),
                        CategoryOptionFilter.of("콜드부르 및 더치", "COLD_BREW_DUTCH"),
                        CategoryOptionFilter.of("헤이즐넛", "HAZELNUT"),
                        CategoryOptionFilter.of("카페라떼 및 믹스", "CAFE_LATTE_MIX"),
                        CategoryOptionFilter.of("바닐라", "VANILLA"),
                        CategoryOptionFilter.of("카라멜", "CARAMEL"),
                        CategoryOptionFilter.of("카푸치노", "CAPPUCCINO"),
                        CategoryOptionFilter.of("초콜릿", "CHOCOLATE"),
                        CategoryOptionFilter.of("방탄커피", "BULLET_PROOF_COFFEE")
                ),
                CategoryOption.TRADITIONAL_TEA_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("보리차", "BARLEY_TEA"),
                        CategoryOptionFilter.of("옥수수차", "CORN_TEA"),
                        CategoryOptionFilter.of("녹차", "GREEN_TEA"),
                        CategoryOptionFilter.of("콤부차", "KOMBUCHA"),
                        CategoryOptionFilter.of("팥차", "RED_BEAN_TEA"),
                        CategoryOptionFilter.of("유자차", "CITRON_TEA"),
                        CategoryOptionFilter.of("생강차", "GINGER_TEA"),
                        CategoryOptionFilter.of("작두콩차", "BLACK_SOYBEAN_TEA"),
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
