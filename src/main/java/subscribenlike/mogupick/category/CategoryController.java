package subscribenlike.mogupick.category;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.category.common.sucess.CategorySuccessCode;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.model.CategoryOptionAndFilterResponse;
import subscribenlike.mogupick.category.model.CategoryOptionResponse;
import subscribenlike.mogupick.category.model.RootCategoryResponse;
import subscribenlike.mogupick.common.success.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/root")
    public ResponseEntity<SuccessResponse<List<RootCategoryResponse>>> getRootCategories(){
        return ResponseEntity
                .status(CategorySuccessCode.ROOT_CATEGORIES_FETCHED.getStatus())
                .body(SuccessResponse.from(CategorySuccessCode.ROOT_CATEGORIES_FETCHED,categoryService.getRootCategories()));
    }

    @GetMapping("/options")
    public ResponseEntity<SuccessResponse<List<CategoryOptionResponse>>> getOptions(@RequestParam RootCategory rootCategory){
        return ResponseEntity
                .status(CategorySuccessCode.CATEGORY_OPTIONS_FETCHED.getStatus())
                .body(SuccessResponse.from(CategorySuccessCode.CATEGORY_OPTIONS_FETCHED,categoryService.getCategoryOptionDtoByRootCategory(rootCategory)));
    }

    @GetMapping("/options-filters")
    public ResponseEntity<SuccessResponse<List<CategoryOptionAndFilterResponse>>> getOptionsAndFilters(@RequestParam RootCategory rootCategory){
        return ResponseEntity
                .status(CategorySuccessCode.CATEGORY_OPTIONS_AND_FILTERS_FETCHED.getStatus())
                .body(SuccessResponse.from(CategorySuccessCode.CATEGORY_OPTIONS_AND_FILTERS_FETCHED,categoryService.getFiltersByRootCategoryAndOption(rootCategory)));
    }
}
