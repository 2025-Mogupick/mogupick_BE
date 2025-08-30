package subscribenlike.mogupick.category.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.category.domain.RootCategory;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RootCategoryResponse {
    private RootCategory rootCategory;
    private String name;

    public static RootCategoryResponse from(RootCategory rootCategory) {
        return new RootCategoryResponse(rootCategory, rootCategory.getName());
    }
}
