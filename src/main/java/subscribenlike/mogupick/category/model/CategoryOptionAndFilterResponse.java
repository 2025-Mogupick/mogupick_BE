package subscribenlike.mogupick.category.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class CategoryOptionAndFilterResponse {
    private CategoryOptionResponse option;
    private List<CategoryOptionFilter> optionFilters;
}

