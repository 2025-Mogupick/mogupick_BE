package subscribenlike.mogupick.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.model.CategoryOptionAndFilterResponse;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.CategoryFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class CategoryOptionMappingGraphTest {

    @Autowired
    private CategoryOptionMappingGraph categoryOptionMappingGraph;

    @Test
    void 간편식_카테고리의_옵션_리스트를_조회할_수_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOption> options = categoryOptionMappingGraph.getOptionsByRootCategory(convenienceFood);

        // Then
        assertThat(options).hasSize(7);
        assertThat(options).containsExactlyInAnyOrder(
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT,
                CategoryOption.CALORIE,
                CategoryOption.QUANTITY,
                CategoryOption.EAT_METHOD,
                CategoryOption.COOK_METHOD
        );
    }

    @Test
    void 간편식_카테고리의_옵션과_필터_리스트를_조회할_수_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(convenienceFood);

        // Then
        assertThat(optionsAndFilters).hasSize(7);
        
        // 각 옵션의 이름 확인
        assertThat(optionsAndFilters)
                .extracting("option.name")
                .containsExactlyInAnyOrder(
                        "가격",
                        "별점 갯수",
                        "중량",
                        "칼로리",
                        "개당 수량",
                        "간편식 섭취 방법",
                        "에어프라이어 전자렌지 조리 가능"
                );
    }

    @Test
    void 간편식_카테고리의_가격_옵션이_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse priceOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("가격"))
                .findFirst()
                .orElseThrow();

        assertThat(priceOption.getOption().getType()).isEqualTo("RANGE");
        assertThat(priceOption.getOption().isMultiple()).isFalse();
        assertThat(priceOption.getOptionFilters()).hasSize(5);
    }

    @Test
    void 간편식_카테고리의_별점_옵션이_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse ratingOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("별점 갯수"))
                .findFirst()
                .orElseThrow();

        assertThat(ratingOption.getOption().getType()).isEqualTo("CHOICE");
        assertThat(ratingOption.getOption().isMultiple()).isFalse();
        assertThat(ratingOption.getOptionFilters()).hasSize(5);
    }

    @Test
    void 간편식_카테고리의_중량_옵션이_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse weightOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("중량"))
                .findFirst()
                .orElseThrow();

        assertThat(weightOption.getOption().getType()).isEqualTo("CHOICE");
        assertThat(weightOption.getOption().isMultiple()).isTrue();
        assertThat(weightOption.getOptionFilters()).hasSize(6);
    }

    @Test
    void 간편식_카테고리의_칼로리_옵션이_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse calorieOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("칼로리"))
                .findFirst()
                .orElseThrow();

        assertThat(calorieOption.getOption().getType()).isEqualTo("CHOICE");
        assertThat(calorieOption.getOption().isMultiple()).isTrue();
        assertThat(calorieOption.getOptionFilters()).hasSize(5);
    }

    @Test
    void 간편식_카테고리의_개당수량_옵션이_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse quantityOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("개당 수량"))
                .findFirst()
                .orElseThrow();

        assertThat(quantityOption.getOption().getType()).isEqualTo("CHOICE");
        assertThat(quantityOption.getOption().isMultiple()).isTrue();
        assertThat(quantityOption.getOptionFilters()).hasSize(5);
    }

    @Test
    void 간편식_카테고리의_섭취방법_옵션이_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse eatMethodOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("간편식 섭취 방법"))
                .findFirst()
                .orElseThrow();

        assertThat(eatMethodOption.getOption().getType()).isEqualTo("CHOICE");
        assertThat(eatMethodOption.getOption().isMultiple()).isTrue();
        assertThat(eatMethodOption.getOptionFilters()).hasSize(3);
    }

    @Test
    void 간편식_카테고리의_조리방법_옵션이_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        List<CategoryOptionAndFilterResponse> optionsAndFilters = 
                categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(convenienceFood);

        // Then
        CategoryOptionAndFilterResponse cookMethodOption = optionsAndFilters.stream()
                .filter(response -> response.getOption().getName().equals("에어프라이어 전자렌지 조리 가능"))
                .findFirst()
                .orElseThrow();

        assertThat(cookMethodOption.getOption().getType()).isEqualTo("CHOICE");
        assertThat(cookMethodOption.getOption().isMultiple()).isTrue();
        assertThat(cookMethodOption.getOptionFilters()).hasSize(3);
    }

    @Test
    void 다른_카테고리들은_옵션이_설정되어_있지_않다() {
        // Given
        RootCategory freshFood = CategoryFixture.신선식품();
        RootCategory meatSeafood = CategoryFixture.정육수산물();
        RootCategory dairyBeverage = CategoryFixture.유제품음료();

        // When & Then
        assertThat(categoryOptionMappingGraph.getOptionsByRootCategory(freshFood)).isEmpty();
        assertThat(categoryOptionMappingGraph.getOptionsByRootCategory(meatSeafood)).isEmpty();
        assertThat(categoryOptionMappingGraph.getOptionsByRootCategory(dairyBeverage)).isEmpty();
    }

    @Test
    void 다른_카테고리들은_옵션과_필터가_설정되어_있지_않다() {
        // Given
        RootCategory freshFood = CategoryFixture.신선식품();
        RootCategory meatSeafood = CategoryFixture.정육수산물();
        RootCategory dairyBeverage = CategoryFixture.유제품음료();

        // When & Then
        assertThat(categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(freshFood)).isEmpty();
        assertThat(categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(meatSeafood)).isEmpty();
        assertThat(categoryOptionMappingGraph.getOptionAndFiltersByRootCategory(dairyBeverage)).isEmpty();
    }
}
