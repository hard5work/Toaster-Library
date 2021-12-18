package com.motion.toasterlibrary.kotlin


import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ApiCore {

    /**
    declares static object
    use companion object where static function is needed.

     */
    companion object {
        /** val datatype value
         *
         * cannot be changes and must be declared at initial
         * similar as final String TAG = "tag";
         * */
        val TAG = "APICore"

        /** var -> values can be changed similar to int L;
         * Retrofit? => declares that if there is no value for core return null
         * it return default value
         */
        var core: Retrofit? = null

        private var client: OkHttpClient? = null
        private var request: GenericDefinition? = null


        private fun client(context: Context): OkHttpClient?{
            if (client == null){
                client = OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(GenericRequestInterceptor())
                    .addInterceptor(ReauthInterceptor(context))
                    .build()
            }
            return client
        }

        private fun core (context: Context): Retrofit? {
            if(core == null){
                try{
                    val baseUrl = APIContracts.baseUrl!!
                    core = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(client(context)!!)
                        .addConverterFactory(GsonConverterFactory.create(JsonUtils.gson))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                }catch (e: Exception){
                    Log.e(TAG, "core: ", e )
                }
            }
            return core
        }

        private fun request(context: Context) :GenericDefinition? {
            if (request == null) request = core(context)!!.create(GenericDefinition::class.java)
            return request
        }

        /** Function to call methods */

        fun send(context: Context, urlName: String, req: JsonObject, method: Int,
                 header: Boolean, body:Boolean): Observable<JsonObject>{
            val meta: RequestMeta = getRequestMetaDynamic(urlName,method,header,body)
            return requestFactory(context,meta,req)
        }

        private fun getRequestMetaDynamic(
            urlName: String,
            method: Int,
            header: Boolean,
            body: Boolean,

        ):RequestMeta {
            val meta: RequestMeta
            return try{
                meta = UrlsContracts().forNames(urlName,method,header,body)
                if (meta == null) throw  Exception("Null meta; RequestMeta was probably not defined")
                meta
            }catch (e:Exception){
                throw Error("No RequestMeta with name $urlName found. ${e.message}")
            }

        }

        private fun requestFactory(context: Context,meta: RequestMeta, req: JsonObject):Observable<JsonObject>{
            val headers = getRequestMetaHeaders(context,meta)
            return if (meta.method.equals("POST")){
                request(context)!!.post(meta.url,headers,req)
                    .compose(RxUtils.defaultTransformers())
            }
            else {

                val params = getRequestMetaParams(meta, req)
                request(context)!!.get(meta.url,params,headers).compose(RxUtils.defaultTransformers())
            }
        }

        private fun getRequestMetaParams(meta: RequestMeta, req: JsonObject): Map<String, String> {
            // TODO: 10/20/19  redo this with
            val queries: MutableMap<String, String> = HashMap()
            for (key in meta.queries) {
                val value: String? = JsonUtils.findIn(req, key.first)
                if (value != null) queries[key.second] = value
            }
            return queries
        }



        private fun getRequestMetaHeaders(
            context: Context,
            meta: RequestMeta
        ): Map<String, String> {
            val headers: MutableMap<String, String> = reworkHeaderOnAuthEmpty(context, meta)
            if (meta.authHeader != null) headers.putAll(meta.authHeader!!)
            return headers
        }

        private fun reworkHeaderOnAuthEmpty(
            context: Context,
            meta: RequestMeta
        ): HashMap<String, String> {
            val map = HashMap<String, String>()
            val auth: String? = meta.authHeader!!["Authorization"]
            return map
        }


        /** This for declareing post get method to request data from the server */
        interface GenericDefinition {

            /** get/post method to connect to rest API */
            @POST
            fun post(@Url url: String?, @Body request: JsonObject?): Observable<JsonObject?>?
            /**        passes url value that can null */
            /** Passes JsonObject that can be null <> return type
             *  Observable<JsonObject?>? it can return null value*/

            @POST
            fun post(
                @Url url: String?,
                @HeaderMap headers: Map<String, String>?,
                @Body req: JsonObject?
            ): Observable<JsonObject?>

            /** Get method to direct path of the end URL*/
            @GET("{path}")
            operator fun get(@Path("path") path: String?): Observable<JsonObject?>?

            /** get Method to get queries  */
            @GET
            operator fun get(
                @Url url: String?,
                @QueryMap querymap: Map<String, String>?,
                @HeaderMap headers: Map<String, String>?
            ): Observable<JsonObject?>

            // TODO: 9/15/19 change this to get download ResponseBody
            @GET
            fun streamGet(
                @Url url: String?,
                @QueryMap queries: Map<String?, String?>?,
                @HeaderMap headers: Map<String?, String?>?
            ): Observable<ResponseBody?>?

            /** This method is used to publish photos in API using retrofit [multipart] -> no need to decode image
             * for uploading to database */
            @Multipart
            @POST
            fun post(
                @Url url: String?,
                @HeaderMap headers: Map<String?, String?>?,
                @PartMap body: Map<String?, RequestBody?>?,
                @Part files: List<MultipartBody.Part?>?
            ): Observable<JsonObject?>?

            @Multipart
            @POST
            fun posts(
                @Url url: String?,
                @HeaderMap headers: Map<String?, String?>?,
                @PartMap body: Map<String?, RequestBody?>?,
                @Part files: List<MultipartBody.Part?>?
            ): Observable<JsonObject?>?

        }

    }
}