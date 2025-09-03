package subscribenlike.mogupick.searchKeyword.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Operation(summary = "상품 검색", description = "입력한 검색어를 포함한 구독 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 검색 성공"
            )
    })
    @GetMapping
    public List<SearchProductResponse> searchProducts(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestParam SearchKeywordRequest searchKeywordRequest) {
        return searchKeywordService.findByKeyword(userDetails.getUsername(), searchKeywordRequest);
    }

    @Operation(summary = "연관 검색어 조회", description = "입력한 검색어를 포함한 연관검색어를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "연관검색어 조회 성공"
            )
    })
    @GetMapping("/related")
    public List<SearchKeywordResponse> findRelatedKeyword(@RequestParam SearchKeywordRequest searchKeywordRequest) {
        return searchKeywordService.findRelatedKeyword(searchKeywordRequest.content());
    }

    @Operation(summary = "실시간 급상승 검색어 조회", description = "실시간으로 많이 검색된 검색어들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "실시간 급상승 검색어 조회 성공"
            )
    })
    @GetMapping("/top-rising/today")
    public List<SearchKeywordResponse> getTop10RisingToday() {
        return searchKeywordService.findTop10RisingToday();
    }
}
