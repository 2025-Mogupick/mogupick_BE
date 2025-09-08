package subscribenlike.mogupick.category.config.baby_supplies;

import subscribenlike.mogupick.category.config.CategoryOptionConfig;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BabySuppliesCategoryOptionConfig implements CategoryOptionConfig {

    private List<CategoryOption> options;
    private Map<CategoryOption, List<CategoryOptionFilter>> categoryFilters;
    private Map<SubCategory, CategoryOptionConfig> subCategoryConfigs;

    public BabySuppliesCategoryOptionConfig() {
        init();
    }

    private void init() {
        this.subCategoryConfigs = Map.of(
                SubCategory.DIAPER, new DiaperCategoryOptionConfig(),
                SubCategory.WIPES, new WipesCategoryOptionConfig(),
                SubCategory.BABY_FORMULA, new BabyFormulaCategoryOptionConfig(),
                SubCategory.BABY_FOOD, new BabyFoodCategoryOptionConfig(),
                SubCategory.BABY_BATH, new BabyBathCategoryOptionConfig()
        );

        // 루트 카테고리 옵션 초기화 (육아용품은 모든 서브카테고리가 루트 옵션을 공유하므로 비워둠)
        this.options = Arrays.asList();

        // 루트 카테고리 필터 초기화
        this.categoryFilters = Map.of();
    }

    @Override
    public RootCategory getRootCategory() {
        return RootCategory.BABY_SUPPLIES;
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
