package com.lonecode.mymoviecatalogue4.ui.favorite;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonecode.mymoviecatalogue4.ListMovieAdapter;
import com.lonecode.mymoviecatalogue4.Movie;
import com.lonecode.mymoviecatalogue4.R;
import com.lonecode.mymoviecatalogue4.ui.movie.MovieViewModel;

import java.util.ArrayList;

public class FavoriteMovieFragment extends Fragment implements ListMovieAdapter.OnItemClickCallback {
    private View root;
    private RecyclerView rvFavoriteMovie;
    private ListMovieAdapter listMovieAdapter;
    private ArrayList<Movie> list = new ArrayList<>();
    private MovieViewModel movieViewModel;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        rvFavoriteMovie = root.findViewById(R.id.rv_favorite_movie);
        rvFavoriteMovie.setHasFixedSize(true);

        list.addAll(MovieData.getListData());
        int listSize = list.size();
        Log.i("listsize: ",  String.valueOf(listSize));

        rvFavoriteMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        final ListMovieAdapter listMovieAdapter = new ListMovieAdapter(list);
        listMovieAdapter.notifyDataSetChanged();
        rvFavoriteMovie.setAdapter(listMovieAdapter);

        movieViewModel =
                ViewModelProviders.of(this).get(MovieViewModel.class);

        movieViewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies != null) {
                    listMovieAdapter.setData(movies);
//                    showLoading(false);
                }
            }
        });

        return root;
    }

    @Override
    public void onItemClicked(Movie movie) {

    }
}
