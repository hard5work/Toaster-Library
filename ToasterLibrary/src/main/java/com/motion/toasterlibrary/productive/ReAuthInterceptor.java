package com.motion.toasterlibrary.productive;

import android.content.Context;

import com.google.gson.JsonObject;
import com.motion.toasterlibrary.productive.JsonUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class ReAuthInterceptor implements Interceptor {

    public interface Defination{
        @POST()
        Call<JsonObject> refresh(@Url String url, @Body JsonObject body);
    }

    private Context context;

    public ReAuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response res = chain.proceed(chain.request());
        Request req = chain.request();

        if (res.code() == 401 && !isRestricted(req.url().toString())){
            System.out.println("ReAuth: 401 is detected");
            Retrofit core = createRetrofitClient();
        }
        return res;
    }

    private boolean isRestricted(String url){
        return false;
    }

    private  Retrofit createRetrofitClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(3, TimeUnit.SECONDS)
                .build();
        Retrofit core = new Retrofit.Builder()
                .baseUrl("")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(JsonUtils.gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return core;
    }
}
