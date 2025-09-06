package subscribenlike.mogupick.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.category.domain.SubCategory;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(staticName = "of")
public class CategoryOptionsResponse {
    private List<CategoryOptionAndFilterResponse> common;
    private Map<SubCategory, List<CategoryOptionAndFilterResponse>> subCategories;
}
