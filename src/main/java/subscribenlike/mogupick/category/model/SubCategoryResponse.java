package subscribenlike.mogupick.category.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SubCategoryResponse {
    private SubCategory key;
    private String name;

    public static SubCategoryResponse from(SubCategory subCategory) {
        return new SubCategoryResponse(subCategory, subCategory.getName());
    }
}
