package com.lonecode.mymoviecatalogue4.helper;

import android.database.Cursor;

import com.lonecode.mymoviecatalogue4.Movie;
import com.lonecode.mymoviecatalogue4.db.DatabaseMovie;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<Movie> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Movie> movieList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie._ID));
            String movieid = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.MOVIEID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.DESCRIPTION));
            String userScore = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.USERSCORE));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.RELEASEDATE));
            String originalLanguage = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.ORIGINALLANGUAGE));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.POSTERPATH));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.CATEGORY));

//            movieList.add(new Movie(0, movieid, name, description, userScore, releaseDate, originalLanguage, posterPath, category));
            movieList.add(new Movie(movieid, name, description, userScore, releaseDate, originalLanguage, posterPath, category));
        }

        return movieList;
    }

    public static Movie mapCursorToObject(Cursor cursor) {
        cursor.moveToFirst();
//        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie._ID));
        String movieid = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.MOVIEID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.NAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.DESCRIPTION));
        String userScore = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.USERSCORE));
        String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.RELEASEDATE));
        String originalLanguage = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.ORIGINALLANGUAGE));
        String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.POSTERPATH));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseMovie.FavMovie.CATEGORY));

        return new Movie(movieid, name, description, userScore, releaseDate, originalLanguage, posterPath, category);
    }
}
