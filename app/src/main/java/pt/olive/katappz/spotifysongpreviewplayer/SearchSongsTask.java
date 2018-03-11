package pt.olive.katappz.spotifysongpreviewplayer;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.ICompletedSongSearchListener;
import pt.olive.katappz.spotifysongpreviewplayer.models.Song;
import pt.olive.katappz.spotifysongpreviewplayer.models.Token;
import pt.olive.katappz.spotifysongpreviewplayer.models.spotify.Item;
import pt.olive.katappz.spotifysongpreviewplayer.models.spotify.SearchResult;

import static pt.olive.katappz.spotifysongpreviewplayer.Consts.SPOTIFY_SEARCH_SONG_URL;

/**
 * Async task that searches Spotify for songs with a given name.
 * @author Katy
 * @version 1.0
 * @since   2018-02-07
 */
public class SearchSongsTask extends AsyncTask<String, Void, SearchResult> {

    private String TAG = "SearchSongsTask";
    private ICompletedSongSearchListener completedSearchListener;

    public SearchSongsTask(ICompletedSongSearchListener completedSongSearchListener) {
        completedSearchListener = completedSongSearchListener;
    }

    @Override
    protected SearchResult doInBackground(String... params) {

        String searchQuery = params != null && params.length > 0 ? params[0] : "";
        SearchResult results = null;

        if (!TextUtils.isEmpty(searchQuery)) {

            try {

                Token token = SpotifyAuthentication.getToken();
                if (token == null || token.isTokenExpired()) {
                    token = SpotifyAuthentication.requestSpotifyToken();
                }

                if (token != null && token.access_token != null && !TextUtils.isEmpty(token.access_token)) {
                    String urlString = String.format(SPOTIFY_SEARCH_SONG_URL, searchQuery);
                    String encodedUrlString = urlString.replace(" ", "%20");

                    Request request = new Request.Builder()
                            .url(encodedUrlString)
                            .header("Authorization", "Bearer " + token.access_token)
                            .get()
                            .build();
                    try {

                        OkHttpClient httpClient = new OkHttpClient();
                        Response response = httpClient.newCall(request).execute();
                        if (response != null) {
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                Gson gson = new Gson();
                                results = gson.fromJson(responseBody.string(), SearchResult.class);
                            }
                            response.close();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            } catch (IOException ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
        return results;
    }

    @Override
    protected void onPostExecute(SearchResult results) {
        super.onPostExecute(results);

        ArrayList<Song> searchResults = new ArrayList<>();
        if (results != null) {
            for (Item result : results.getTracks().getItems()) {
                if (result.getType().equals("track")) {
                    Song track = new Song(result);
                    searchResults.add(track);
                }
            }
        }
        if(completedSearchListener != null)
            completedSearchListener.onCompletedSongSearch(searchResults);
    }
}