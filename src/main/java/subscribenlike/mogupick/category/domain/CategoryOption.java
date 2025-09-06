package subscribenlike.mogupick.category.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subscribenlike.mogupick.category.common.exception.CategoryErrorCode;
import subscribenlike.mogupick.category.common.exception.CategoryException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CategoryOption {

    PRICE("가격", CategoryOptionType.RANGE, false),
    RATING("별점 갯수", CategoryOptionType.CHOICE, false),
    WEIGHT("중량", CategoryOptionType.RANGE, true),
    CALORIE("칼로리", CategoryOptionType.RANGE, true),
    QUANTITY("개당 수량", CategoryOptionType.CHOICE, true),
    EAT_METHOD("간편식 섭취 방법", CategoryOptionType.CHOICE, true),
    COOK_METHOD("에어프라이어 전자렌지 조리 가능", CategoryOptionType.CHOICE, true),
    SINGLE_ITEM("낱개상품", CategoryOptionType.CHOICE, false),
    TASTE("맛", CategoryOptionType.CHOICE, false),
    ORGANIC("친환경/유기농", CategoryOptionType.CHOICE, false),
    PROCESSING_TYPE("가공형태", CategoryOptionType.CHOICE, false),
    PREPARATION("손질여부", CategoryOptionType.CHOICE, false),
    COMPOSITION("구성", CategoryOptionType.CHOICE, false),
    TYPE("종류", CategoryOptionType.CHOICE, false),
    GRADE("등급", CategoryOptionType.CHOICE, false),
    ORIGIN("생산지", CategoryOptionType.CHOICE, true),
    PACKAGING("포장형태", CategoryOptionType.CHOICE, true),
    STORAGE_METHOD("보관방식", CategoryOptionType.CHOICE, false),
    CONVENIENCE_FOOD("간편식", CategoryOptionType.CHOICE, false),
    PROCESSING_METHOD("가공방식", CategoryOptionType.CHOICE, false),
    CUT_TYPE("절단형태", CategoryOptionType.CHOICE, false),
    TOTAL_COUNT("총 개수", CategoryOptionType.CHOICE, false),
    CHARACTERISTICS("특징", CategoryOptionType.CHOICE, true),
    FARMING_ENVIRONMENT("사육환경번호", CategoryOptionType.CHOICE, true),
    TOTAL_QUANTITY("총 수량", CategoryOptionType.RANGE, true),
    CONTAINER_TYPE("용기형태", CategoryOptionType.CHOICE, true),
    VOLUME("용량", CategoryOptionType.CHOICE, true),
    STERILIZATION_TYPE("멸균/냉장", CategoryOptionType.CHOICE, false),
    FLAVOR("맛", CategoryOptionType.CHOICE, false),
    SUGAR_CONTENT("설탕함량", CategoryOptionType.CHOICE, true),
    CAFFEINE_CONTENT("카페인유무", CategoryOptionType.CHOICE, false),
    FLAVOR_SCENT("맛/향", CategoryOptionType.CHOICE, true),
    TRADITIONAL_TEA_TYPE("전통차 종류", CategoryOptionType.CHOICE, true),
    PORTION_SIZE("섭취분량", CategoryOptionType.CHOICE, false),
    CONVENIENCE_FOOD_TYPE("종류", CategoryOptionType.CHOICE, true),
    PACKAGING_TYPE("포장형태", CategoryOptionType.CHOICE, true),
    SNACK_TYPE("과자/스낵 종류", CategoryOptionType.CHOICE, true),
    INDIVIDUAL_PACKAGING("개별포장 여부", CategoryOptionType.CHOICE, false),
    SUPPLEMENT_TYPE("건강기능식품 종류", CategoryOptionType.CHOICE, true),
    TARGET_AUDIENCE("섭취대상", CategoryOptionType.CHOICE, false),
    CERTIFICATION("식약처인증 여부", CategoryOptionType.CHOICE, false),
    FORM("형태", CategoryOptionType.CHOICE, true),
    JUICE_TYPE("건강즙 종류", CategoryOptionType.CHOICE, true),
    JUICE_PACKAGING("건강즙 포장형태", CategoryOptionType.CHOICE, true),
    JUICE_METHOD("착즙 여부", CategoryOptionType.CHOICE, false),
    CLEANING_TYPE("청소 종류", CategoryOptionType.CHOICE, false),
    FORMULATION("제형", CategoryOptionType.CHOICE, true),
    WASHER_TYPE("세탁기타입", CategoryOptionType.CHOICE, false),
    SCENT("향", CategoryOptionType.CHOICE, true),
    HYGIENE_TYPE("위생용품 종류", CategoryOptionType.CHOICE, true),
    SANITARY_PAD_TYPE("생리대 종류", CategoryOptionType.CHOICE, true),
    DIAPER_SIZE("기저귀 사이즈", CategoryOptionType.CHOICE, true),
    SHEET_COUNT("개당 매수", CategoryOptionType.RANGE, false),
    DIAPER_WEIGHT("허용무게", CategoryOptionType.RANGE, false),
    DIAPER_SIZE_STAGE("단계 및 크기", CategoryOptionType.CHOICE, false),
    DIAPER_TYPE("형태", CategoryOptionType.CHOICE, false),
    SWIMMING_DIAPER("수영장용 여부", CategoryOptionType.CHOICE, false),
    GENDER_TARGET("사용대상", CategoryOptionType.CHOICE, true),
    TISSUE_TYPE("종류", CategoryOptionType.CHOICE, false),
    EMBOSSING("엠보싱 유무", CategoryOptionType.CHOICE, false),
    BABY_WEIGHT("개당 중량", CategoryOptionType.RANGE, false),
    BABY_AGE("대상월령", CategoryOptionType.CHOICE, true),
    BABY_STAGE("단계", CategoryOptionType.CHOICE, false),
    BABY_FORM("형태", CategoryOptionType.CHOICE, true),
    BABY_FOOD_TYPE("종류", CategoryOptionType.CHOICE, true),
    SPOON_HOLDER("뚜겅 유무", CategoryOptionType.CHOICE, false),
    TOTAL_WEIGHT("총 중량", CategoryOptionType.RANGE, false),
    FEEDING_TARGET("급여대상", CategoryOptionType.CHOICE, true),
    FOOD_TYPE("종류", CategoryOptionType.CHOICE, true),
    TARGET_SIZE("대상크기", CategoryOptionType.CHOICE, true),
    FUNCTION("기능", CategoryOptionType.CHOICE, true),
    GRAIN_SIZE("알갱이 크기", CategoryOptionType.CHOICE, false),
    GRAIN_FREE("그레인프리 여부", CategoryOptionType.CHOICE, false),
    DOG_BREED("견종", CategoryOptionType.CHOICE, false),
    PRODUCT_TYPE("종류", CategoryOptionType.CHOICE, false),
    TOOTHPASTE_TYPE("타입", CategoryOptionType.CHOICE, true),
    PET_SIZE("반려견 위생용품 사이즈", CategoryOptionType.CHOICE, true),
    SAND_TYPE("모래타입", CategoryOptionType.CHOICE, false),
    SAND_KIND("모래종류", CategoryOptionType.CHOICE, true),
    POWER_CONSUMPTION("소비전력", CategoryOptionType.CHOICE, false),
    AQUARIUM_PLANT_TYPE("수초 종류", CategoryOptionType.CHOICE, true),
    ARTIFICIAL_NATURAL("유형", CategoryOptionType.CHOICE, false),
    HAIRBALL_CONTROL("헤어볼 억제 및 제거 기능여부", CategoryOptionType.CHOICE, false),
    FILTER_TYPE("여과기 종류", CategoryOptionType.CHOICE, false),
    ;

    private final String name;
    private final CategoryOptionType type;
    private final boolean isMultiple;

    public static void validate(String name) {
        String upperName = name.toUpperCase();

        Arrays.stream(CategoryOption.values())
                .filter(option->option.name().equals(upperName))
                .findFirst()
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.INVALID_INPUT_VALUE, upperName));
    }
}
