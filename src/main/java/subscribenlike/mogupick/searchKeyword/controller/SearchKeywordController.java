package subscribenlike.mogupick.searchKeyword.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.searchKeyword.dto.RecentKeywordResponse;
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
    public ResponseEntity<List<SearchProductResponse>> searchProducts(@AuthenticationPrincipal UserDetails userDetails,
                                                                      @RequestParam SearchKeywordRequest searchKeywordRequest) {
        return ResponseEntity.ok(searchKeywordService.findByKeyword(userDetails.getUsername(), searchKeywordRequest));
    }

    @Operation(summary = "연관 검색어 조회", description = "입력한 검색어를 포함한 연관검색어를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "연관검색어 조회 성공"
            )
    })
    @GetMapping("/related")
    public ResponseEntity<List<SearchKeywordResponse>> findRelatedKeyword(
            @RequestParam SearchKeywordRequest searchKeywordRequest) {
        return ResponseEntity.ok(searchKeywordService.findRelatedKeyword(searchKeywordRequest.content()));
    }

    @Operation(summary = "실시간 급상승 검색어 조회", description = "실시간으로 많이 검색된 검색어들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "실시간 급상승 검색어 조회 성공"
            )
    })
    @GetMapping("/top-rising/today")
    public ResponseEntity<List<SearchKeywordResponse>> getTop10RisingToday() {
        return ResponseEntity.ok(searchKeywordService.findTop10RisingToday());
    }

    @Operation(summary = "최근 검색어 조회", description = "최근 검색했던 키워드들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "최근 검색한 검색어 조회 성공"
            )
    })
    @GetMapping("/recent")
    public ResponseEntity<List<RecentKeywordResponse>> getRecentKeywords(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(searchKeywordService.findRecentKeywords(userDetails.getUsername()));
    }

    @Operation(summary = "최근 검색어 삭제", description = "최근 검색했던 키워드를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "최근 검색한 검색어 삭제 성공"
            )
    })
    @DeleteMapping("/recent/{keywordId}")
    public ResponseEntity<?> deleteRecentKeywords(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long keywordId) {
        searchKeywordService.deleteRecentKeyword(userDetails.getUsername(), keywordId);
        return ResponseEntity.noContent()
                .build();
    }
}
