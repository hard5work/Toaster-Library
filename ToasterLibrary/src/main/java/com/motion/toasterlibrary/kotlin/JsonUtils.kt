package com.motion.toasterlibrary.kotlin
import android.os.Bundle
import com.google.gson.*
import java.util.*
import kotlin.collections.ArrayList

object JsonUtils {
    var gson: Gson? = null
    var parser: JsonParser? = null
    fun optString(element: JsonElement): String? {
        return if (element.isJsonNull) null else element.asString
    }

    /**
     * Converts
     *
     * @return
     */
    fun toCamelCase(`object`: JsonObject): JsonObject {
        val obj = JsonObject()
        for (key in `object`.keySet()) {
            obj.add(toCamelCase(key), `object`[key])
        }
        return obj
    }

    fun toCamelCase(inputString: String): String {
        return inputString.substring(0, 1).lowercase(Locale.getDefault()) + inputString.substring(1)
    }

    fun safeString(element: JsonElement, orElse: String): String {
        return try {
            element.asString
        } catch (e: NullPointerException) {
            orElse
        } catch (e: UnsupportedOperationException) {
            orElse
        }
    }

    fun safeStringTypeTwo(element: String, orElse: String): String {
        return try {
            element
        } catch (e: NullPointerException) {
            orElse
        } catch (e: UnsupportedOperationException) {
            orElse
        }
    }

    fun safeBoolean(element: JsonElement, orElse: Boolean): Boolean {
        return try {
            element.asBoolean
        } catch (e: NullPointerException) {
            orElse
        } catch (e: UnsupportedOperationException) {
            orElse
        }
    }

    fun safeInt(element: JsonElement, orElse: Int): Int {
        return try {
            element.asInt
        } catch (e: NullPointerException) {
            orElse
        } catch (e: UnsupportedOperationException) {
            orElse
        } catch (e: NumberFormatException) {
            orElse
        }
    }

    fun safeFloat(element: JsonElement, orElse: Float): Float {
        return try {
            element.asFloat
        } catch (e: NullPointerException) {
            orElse
        } catch (e: UnsupportedOperationException) {
            orElse
        } catch (e: NumberFormatException) {
            orElse
        }
    }

    fun safeArray(element: JsonElement): JsonArray {
        return try {
            val parser = JsonParser()
            val data = element.asString
            parser.parse(data).asJsonArray
        } catch (e: Exception) {
            JsonArray()
        }
    }

    // TODO: 9/15/19 replace with a stream lib
    fun findIn(req: JsonObject, first: String): String? {
        for (key in req.keySet()) if (first == key) return req[key].asString
        return null
    }

    /**
     * Get only props that is available in props.
     * @param props
     * @param object
     * @return
     */
    fun filter(props: List<String>, `object`: JsonObject?): JsonObject {
        val res = JsonObject()
        for (prop in props) {
            val e = from(`object`, prop)
            val names = prop.split("\\.").toTypedArray()
            if (e != null) res.add(names[names.size - 1], e)
        }
        return res
    }

    /**
     * Searches deep inside an object using dot separated values.
     * @param obj
     * @param path dot separated key value like "apple.red"
     * @return
     */
    @Throws(JsonSyntaxException::class)
    fun from(obj: JsonObject?, path: String): JsonElement? {
        var obj = obj
        val seg = path.split("\\.").toTypedArray()
        for (element in seg) {
            obj = if (obj != null) {
                val ele = obj[element] ?: continue
                if (!ele.isJsonObject) return ele else ele.asJsonObject
            } else {
                return null
            }
        }
        return null
    }

    fun toList(asJsonArray: JsonArray): ArrayList<String> {
        val list = ArrayList<String>()
        for (element in asJsonArray) {
            list.add(element.asString)
        }
        return list
    }

    fun toJsonArray(rcPinsOps: ArrayList<String?>): JsonArray {
        val array = JsonArray()
        for (rcPinsOp in rcPinsOps) {
            array.add(rcPinsOp)
        }
        return array
    }

    /**
     * For now handles only string elements.
     * @param object
     * @return
     */
    fun toBundle(`object`: JsonObject): Bundle {
        val bundle = Bundle()
        for (key in `object`.keySet()) {
            bundle.putString(key, `object`[key].asString)
        }
        return bundle
    }

    init {
        gson = Gson()
        parser = JsonParser()
    }
}