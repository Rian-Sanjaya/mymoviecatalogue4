package com.lonecode.mymoviecatalogue4;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lonecode.mymoviecatalogue4.helper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.lonecode.mymoviecatalogue4.helper.NetworkUtils.IMG_URL;

interface LoadListSearchTvCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movie);
}

public class SearchTvShowActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ListMovieAdapter.OnItemClickCallback, LoadListSearchTvCallback {
    private ProgressBar progressBar;
    private RecyclerView rvSearchTv;
    private ListMovieAdapter listMovieAdapter;
    private ArrayList<Movie> list = new ArrayList<>();
    private TextView tvNotFount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tv_show);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBarSearchTv);
        tvNotFount = findViewById(R.id.tv_not_found);

        rvSearchTv = findViewById(R.id.rv_search_tv_show);
        rvSearchTv.setHasFixedSize(true);

        rvSearchTv.setLayoutManager(new LinearLayoutManager(this));
        listMovieAdapter = new ListMovieAdapter(this,this);
        listMovieAdapter.notifyDataSetChanged();
        rvSearchTv.setAdapter(listMovieAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getText(R.string.search_tv_show));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoading(true);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movie) {
        Log.i("movie size: ",  String.valueOf(movie.size()));
        if (movie != null && movie.size() > 0) {
            list.clear();
            list.addAll(movie);
            listMovieAdapter.setData(list);
            tvNotFount.setVisibility(TextView.INVISIBLE);

        } else {
            list.clear();
            listMovieAdapter.setData(list);
            tvNotFount.setVisibility(TextView.VISIBLE);

        }
        showLoading(false);
    }

    private class getJson extends AsyncTask<URL, Void, ArrayList<Movie>> {
        private final WeakReference<LoadListSearchTvCallback> weakCallback;

        private getJson(LoadListSearchTvCallback callback) {
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

            try {
                result = NetworkUtils.getResponseFromHttpUrl(searchURL);
                Log.i("httpresponse: ", result);

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
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movie) {
            super.onPostExecute(movie);
            weakCallback.get().postExecute(movie);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String title) {
        if (title.length() >= 3) {
            String API_KEY = BuildConfig.TMDB_API_KEY;
            String language;

            if (Locale.getDefault().getLanguage().contentEquals("in")) {
                language = "id-ID";
            } else {
                language = "en-US";
            }

            Uri buildUri = Uri.parse("https://api.themoviedb.org/3/search/tv").buildUpon()
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("language", language)
                    .appendQueryParameter("query", title)
                    .build();

            URL url = null;
            try {
                url = new URL(buildUri.toString());
                new SearchTvShowActivity.getJson(this).execute(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            list.clear();
            listMovieAdapter.setData(list);
            tvNotFount.setVisibility(TextView.VISIBLE);
        }

        return true;
    }

    @Override
    public void onItemClicked(Movie movie) {
        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
        movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_ACTIONBAR_TITLE, getString(R.string.tvshow_detail));
        movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_DETAIL, movie);
        startActivity(movieDetailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) (menu.findItem(R.id.search_movie)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.type_tvshow_title));
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
}
