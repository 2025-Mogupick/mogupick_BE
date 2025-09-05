package subscribenlike.mogupick.category.config.health_supplements;

import subscribenlike.mogupick.category.common.exception.CategoryErrorCode;
import subscribenlike.mogupick.category.common.exception.CategoryException;
import subscribenlike.mogupick.category.config.CategoryOptionConfig;
import subscribenlike.mogupick.category.config.health_supplements.VitaminsCategoryOptionConfig;
import subscribenlike.mogupick.category.config.health_supplements.OmegaProbioticsCategoryOptionConfig;
import subscribenlike.mogupick.category.config.health_supplements.ProteinSupplementsCategoryOptionConfig;
import subscribenlike.mogupick.category.config.health_supplements.HealthTeaPowderCategoryOptionConfig;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HealthSupplementsCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> categoryFilters;
    private Map<SubCategory, CategoryOptionConfig> subCategoryConfigs;

    public HealthSupplementsCategoryOptionConfig() {
        init();
    }

    private void init() {
        this.subCategoryConfigs = Map.of(
                SubCategory.VITAMINS, new VitaminsCategoryOptionConfig(),
                SubCategory.OMEGA_PROBIOTICS, new OmegaProbioticsCategoryOptionConfig(),
                SubCategory.PROTEIN_SUPPLEMENTS, new ProteinSupplementsCategoryOptionConfig(),
                SubCategory.HEALTH_TEA_POWDER, new HealthTeaPowderCategoryOptionConfig()
        );

        // 루트 카테고리 옵션 초기화
        this.options = Arrays.asList(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT
        );

        // 루트 카테고리 필터 초기화
        this.categoryFilters = Map.of(
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
