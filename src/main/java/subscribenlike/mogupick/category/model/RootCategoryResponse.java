package subscribenlike.mogupick.category.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RootCategoryResponse {
    private RootCategory key;
    private String name;
    private List<SubCategoryResponse> subCategories;

    public static RootCategoryResponse from(RootCategory rootCategory) {
        List<SubCategoryResponse> subCategoryResponses = SubCategory.findByRootCategory(rootCategory).stream()
                .map(SubCategoryResponse::from)
                .toList();

        return new RootCategoryResponse(rootCategory, rootCategory.getName(), subCategoryResponses);
    }
}
