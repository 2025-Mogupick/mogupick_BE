package subscribenlike.mogupick.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subscribenlike.mogupick.category.domain.CategoryOption;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryOptionDto {
    private String name;
    private String type;
    
    @JsonProperty("multiple")
    private boolean isMultiple;

    public static CategoryOptionDto from(CategoryOption option) {
        return new CategoryOptionDto(option.getName(), option.getType().name(), option.isMultiple());
    }
}
