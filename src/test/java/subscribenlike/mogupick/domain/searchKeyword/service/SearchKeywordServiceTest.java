package subscribenlike.mogupick.domain.searchKeyword.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.brand.BrandFixture;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
//import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.ProductFixture;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.searchKeyword.domain.SearchKeyword;
import subscribenlike.mogupick.searchKeyword.dto.SearchKeywordRequest;
import subscribenlike.mogupick.searchKeyword.dto.SearchKeywordResponse;
import subscribenlike.mogupick.searchKeyword.dto.SearchProductResponse;
import subscribenlike.mogupick.searchKeyword.repository.SearchKeywordRepository;
import subscribenlike.mogupick.searchKeyword.service.SearchKeywordService;
import subscribenlike.mogupick.support.annotation.ServiceTest;
import subscribenlike.mogupick.support.fixture.SearchKeywordFixture;

@ServiceTest
class SearchKeywordServiceTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    SearchKeywordRepository searchKeywordRepository;
    @Autowired
    SearchKeywordService searchKeywordService;

//    @Test
//    void 검색한_키워드를_포함한_구독_상품을_조회한다() {
//        Member member = ProductTestMemberFixture.김회원();
//        memberRepository.save(member);
//        Brand brand = BrandFixture.쿠팡(member);
//        brandRepository.save(brand);
//        Product coupang = ProductFixture.쿠팡구독(brand);
//        productRepository.save(coupang);
//        Product naver = ProductFixture.네이버구독(brand);
//        productRepository.save(naver);
//        Product mogupick = ProductFixture.모구픽구독(brand);
//        productRepository.save(mogupick);
//
//        SearchKeywordRequest request = new SearchKeywordRequest("구독");
//        List<SearchProductResponse> responseList = searchKeywordService.findByKeyword(request);
//        assertThat(responseList.size()).isEqualTo(3);
//    }

    @Test
    void 같은_키워드를_검색하면_카운트만_증가한다() {
        SearchKeywordRequest request = new SearchKeywordRequest("구독");
        searchKeywordService.findByKeyword(null, request);
        searchKeywordService.findByKeyword(null, request);
        searchKeywordService.findByKeyword(null, request);

        assertThat(searchKeywordRepository.count()).isEqualTo(1);
        assertThat(
                searchKeywordRepository.findByNormalizedContent(request.content()).get().getSearchedCount()).isEqualTo(
                3);
    }

    @Test
    void 가장_검색이_많이된_5개의_검색어_조회() {
        SearchKeyword 생수 = SearchKeywordFixture.생수();
        searchKeywordRepository.save(생수);

        SearchKeyword 생수한묶음 = SearchKeywordFixture.생수한묶음();
        searchKeywordRepository.save(생수한묶음);

        SearchKeyword 생수박스 = SearchKeywordFixture.생수박스();
        searchKeywordRepository.save(생수박스);

        SearchKeyword 생수대용량 = SearchKeywordFixture.생수대용량();
        searchKeywordRepository.save(생수대용량);

        SearchKeyword 미니생수 = SearchKeywordFixture.미니생수();
        searchKeywordRepository.save(미니생수);

        //가장 조회수 낮음
        SearchKeyword 해외생수 = SearchKeywordFixture.해외생수();
        searchKeywordRepository.save(해외생수);

        SearchKeywordRequest request = new SearchKeywordRequest("생수");

        List<SearchKeywordResponse> result = searchKeywordService.findRelatedKeyword(request.content());
        assertThat(result.size()).isEqualTo(5);
        assertThat(result)
                .extracting(SearchKeywordResponse::content)
                .doesNotContain("해외 생수");
    }
}
