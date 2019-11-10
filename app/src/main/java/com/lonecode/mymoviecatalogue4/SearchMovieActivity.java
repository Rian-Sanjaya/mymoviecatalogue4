package com.lonecode.mymoviecatalogue4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lonecode.mymoviecatalogue4.helper.NetworkUtils;
import com.lonecode.mymoviecatalogue4.ui.movie.MovieViewModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import cz.msebera.android.httpclient.Header;

interface LoadListSearchMovieCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movie);
}

public class SearchMovieActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ListMovieAdapter.OnItemClickCallback, LoadListSearchMovieCallback {
    private SearchView searchView;
    private ProgressBar progressBar;
    private RecyclerView rvSearchMovie;
    private ListMovieAdapter listMovieAdapter;
    private MovieViewModel movieViewModel;
    private String movieTitle = "";
    private ArrayList<Movie> list = new ArrayList<>();
    private TextView tvMovieNotFount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBarSearchMovie);
        tvMovieNotFount = findViewById(R.id.tv_movie_not_found);

        rvSearchMovie = findViewById(R.id.rv_search_movies);
        rvSearchMovie.setHasFixedSize(true);

        rvSearchMovie.setLayoutManager(new LinearLayoutManager(this));
        listMovieAdapter = new ListMovieAdapter(this,this);
        listMovieAdapter.notifyDataSetChanged();
        rvSearchMovie.setAdapter(listMovieAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getText(R.string.search_movie));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void preExecute() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoading(true);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movie) {
//        Log.i("moviesize: ", String.valueOf(movie.size()));
        if (movie != null && movie.size() > 0) {
            list.clear();
            list.addAll(movie);
            listMovieAdapter.setData(list);
            tvMovieNotFount.setVisibility(TextView.INVISIBLE);

        } else {
//            Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
            list.clear();
            listMovieAdapter.setData(list);
            tvMovieNotFount.setVisibility(TextView.VISIBLE);

        }
        showLoading(false);
    }

    private class getJson extends AsyncTask<URL, Void, ArrayList<Movie>> {
        private final WeakReference<LoadListSearchMovieCallback> weakCallback;

        private getJson(LoadListSearchMovieCallback callback) {
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            URL searchURL = params[0];
            String result = null;
            ArrayList<Movie> listMovies = new ArrayList<>();
            String IMG_URL = "https://image.tmdb.org/t/p/w500/";

            try {
                result = NetworkUtils.getResponseFromHttpUrl(searchURL);
                Log.i("httpresponse: ", result);

                JSONObject responseObject = new JSONObject(result);
                JSONArray listMv = responseObject.getJSONArray("results");
                for (int i = 0; i < listMv.length(); i++) {
                    JSONObject movie = listMv.getJSONObject(i);
                    Movie movieItems = new Movie();
                    movieItems.setMovieid(String.valueOf(movie.getInt("id")));
                    movieItems.setName(movie.getString("title"));
                    movieItems.setDescription(movie.getString("overview"));
                    movieItems.setPosterPath(IMG_URL + movie.getString("poster_path"));
                    movieItems.setUserScore( new DecimalFormat("#").format(movie.getDouble("vote_average") * 10));
                    movieItems.setReleaseDate(movie.getString("release_date"));
                    movieItems.setOriginalLanguage(movie.getString("original_language"));
                    movieItems.setCategory("movie");
                    listMovies.add(movieItems);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return listMovies;
            }

//            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movie) {
            super.onPostExecute(movie);
            weakCallback.get().postExecute(movie);
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search_movie)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.type_movie_title));
            searchView.setOnQueryTextListener(this);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
//        Toast.makeText(SearchMovieActivity.this, "Movie title", Toast.LENGTH_LONG).show();
//        return true;
        return false;
    }

    @Override
    public boolean onQueryTextChange(String title) {
        if (title.length() >= 3) {
//            Toast.makeText(SearchMovieActivity.this, newText, Toast.LENGTH_LONG).show();
//            String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=" + language + "&query=" + newText;

            String API_KEY = "e4621b68dcd1fa1de4a66cfd0664dc28";
            String language;

            if (Locale.getDefault().getLanguage().contentEquals("in")) {
                language = "id-ID";
            } else {
                language = "en-US";
            }

            Uri buildUri = Uri.parse("https://api.themoviedb.org/3/search/movie").buildUpon()
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("language", language)
                    .appendQueryParameter("query", title)
                    .build();

            URL url = null;
            try {
                url = new URL(buildUri.toString());
                new getJson(this).execute(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            list.clear();
            listMovieAdapter.setData(list);
            tvMovieNotFount.setVisibility(TextView.VISIBLE);
        }

        return true;
    }

    @Override
    public void onItemClicked(Movie movie) {
        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
        movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_ACTIONBAR_TITLE, getString(R.string.movie_detail));
        movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_DETAIL, movie);
        startActivity(movieDetailIntent);
    }
}
