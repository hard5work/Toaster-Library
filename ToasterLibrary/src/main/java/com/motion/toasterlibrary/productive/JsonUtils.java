package com.motion.toasterlibrary.productive;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtils {
    public static Gson gson;
    public static JsonParser parser;

    static {
        gson = new Gson();
        parser = new JsonParser();
    }

    public static String findIn(JsonObject req, String first) {
        for (String key : req.keySet())
            if (first.equals(key))
                return req.get(key).getAsString();

        return null;
    }
}
