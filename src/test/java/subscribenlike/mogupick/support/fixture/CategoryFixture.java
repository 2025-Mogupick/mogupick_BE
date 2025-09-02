package subscribenlike.mogupick.support.fixture;

import lombok.Getter;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;
import subscribenlike.mogupick.category.domain.CategoryOptionType;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;

@Getter
public enum CategoryFixture {
    // RootCategory Fixtures
    신선식품(RootCategory.FRESH_FOOD),
    정육수산물(RootCategory.MEAT_SEAFOOD),
    유제품음료(RootCategory.DAIRY_BEVERAGE),
    간편식(RootCategory.CONVENIENCE_FOOD),
    간식(RootCategory.SNACK),
    건강식품(RootCategory.HEALTH_SUPPLEMENTS),
    생활잡화(RootCategory.DAILY_GOODS),
    위생용품(RootCategory.HYGIENE),
    반려동물(RootCategory.PET_SUPPLIES),
    육아용품(RootCategory.BABY_SUPPLIES);

    private final RootCategory rootCategory;

    CategoryFixture(RootCategory rootCategory) {
        this.rootCategory = rootCategory;
    }

    public static RootCategory 신선식품() {
        return 신선식품.rootCategory;
    }

    public static RootCategory 정육수산물() {
        return 정육수산물.rootCategory;
    }

    public static RootCategory 유제품음료() {
        return 유제품음료.rootCategory;
    }

    public static RootCategory 간편식() {
        return 간편식.rootCategory;
    }

    public static RootCategory 간식() {
        return 간식.rootCategory;
    }

    public static RootCategory 건강식품() {
        return 건강식품.rootCategory;
    }

    public static RootCategory 생활잡화() {
        return 생활잡화.rootCategory;
    }

    public static RootCategory 위생용품() {
        return 위생용품.rootCategory;
    }

    public static RootCategory 반려동물() {
        return 반려동물.rootCategory;
    }

    public static RootCategory 육아용품() {
        return 육아용품.rootCategory;
    }

    // CategoryOption Fixtures
    public static CategoryOption 가격옵션() {
        return CategoryOption.PRICE;
    }

    public static CategoryOption 별점옵션() {
        return CategoryOption.RATING;
    }

    public static CategoryOption 중량옵션() {
        return CategoryOption.WEIGHT;
    }

    public static CategoryOption 칼로리옵션() {
        return CategoryOption.CALORIE;
    }

    public static CategoryOption 수량옵션() {
        return CategoryOption.QUANTITY;
    }

    public static CategoryOption 섭취방법옵션() {
        return CategoryOption.EAT_METHOD;
    }

    public static CategoryOption 조리방법옵션() {
        return CategoryOption.COOK_METHOD;
    }

    // CategoryOptionFilter Fixtures
    public static CategoryOptionFilter 가격_1만원미만() {
        return CategoryOptionFilter.of("10,000원 미만", "(0,10000)");
    }

    public static CategoryOptionFilter 가격_1만원이상2만원미만() {
        return CategoryOptionFilter.of("10,000원 이상-20,000원 미만", "[10000,20000)");
    }

    public static CategoryOptionFilter 별점_5개() {
        return CategoryOptionFilter.of("5개", "5");
    }

    public static CategoryOptionFilter 중량_200g이하() {
        return CategoryOptionFilter.of("200g 이하", "(0,200]");
    }

    public static CategoryOptionFilter 칼로리_100kcal이하() {
        return CategoryOptionFilter.of("100kcal 이하", "(0,100]");
    }

    public static CategoryOptionFilter 수량_6개이하() {
        return CategoryOptionFilter.of("6개 이하", "(0,6]");
    }

    public static CategoryOptionFilter 섭취방법_즉석완조리() {
        return CategoryOptionFilter.of("즉석완조리식품", "즉석완조리식품");
    }

    public static CategoryOptionFilter 조리방법_에어프라이어() {
        return CategoryOptionFilter.of("에어프라이어", "에어프라이어");
    }
}
