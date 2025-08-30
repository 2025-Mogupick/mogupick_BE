package subscribenlike.mogupick.category.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubCategory {
    FRUIT("과일", RootCategory.FRESH_FOOD),
    VEGETABLE("채소", RootCategory.FRESH_FOOD),
    RICE_GRAINS("쌀·잡곡", RootCategory.FRESH_FOOD),

    MEAT("정육", RootCategory.MEAT_SEAFOOD),
    EGG("계란·난류", RootCategory.MEAT_SEAFOOD),
    SEAFOOD("수산물", RootCategory.MEAT_SEAFOOD),
    PROCESSED_MEAT_SEAFOOD("가공육·해산물", RootCategory.MEAT_SEAFOOD),

    DAIRY("유제품", RootCategory.DAIRY_BEVERAGE),
    BEVERAGE("음료", RootCategory.DAIRY_BEVERAGE),

    FROZEN("냉동식품", RootCategory.CONVENIENCE_FOOD),
    MEAL_KIT("밀키트", RootCategory.CONVENIENCE_FOOD),
    LUNCH_SALAD("도시락·샐러드", RootCategory.CONVENIENCE_FOOD),

    SNACKS("과자·스낵", RootCategory.SNACK),
    BREAD_DESSERT("빵·디저트", RootCategory.SNACK),
    ICE_CREAM("아이스크림·빙과", RootCategory.SNACK),
    CHOCOLATE_CANDY("초콜릿·캔디·껌", RootCategory.SNACK),

    VITAMINS("비타민·미네랄", RootCategory.HEALTH_SUPPLEMENTS),
    OMEGA_PROBIOTICS("오메가·유산균·홍삼", RootCategory.HEALTH_SUPPLEMENTS),
    PROTEIN_SUPPLEMENTS("단백질·헬스 보조제", RootCategory.HEALTH_SUPPLEMENTS),
    HEALTH_TEA_POWDER("건강차·분말류", RootCategory.HEALTH_SUPPLEMENTS),

    KITCHEN("주방용품", RootCategory.DAILY_GOODS),
    CLEANING("청소용품", RootCategory.DAILY_GOODS),
    LAUNDRY("세탁용품", RootCategory.DAILY_GOODS),
    STATIONERY_APPLIANCES("문구·소형가전", RootCategory.DAILY_GOODS),

    PERSONAL_HYGIENE("개인 위생", RootCategory.HYGIENE),
    HYGIENE_CONSUMABLES("위생 소모품", RootCategory.HYGIENE),
    ORAL_SKINCARE("구강·스킨케어 기본품", RootCategory.HYGIENE),

    DOG_FOOD("강아지 식품", RootCategory.PET_SUPPLIES),
    CAT_FOOD("고양이 식품", RootCategory.PET_SUPPLIES),
    PET_HYGIENE("위생·관리", RootCategory.PET_SUPPLIES),
    PET_TOY_HOUSE("장난감·하우스", RootCategory.PET_SUPPLIES),

    DIAPER_WIPES("기저귀·물티슈", RootCategory.BABY_SUPPLIES),
    FORMULA_BABYFOOD("분유·이유식", RootCategory.BABY_SUPPLIES),
    WEANING_SUPPLIES("이유용품", RootCategory.BABY_SUPPLIES),
    BABY_BATH("유아 위생·목욕용품", RootCategory.BABY_SUPPLIES),
    BABY_SNACK_BEVERAGE("유아 간식·음료", RootCategory.BABY_SUPPLIES);

    private final String name;
    private final RootCategory root;

}

