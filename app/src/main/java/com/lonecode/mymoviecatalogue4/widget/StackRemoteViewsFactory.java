package com.lonecode.mymoviecatalogue4.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.lonecode.mymoviecatalogue4.Movie;
import com.lonecode.mymoviecatalogue4.R;
import com.lonecode.mymoviecatalogue4.db.FavMovieHelper;
import com.lonecode.mymoviecatalogue4.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.lonecode.mymoviecatalogue4.db.DatabaseMovie.FavMovie.CONTENT_URI;

interface LoadListFavMovieCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movie);
}

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, LoadListFavMovieCallback {

    private ArrayList<Movie> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    private FavMovieHelper favMovieHelper;

    private static Cursor cursor;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    private void getData() {
//        favMovieHelper = FavMovieHelper.getInstance(mContext);
//        favMovieHelper.open();

//        new LoadFavMovieAsync(favMovieHelper, this).execute();

        new LoadFavMovieAsync(mContext, this).execute();
    }

    private static class LoadFavMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
//        private final WeakReference<FavMovieHelper> weakFavMovieHelper;
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadListFavMovieCallback> weakCallback;

//        private LoadFavMovieAsync(FavMovieHelper favMovieHelper, LoadListFavMovieCallback callback) {
//            weakFavMovieHelper = new WeakReference<>(favMovieHelper);
//            weakCallback = new WeakReference<>(callback);
//        }

        private LoadFavMovieAsync(Context context, LoadListFavMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            weakCallback.get().preExecute();
        }

//        @Override
//        protected ArrayList<Movie> doInBackground(Void... voids) {
//            Cursor dataCursor = weakFavMovieHelper.get().queryAllByCategory("movie");
//            return MappingHelper.mapCursorToArrayList(dataCursor);
//        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            if (cursor != null) {
                cursor.close();
            }

            final long identityToken = Binder.clearCallingIdentity();

            Uri uriWithCategory = Uri.parse(CONTENT_URI + "/category/movie");
            Context context = weakContext.get();
            cursor = context.getContentResolver().query(uriWithCategory, null, null, null, null);

            Binder.restoreCallingIdentity(identityToken);

            return MappingHelper.mapCursorToArrayList(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movie) {
            super.onPostExecute(movie);
            weakCallback.get().postExecute(movie);
        }
    }

    @Override
    public void preExecute() {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("Text preExecute", "Test preExecute");
//            }
//        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movie) {
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
            mWidgetItems.add(movieItems);
        }

        int listSize = mWidgetItems.size();
        Log.i("listsize: ", String.valueOf(listSize));
    }

    @Override
    public void onCreate() {

//        getData();
    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.clear();
//        getData();

        if (cursor != null) {
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        Uri uriWithCategory = Uri.parse(CONTENT_URI + "/category/movie");
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        mWidgetItems = MappingHelper.mapCursorToArrayList(cursor);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

//        favMovieHelper.close();
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String IMG_URL = "https://image.tmdb.org/t/p/w500";

        Movie movie = mWidgetItems.get(position);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        try {
//            Bitmap bitmap = Glide
//                    .with(mContext)
//                    .asBitmap()
//                    .load(movie.getPosterPath())
//
//                    .submit()
//                    .get();

//            Bitmap bitmap = Picasso.get().load(movie.getPosterPath()).get();
//
//            rv.setImageViewBitmap(R.id.imageView, bitmap);
            rv.setTextViewText(R.id.tvMovieTitle, movie.getName());

        } catch (Exception e) {
            Log.e("get bitmap error", e.getMessage());
        }

        Bundle extras = new Bundle();
        extras.putInt(ImageBannerWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
