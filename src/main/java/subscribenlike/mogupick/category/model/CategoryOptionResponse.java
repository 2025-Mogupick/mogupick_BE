package subscribenlike.mogupick.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.category.domain.CategoryOption;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryOptionResponse {
    private CategoryOption key;
    private String name;
    private String type;

    @JsonProperty("multiple")
    private boolean isMultiple;

    public static CategoryOptionResponse from(CategoryOption option) {
        return new CategoryOptionResponse(option, option.getName(), option.getType().name(), option.isMultiple());
    }
}
