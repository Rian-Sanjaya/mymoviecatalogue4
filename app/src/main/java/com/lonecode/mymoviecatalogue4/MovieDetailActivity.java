package com.lonecode.mymoviecatalogue4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.lonecode.mymoviecatalogue4.db.DatabaseMovie;
import com.lonecode.mymoviecatalogue4.db.FavMovieHelper;
import com.lonecode.mymoviecatalogue4.helper.CompareDrawable;
import com.lonecode.mymoviecatalogue4.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

interface LoadFavMovieCallback {
    void preExecute();
    void postExecute(ArrayList<Movie> movie);
}

public class MovieDetailActivity extends AppCompatActivity implements LoadFavMovieCallback {
    public static final String EXTRA_ACTIONBAR_TITLE = "extra_actionbar_title";
    public static final String EXTRA_MOVIE_DETAIL = "extra_movie_detail";
    private static Movie movie;
    private FavMovieHelper favMovieHelper;
    private ArrayList<Movie> listFavMovie;
    private Menu favMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE_DETAIL);

        favMovieHelper = FavMovieHelper.getInstance(getApplicationContext());
        favMovieHelper.open();

        new LoadFavMovieAsync(favMovieHelper, this).execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_ACTIONBAR_TITLE));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showDetail() {
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
    }

    private void insertDb() {
        ContentValues values = new ContentValues();
        values.put(DatabaseMovie.FavMovie.MOVIEID, movie.getMovieid());
        values.put(DatabaseMovie.FavMovie.NAME, movie.getName());
        values.put(DatabaseMovie.FavMovie.DESCRIPTION, movie.getDescription());
        values.put(DatabaseMovie.FavMovie.CATEGORY, movie.getCategory());
        values.put(DatabaseMovie.FavMovie.ORIGINALLANGUAGE, movie.getOriginalLanguage());
        values.put(DatabaseMovie.FavMovie.POSTERPATH, movie.getPosterPath());
        values.put(DatabaseMovie.FavMovie.RELEASEDATE, movie.getReleaseDate());
        values.put(DatabaseMovie.FavMovie.USERSCORE, movie.getUserScore());
        values.put(DatabaseMovie.FavMovie.DATECREATED, getCurrentDate());

        long result = favMovieHelper.insert(values);

        if (result > 0) {
            if (favMenu != null) {
                favMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_home_black_24dp))  ;
            }
//           Toast.makeText(this, "Berhasil insert", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Gagal insert", Toast.LENGTH_LONG).show();
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                progressBar.setVisibility(View.VISIBLE);
                Log.i("Text preExecute", "Test preExecute");
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movie) {
        if (movie.size() > 0) {
            listFavMovie = movie;
//            Toast.makeText(this, "Fav Movie size: " + String.valueOf(listFavMovie.size()), Toast.LENGTH_LONG).show();
//            Toast.makeText(this, listFavMovie.get(0).getName(), Toast.LENGTH_LONG).show();

        } else {
//            Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
        }

        showDetail();
    }

    private static class LoadFavMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<FavMovieHelper> weakFavMovieHelper;
        private final WeakReference<LoadFavMovieCallback> weakCallback;

        private LoadFavMovieAsync(FavMovieHelper favMovieHelper, LoadFavMovieCallback callback) {
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
            Cursor dataCursor = weakFavMovieHelper.get().queryByMovieId(movie.getMovieid());
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movie) {
            super.onPostExecute(movie);
            weakCallback.get().postExecute(movie);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        favMovieHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);

        if (listFavMovie != null && listFavMovie.size() > 0) {
            if (menu.size() > 0) {
                this.favMenu = menu;
                menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_home_black_24dp));
            }
        } else {
            if (menu.size() > 0) {
                this.favMenu = menu;
                menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_dashboard_black_24dp));
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.i("menu id: ", item.getTitle().toString());
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_favorite:
                if (listFavMovie != null && listFavMovie.size() > 0) {
                    long result = favMovieHelper.deleteByMovieId(listFavMovie.get(0).getMovieid());

                } else {
                    insertDb();
                }

//                if (favMenu != null) {
//                    if (CompareDrawable.areDrawablesIdentical(favMenu.getItem(0).getIcon(), this.getResources().getDrawable(R.drawable.ic_home_black_24dp))) {
//                        long result = favMovieHelper.deleteByMovieId(listFavMovie.get(0).getMovieid());
//
//                        if (result > 0) {
//                            favMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_dashboard_black_24dp))  ;
//                            Toast.makeText(this, "Movie " + listFavMovie.get(0).getName() + " dihapus", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_LONG).show();
//                        }
//
//                    } else {
//                        insertDb();
//                    }
//                }

//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(getIntent());
//                overridePendingTransition(0, 0);

                recreate();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
