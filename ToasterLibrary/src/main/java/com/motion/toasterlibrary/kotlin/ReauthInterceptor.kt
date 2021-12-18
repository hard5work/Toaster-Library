package com.motion.toasterlibrary.kotlin


import android.content.Context
import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import java.io.IOException


class ReauthInterceptor
/**
 * @param context application context
 */(private val context: Context) : Interceptor {
    private interface Definition {
        @POST
        fun refresh(@Url url: String?, @Body body: JsonObject?): Call<JsonObject?>?
    }

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val res = chain.proceed(chain.request())
        val req = chain.request()
        // handle 401 case
        if (res.code() == 401) {
            try {
//                val jes = JSONObject(res.body()!!.string())
                throw IOException(res.body()!!.string())
            } catch (e: JSONException) {
                e.printStackTrace()
                throw IOException("Unexpected Error Occured ")
            }
            //            throw  new IOException(res.body().string());
        }
        return res
    }



}