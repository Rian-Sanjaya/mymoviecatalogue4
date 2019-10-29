package com.lonecode.mymoviecatalogue4;

public class Globals {
    private static Globals instance;

    private String language = "en";

    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setLanguage(String l) {
        this.language = l;
    }

    public String getLanguage() {
        return this.language;
    }

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }

        return instance;
    }
}
