package es.miw.fem.rafa.ufoodbefree.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSingle {
    private static Gson gson;

    public static Gson getGsonParser() {
        if(null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }
}
