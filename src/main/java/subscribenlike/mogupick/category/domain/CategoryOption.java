package subscribenlike.mogupick.category.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryOption {

    PRICE("가격", CategoryOptionType.RANGE, false),
    RATING("별점 갯수", CategoryOptionType.CHOICE, false),
    WEIGHT("중량", CategoryOptionType.CHOICE, true),
    CALORIE("칼로리", CategoryOptionType.CHOICE, true),
    QUANTITY("개당 수량", CategoryOptionType.CHOICE, true),
    EAT_METHOD("간편식 섭취 방법", CategoryOptionType.CHOICE, true),
    COOK_METHOD("에어프라이어 전자렌지 조리 가능", CategoryOptionType.CHOICE, true),
    ;

    private final String name;
    private final CategoryOptionType type;
    private final boolean isMultiple;
}
