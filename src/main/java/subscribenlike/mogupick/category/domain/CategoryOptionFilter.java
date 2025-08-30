package subscribenlike.mogupick.category.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CategoryOptionFilter {
    private String name;
    private String expression;
}
