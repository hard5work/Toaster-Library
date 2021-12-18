package com.motion.toasterlibrary.kotlin

import android.util.Log
import com.google.gson.JsonObject

class UrlsContracts {

    fun forNames(urlName: String, methods: Int, header: Boolean, body: Boolean): RequestMeta {

        Log.e("fornames", "forNames:  $urlName")
        var authHeader: MutableMap<String, String>? = HashMap()
        if (header)
            authHeader!!["Content-Type"] = "application/json" else authHeader = HashMap()

        var extraCustomerIdBody: JsonObject? = JsonObject()
        if (body) extraCustomerIdBody!!.addProperty("requestFromFlag", 0)
        else extraCustomerIdBody = null
        var method = ""
        method = if (methods == 0) {
            "GET"
        } else "POST"
        val meta = RequestMeta()
        meta.url = APIContracts.baseUrl + urlName
        meta.method = method
        meta.authHeader = authHeader
        meta.extraBody = extraCustomerIdBody

        Log.e("Fornames", "forNames: " + meta.url)
        return meta
    }


}