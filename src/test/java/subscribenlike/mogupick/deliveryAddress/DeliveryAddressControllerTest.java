package subscribenlike.mogupick.deliveryAddress;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import subscribenlike.mogupick.deliveryAddress.controller.DeliveryAddressController;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressRequest;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressResponse;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressUpdateRequest;
import subscribenlike.mogupick.deliveryAddress.service.DeliveryAddressService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeliveryAddressController.class)
public class DeliveryAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeliveryAddressService deliveryAddressService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("배송지 등록 성공")
    void 배송지_등록_성공() throws Exception {
        DeliveryAddressRequest request = new DeliveryAddressRequest("서울시 강남구", "역삼동 123-45", "홍길동", "010-1234-5678");
        DeliveryAddressResponse response = new DeliveryAddressResponse(1L, "서울시 강남구", "역삼동 123-45", "홍길동", "010-1234-5678");

        when(deliveryAddressService.register(anyLong(), any(DeliveryAddressRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/delivery-addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("memberId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.receiver").value("홍길동"))
                .andExpect(jsonPath("$.baseAddress").value("서울시 강남구"))
                .andExpect(jsonPath("$.detailAddress").value("역삼동 123-45"))
                .andExpect(jsonPath("$.contact").value("010-1234-5678"));
    }

    @Test
    @DisplayName("회원 배송지 전체 조회 성공")
    void 배송지_전체조회_성공() throws Exception {
        List<DeliveryAddressResponse> responses = List.of(
                new DeliveryAddressResponse(1L, "서울시 강남구", "역삼동 123-45", "홍길동", "010-1234-5678"),
                new DeliveryAddressResponse(2L, "서울시 서초구", "서초동 987-65", "김철수", "010-9876-5432")
        );

        when(deliveryAddressService.findAllByMemberId(anyLong())).thenReturn(responses);

        mockMvc.perform(get("/api/delivery-addresses")
                        .header("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].receiver").value("홍길동"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].receiver").value("김철수"));
    }

    @Test
    @DisplayName("배송지 수정 성공")
    void 배송지_수정_성공() throws Exception {
        DeliveryAddressUpdateRequest updateRequest = new DeliveryAddressUpdateRequest("수정된 기본주소", "수정된 상세주소", "수정된 수령인", "010-1111-2222");
        DeliveryAddressResponse updatedResponse = new DeliveryAddressResponse(
                1L,
                updateRequest.baseAddress(),
                updateRequest.detailAddress(),
                updateRequest.receiver(),
                updateRequest.contact()
        );

        when(deliveryAddressService.update(anyLong(), anyLong(), any(DeliveryAddressUpdateRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/delivery-addresses/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .header("memberId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.receiver").value("수정된 수령인"))
                .andExpect(jsonPath("$.baseAddress").value("수정된 기본주소"))
                .andExpect(jsonPath("$.detailAddress").value("수정된 상세주소"))
                .andExpect(jsonPath("$.contact").value("010-1111-2222"));
    }

    @Test
    @DisplayName("배송지 삭제 성공")
    void 배송지_삭제_성공() throws Exception {
        mockMvc.perform(delete("/api/delivery-addresses/{id}", 1L)
                        .header("memberId", "1"))
                .andExpect(status().isNoContent());
    }
}
