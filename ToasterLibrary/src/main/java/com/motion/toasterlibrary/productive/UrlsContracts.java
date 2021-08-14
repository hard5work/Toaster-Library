package com.motion.toasterlibrary.productive;

import android.util.Log;

import com.motion.toasterlibrary.classes.APIContracts;
import com.motion.toasterlibrary.interfaces.Urls;

public class UrlsContracts {

    public static String TAG = "UrlsContracts";
    @Urls
    public static synchronized RequestMeta forName(String urlName){
        RequestMeta meta = new RequestMeta();
        Log.e(TAG, "forName: " + urlName );
        if (APIContracts.APIName.User.Login.equals(urlName)) {
            meta.url = APIContracts.baseUrl + APIContracts.APIName.User.Login;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            meta.extraBody = null;
            meta.authHeader = null;
        }
        else if (APIContracts.APIName.User.test.equals(urlName)) {
            meta.url = APIContracts.baseUrl + APIContracts.APIName.User.test;
            meta.method = "GET";
            meta.appendPath = "";
            meta.authorization = false;
            meta.extraBody = null;
            meta.authHeader = null;
        }
        return meta;
    }
}
