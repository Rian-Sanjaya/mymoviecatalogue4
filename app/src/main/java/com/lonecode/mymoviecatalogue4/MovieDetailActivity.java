package com.lonecode.mymoviecatalogue4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ACTIONBAR_TITLE = "extra_actionbar_title";
    public static final String EXTRA_MOVIE_DETAIL = "extra_movie_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE_DETAIL);

        ImageView imgDetailMovie = findViewById(R.id.img_detail_movie);
        Glide.with(this).load(movie.getPosterPath()).into(imgDetailMovie);

        TextView tvDetailMovieName = findViewById(R.id.tv_detail_movie_name);
        tvDetailMovieName.setText(movie.getName());

        TextView tvDetailMovieDescription = findViewById(R.id.tv_detail_movie_description);
        tvDetailMovieDescription.setText(movie.getDescription());

        TextView tvDetailUserScore = findViewById(R.id.tv_detail_userscore);
        tvDetailUserScore.setText(movie.getUserScore() + "%");

        TextView tvDetailReleaseDate = findViewById(R.id.tv_detail_release_date);
        tvDetailReleaseDate.setText(movie.getReleaseDate());

        TextView tvDetailOriginalLanguage = findViewById(R.id.tv_detail_original_language);
        switch (movie.getOriginalLanguage()) {
            case "en":
                tvDetailOriginalLanguage.setText("English");
                break;

            case "ko":
                tvDetailOriginalLanguage.setText("Korea");
                break;

            case "cn":
                tvDetailOriginalLanguage.setText("Chinese");
                break;

            case "ja":
                tvDetailOriginalLanguage.setText("Japan");
                break;

            default:
                tvDetailOriginalLanguage.setText(movie.getOriginalLanguage());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_ACTIONBAR_TITLE));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
