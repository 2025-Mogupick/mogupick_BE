package subscribenlike.mogupick.category.config.pet_supplies;

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

public class FishGoodsCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public FishGoodsCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 관상어 용품 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.POWER_CONSUMPTION,
                CategoryOption.FEEDING_TARGET,
                CategoryOption.FILTER_TYPE,
                CategoryOption.AQUARIUM_PLANT_TYPE,
                CategoryOption.ARTIFICIAL_NATURAL
        );

        // 관상어 용품 서브카테고리 필터 초기화
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
                CategoryOption.POWER_CONSUMPTION, Arrays.asList(
                        CategoryOptionFilter.of("3W 이하", "(0,3]"),
                        CategoryOptionFilter.of("3~5W", "(3,5]"),
                        CategoryOptionFilter.of("5~10W", "(5,10]"),
                        CategoryOptionFilter.of("10W 이상", "(10,)")
                ),
                CategoryOption.FEEDING_TARGET, Arrays.asList(
                        CategoryOptionFilter.of("치어", "FRY"),
                        CategoryOptionFilter.of("성어", "ADULT_FISH")
                ),
                CategoryOption.FILTER_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("걸이식여과기", "HANG_ON_FILTER"),
                        CategoryOptionFilter.of("측면여과기", "SIDE_FILTER"),
                        CategoryOptionFilter.of("상면여과기", "TOP_FILTER"),
                        CategoryOptionFilter.of("저면여과기", "BOTTOM_FILTER"),
                        CategoryOptionFilter.of("외부여과기", "EXTERNAL_FILTER"),
                        CategoryOptionFilter.of("단지여과기", "CANISTER_FILTER"),
                        CategoryOptionFilter.of("스펀지여과기", "SPONGE_FILTER")
                ),
                CategoryOption.AQUARIUM_PLANT_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("전경", "FOREGROUND"),
                        CategoryOptionFilter.of("중경", "MIDGROUND"),
                        CategoryOptionFilter.of("후경", "BACKGROUND"),
                        CategoryOptionFilter.of("마리모", "MARIMO"),
                        CategoryOptionFilter.of("활착", "STEM"),
                        CategoryOptionFilter.of("부상", "FLOATING"),
                        CategoryOptionFilter.of("음성", "SUBMERSED"),
                        CategoryOptionFilter.of("씨앗", "SEED"),
                        CategoryOptionFilter.of("포트", "POTTED")
                ),
                CategoryOption.ARTIFICIAL_NATURAL, Arrays.asList(
                        CategoryOptionFilter.of("인조", "ARTIFICIAL"),
                        CategoryOptionFilter.of("자연", "NATURAL")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.PET_SUPPLIES;
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
