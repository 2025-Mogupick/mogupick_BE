package subscribenlike.mogupick.global.oauth.client;

import subscribenlike.mogupick.global.oauth.OAuthUserInfo;

import java.util.Map;

public interface OAuthClient {

    String getProvider();

    Map<String, Object> getOAuthUserAttributes(String accessToken);
}