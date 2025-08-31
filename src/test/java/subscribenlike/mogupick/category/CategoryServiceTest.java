package subscribenlike.mogupick.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.model.CategoryOptionAndFilterResponse;
import subscribenlike.mogupick.category.model.RootCategoryResponse;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.CategoryFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void 루트_카테고리_리스트를_조회할_수_있다() {
        // When
        List<RootCategoryResponse> rootCategories = categoryService.getRootCategories();

        // Then
        assertThat(rootCategories).hasSize(10);
        assertThat(rootCategories)
                .extracting("key")
                .containsExactlyInAnyOrder(
                        RootCategory.FRESH_FOOD,
                        RootCategory.MEAT_SEAFOOD,
                        RootCategory.DAIRY_BEVERAGE,
                        RootCategory.CONVENIENCE_FOOD,
                        RootCategory.SNACK,
                        RootCategory.HEALTH_SUPPLEMENTS,
                        RootCategory.DAILY_GOODS,
                        RootCategory.HYGIENE,
                        RootCategory.PET_SUPPLIES,
                        RootCategory.BABY_SUPPLIES
                );
    }

    @Test
    void 루트_카테고리_응답에_올바른_이름이_포함되어_있다() {
        // When
        List<RootCategoryResponse> rootCategories = categoryService.getRootCategories();

        // Then
        assertThat(rootCategories)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "신선식품",
                        "정육·수산물",
                        "유제품·음료",
                        "간편식",
                        "간식",
                        "건강식품",
                        "생활 잡화",
                        "위생용품",
                        "반려동물",
                        "육아용품"
                );
    }

    @Test
    void 간편식_카테고리의_옵션과_필터를_조회할_수_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryService.getFiltersByRootCategoryAndOption(convenienceFood);

        // Then
        assertThat(optionsAndFilters).hasSize(7);
        
        // 가격 옵션 검증
        assertThat(optionsAndFilters)
                .anyMatch(response -> 
                        response.getOption().getName().equals("가격") &&
                        response.getOption().getType().equals("RANGE") &&
                        !response.getOption().isMultiple()
                );

        // 별점 옵션 검증
        assertThat(optionsAndFilters)
                .anyMatch(response -> 
                        response.getOption().getName().equals("별점 갯수") &&
                        response.getOption().getType().equals("CHOICE") &&
                        !response.getOption().isMultiple()
                );

        // 중량 옵션 검증 (복수 선택 가능)
        assertThat(optionsAndFilters)
                .anyMatch(response -> 
                        response.getOption().getName().equals("중량") &&
                        response.getOption().getType().equals("CHOICE") &&
                        response.getOption().isMultiple()
                );
    }

    @Test
    void 간편식_카테고리의_가격_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryService.getFiltersByRootCategoryAndOption(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse priceOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("가격"))
                .findFirst()
                .orElseThrow();

        assertThat(priceOption.getOptionFilters()).hasSize(5);
        assertThat(priceOption.getOptionFilters())
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "10,000원 미만",
                        "10,000원 이상-20,000원 미만",
                        "20,000원 이상-30,000원 미만",
                        "30,000원 이상-40,000원 미만",
                        "40,000원 이상"
                );
    }

    @Test
    void 간편식_카테고리의_별점_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryService.getFiltersByRootCategoryAndOption(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse ratingOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("별점 갯수"))
                .findFirst()
                .orElseThrow();

        assertThat(ratingOption.getOptionFilters()).hasSize(5);
        assertThat(ratingOption.getOptionFilters())
                .extracting("name")
                .containsExactlyInAnyOrder("1개", "2개", "3개", "4개", "5개");
    }

    @Test
    void 간편식_카테고리의_중량_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryService.getFiltersByRootCategoryAndOption(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse weightOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("중량"))
                .findFirst()
                .orElseThrow();

        assertThat(weightOption.getOptionFilters()).hasSize(6);
        assertThat(weightOption.getOptionFilters())
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "200g 이하",
                        "200g-500g",
                        "500g-1kg",
                        "1kg-2kg",
                        "2kg-3kg",
                        "3kg 이상"
                );
    }

    @Test
    void 간편식_카테고리의_칼로리_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryService.getFiltersByRootCategoryAndOption(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse calorieOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("칼로리"))
                .findFirst()
                .orElseThrow();

        assertThat(calorieOption.getOptionFilters()).hasSize(5);
        assertThat(calorieOption.getOptionFilters())
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "100kcal 이하",
                        "100kcal-200kcal",
                        "200kcal-300kcal",
                        "300kcal-500kcal",
                        "500kcal 이상"
                );
    }

    @Test
    void 간편식_카테고리의_개당수량_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryService.getFiltersByRootCategoryAndOption(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse quantityOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("개당 수량"))
                .findFirst()
                .orElseThrow();

        assertThat(quantityOption.getOptionFilters()).hasSize(5);
        assertThat(quantityOption.getOptionFilters())
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "6개 이하",
                        "6개-10개",
                        "10개-20개",
                        "20개-30개",
                        "50개 이상"
                );
    }

    @Test
    void 간편식_카테고리의_섭취방법_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryService.getFiltersByRootCategoryAndOption(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse eatMethodOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("간편식 섭취 방법"))
                .findFirst()
                .orElseThrow();

        assertThat(eatMethodOption.getOptionFilters()).hasSize(3);
        assertThat(eatMethodOption.getOptionFilters())
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "즉석완조리식품",
                        "즉석섭취식품",
                        "즉석반조리식품"
                );
    }

    @Test
    void 간편식_카테고리의_조리방법_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryService.getFiltersByRootCategoryAndOption(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse cookMethodOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("에어프라이어 전자렌지 조리 가능"))
                .findFirst()
                .orElseThrow();

        assertThat(cookMethodOption.getOptionFilters()).hasSize(3);
        assertThat(cookMethodOption.getOptionFilters())
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "에어프라이어",
                        "에어프라이어,전자렌지",
                        "전자렌지"
                );
    }
}
