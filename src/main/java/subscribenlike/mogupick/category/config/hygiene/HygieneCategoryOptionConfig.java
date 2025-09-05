package subscribenlike.mogupick.category.config.hygiene;

import subscribenlike.mogupick.category.common.exception.CategoryErrorCode;
import subscribenlike.mogupick.category.common.exception.CategoryException;
import subscribenlike.mogupick.category.config.CategoryOptionConfig;
import subscribenlike.mogupick.category.config.hygiene.PersonalHygieneCategoryOptionConfig;
import subscribenlike.mogupick.category.config.hygiene.HygieneConsumablesCategoryOptionConfig;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HygieneCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> categoryFilters;
    private Map<SubCategory, CategoryOptionConfig> subCategoryConfigs;

    public HygieneCategoryOptionConfig() {
        init();
    }

    private void init() {
        this.subCategoryConfigs = Map.of(
                SubCategory.PERSONAL_HYGIENE, new PersonalHygieneCategoryOptionConfig(),
                SubCategory.HYGIENE_CONSUMABLES, new HygieneConsumablesCategoryOptionConfig()
        );

        // 루트 카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.TOTAL_QUANTITY
        );

        // 루트 카테고리 필터 초기화
        this.categoryFilters = Map.of(
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
                )
        );
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.HYGIENE;
    }

    @Override
    public List<CategoryOption> getCategoryOptions() {
        return options;
    }

    @Override
    public Map<CategoryOption, List<CategoryOptionFilter>> getCategoryFilters() {
        return categoryFilters;
    }

    @Override
    public List<CategoryOption> getCategoryOptions(SubCategory subCategory) {
        return subCategoryConfigs.getOrDefault(subCategory, this).getCategoryOptions();
    }

    @Override
    public Map<CategoryOption, List<CategoryOptionFilter>> getCategoryFilters(SubCategory subCategory) {
        return subCategoryConfigs.getOrDefault(subCategory, this).getCategoryFilters();
    }

    @Override
    public boolean hasSubCategory(SubCategory subCategory) {
        return subCategoryConfigs.containsKey(subCategory);
    }
}
