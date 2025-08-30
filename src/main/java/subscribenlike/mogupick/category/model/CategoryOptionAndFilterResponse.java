package subscribenlike.mogupick.category.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class CategoryOptionAndFilterResponse {
    private CategoryOptionDto option;
    private List<CategoryOptionFilter> optionFilters;
}

