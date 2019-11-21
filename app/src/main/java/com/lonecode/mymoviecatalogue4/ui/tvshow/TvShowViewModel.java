package com.lonecode.mymoviecatalogue4.ui.tvshow;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lonecode.mymoviecatalogue4.BuildConfig;
import com.lonecode.mymoviecatalogue4.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.lonecode.mymoviecatalogue4.helper.NetworkUtils.IMG_URL;

public class TvShowViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<Movie>> list = new MutableLiveData<>();

    void setMovie(String title) {
        String language;

        if (Locale.getDefault().getLanguage().contentEquals("in")) {
            language = "id-ID";
        } else {
            language = "en-US";
        }

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listMovies = new ArrayList<>();

        String url;
        if (title == "") {
            url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=" + language;
        } else {
            url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=" + language + "&query=" + title;
        }
        Log.i("isiurl", url);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("ResponseBody", result);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray listMv = responseObject.getJSONArray("results");
                    for (int i = 0; i < listMv.length(); i++) {
                        JSONObject movie = listMv.getJSONObject(i);
                        Movie movieItems = new Movie();
                        movieItems.setMovieid(String.valueOf(movie.getInt("id")));
                        movieItems.setName(movie.getString("name"));
                        movieItems.setDescription(movie.getString("overview"));
                        movieItems.setPosterPath(IMG_URL + movie.getString("poster_path"));
                        movieItems.setUserScore( new DecimalFormat("#").format(movie.getDouble("vote_average") * 10));
                        movieItems.setReleaseDate(movie.getString("first_air_date"));
                        movieItems.setOriginalLanguage(movie.getString("original_language"));
                        movieItems.setCategory("tvshow");
                        listMovies.add(movieItems);
                    }
                    list.postValue(listMovies);

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    LiveData<ArrayList<Movie>> getMovies() { return list; }
}