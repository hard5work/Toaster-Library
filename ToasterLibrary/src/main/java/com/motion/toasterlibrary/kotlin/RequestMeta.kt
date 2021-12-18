package com.motion.toasterlibrary.kotlin
import android.util.Pair
import com.google.gson.JsonObject

class RequestMeta {
    var url: String? = null
    var method: String? = null
    var appendPath: String? = null
    var authorization = false
    var extraBody: JsonObject? = null

    /**
     * If authHeader is set to null then it would be a universal understanding that auth is not needed for this and thus
     * other auth handlers like ReAuthInterceptors will not try to re auth that given url.
     */
    var authHeader: Map<String, String>? = HashMap()
    var queries: List<Pair<String, String>> = ArrayList()
    var streaming = false

    constructor() {}
    constructor(
        url: String?,
        method: String?,
        appendPath: String?,
        authorization: Boolean,
        extraBody: JsonObject?
    ) {
        this.url = url
        this.method = method
        this.appendPath = appendPath
        this.authorization = authorization
        this.extraBody = extraBody
    }
}
