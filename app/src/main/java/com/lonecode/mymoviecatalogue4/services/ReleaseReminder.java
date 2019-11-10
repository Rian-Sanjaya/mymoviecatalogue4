package com.lonecode.mymoviecatalogue4.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.lonecode.mymoviecatalogue4.Movie;
import com.lonecode.mymoviecatalogue4.SearchMovieActivity;
import com.lonecode.mymoviecatalogue4.helper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

interface LoadNewMovieListCallback {
//    void preExecute();

    void postExecute(ArrayList<Movie> movie);
}

public class ReleaseReminder extends BroadcastReceiver implements LoadNewMovieListCallback {
    private int NOTIFICATION_ID = 201;
    private ArrayList<Movie> listNewMovie = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        getNewMovieRelease();
    }

    public void setMovieReleaseReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 36);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        if (alarmManager != null) {
            // akan menjalankan obyek PendingIntent pada setiap waktu yang ditentukan dalam millissecond dengan interval per hari.
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelMovieReleaseReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void getNewMovieRelease() {
        String API_KEY = "e4621b68dcd1fa1de4a66cfd0664dc28";

        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());
        Date date = new Date();

        Uri buildUri = Uri.parse("https://api.themoviedb.org/3/discover/movie").buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("primary_release_date.gte", df.format(date))
                .appendQueryParameter("primary_release_date.lte", df.format(date))
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
            new getJson(this).execute(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class getJson extends AsyncTask<URL, Void, ArrayList<Movie>> {
        private final WeakReference<LoadNewMovieListCallback> weakCallback;

        private getJson(LoadNewMovieListCallback callback) {
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL newURL = urls[0];
            String result = null;
            ArrayList<Movie> listMovies = new ArrayList<>();
            String IMG_URL = "https://image.tmdb.org/t/p/w500/";

            try {
                result = NetworkUtils.getResponseFromHttpUrl(newURL);
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

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movie) {
            super.onPostExecute(movie);
            weakCallback.get().postExecute(movie);
        }
    }

//    @Override
//    public void preExecute() {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//    }

    @Override
    public void postExecute(ArrayList<Movie> movie) {
        Log.i("moviesize: ", String.valueOf(movie.size()));
        if (movie != null && movie.size() > 0) {
            listNewMovie.clear();
            listNewMovie.addAll(movie);


        } else {
//            Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
            listNewMovie.clear();
        }
    }
}
