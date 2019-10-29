package com.lonecode.mymoviecatalogue4.ui.tvshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lonecode.mymoviecatalogue4.ListMovieAdapter;
import com.lonecode.mymoviecatalogue4.Movie;
import com.lonecode.mymoviecatalogue4.MovieDetailActivity;
import com.lonecode.mymoviecatalogue4.R;

import java.util.ArrayList;

public class TvShowFragment extends Fragment implements ListMovieAdapter.OnItemClickCallback {

    private TvShowViewModel tvshowViewModel;
    private ListMovieAdapter listMovieAdapter;
    private ProgressBar progressBar;
    private View root;

    private RecyclerView rvMovies;
    private ArrayList<Movie> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_tvshow, container, false);

        progressBar = root.findViewById(R.id.progressBarTv);

        rvMovies = root.findViewById(R.id.rv_tvshow);
        rvMovies.setHasFixedSize(true);

        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        listMovieAdapter = new ListMovieAdapter(getContext(), this);
        listMovieAdapter.notifyDataSetChanged();
        rvMovies.setAdapter(listMovieAdapter);

        tvshowViewModel =
                ViewModelProviders.of(this).get(TvShowViewModel.class);

        tvshowViewModel.setMovie();
        showLoading(true);

        tvshowViewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies != null) {
                    listMovieAdapter.setData(movies);
                    showLoading(false);
                }
            }
        });

        return root;
    }

    @Override
    public void onItemClicked(Movie movie) {
        Intent movieDetailIntent = new Intent(getContext(), MovieDetailActivity.class);
        movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_ACTIONBAR_TITLE, getString(R.string.tvshow_detail));
        movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_DETAIL, movie);
        startActivity(movieDetailIntent);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}