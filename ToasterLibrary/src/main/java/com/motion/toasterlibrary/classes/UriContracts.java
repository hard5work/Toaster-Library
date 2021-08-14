package com.motion.toasterlibrary.classes;

import com.motion.toasterlibrary.BuildConfig;

public class UriContracts {

    public static final String URI_API_CONTRACT;
    public static final String URI_URLSCONTRACTS;
    static {
        String uri = BuildConfig.LIBRARY_PACKAGE_NAME;

        URI_API_CONTRACT = uri + ".classes.APIContracts";
        URI_URLSCONTRACTS = uri + ".productive.UrlsContracts";
    }
}
