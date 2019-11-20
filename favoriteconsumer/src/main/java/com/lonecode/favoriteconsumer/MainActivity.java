package com.lonecode.favoriteconsumer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lonecode.favoriteconsumer.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.lonecode.favoriteconsumer.db.DatabaseMovie.FavMovie.CONTENT_URI;

interface LoadListFavMovieCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movie);
}

public class MainActivity extends AppCompatActivity implements ListMovieAdapter.OnItemClickCallback, LoadListFavMovieCallback {
    private RecyclerView rvFavoriteMovie;
    private ListMovieAdapter listMovieAdapter;
    private ArrayList<Movie> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new LoadFavMovieAsync(this, this).execute();
    }

    private void showList(ArrayList<Movie> movie) {
        rvFavoriteMovie = findViewById(R.id.rv_favorite_movie);
        rvFavoriteMovie.setHasFixedSize(true);

//        list.addAll(movie);
        for (int i = 0; i < movie.size(); i++) {
            Movie movieItems = new Movie();
            movieItems.setMovieid(String.valueOf(movie.get(i).getMovieid()));
            movieItems.setName(movie.get(i).getName());
            movieItems.setDescription("");
            movieItems.setPosterPath(movie.get(i).getPosterPath());
            movieItems.setUserScore(movie.get(i).getUserScore());
            movieItems.setReleaseDate(movie.get(i).getReleaseDate());
            movieItems.setOriginalLanguage(movie.get(i).getOriginalLanguage());
            movieItems.setCategory("movie");
            list.add(movieItems);
        }
        int listSize = list.size();
        Log.i("listsize: ", String.valueOf(listSize));

        rvFavoriteMovie.setLayoutManager(new LinearLayoutManager(this));
        listMovieAdapter = new ListMovieAdapter(this, this);
        listMovieAdapter.notifyDataSetChanged();
        rvFavoriteMovie.setAdapter(listMovieAdapter);
        listMovieAdapter.setData(list);
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("Text preExecute", "Test preExecute");
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movie) {
        if (movie.size() > 0) {
            showList(movie);

        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
        }
    }

    private static class LoadFavMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadListFavMovieCallback> weakCallback;

        private LoadFavMovieAsync(Context context, LoadListFavMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Uri uriWithCategory = Uri.parse(CONTENT_URI + "/category/movie");

            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(uriWithCategory, null, null, null, null);
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movie) {
            super.onPostExecute(movie);
            weakCallback.get().postExecute(movie);
        }
    }

    @Override
    public void onItemClicked(Movie movie) {

    }
}
