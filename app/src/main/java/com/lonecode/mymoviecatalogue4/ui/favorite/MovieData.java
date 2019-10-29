package com.lonecode.mymoviecatalogue4.ui.favorite;

import com.lonecode.mymoviecatalogue4.Movie;

import java.util.ArrayList;

public class MovieData {
    public static String[][] data = new String[][] {
            {"Avenger", "Avenger Description", "https://upload.wikimedia.org/wikipedia/commons/8/87/Ahmad_Dahlan.jpg"},
            {"Spiderman", "Spiderman Description", "https://upload.wikimedia.org/wikipedia/commons/3/3f/Ahmad_Yani.jpg"}
    };

    public static ArrayList<Movie> getListData() {
        ArrayList<Movie> list = new ArrayList<>();

        for (String[] aData : data) {
            Movie movie = new Movie();
            movie.setName(aData[0]);
            movie.setDescription(aData[1]);
            movie.setPosterPath(aData[2]);
            list.add(movie);
        }

        return list;
    }
}
