package subscribenlike.mogupick.billing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import subscribenlike.mogupick.billing.util.AesGcm;

import java.util.Base64;

@Configuration
public class BillingConfig {

    @Bean
    public WebClient tossWebClient(
            @Value("${mogupick.toss.base-url}") String baseUrl,
            @Value("${mogupick.toss.secret-key}") String secretKey
    ) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                        .build())
                // 시크릿키를 Basic Auth로 사용
                .defaultHeaders(h -> h.setBasicAuth(secretKey, ""))
                .build();
    }

    @Bean
    public AesGcm aesGcm(@Value("${mogupick.crypto.aes256-gcm-base64-key}") String base64Key) {
        return new AesGcm(Base64.getDecoder().decode(base64Key));
    }
}
