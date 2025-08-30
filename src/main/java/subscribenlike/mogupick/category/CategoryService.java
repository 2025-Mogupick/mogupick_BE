package subscribenlike.mogupick.category;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.model.CategoryOptionAndFilterResponse;
import subscribenlike.mogupick.category.model.RootCategoryResponse;

import java.util.Arrays;
import java.util.List;

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
}
