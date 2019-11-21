package com.lonecode.mymoviecatalogue4.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.lonecode.mymoviecatalogue4.Movie;
import com.lonecode.mymoviecatalogue4.R;
import com.lonecode.mymoviecatalogue4.db.FavMovieHelper;
import com.lonecode.mymoviecatalogue4.helper.MappingHelper;

import java.util.ArrayList;

import static com.lonecode.mymoviecatalogue4.db.DatabaseMovie.FavMovie.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Movie> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    private FavMovieHelper favMovieHelper;

    private static Cursor cursor;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.clear();

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
        Movie movie = mWidgetItems.get(position);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        try {
            Bitmap bitmap = Glide
                    .with(mContext)
                    .asBitmap()
                    .load(movie.getPosterPath())
                    .submit()
                    .get();

//            Bitmap bitmap = Picasso.get().load(movie.getPosterPath()).get();
//
            rv.setImageViewBitmap(R.id.imageView, bitmap);
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
