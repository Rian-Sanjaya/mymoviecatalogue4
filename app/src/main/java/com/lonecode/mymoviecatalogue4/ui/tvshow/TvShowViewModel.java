package com.lonecode.mymoviecatalogue4.ui.tvshow;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lonecode.mymoviecatalogue4.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class TvShowViewModel extends ViewModel {
    private static final String API_KEY = "e4621b68dcd1fa1de4a66cfd0664dc28";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    private MutableLiveData<ArrayList<Movie>> list = new MutableLiveData<>();

    void setMovie() {
        String language;

        if (Locale.getDefault().getLanguage().contentEquals("in")) {
            language = "id-ID";
        } else {
            language = "en-US";
        }

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listMovies = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=" + language;
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
                        movieItems.setName(movie.getString("name"));
                        movieItems.setDescription(movie.getString("overview"));
                        movieItems.setPosterPath(IMG_URL + movie.getString("poster_path"));
                        movieItems.setUserScore( new DecimalFormat("#").format(movie.getDouble("vote_average") * 10));
                        movieItems.setReleaseDate(movie.getString("first_air_date"));
                        movieItems.setOriginalLanguage(movie.getString("original_language"));
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