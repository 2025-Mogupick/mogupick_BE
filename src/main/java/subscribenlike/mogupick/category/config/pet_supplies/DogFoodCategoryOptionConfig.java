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

public class DogFoodCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public DogFoodCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 강아지 사료 및 간식 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.TOTAL_WEIGHT,
                CategoryOption.FEEDING_TARGET,
                CategoryOption.FOOD_TYPE,
                CategoryOption.TARGET_SIZE,
                CategoryOption.FUNCTION,
                CategoryOption.GRAIN_SIZE,
                CategoryOption.GRAIN_FREE,
                CategoryOption.DOG_BREED
        );

        // 강아지 사료 및 간식 서브카테고리 필터 초기화
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
                CategoryOption.TOTAL_WEIGHT, Arrays.asList(
                        CategoryOptionFilter.of("1kg 이하", "(0,1000]"),
                        CategoryOptionFilter.of("1~5kg", "(1000,5000]"),
                        CategoryOptionFilter.of("5~10kg", "(5000,10000]"),
                        CategoryOptionFilter.of("10kg 이상", "(10000,)")
                ),
                CategoryOption.FEEDING_TARGET, Arrays.asList(
                        CategoryOptionFilter.of("전연령", "ALL_AGES"),
                        CategoryOptionFilter.of("퍼피 및 키튼", "PUPPY_KITTEN"),
                        CategoryOptionFilter.of("어덜트", "ADULT"),
                        CategoryOptionFilter.of("시니어", "SENIOR")
                ),
                CategoryOption.FOOD_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("소", "BEEF"),
                        CategoryOptionFilter.of("돼지", "PORK"),
                        CategoryOptionFilter.of("닭", "CHICKEN"),
                        CategoryOptionFilter.of("오리", "DUCK"),
                        CategoryOptionFilter.of("양", "LAMB"),
                        CategoryOptionFilter.of("칠며조", "TURKEY"),
                        CategoryOptionFilter.of("사슴", "VENISON"),
                        CategoryOptionFilter.of("기타육류", "OTHER_MEAT"),
                        CategoryOptionFilter.of("연어", "SALMON"),
                        CategoryOptionFilter.of("생선", "FISH"),
                        CategoryOptionFilter.of("곤충", "INSECT"),
                        CategoryOptionFilter.of("고구마", "SWEET_POTATO"),
                        CategoryOptionFilter.of("과일 및 야채", "FRUIT_VEGETABLE"),
                        CategoryOptionFilter.of("우유", "MILK"),
                        CategoryOptionFilter.of("캥거루", "KANGAROO"),
                        CategoryOptionFilter.of("말", "HORSE"),
                        CategoryOptionFilter.of("거위", "GOOSE"),
                        CategoryOptionFilter.of("토끼", "RABBIT")
                ),
                CategoryOption.TARGET_SIZE, Arrays.asList(
                        CategoryOptionFilter.of("전체", "ALL"),
                        CategoryOptionFilter.of("소형견", "SMALL"),
                        CategoryOptionFilter.of("중형견", "MEDIUM"),
                        CategoryOptionFilter.of("대형견", "LARGE")
                ),
                CategoryOption.FUNCTION, Arrays.asList(
                        CategoryOptionFilter.of("눈물개선 및 눈건강", "EYE_HEALTH"),
                        CategoryOptionFilter.of("다이어트", "DIET"),
                        CategoryOptionFilter.of("면역력", "IMMUNITY"),
                        CategoryOptionFilter.of("뼈 및 관절 강화", "BONE_JOINT"),
                        CategoryOptionFilter.of("소화기능", "DIGESTION"),
                        CategoryOptionFilter.of("알러지 예방", "ALLERGY_PREVENTION"),
                        CategoryOptionFilter.of("치석제거", "TARTAR_REMOVAL"),
                        CategoryOptionFilter.of("피부 및 털 개선", "SKIN_COAT"),
                        CategoryOptionFilter.of("심장 및 간", "HEART_LIVER")
                ),
                CategoryOption.GRAIN_SIZE, Arrays.asList(
                        CategoryOptionFilter.of("작은알갱이(8mm미만)", "SMALL_UNDER_8MM"),
                        CategoryOptionFilter.of("보통알갱이(8~13mm)", "MEDIUM_8_13MM"),
                        CategoryOptionFilter.of("큰알갱이(13mm 이상)", "LARGE_OVER_13MM")
                ),
                CategoryOption.GRAIN_FREE, Arrays.asList(
                        CategoryOptionFilter.of("그레인프리", "GRAIN_FREE")
                ),
                CategoryOption.DOG_BREED, Arrays.asList(
                        CategoryOptionFilter.of("푸들", "POODLE"),
                        CategoryOptionFilter.of("말티즈", "MALTESE"),
                        CategoryOptionFilter.of("포메라니안", "POMERANIAN"),
                        CategoryOptionFilter.of("진돗개", "JINDO_DOG"),
                        CategoryOptionFilter.of("비숑프리제", "BICHON_FRISE"),
                        CategoryOptionFilter.of("닥스훈트", "DACHSHUND"),
                        CategoryOptionFilter.of("기타", "OTHER")
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
