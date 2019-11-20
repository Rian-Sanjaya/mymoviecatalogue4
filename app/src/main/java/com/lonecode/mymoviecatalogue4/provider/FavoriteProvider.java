package com.lonecode.mymoviecatalogue4.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.lonecode.mymoviecatalogue4.db.FavMovieHelper;

import static com.lonecode.mymoviecatalogue4.db.DatabaseMovie.AUTHORITY;
import static com.lonecode.mymoviecatalogue4.db.DatabaseMovie.FavMovie.CONTENT_URI;
import static com.lonecode.mymoviecatalogue4.db.DatabaseMovie.TABLE_NAME;

public class FavoriteProvider extends ContentProvider {

    private static final int FAV = 1;
    private static final int FAV_ID = 2;;
    private static final int FAV_CATEGORY = 3;
    private FavMovieHelper favMovieHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        // content://com.lonecode.mymoviecatalogue4/fav_movie
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV);

        // content://com.dicoding.picodiploma.mynotesapp/fav_movie/id
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                FAV_ID);

        // content://com.dicoding.picodiploma.mynotesapp/fav_movie/category/catname
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/category/*",
                FAV_CATEGORY);
    }

    public FavoriteProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        favMovieHelper = FavMovieHelper.getInstance(getContext());
        favMovieHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
//        throw new UnsupportedOperationException("Not yet implemented");

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case FAV:
                cursor = favMovieHelper.queryAll();
                break;

            case FAV_ID:
                cursor = favMovieHelper.queryByMovieId(uri.getLastPathSegment());
                break;

            case FAV_CATEGORY:
                cursor = favMovieHelper.queryAllByCategory(uri.getLastPathSegment());
                break;

            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
//        throw new UnsupportedOperationException("Not yet implemented");

        long added;

        switch (sUriMatcher.match(uri)) {
            case FAV:
                added = favMovieHelper.insert(values);
                break;

            default:
                added = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
//        throw new UnsupportedOperationException("Not yet implemented");

        int deleted;

        switch (sUriMatcher.match(uri)) {
            case FAV_ID:
                deleted = favMovieHelper.deleteByMovieId(uri.getLastPathSegment());
                break;

            default:
                deleted = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return deleted;
    }
}
