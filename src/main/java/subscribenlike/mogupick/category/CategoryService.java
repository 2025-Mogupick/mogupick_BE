package subscribenlike.mogupick.category;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subscribenlike.mogupick.category.common.exception.CategoryErrorCode;
import subscribenlike.mogupick.category.common.exception.CategoryException;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.model.CategoryOptionAndFilterResponse;
import subscribenlike.mogupick.category.model.CategoryOptionResponse;
import subscribenlike.mogupick.category.model.RootCategoryResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryOptionMappingGraph categoryOptionMappingGraph;

    public List<RootCategoryResponse> getRootCategories() {
        // 카테고리 리스트 조회
        return Arrays.stream(RootCategory.values()).map(RootCategoryResponse::from).toList();
    }

    public List<CategoryOptionAndFilterResponse> getFiltersByRootCategoryAndOption(RootCategory rootCategory) {
        // 카테고리 옵션 및 옵션에 대한 필터 리스트 조회
        return categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(rootCategory);
    }


    public List<CategoryOptionResponse> getCategoryOptionDtoByRootCategory(RootCategory rootCategory) {
        return categoryOptionMappingGraph.getOptionsByRootCategory(rootCategory)
                .stream().map(CategoryOptionResponse::from).toList();
    }

    public List<CategoryOption> getCategoryOptionsByRootCategory(RootCategory rootCategory) {
        return categoryOptionMappingGraph.getOptionsByRootCategory(rootCategory);
    }


    public void validateCategoryOptionFromMap(RootCategory rootCategory, Map<String, String> options) {
        List<CategoryOption> categoryOptions = getCategoryOptionsByRootCategory(rootCategory);
        List<String> categoryOptionKeys = categoryOptions.stream().map(CategoryOption::name).toList();

        // CategoryOptions에 포함되지 않는 옵션이 있다면
        List<String> unavailableOptions = options.keySet().stream()
                .filter(option -> !categoryOptionKeys.contains(option))
                .toList();

        if(!unavailableOptions.isEmpty()) {
            throw new CategoryException(CategoryErrorCode.INVALID_INPUT_VALUE,unavailableOptions);
        }
    }
}
