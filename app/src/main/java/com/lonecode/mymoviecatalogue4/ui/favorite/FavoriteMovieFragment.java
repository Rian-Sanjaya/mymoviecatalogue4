package com.lonecode.mymoviecatalogue4.ui.favorite;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lonecode.mymoviecatalogue4.ListMovieAdapter;
import com.lonecode.mymoviecatalogue4.Movie;
import com.lonecode.mymoviecatalogue4.R;
import com.lonecode.mymoviecatalogue4.db.FavMovieHelper;
import com.lonecode.mymoviecatalogue4.helper.MappingHelper;
import com.lonecode.mymoviecatalogue4.ui.movie.MovieViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

interface LoadListFavMovieCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movie);
}

public class FavoriteMovieFragment extends Fragment implements ListMovieAdapter.OnItemClickCallback, LoadListFavMovieCallback {
    private View root;
    private RecyclerView rvFavoriteMovie;
    private ListMovieAdapter listMovieAdapter;

    private ArrayList<Movie> list = new ArrayList<>();
    private MovieViewModel movieViewModel;

    private FavMovieHelper favMovieHelper;
    private ArrayList<Movie> listFavMovie;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_favorite_movie, container, false);

        favMovieHelper = FavMovieHelper.getInstance(getActivity().getApplicationContext());
        favMovieHelper.open();

        new LoadFavMovieAsync(favMovieHelper, this).execute();

        return root;
    }

    private void showList(ArrayList<Movie> movie) {
        rvFavoriteMovie = root.findViewById(R.id.rv_favorite_movie);
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

        rvFavoriteMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        listMovieAdapter = new ListMovieAdapter(getContext(), this);
        listMovieAdapter.notifyDataSetChanged();
        rvFavoriteMovie.setAdapter(listMovieAdapter);
        listMovieAdapter.setData(list);
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(new Runnable() {
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

            listFavMovie = movie;
//            Toast.makeText(getContext(), "Fav Movie size: " + String.valueOf(listFavMovie.size()), Toast.LENGTH_LONG).show();
//            Toast.makeText(this, listFavMovie.get(0).getName(), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getContext(), "No Data", Toast.LENGTH_LONG).show();
        }
    }

    private static class LoadFavMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<FavMovieHelper> weakFavMovieHelper;
        private final WeakReference<LoadListFavMovieCallback> weakCallback;

        private LoadFavMovieAsync(FavMovieHelper favMovieHelper, LoadListFavMovieCallback callback) {
            weakFavMovieHelper = new WeakReference<>(favMovieHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Cursor dataCursor = weakFavMovieHelper.get().queryAllByCategory("movie");
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movie) {
            super.onPostExecute(movie);
            weakCallback.get().postExecute(movie);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        favMovieHelper.close();
    }

    @Override
    public void onItemClicked(Movie movie) {

    }
}
