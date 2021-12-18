package com.motion.toasterlibrary.productive;

import android.util.Log;

import com.google.gson.JsonObject;
import com.motion.toasterlibrary.classes.APIContracts;
import com.motion.toasterlibrary.interfaces.Urls;

import java.util.HashMap;
import java.util.Map;

public class UrlsContracts {

    public static String TAG = "UrlsContracts";

    public static Map<String, String> authHeaders = new HashMap<>();

    public static JsonObject authBody = new JsonObject();

    @Urls
    public static synchronized RequestMeta forName(String urlName) {
        RequestMeta meta = new RequestMeta();
        Log.e(TAG, "forName: " + urlName);
        if (APIContracts.APIName.User.Login.equals(urlName)) {
            meta.url = APIContracts.baseUrl + APIContracts.APIName.User.Login;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            meta.extraBody = null;
            meta.authHeader = null;
        } else if (APIContracts.APIName.User.test.equals(urlName)) {
            meta.url = APIContracts.baseUrl + APIContracts.APIName.User.test;
            meta.method = "GET";
            meta.appendPath = "";
            meta.authorization = false;
            meta.extraBody = null;
            meta.authHeader = null;
        }
        return meta;
    }


    public static synchronized RequestMeta dynamicName(String urlName, int method, boolean body, boolean header) {
        String methods ="";
        if (method==0){
            methods = "GET";
        } else {
            methods = "POST";
        }

        Map<String, String> authHeader = new HashMap<>();
        if (header)
            authHeader = authHeaders;
        else
            authHeader = null;

        JsonObject extraCustomerIdBody = new JsonObject();
        if (body)
            extraCustomerIdBody = authBody;
        else
            extraCustomerIdBody = null;

        RequestMeta meta = new RequestMeta();
        meta.url = APIContracts.baseUrl + urlName;
        meta.method = methods;
        meta.extraBody = null;
        meta.authHeader = authHeader;

        return meta;
    }

    public static void setHeader(String key, String value){
        authHeaders.put(key,value);
    }

    public static void setBody(String key , String value){
        authBody.addProperty(key,value);
    }


}
