package com.lonecode.mymoviecatalogue4.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.lonecode.mymoviecatalogue4.BuildConfig;
import com.lonecode.mymoviecatalogue4.Movie;
import com.lonecode.mymoviecatalogue4.MovieDetailActivity;
import com.lonecode.mymoviecatalogue4.R;
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

import static com.lonecode.mymoviecatalogue4.helper.NetworkUtils.IMG_URL;

interface LoadNewMovieListCallback {
//    void preExecute();

    void postExecute(ArrayList<Movie> movie);
}

public class ReleaseReminder extends BroadcastReceiver implements LoadNewMovieListCallback {
    private int NOTIFICATION_ID = 201;
    private int STACK_NOTIF_ID = 2001;
    private static final int MAX_NOTIFICATION = 3;
    private ArrayList<Movie> listNewMovie = new ArrayList<>();
//    private final List<Movie> listNewMovie = new ArrayList<>();
    private int idNotification = 0;

    private Context thisContext;
//    private Intent thisIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        thisContext = context;
//        thisIntent = intent;
        getNewMovieRelease();
    }

    public void setMovieReleaseReminder(Context context) {
//        thisContext = context;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
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
        String API_KEY = BuildConfig.TMDB_API_KEY;

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
            sendNotification(listNewMovie);

        } else {
//            Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
            listNewMovie.clear();
        }
    }



    private void sendNotification(ArrayList<Movie> movie) {
        String CHANNEL_ID = "channel_2";
        String CHANNEL_NAME = "new realease movie channel";
        String GROUP_KEY_MOVIES = "group_key_moview";
        String STACK_CHANNEL_ID = "channel_21";
        String STACK_CHANNEL_NAME = "stack new release movie channel";

        NotificationManager notificationManagerCompat = (NotificationManager) thisContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder;

        if (movie.size() < MAX_NOTIFICATION) {

            for (int i = 0; i < movie.size(); i++) {
                Integer notifId = Integer.parseInt(movie.get(i).getMovieid());

                Intent intent = new  Intent(thisContext, MovieDetailActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(thisContext, notifId, intent, 0);

                builder = new NotificationCompat.Builder(thisContext, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_movie)
                        .setContentTitle("Movie Catalogue")
                        .setContentText("New movie " + movie.get(i).getName() + "is relaesed")
                        .setColor(ContextCompat.getColor(thisContext, android.R.color.transparent))
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setSound(alarmSound)
                        .setAutoCancel(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                            CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_HIGH);
                    channel.enableVibration(true);
                    channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
                    builder.setChannelId(CHANNEL_ID);
                    if (notificationManagerCompat != null) {
                        notificationManagerCompat.createNotificationChannel(channel);
                    }
                }

                Notification notification = builder.build();

                if (notificationManagerCompat != null) {
                    notificationManagerCompat.notify(notifId, notification);
                }
            }

        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .setBigContentTitle(movie.size() + " new movies release")
                    .setSummaryText("Movie Catalogue");

            for (int i = 0; i < movie.size(); i++) {
                inboxStyle.addLine(movie.get(i).getName());
            }

            Intent intent = new Intent(thisContext, MovieDetailActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(thisContext, STACK_NOTIF_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder = new NotificationCompat.Builder(thisContext, STACK_CHANNEL_NAME)
                    .setSmallIcon(R.drawable.ic_movie)
                    .setContentTitle(movie.size() + " new movies release")
                    .setContentIntent(pendingIntent)
                    .setGroup(GROUP_KEY_MOVIES)
                    .setGroupSummary(true)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(STACK_CHANNEL_ID,
                        STACK_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH);

                builder.setChannelId(STACK_CHANNEL_ID);

                if (notificationManagerCompat != null) {
                    notificationManagerCompat.createNotificationChannel(channel);
                }
            }

            Notification notification = builder.build();

            if (notificationManagerCompat != null) {
                notificationManagerCompat.notify(STACK_NOTIF_ID, notification);
            }
        }
    }
}
