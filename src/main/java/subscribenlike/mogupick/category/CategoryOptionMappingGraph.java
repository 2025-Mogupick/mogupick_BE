package subscribenlike.mogupick.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import subscribenlike.mogupick.category.domain.CategoryOption;
import subscribenlike.mogupick.category.domain.CategoryOptionFilter;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.model.CategoryOptionAndFilterResponse;
import subscribenlike.mogupick.category.model.CategoryOptionResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CategoryOptionMappingGraph {

    private final Map<RootCategory, ArrayList<CategoryOptionMappingNode>> graph;

    private CategoryOptionMappingGraph() {
        this.graph = new ConcurrentHashMap<>();
        init();
    }

    public void init() {
        graphInit();
        optionsInit();
        optionFiltersInit();
    }

    private void graphInit() {
        // 루트 카테고리 별 옵션 리스트 생성
        Arrays.stream(RootCategory.values())
                .forEach(rootCategory -> graph.put(rootCategory, new ArrayList<>()));
    }

    private void optionFiltersInit() {
        // 가격 범위 필터 (범위 + 선택)
        addFilters(RootCategory.CONVENIENCE_FOOD, CategoryOption.PRICE,
                CategoryOptionFilter.of("10,000원 미만", "(0,10000)"),
                CategoryOptionFilter.of("10,000원 이상-20,000원 미만", "[10000,20000)"),
                CategoryOptionFilter.of("20,000원 이상-30,000원 미만", "[20000,30000)"),
                CategoryOptionFilter.of("30,000원 이상-40,000원 미만", "[30000,40000)"),
                CategoryOptionFilter.of("40,000원 이상", "[40000,)")
        );

        // 별점 갯수 필터 (단일 선택)
        addFilters(RootCategory.CONVENIENCE_FOOD, CategoryOption.RATING,
                CategoryOptionFilter.of("1개", "1"),
                CategoryOptionFilter.of("2개", "2"),
                CategoryOptionFilter.of("3개", "3"),
                CategoryOptionFilter.of("4개", "4"),
                CategoryOptionFilter.of("5개", "5")
        );

        // 중량 필터 (복수 선택)
        addFilters(RootCategory.CONVENIENCE_FOOD, CategoryOption.WEIGHT,
                CategoryOptionFilter.of("200g 이하", "(0,200]"),
                CategoryOptionFilter.of("200g-500g", "(200,500]"),
                CategoryOptionFilter.of("500g-1kg", "(500,1000]"),
                CategoryOptionFilter.of("1kg-2kg", "(1000,2000]"),
                CategoryOptionFilter.of("2kg-3kg", "(2000,3000]"),
                CategoryOptionFilter.of("3kg 이상", "(3000,)")
        );

        // 간편식 섭취 방법 필터 (복수 선택)
        addFilters(RootCategory.CONVENIENCE_FOOD, CategoryOption.EAT_METHOD,
                CategoryOptionFilter.of("즉석완조리식품", "즉석완조리식품"),
                CategoryOptionFilter.of("즉석섭취식품", "즉석섭취식품"),
                CategoryOptionFilter.of("즉석반조리식품", "즉석반조리식품")
        );

        // 칼로리 필터 (복수 선택)
        addFilters(RootCategory.CONVENIENCE_FOOD, CategoryOption.CALORIE,
                CategoryOptionFilter.of("100kcal 이하", "(0,100]"),
                CategoryOptionFilter.of("100kcal-200kcal", "(100,200]"),
                CategoryOptionFilter.of("200kcal-300kcal", "(200,300]"),
                CategoryOptionFilter.of("300kcal-500kcal", "(300,500]"),
                CategoryOptionFilter.of("500kcal 이상", "(500,)")
        );

        // 에어프라이어 전자렌지 조리 가능 필터 (복수 선택)
        addFilters(RootCategory.CONVENIENCE_FOOD, CategoryOption.COOK_METHOD,
                CategoryOptionFilter.of("에어프라이어", "에어프라이어"),
                CategoryOptionFilter.of("에어프라이어,전자렌지", "에어프라이어,전자렌지"),
                CategoryOptionFilter.of("전자렌지", "전자렌지")
        );

        // 개당 수량 필터 (복수 선택)
        addFilters(RootCategory.CONVENIENCE_FOOD, CategoryOption.QUANTITY,
                CategoryOptionFilter.of("6개 이하", "(0,6]"),
                CategoryOptionFilter.of("6개-10개", "(6,10]"),
                CategoryOptionFilter.of("10개-20개", "(10,20]"),
                CategoryOptionFilter.of("20개-30개", "(20,30]"),
                CategoryOptionFilter.of("50개 이상", "[50,)")
        );
    }

    private void optionsInit() {
        addAllOptions(
                RootCategory.CONVENIENCE_FOOD,
                CategoryOption.PRICE,
                CategoryOption.RATING,
                CategoryOption.WEIGHT,
                CategoryOption.CALORIE,
                CategoryOption.QUANTITY,
                CategoryOption.EAT_METHOD,
                CategoryOption.COOK_METHOD);
    }

    public List<CategoryOption> getOptionsByRootCategory(RootCategory rootCategory) {
        return graph.get(rootCategory).stream()
                .map(CategoryOptionMappingNode::getCategoryOption)
                .toList();
    }

    public List<CategoryOptionAndFilterResponse> getOptionAndFiltersByRootCategory(RootCategory rootCategory) {
        return graph.get(rootCategory).stream()
                .map(node ->
                        CategoryOptionAndFilterResponse.of(
                                CategoryOptionResponse.from(node.getCategoryOption()),
                                node.getFilters()
                        ))
                .toList();
    }

    private void addAllOptions(RootCategory rootCategory, CategoryOption... options) {
        Arrays.stream(options).forEach(option ->
                graph.get(rootCategory).add(CategoryOptionMappingNode.of(rootCategory, option))
        );
    }

    private void addFilters(RootCategory rootCategory,
                            CategoryOption categoryOption,
                            CategoryOptionFilter... filters) {

        CategoryOptionMappingNode node = graph.get(rootCategory).stream()
                .filter(n -> n.getCategoryOption() == categoryOption)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "%s 에 %s 옵션이 존재하지 않습니다.".formatted(rootCategory.name(), categoryOption.name())));

        node.addFilter(filters);
    }
}

@Getter
@AllArgsConstructor(staticName = "of")
class CategoryOptionMappingNode {
    private RootCategory rootCategory;
    private CategoryOption categoryOption;
    private final ArrayList<CategoryOptionFilter> filters = new ArrayList<>();

    public void addFilter(CategoryOptionFilter... optionFilters) {
        filters.addAll(Arrays.asList(optionFilters));
    }
}

