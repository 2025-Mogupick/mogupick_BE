package subscribenlike.mogupick.billing.service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class TossPaymentsClient {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
            new ParameterizedTypeReference<>() {};

    private final WebClient webClient;

    // BillingConfig 에서 만든 tossWebClient 를 주입(@Value 패턴 C)
    public TossPaymentsClient(WebClient tossWebClient) {
        this.webClient = tossWebClient;
    }

    /** 결제창(authKey) -> 빌링키 발급 */
    public Map<String, Object> issueBillingKeyByAuthKey(String authKey, String customerKey) {
        log.info("toss.issueBillingKey start customerKey={}", mask(customerKey));
        try {
            Map<String, Object> res = webClient.post()
                    .uri("/v1/billing/authorizations/issue")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("authKey", authKey, "customerKey", customerKey))
                    .retrieve()
                    .onStatus(s -> s.isError(),
                            r -> r.bodyToMono(String.class)
                                    .flatMap(b -> Mono.error(new IllegalStateException("Toss issue billingKey failed: " + b))))
                    .bodyToMono(MAP_TYPE)
                    .block();

            log.info("toss.issueBillingKey ok customerKey={}", mask(customerKey));
            return res;
        } catch (RuntimeException e) {
            log.error("toss.issueBillingKey error customerKey={} msg={}", mask(customerKey), e.getMessage());
            throw e;
        }
    }

    /** 빌링키로 결제 승인 */
    public Map<String, Object> approveBilling(String billingKey, int amount, String customerKey, String orderId, String orderName) {
        log.info("toss.approve start orderId={} amount={} customerKey={}", orderId, amount, mask(customerKey));
        try {
            Map<String, Object> res = webClient.post()
                    .uri("/v1/billing/{billingKey}", billingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "amount", amount,
                            "customerKey", customerKey,
                            "orderId", orderId,
                            "orderName", orderName
                    ))
                    .retrieve()
                    .onStatus(s -> s.isError(),
                            r -> r.bodyToMono(String.class)
                                    .flatMap(b -> Mono.error(new IllegalStateException("Toss approve billing failed: " + b))))
                    .bodyToMono(MAP_TYPE)
                    .block();

            log.info("toss.approve ok orderId={} amount={}", orderId, amount);
            return res;
        } catch (RuntimeException e) {
            log.error("toss.approve error orderId={} msg={}", orderId, e.getMessage());
            throw e;
        }
    }

    /** 주문ID로 결제 단건 조회(선택) */
    public Map<String, Object> getPaymentByOrderId(String orderId) {
        log.info("toss.getPaymentByOrderId start orderId={}", orderId);
        try {
            Map<String, Object> res = webClient.post()
                    .uri("/v1/payments/{orderId}", orderId)
                    .retrieve()
                    .onStatus(s -> s.isError(),
                            r -> r.bodyToMono(String.class)
                                    .flatMap(b -> Mono.error(new IllegalStateException("Toss get payment failed: " + b))))
                    .bodyToMono(MAP_TYPE)
                    .block();

            log.info("toss.getPaymentByOrderId ok orderId={}", orderId);
            return res;
        } catch (RuntimeException e) {
            log.error("toss.getPaymentByOrderId error orderId={} msg={}", orderId, e.getMessage());
            throw e;
        }
    }

    // -------- helpers --------
    private String mask(String str) {
        if (str == null || str.length() < 4) return "***";
        return str.substring(0, 2) + "***";
    }
}
