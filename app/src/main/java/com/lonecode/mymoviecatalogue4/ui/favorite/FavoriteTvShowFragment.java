package com.lonecode.mymoviecatalogue4.ui.favorite;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lonecode.mymoviecatalogue4.ListMovieAdapter;
import com.lonecode.mymoviecatalogue4.Movie;
import com.lonecode.mymoviecatalogue4.R;
import com.lonecode.mymoviecatalogue4.db.FavMovieHelper;
import com.lonecode.mymoviecatalogue4.helper.MappingHelper;
import com.lonecode.mymoviecatalogue4.ui.movie.MovieViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

interface LoadListFavTvShowCallback {
    void preExecute();
    void postExecute(ArrayList<Movie> movie);
}

public class FavoriteTvShowFragment extends Fragment implements ListMovieAdapter.OnItemClickCallback, LoadListFavTvShowCallback {
    private View root;
    private RecyclerView rvFavoriteTvShow;
    private ListMovieAdapter listMovieAdapter;

    private ArrayList<Movie> list = new ArrayList<>();
    private MovieViewModel movieViewModel;

    private FavMovieHelper favMovieHelper;
    private ArrayList<Movie> listFavTvShow;

    public FavoriteTvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);

        favMovieHelper = FavMovieHelper.getInstance(getActivity().getApplicationContext());
        favMovieHelper.open();

        new LoadFavTvShowAsync(favMovieHelper, this).execute();

        return root;
    }

    private void showList(ArrayList<Movie> movie) {
        rvFavoriteTvShow = root.findViewById(R.id.rv_favorite_tvshow);
        rvFavoriteTvShow.setHasFixedSize(true);

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
        Log.i("listsize: ",  String.valueOf(listSize));

        rvFavoriteTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
        listMovieAdapter = new ListMovieAdapter(getContext(), this);
        listMovieAdapter.notifyDataSetChanged();
        rvFavoriteTvShow.setAdapter(listMovieAdapter);
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

            listFavTvShow = movie;
//            Toast.makeText(getContext(), "Fav Movie size: " + String.valueOf(listFavTvShow.size()), Toast.LENGTH_LONG).show();
//            Toast.makeText(this, listFavMovie.get(0).getName(), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getContext(), "No Data", Toast.LENGTH_LONG).show();
        }
    }

    public static class LoadFavTvShowAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<FavMovieHelper> weakFavMovieHelper;
        private final WeakReference<LoadListFavTvShowCallback> weakCallback;

        private LoadFavTvShowAsync(FavMovieHelper favMovieHelper, LoadListFavTvShowCallback callback) {
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
            Cursor dataCursor = weakFavMovieHelper.get().queryAllByCategory("tvshow");
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
