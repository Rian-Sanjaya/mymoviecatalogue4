package com.lonecode.mymoviecatalogue4.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseMovie {
    public static final String AUTHORITY = "com.lonecode.mymoviecatalogue4";
    private static final String SCHEME = "content";
    public static String TABLE_NAME = "fav_movie";

    private DatabaseMovie() {}

    public static final class FavMovie implements BaseColumns {
        public static final String MOVIEID = "movieid";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String USERSCORE = "userScore";
        public static final String RELEASEDATE = "releaseDate";
        public static final String ORIGINALLANGUAGE = "originalLanguage";
        public static final String POSTERPATH = "posterPath";
        public static final String CATEGORY = "category";
        public static final String DATECREATED = "datecreated";

        // untuk membuat URI content://com.lonecode.mymoviecatalogue4/fav_movie
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
