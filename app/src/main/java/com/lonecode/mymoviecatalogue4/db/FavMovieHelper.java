package com.lonecode.mymoviecatalogue4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.lonecode.mymoviecatalogue4.db.DatabaseMovie.TABLE_NAME;

public class FavMovieHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static FavMovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private FavMovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FavMovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavMovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                DatabaseMovie.FavMovie._ID + " ASC");
    }

    public Cursor queryAllByCategory(String category) {
        return database.query(
                DATABASE_TABLE,
                null,
                DatabaseMovie.FavMovie.CATEGORY + " = ?",
                new String[]{category},
                null,
                null,
                 DatabaseMovie.FavMovie.DATECREATED + " DESC",
                null);
    }

    public Cursor queryByMovieId(String movieid) {
        return database.query(
                DATABASE_TABLE,
                null,
                DatabaseMovie.FavMovie.MOVIEID + " = ?",
                new String[]{movieid},
                null,
                null,
                null,
                null);
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, DatabaseMovie.FavMovie._ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, DatabaseMovie.FavMovie._ID + " = ?", new String[]{id});
    }

    public int deleteByMovieId(String id) {
        return database.delete(DATABASE_TABLE, DatabaseMovie.FavMovie.MOVIEID + " = ?", new String[]{id});
    }
}
