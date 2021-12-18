package com.motion.toasterlibrary.productive;

import android.content.Context;

import com.google.gson.JsonObject;
import com.motion.toasterlibrary.classes.APIContracts;

import io.reactivex.Observable;

public class RequestAPI {
    public static Observable<JsonObject> sendRequestPost(Context context, JsonObject req , String urlName, boolean header, boolean body) {
        return ApiCore.send(context, urlName, req, 1, header, body)
                .map(res -> res);
    }
    public static Observable<JsonObject> sendRequestGet(Context context, JsonObject req , String urlName, boolean header, boolean body) {
        return ApiCore.send(context, urlName, req, 0, header, body)
                .map(res -> res);
    }

}
