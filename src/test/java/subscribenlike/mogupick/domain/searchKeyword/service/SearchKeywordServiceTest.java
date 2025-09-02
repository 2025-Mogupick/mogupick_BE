package subscribenlike.mogupick.domain.searchKeyword.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subscribenlike.mogupick.brand.BrandFixture;
import subscribenlike.mogupick.brand.domain.Brand;
import subscribenlike.mogupick.brand.repository.BrandRepository;
import subscribenlike.mogupick.member.ProductTestMemberFixture;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.ProductFixture;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.searchKeyword.dto.SearchKeywordRequest;
import subscribenlike.mogupick.searchKeyword.dto.SearchProductResponse;
import subscribenlike.mogupick.searchKeyword.repository.SearchKeywordRepository;
import subscribenlike.mogupick.searchKeyword.service.SearchKeywordService;
import subscribenlike.mogupick.support.annotation.ServiceTest;

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

    @Test
    void 검색한_키워드를_포함한_구독_상품을_조회한다() {
        Member member = ProductTestMemberFixture.김회원();
        memberRepository.save(member);
        Brand brand = BrandFixture.쿠팡(member);
        brandRepository.save(brand);
        Product coupang = ProductFixture.쿠팡구독(brand);
        productRepository.save(coupang);
        Product naver = ProductFixture.네이버구독(brand);
        productRepository.save(naver);
        Product mogupick = ProductFixture.모구픽구독(brand);
        productRepository.save(mogupick);

        SearchKeywordRequest request = new SearchKeywordRequest("구독");
        List<SearchProductResponse> responseList = searchKeywordService.findByKeyword(request);
        assertThat(responseList.size()).isEqualTo(3);
    }

    @Test
    void 같은_키워드를_검색하면_카운트만_증가한다() {
        SearchKeywordRequest request = new SearchKeywordRequest("구독");
        searchKeywordService.findByKeyword(request);
        searchKeywordService.findByKeyword(request);
        searchKeywordService.findByKeyword(request);

        assertThat(searchKeywordRepository.count()).isEqualTo(1);
        assertThat(
                searchKeywordRepository.findByNormalizedContent(request.content()).get().getSearchedCount()).isEqualTo(
                3);
    }
}
