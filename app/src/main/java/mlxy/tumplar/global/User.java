package mlxy.tumplar.global;

import android.text.TextUtils;

import mlxy.tumplar.tumblr.TumblrClient;
import mlxy.utils.Prefs;

public class User {
    public static volatile boolean hasLogedIn = false;
    public static volatile com.tumblr.jumblr.types.User info;
    public static volatile String avatarUrl;

    private static volatile String access_token;
    private static volatile String access_token_secret;

    public static void login(String token, String tokenSecret) {
        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(tokenSecret)) {
            return;
        }

        access_token = token;
        access_token_secret = tokenSecret;

        TumblrClient.setToken(token, tokenSecret);

        hasLogedIn = true;
    }

    public static void logout() {
        access_token = null;
        access_token_secret = null;

        TumblrClient.setToken("", "");

        Prefs.remove(Application.context, Constants.KEY_TOKEN);
        Prefs.remove(Application.context, Constants.KEY_TOKEN_SECRET);

        hasLogedIn = false;
    }
}
