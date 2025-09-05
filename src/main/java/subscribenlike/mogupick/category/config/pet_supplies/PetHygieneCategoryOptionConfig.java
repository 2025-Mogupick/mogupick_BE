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

public class PetHygieneCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public PetHygieneCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 강아지 용품 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.PRODUCT_TYPE,
                CategoryOption.TOOTHPASTE_TYPE,
                CategoryOption.FEEDING_TARGET,
                CategoryOption.FLAVOR_SCENT,
                CategoryOption.PET_SIZE,
                CategoryOption.TOTAL_QUANTITY
        );

        // 강아지 용품 서브카테고리 필터 초기화
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
                CategoryOption.PRODUCT_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("하우스 및 울타리", "HOUSE_FENCE"),
                        CategoryOptionFilter.of("급식기 및 급수기", "FEEDER_WATERER"),
                        CategoryOptionFilter.of("의류 및 패션", "CLOTHING_FASHION"),
                        CategoryOptionFilter.of("배변용품", "LITTER_PRODUCTS"),
                        CategoryOptionFilter.of("미용 및 목욕", "GROOMING_BATH"),
                        CategoryOptionFilter.of("장난감 및 훈련용품", "TOYS_TRAINING"),
                        CategoryOptionFilter.of("이동장 및 외출용품", "CARRIER_OUTDOOR"),
                        CategoryOptionFilter.of("건강 관리", "HEALTH_CARE"),
                        CategoryOptionFilter.of("펫캠", "PET_CAMERA")
                ),
                CategoryOption.TOOTHPASTE_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("일반치약", "REGULAR_TOOTHPASTE"),
                        CategoryOptionFilter.of("바르는치약", "APPLYING_TOOTHPASTE"),
                        CategoryOptionFilter.of("뿌리는치약", "SPRAY_TOOTHPASTE"),
                        CategoryOptionFilter.of("마시는치약", "DRINKING_TOOTHPASTE")
                ),
                CategoryOption.FEEDING_TARGET, Arrays.asList(
                        CategoryOptionFilter.of("전연령", "ALL_AGES"),
                        CategoryOptionFilter.of("퍼피 및 키튼", "PUPPY_KITTEN"),
                        CategoryOptionFilter.of("어덜트", "ADULT"),
                        CategoryOptionFilter.of("시니어", "SENIOR")
                ),
                CategoryOption.FLAVOR_SCENT, Arrays.asList(
                        CategoryOptionFilter.of("고기", "MEAT"),
                        CategoryOptionFilter.of("과일", "FRUIT"),
                        CategoryOptionFilter.of("일반", "REGULAR"),
                        CategoryOptionFilter.of("기타", "OTHER")
                ),
                CategoryOption.PET_SIZE, Arrays.asList(
                        CategoryOptionFilter.of("신생아", "NEWBORN"),
                        CategoryOptionFilter.of("초소형", "EXTRA_SMALL"),
                        CategoryOptionFilter.of("소형", "SMALL"),
                        CategoryOptionFilter.of("중형", "MEDIUM"),
                        CategoryOptionFilter.of("대형", "LARGE"),
                        CategoryOptionFilter.of("특대형", "EXTRA_LARGE")
                ),
                CategoryOption.TOTAL_QUANTITY, Arrays.asList(
                        CategoryOptionFilter.of("50매 이하", "(0,50]"),
                        CategoryOptionFilter.of("50~100매", "(50,100]"),
                        CategoryOptionFilter.of("100~200매", "(100,200]"),
                        CategoryOptionFilter.of("200매 이상", "(200,)")
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
