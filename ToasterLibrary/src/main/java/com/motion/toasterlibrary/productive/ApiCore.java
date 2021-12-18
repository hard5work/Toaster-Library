package com.motion.toasterlibrary.productive;

import android.content.Context;
import android.util.Log;

import androidx.core.util.Pair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.motion.toasterlibrary.classes.UriContracts;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public class ApiCore {

    public static final String TAG ="ApiCore";

    @Deprecated
    public static Retrofit core;
    private static OkHttpClient client;


    private static Retrofit core2;
    private static OkHttpClient client2;
    private static GenericDefination request2;

    private static OkHttpClient client(Context context){
        if (client2== null){
            client2 = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new GenericRequestInterceptor())
                    .addInterceptor(new ReAuthInterceptor(context))
                    .build();
        }
        return client2;
    }
    private static Retrofit core(Context context){
        if (core2 == null){
            try{
               String baseUrl = (String) Reflect.getAPIContractVar(UriContracts.URI_API_CONTRACT,"baseUrl");
                core2 = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(client(context))
                        .addConverterFactory(GsonConverterFactory.create(JsonUtils.gson))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }catch (Exception e){
                Log.e(TAG, "core: init  " + e.getMessage() );
            }
        }
        return core2;
    }

    private static GenericDefination request(Context context){
        if (request2 ==null) request2 = core(context).create(GenericDefination.class);
        return request2;
    }

    public static  Observable<JsonObject> send(Context context,String urlName, JsonObject req){
        RequestMeta meta = getRequestMeta(urlName);
        return requestFactory(context,meta,req);
    }   public static  Observable<JsonObject> send(Context context,String urlName, String className, JsonObject req){
        RequestMeta meta = getRequestMeta(className,urlName);
        return requestFactory(context,meta,req);
    }

    private static Observable<JsonObject> requestFactory(Context context,RequestMeta meta, JsonObject req){
        Log.i(TAG, "send: url is " + meta.url);
        Map<String,String> headers = getRequestMetaHeaders(context,meta);
        Log.i(TAG, "send: headers are " + headers);

        if (meta.method.equals("POST")){
            putExtraBodyInto(context,meta,req);

            // TEST remove these before release
            Log.i(TAG, "send: body are " + req);

            return request(context).post(meta.url,headers,req).compose(RxUtils.defaultTransformers());

        }else{

            // TEST remove these before release
            Log.i(TAG, "send: queries are " + meta.queries);

            Map<String, String> params = getRequestMetaParams(meta,req);

            return  request(context).get(meta.url,params,headers)
                    .compose(RxUtils.defaultTransformers());
        }
    }

    private static Map<String,String> getRequestMetaParams(RequestMeta meta,JsonObject req){
        Map<String,String> queries = new HashMap<>();
        for (Pair<String,String> key : meta.queries){
            String value = JsonUtils.findIn(req,key.first);
            if (value != null) queries.put(key.second,value);
        }
        return queries;
    }

    private static void putExtraBodyInto(Context context, RequestMeta meta, JsonObject req){
        if (meta.extraBody != null)
            for (Map.Entry<String, JsonElement> stringJsonElementEntry : meta.extraBody.entrySet()) {
                req.add(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue());
            }
    }

    private static  Map<String, String> getRequestMetaHeaders(Context context, RequestMeta meta){
        Map<String,String> headers = reworkHeaderOnAuthEmpty(context,meta);
        if (meta.authHeader != null) headers.putAll(meta.authHeader);
        return headers;
    }

    private static HashMap<String, String> reworkHeaderOnAuthEmpty(Context context, RequestMeta meta){
        HashMap<String, String> map = new HashMap<>();
        if (meta.authHeader == null) return map;
        String auth = meta.authHeader.get("Authorization");
     /*   if (auth != null && !auth.isEmpty() && (auth.trim().toLowerCase().equals("bearer") || (UserCore.customerId == null || UserCore.customerId.isEmpty())) ) { // is useless
            // try to get from prefs
            UserPref.restoreUserPrefs(context);
            map.put("Authorization", "Bearer " + UserCore.accessToken);
            Log.w(TAG, "reworkHeaderOnAuthEmpty: " + map.toString());
            Log.w(TAG, "reworkHeaderOnAuthEmpty: " + UserCore.customerId);
        }*/
        return map;
    }

    private static RequestMeta getRequestMeta(String urlName){
        RequestMeta meta;
        try{
            meta = (RequestMeta) Reflect.invokeStaticeNoVerify(UriContracts.URI_URLSCONTRACTS,"Urls",urlName);
            if (meta== null) throw new Exception("Null meta; RequestMeta was probably not defined");
            return meta;
        }catch (Exception e){
            throw new Error("No RequestMeta with name " + urlName + " found. ");
        }
    }
    private static RequestMeta getRequestMeta(String urlName,String clazzName ){
        RequestMeta meta;
        try{
            meta = (RequestMeta) Reflect.invokeStaticeNoVerify(clazzName,"Urls",urlName);
            if (meta== null) throw new Exception("Null meta; RequestMeta was probably not defined");
            return meta;
        }catch (Exception e){
            throw new Error("No RequestMeta with name " + urlName + " found. ");
        }
    }

    public interface GenericDefination{
        @POST()
        Observable<JsonObject> post(@Url String url, @Body JsonObject request);

        @POST()
        Observable<JsonObject> post(@Url String url, @HeaderMap Map<String,String> headers, @Body JsonObject req);

        @GET("{path}")
        Observable<JsonObject> get(@Path("path") String path);

        @GET()
        Observable<JsonObject> get(@Url String url, @QueryMap Map<String,String> queryMap, @HeaderMap Map<String,String> headers);

        @GET
        Observable<ResponseBody> streamGet(@Url String url, @QueryMap Map<String ,String> queries, @HeaderMap Map<String,String> headers);

        @Multipart
        @POST()
        Observable<JsonObject> post(@Url String url,
                                    @HeaderMap Map<String,String> header,
                                    @PartMap Map<String, RequestBody> body,
                                    @Part List<MultipartBody.Part> files);


    }

}
