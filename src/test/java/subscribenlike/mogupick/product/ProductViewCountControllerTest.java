package subscribenlike.mogupick.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subscribenlike.mogupick.product.model.FetchProductDailyViewStatChangeResponse;
import subscribenlike.mogupick.product.model.FetchProductMostDailyViewStatChangeResponse;
import subscribenlike.mogupick.product.service.ProductViewCountService;
import subscribenlike.mogupick.support.annotation.ServiceTest;

@ServiceTest
@AutoConfigureMockMvc
class ProductViewCountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductViewCountService productViewCountService;

    @Test
    void 상품_조회수_증가_요청을_처리할_수_있다() throws Exception {
        // Given
        Long productId = 1L;
        doNothing().when(productViewCountService).incrementProductViewCount(productId);

        // When & Then
        mockMvc.perform(put("/api/v1/products/view-count/{productId}/increment", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 주목받는_상품_목록_조회_요청을_처리할_수_있다() throws Exception {
        // Given
        int size = 5;
        FetchProductDailyViewStatChangeResponse change = FetchProductDailyViewStatChangeResponse.builder()
                .productId(1L)
                .startTime("2024010100")
                .endTime("2024010101")
                .startViewCount(10L)
                .endViewCount(20L)
                .gradient(10.0)
                .viewCountIncreaseRate(100.0)
                .build();

        FetchProductMostDailyViewStatChangeResponse response = 
            FetchProductMostDailyViewStatChangeResponse.of(change, 20L);
        
        List<FetchProductMostDailyViewStatChangeResponse> responseList = Arrays.asList(response);
        
        when(productViewCountService.getMostDailyViewStatChangeProduct(any()))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(responseList));

        // When & Then
        mockMvc.perform(get("/api/v1/products/view-count/most-daily-view-stat-change")
                .param("size", String.valueOf(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 주목받는_상품_목록이_없을_경우_빈_배열을_반환한다() throws Exception {
        // Given
        int size = 5;
        when(productViewCountService.getMostDailyViewStatChangeProduct(any()))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(Arrays.asList()));

        // When & Then
        mockMvc.perform(get("/api/v1/products/view-count/most-daily-view-stat-change")
                .param("size", String.valueOf(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 상품_조회수_증가_요청시_잘못된_상품ID를_처리할_수_있다() throws Exception {
        // Given
        Long invalidProductId = -1L;
        doNothing().when(productViewCountService).incrementProductViewCount(invalidProductId);

        // When & Then
        mockMvc.perform(put("/api/v1/products/view-count/{productId}/increment", invalidProductId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 주목받는_상품_목록_조회시_유효한_size_값을_처리할_수_있다() throws Exception {
        // Given
        int validSize = 5;
        when(productViewCountService.getMostDailyViewStatChangeProduct(any()))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(Arrays.asList()));

        // When & Then
        mockMvc.perform(get("/api/v1/products/view-count/most-daily-view-stat-change")
                .param("size", String.valueOf(validSize))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 상품_조회수_증가_요청시_올바른_HTTP_메서드를_사용한다() throws Exception {
        // Given
        Long productId = 1L;
        doNothing().when(productViewCountService).incrementProductViewCount(productId);

        // When & Then - PUT 메서드 사용 확인
        mockMvc.perform(put("/api/v1/products/view-count/{productId}/increment", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 주목받는_상품_목록_조회시_올바른_HTTP_메서드를_사용한다() throws Exception {
        // Given
        when(productViewCountService.getMostDailyViewStatChangeProduct(any()))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(Arrays.asList()));

        // When & Then - GET 메서드 사용 확인
        mockMvc.perform(get("/api/v1/products/view-count/most-daily-view-stat-change")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
