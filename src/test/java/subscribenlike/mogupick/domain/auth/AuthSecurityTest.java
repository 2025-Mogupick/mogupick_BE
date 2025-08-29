package subscribenlike.mogupick.domain.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("인증 없이 보호된 API 호출 시 401/403 응답을 받는다")
    void securityFilterTest() throws Exception {

        // given
        String protectedUrl = "/api/v1/test";

        // when & then
        mockMvc.perform(get(protectedUrl))
                .andExpect(status().isForbidden());
    }
}
