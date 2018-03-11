package pt.olive.katappz.spotifysongpreviewplayer;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pt.olive.katappz.spotifysongpreviewplayer.models.Token;

/**
 * Does Spotify web api authentication.
 * @author Katy
 * @version 1.0
 * @since   2018-02-07
 */
public class SpotifyAuthentication {

    private static final String TAG = "SpotifyAuthentication";

    private static Token token = null;

    public static Token getToken() { return token; }
    public static void setToken(Calendar tokenDate, Token _token)
    {
        token = _token;
        tokenDate.add(Calendar.SECOND, token.expires_in);
        token.expireDate = tokenDate;
    }

    public static Token requestSpotifyToken() throws IOException {

        Token token = null;
        OkHttpClient httpClient = new OkHttpClient();

        String basicAuth = Consts.SPOTIFY_CLIENT_ID + ":" + Consts.SPOTIFY_SECRET;
        basicAuth = "Basic " + Base64.encodeToString(basicAuth.getBytes(), Base64.NO_WRAP);

        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url(Consts.SPOTIFY_REQUEST_TOKEN_URL)
                .header("Authorization", basicAuth)
                .post(body)
                .build();
        try {

            Response response = httpClient.newCall(request).execute();
            if (response != null) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    Gson gson = new Gson();
                    token = gson.fromJson(responseBody.string(), Token.class);
                    if(token != null)
                        setToken(Calendar.getInstance(), token);
                }
                response.close();
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return token;
    }

}
