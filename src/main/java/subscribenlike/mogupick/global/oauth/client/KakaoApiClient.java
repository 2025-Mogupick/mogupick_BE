package subscribenlike.mogupick.global.oauth.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthClient {

    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.url.user-info}")
    private String userInfoUrl;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public Map<String, Object> getOAuthUserAttributes(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                request,
                Map.class
        ).getBody();
    }
}