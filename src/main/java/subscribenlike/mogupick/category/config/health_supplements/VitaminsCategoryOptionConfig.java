package subscribenlike.mogupick.category.config.health_supplements;

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

public class VitaminsCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> filters;

    public VitaminsCategoryOptionConfig() {
        init();
    }

    private void init() {
        // 건강기능식품 서브카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT,
                CategoryOption.SUPPLEMENT_TYPE,
                CategoryOption.TARGET_AUDIENCE,
                CategoryOption.CERTIFICATION,
                CategoryOption.FORM,
                CategoryOption.CONTAINER_TYPE
        );

        // 건강기능식품 서브카테고리 필터 초기화
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
                CategoryOption.SUPPLEMENT_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("멀티비타민", "MULTI_VITAMIN"),
                        CategoryOptionFilter.of("비타민A", "VITAMIN_A"),
                        CategoryOptionFilter.of("비타민B", "VITAMIN_B"),
                        CategoryOptionFilter.of("비타민C", "VITAMIN_C"),
                        CategoryOptionFilter.of("비타민D", "VITAMIN_D"),
                        CategoryOptionFilter.of("비타민E", "VITAMIN_E"),
                        CategoryOptionFilter.of("비타민K", "VITAMIN_K"),
                        CategoryOptionFilter.of("비오틴", "BIOTIN"),
                        CategoryOptionFilter.of("칼슘", "CALCIUM"),
                        CategoryOptionFilter.of("마그네슘", "MAGNESIUM"),
                        CategoryOptionFilter.of("아연", "ZINC"),
                        CategoryOptionFilter.of("철분", "IRON"),
                        CategoryOptionFilter.of("미네랄", "MINERAL"),
                        CategoryOptionFilter.of("유산균", "PROBIOTICS"),
                        CategoryOptionFilter.of("오메가3", "OMEGA3"),
                        CategoryOptionFilter.of("이너뷰티", "INNER_BEAUTY")
                ),
                CategoryOption.TARGET_AUDIENCE, Arrays.asList(
                        CategoryOptionFilter.of("범용(패밀리)", "GENERAL_FAMILY"),
                        CategoryOptionFilter.of("성인", "ADULT"),
                        CategoryOptionFilter.of("여성", "WOMAN"),
                        CategoryOptionFilter.of("남성", "MAN"),
                        CategoryOptionFilter.of("어린이", "CHILD"),
                        CategoryOptionFilter.of("임산부", "PREGNANT"),
                        CategoryOptionFilter.of("시니어", "SENIOR")
                ),
                CategoryOption.CERTIFICATION, Arrays.asList(
                        CategoryOptionFilter.of("식약처인증", "KFDA_CERTIFIED")
                ),
                CategoryOption.FORM, Arrays.asList(
                        CategoryOptionFilter.of("액상", "LIQUID"),
                        CategoryOptionFilter.of("스틱", "STICK"),
                        CategoryOptionFilter.of("츄어블", "CHEWABLE"),
                        CategoryOptionFilter.of("캔디", "CANDY"),
                        CategoryOptionFilter.of("분말", "POWDER"),
                        CategoryOptionFilter.of("캡슐", "CAPSULE")
                ),
                CategoryOption.CONTAINER_TYPE, Arrays.asList(
                        CategoryOptionFilter.of("플라스틱병", "PLASTIC_BOTTLE"),
                        CategoryOptionFilter.of("파우치", "POUCH"),
                        CategoryOptionFilter.of("종이백", "PAPER_BAG"),
                        CategoryOptionFilter.of("스틱 및 포", "STICK_POWDER")
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.HEALTH_SUPPLEMENTS;
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
