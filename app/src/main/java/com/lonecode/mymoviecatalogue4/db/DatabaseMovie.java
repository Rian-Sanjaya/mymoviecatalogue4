package com.lonecode.mymoviecatalogue4.db;

import android.provider.BaseColumns;

public class DatabaseMovie {
    public static String TABLE_NAME = "fav_movie";

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
    }
}
