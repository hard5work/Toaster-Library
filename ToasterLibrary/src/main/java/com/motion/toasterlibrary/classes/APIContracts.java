package com.motion.toasterlibrary.classes;

import com.motion.toasterlibrary.interfaces.IBaseAPIContract;

@IBaseAPIContract
public class APIContracts {

    public static String baseUrl;

    public void BaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public static class APIName{

        public static class User {
            public static final String Login = "Token/RequestTokenForCustomer";
            public static final String test = "entries";
        }
    }
}
