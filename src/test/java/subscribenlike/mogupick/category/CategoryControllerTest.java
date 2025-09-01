package subscribenlike.mogupick.category;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.model.CategoryOptionAndFilterResponse;
import subscribenlike.mogupick.category.model.CategoryOptionResponse;
import subscribenlike.mogupick.category.model.RootCategoryResponse;
import subscribenlike.mogupick.support.DatabaseCleanerExtension;
import subscribenlike.mogupick.support.fixture.CategoryFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.junit.jupiter.api.extension.ExtendWith(DatabaseCleanerExtension.class)
class CategoryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CategoryController categoryController;

    @Test
    void 루트_카테고리_리스트_조회_API가_정상적으로_동작한다() {
        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/root")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        
        var responseBody = response.jsonPath();
        assertThat(responseBody.getInt("status")).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getString("message")).isEqualTo("루트 카테고리 리스트 조회에 성공하였습니다.");
        
        List<RootCategoryResponse> data = responseBody.getList("data", RootCategoryResponse.class);
        assertThat(data).hasSize(10);
    }

    @Test
    void 루트_카테고리_리스트_조회_API_응답에_올바른_카테고리들이_포함되어_있다() {
        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/root")
                .then().log().all()
                .extract();

        // Then
        var responseBody = response.jsonPath();
        List<RootCategoryResponse> data = responseBody.getList("data", RootCategoryResponse.class);
        
        assertThat(data)
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
    void 카테고리_옵션과_필터_조회_API가_정상적으로_동작한다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", convenienceFood.name())
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options-filters")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        
        var responseBody = response.jsonPath();
        assertThat(responseBody.getInt("status")).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getString("message")).isEqualTo("카테고리 옵션 및 필터 조회에 성공하였습니다.");
        
        List<CategoryOptionAndFilterResponse> data = responseBody.getList("data", CategoryOptionAndFilterResponse.class);
        assertThat(data).hasSize(7);
    }

    @Test
    void 간편식_카테고리의_옵션과_필터_조회_API_응답에_올바른_옵션들이_포함되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", convenienceFood.name())
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options-filters")
                .then().log().all()
                .extract();

        // Then
        var responseBody = response.jsonPath();
        List<CategoryOptionAndFilterResponse> data = responseBody.getList("data", CategoryOptionAndFilterResponse.class);
        
        assertThat(data)
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
    void 간편식_카테고리의_가격_옵션_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", convenienceFood.name())
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options-filters")
                .then().log().all()
                .extract();

        // Then
        var responseBody = response.jsonPath();
        List<CategoryOptionAndFilterResponse> data = responseBody.getList("data", CategoryOptionAndFilterResponse.class);
        
        // 가격 옵션 찾기
        var priceOption = data.stream()
                .filter(option -> option.getOption().getName().equals("가격"))
                .findFirst()
                .orElseThrow();
        
        assertThat(priceOption.getOption().getType()).isEqualTo("RANGE");
        assertThat(priceOption.getOption().isMultiple()).isFalse();
        assertThat(priceOption.getOptionFilters()).hasSize(5);
    }

    @Test
    void 간편식_카테고리의_별점_옵션_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", convenienceFood.name())
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options-filters")
                .then().log().all()
                .extract();

        // Then
        var responseBody = response.jsonPath();
        List<CategoryOptionAndFilterResponse> data = responseBody.getList("data", CategoryOptionAndFilterResponse.class);
        
        // 별점 옵션 찾기
        var ratingOption = data.stream()
                .filter(option -> option.getOption().getName().equals("별점 갯수"))
                .findFirst()
                .orElseThrow();
        
        assertThat(ratingOption.getOption().getType()).isEqualTo("CHOICE");
        assertThat(ratingOption.getOption().isMultiple()).isFalse();
        assertThat(ratingOption.getOptionFilters()).hasSize(5);
    }

    @Test
    void 간편식_카테고리의_중량_옵션_필터가_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", convenienceFood.name())
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options-filters")
                .then().log().all()
                .extract();

        // Then
        var responseBody = response.jsonPath();
        List<CategoryOptionAndFilterResponse> data = responseBody.getList("data", CategoryOptionAndFilterResponse.class);
        
        // 중량 옵션 찾기
        var weightOption = data.stream()
                .filter(option -> option.getOption().getName().equals("중량"))
                .findFirst()
                .orElseThrow();
        
        assertThat(weightOption.getOption().getType()).isEqualTo("CHOICE");
        assertThat(weightOption.getOption().isMultiple()).isTrue();
        assertThat(weightOption.getOptionFilters()).hasSize(6);
    }

    @Test
    void 잘못된_루트카테고리_파라미터로_요청시_500_에러가_발생한다() {
        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", "INVALID_CATEGORY")
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options-filters")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 루트카테고리_파라미터가_없을_경우_500_에러가_발생한다() {
        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options-filters")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 카테고리_옵션_조회_API가_정상적으로_동작한다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", convenienceFood.name())
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        
        var responseBody = response.jsonPath();
        assertThat(responseBody.getInt("status")).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getString("message")).isEqualTo("카테고리 옵션 조회에 성공하였습니다.");
        
        List<CategoryOptionResponse> data = responseBody.getList("data", CategoryOptionResponse.class);
        assertThat(data).hasSize(7);
    }

    @Test
    void 간편식_카테고리의_옵션_조회_API_응답에_올바른_옵션들이_포함되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", convenienceFood.name())
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options")
                .then().log().all()
                .extract();

        // Then
        var responseBody = response.jsonPath();
        List<CategoryOptionResponse> data = responseBody.getList("data", CategoryOptionResponse.class);
        
        assertThat(data)
                .extracting("name")
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
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", convenienceFood.name())
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options")
                .then().log().all()
                .extract();

        // Then
        var responseBody = response.jsonPath();
        List<CategoryOptionResponse> data = responseBody.getList("data", CategoryOptionResponse.class);
        
        // 가격 옵션 찾기
        var priceOption = data.stream()
                .filter(option -> option.getName().equals("가격"))
                .findFirst()
                .orElseThrow();
        
        assertThat(priceOption.getType()).isEqualTo("RANGE");
        assertThat(priceOption.isMultiple()).isFalse();
    }

    @Test
    void 간편식_카테고리의_중량_옵션이_올바르게_설정되어_있다() {
        // Given
        RootCategory convenienceFood = CategoryFixture.간편식();

        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", convenienceFood.name())
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options")
                .then().log().all()
                .extract();

        // Then
        var responseBody = response.jsonPath();
        List<CategoryOptionResponse> data = responseBody.getList("data", CategoryOptionResponse.class);
        
        // 중량 옵션 찾기
        var weightOption = data.stream()
                .filter(option -> option.getName().equals("중량"))
                .findFirst()
                .orElseThrow();
        
        assertThat(weightOption.getType()).isEqualTo("CHOICE");
        assertThat(weightOption.isMultiple()).isTrue();
    }

    @Test
    void 잘못된_루트카테고리_파라미터로_카테고리_옵션_조회_요청시_500_에러가_발생한다() {
        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("rootCategory", "INVALID_CATEGORY")
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 루트카테고리_파라미터가_없을_경우_카테고리_옵션_조회_요청시_500_에러가_발생한다() {
        // When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("http://localhost:" + port + "/api/v1/categories/options")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
