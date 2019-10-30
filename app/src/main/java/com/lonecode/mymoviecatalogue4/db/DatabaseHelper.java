package com.lonecode.mymoviecatalogue4.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbmoviecatalogue";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_FAV_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseMovie.TABLE_NAME,
            DatabaseMovie.FavMovie._ID,
            DatabaseMovie.FavMovie.MOVIEID,
            DatabaseMovie.FavMovie.NAME,
            DatabaseMovie.FavMovie.DESCRIPTION,
            DatabaseMovie.FavMovie.CATEGORY,
            DatabaseMovie.FavMovie.ORIGINALLANGUAGE,
            DatabaseMovie.FavMovie.POSTERPATH,
            DatabaseMovie.FavMovie.RELEASEDATE,
            DatabaseMovie.FavMovie.USERSCORE,
            DatabaseMovie.FavMovie.DATECREATED
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAV_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseMovie.TABLE_NAME);
        onCreate(db);
    }
}
