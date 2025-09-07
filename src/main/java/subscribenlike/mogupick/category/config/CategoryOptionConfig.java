package subscribenlike.mogupick.category.config;

import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

import java.util.List;
import java.util.Map;

public interface CategoryOptionConfig {
    RootCategory getRootCategory();
    List<CategoryOption> getCategoryOptions();

    Map<CategoryOption, List<CategoryOptionFilter>> getCategoryFilters();

    List<CategoryOption> getCategoryOptions(SubCategory subCategory);

    Map<CategoryOption, List<CategoryOptionFilter>> getCategoryFilters(SubCategory subCategory);

    boolean hasSubCategory(SubCategory subCategory);

}
