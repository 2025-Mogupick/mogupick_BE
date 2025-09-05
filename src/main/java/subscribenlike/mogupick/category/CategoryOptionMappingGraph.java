package subscribenlike.mogupick.category;

import org.springframework.stereotype.Component;
import subscribenlike.mogupick.category.config.*;
import subscribenlike.mogupick.category.config.convenience_food.ConvenienceFoodCategoryOptionConfig;
import subscribenlike.mogupick.category.config.daily_goods.DailyGoodsCategoryOptionConfig;
import subscribenlike.mogupick.category.config.dairy_beverage.DairyBeverageCategoryOptionConfig;
import subscribenlike.mogupick.category.config.health_supplements.HealthSupplementsCategoryOptionConfig;
import subscribenlike.mogupick.category.config.hygiene.HygieneCategoryOptionConfig;
import subscribenlike.mogupick.category.config.meat_seafood.MeatSeafoodCategoryOptionConfig;
import subscribenlike.mogupick.category.config.pet_supplies.PetSuppliesCategoryOptionConfig;
import subscribenlike.mogupick.category.config.snack.SnacksCategoryOptionConfig;
import subscribenlike.mogupick.category.config.fresh_food.FreshFoodCategoryOptionConfig;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;
import subscribenlike.mogupick.category.config.baby_supplies.BabySuppliesCategoryOptionConfig;
import subscribenlike.mogupick.category.model.CategoryOptionAndFilterResponse;
import subscribenlike.mogupick.category.model.CategoryOptionResponse;
import subscribenlike.mogupick.category.model.CategoryOptionsResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CategoryOptionMappingGraph {

    private Map<RootCategory, CategoryOptionConfig> configs;

    private CategoryOptionMappingGraph() {
        init();
    }

    public void init() {
        this.configs = Map.of(
                RootCategory.FRESH_FOOD, new FreshFoodCategoryOptionConfig(),
                RootCategory.CONVENIENCE_FOOD, new ConvenienceFoodCategoryOptionConfig(),
                RootCategory.MEAT_SEAFOOD, new MeatSeafoodCategoryOptionConfig(),
                RootCategory.DAIRY_BEVERAGE, new DairyBeverageCategoryOptionConfig(),
                RootCategory.SNACK, new SnacksCategoryOptionConfig(),
                RootCategory.HEALTH_SUPPLEMENTS, new HealthSupplementsCategoryOptionConfig(),
                RootCategory.DAILY_GOODS, new DailyGoodsCategoryOptionConfig(),
                RootCategory.HYGIENE, new HygieneCategoryOptionConfig(),
                RootCategory.PET_SUPPLIES, new PetSuppliesCategoryOptionConfig(),
                RootCategory.BABY_SUPPLIES, new BabySuppliesCategoryOptionConfig()
        );
    }

    public List<CategoryOption> getOptionsByRootCategory(RootCategory rootCategory) {
        return configs.get(rootCategory).getCategoryOptions();
    }

    public List<CategoryOption> getOptionsBySubCategory(SubCategory subCategory) {
        return configs.get(subCategory.getRoot()).getCategoryOptions(subCategory);
    }

    public List<CategoryOptionAndFilterResponse> getOptionAndFiltersBySubCategory(SubCategory subCategory) {
        return configs.get(subCategory.getRoot()).getCategoryFilters(subCategory)
                .entrySet().stream()
                .map(optionFilters ->
                        CategoryOptionAndFilterResponse.of(
                                CategoryOptionResponse.from(optionFilters.getKey()),
                                optionFilters.getValue())
                ).toList();
    }

    public List<CategoryOptionAndFilterResponse> getOptionAndFiltersByRootCategory(RootCategory rootCategory) {
        return configs.get(rootCategory).getCategoryFilters()
                .entrySet().stream()
                .map((optionFilters) ->
                        CategoryOptionAndFilterResponse.of(
                                CategoryOptionResponse.from(optionFilters.getKey()),
                                optionFilters.getValue())
                ).toList();
    }

    public CategoryOptionsResponse getAllOptionsAndFiltersByRootCategory(RootCategory rootCategory) {
        // RootCategory의 옵션과 필터 (common)
        List<CategoryOptionAndFilterResponse> common = getOptionAndFiltersByRootCategory(rootCategory);

        // 각 SubCategory의 옵션과 필터
        Map<SubCategory, List<CategoryOptionAndFilterResponse>> subCategories =
                SubCategory.findByRootCategory(rootCategory).stream()
                        .collect(Collectors.toMap(
                                subCategory -> subCategory,
                                this::getOptionAndFiltersBySubCategory
                        ));

        return CategoryOptionsResponse.of(common, subCategories);
    }
}

