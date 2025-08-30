package subscribenlike.mogupick.category.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RootCategory {
    FRESH_FOOD("신선식품"),
    MEAT_SEAFOOD("정육·수산물"),
    DAIRY_BEVERAGE("유제품·음료"),
    CONVENIENCE_FOOD("간편식"),
    SNACK("간식"),
    HEALTH_SUPPLEMENTS("건강식품"),
    DAILY_GOODS("생활 잡화"),
    HYGIENE("위생용품"),
    PET_SUPPLIES("반려동물"),
    BABY_SUPPLIES("육아용품");

    private final String name;
}

