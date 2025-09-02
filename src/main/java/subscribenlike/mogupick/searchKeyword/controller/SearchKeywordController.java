package subscribenlike.mogupick.searchKeyword.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.searchKeyword.dto.SearchKeywordRequest;
import subscribenlike.mogupick.searchKeyword.dto.SearchKeywordResponse;
import subscribenlike.mogupick.searchKeyword.dto.SearchProductResponse;
import subscribenlike.mogupick.searchKeyword.service.SearchKeywordService;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchKeywordController {
    private final SearchKeywordService searchKeywordService;

    @GetMapping
    public List<SearchProductResponse> searchProducts(@RequestParam SearchKeywordRequest searchKeywordRequest) {
        return searchKeywordService.findByKeyword(searchKeywordRequest);
    }

    @GetMapping("/related")
    public List<SearchKeywordResponse> findRelatedKeyword(@RequestParam SearchKeywordRequest searchKeywordRequest) {
        return searchKeywordService.findRelatedKeyword(searchKeywordRequest.content());
    }

    @GetMapping("/top-rising/today")
    public List<SearchKeywordResponse> getTop10RisingToday() {
        return searchKeywordService.findTop10RisingToday();
    }
}
