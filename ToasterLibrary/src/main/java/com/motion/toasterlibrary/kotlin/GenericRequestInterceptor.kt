package com.motion.toasterlibrary.kotlin

import com.auth0.android.jwt.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class GenericRequestInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (BuildConfig.DEBUG) {
            println("D INTERCEPT: " + request.url().url().toString())
            println("D Method " + request.method())
            println("D Headers " + request.headers())
            println("D Body " + request.body())
        }

        return chain.proceed(request)
    }

    companion object {
        const val TAG = "GenericRequestInterceptor"
    }
}