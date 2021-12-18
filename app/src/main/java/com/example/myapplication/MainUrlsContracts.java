package com.example.myapplication;

import android.util.Log;

import com.motion.toasterlibrary.classes.APIContracts;
import com.motion.toasterlibrary.interfaces.Urls;
import com.motion.toasterlibrary.productive.RequestMeta;

public class MainUrlsContracts {
    public static String TAG = "MainUrlsContracts";
    @Urls
    public static synchronized RequestMeta forName(String urlName){
        RequestMeta meta = new RequestMeta();
        Log.e(TAG, "forName: " + urlName );
        if (MainApiContacts.APIName.User.Login2.equals(urlName)) {
            meta.url = APIContracts.baseUrl + MainApiContacts.APIName.User.Login2;
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
