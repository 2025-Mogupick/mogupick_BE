package subscribenlike.mogupick.category.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum SubCategory {
    FRUIT("과일", RootCategory.FRESH_FOOD),
    VEGETABLE("채소", RootCategory.FRESH_FOOD),
    RICE_GRAINS("쌀/잡곡", RootCategory.FRESH_FOOD),

    MEAT("정육", RootCategory.MEAT_SEAFOOD),
    EGG("계란/알류/가공란", RootCategory.MEAT_SEAFOOD),
    SEAFOOD("수산물", RootCategory.MEAT_SEAFOOD),

    WATER("생수", RootCategory.DAIRY_BEVERAGE),
    DAIRY_PRODUCTS("유제품", RootCategory.DAIRY_BEVERAGE),
    BEVERAGE("음료", RootCategory.DAIRY_BEVERAGE),

    FROZEN("냉동식품", RootCategory.CONVENIENCE_FOOD),
    MEAL_KIT("밀키트", RootCategory.CONVENIENCE_FOOD),
    LUNCH_SALAD("도시락·샐러드", RootCategory.CONVENIENCE_FOOD),

    SNACKS("과자/스낵", RootCategory.SNACK),
    CHOCOLATE_CANDY("초콜릿·사탕·껌", RootCategory.SNACK),

    VITAMINS("건강기능식품", RootCategory.HEALTH_SUPPLEMENTS),
    OMEGA_PROBIOTICS("홍삼", RootCategory.HEALTH_SUPPLEMENTS),
    PROTEIN_SUPPLEMENTS("헬스 보조제", RootCategory.HEALTH_SUPPLEMENTS),
    HEALTH_TEA_POWDER("건강즙/음료", RootCategory.HEALTH_SUPPLEMENTS),

    KITCHEN("청소/주방용품", RootCategory.DAILY_GOODS),
    CLEANING("세용품", RootCategory.DAILY_GOODS),

    PERSONAL_HYGIENE("개인 위생", RootCategory.HYGIENE),
    HYGIENE_CONSUMABLES("생리대/성인용 기저귀", RootCategory.HYGIENE),

    DOG_FOOD("강아지 식품", RootCategory.PET_SUPPLIES),
    CAT_FOOD("고양이 식품", RootCategory.PET_SUPPLIES),
    PET_HYGIENE("위생·관리", RootCategory.PET_SUPPLIES),
    PET_TOY_HOUSE("장난감·하우스", RootCategory.PET_SUPPLIES),

    DIAPER("기저귀", RootCategory.BABY_SUPPLIES),
    WIPES("물티슈", RootCategory.BABY_SUPPLIES),
    BABY_FORMULA("분유", RootCategory.BABY_SUPPLIES),
    BABY_FOOD("이유식", RootCategory.BABY_SUPPLIES),
    BABY_BATH("위생/건강/세제", RootCategory.BABY_SUPPLIES),

    ;

    private final String name;
    private final RootCategory root;


    public static List<SubCategory> findByRootCategory(RootCategory rootCategory) {
        return List.of(values()).stream()
                .filter(subCategory -> subCategory.getRoot().equals(rootCategory))
                .toList();
    }
}

