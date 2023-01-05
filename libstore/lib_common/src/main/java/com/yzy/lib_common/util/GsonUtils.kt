package com.yzy.mvvmlib.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

/**
 * Created by yzy on 2020/12/16.
 */
object GsonUtils {
    val gson: Gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        GsonBuilder().disableHtmlEscaping().create()
    }

    fun toJson(any: Any?): String {
        return gson.toJson(any)
    }

    inline fun <reified T> jsonToBean(jsonString: String?): T? {
        var t: T? = null

        try {
            t = gson.fromJson(jsonString, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return t
    }

    inline fun <reified T> jsonToBean(reader: InputStreamReader): T? {
        var t: T? = null

        try {
            t = gson.fromJson(reader, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return t
    }

    inline fun <reified T> jsonToList(jsonString: String?): ArrayList<T>? {
        val list: ArrayList<T> = java.util.ArrayList()
        try {
            val jsonArray: JsonArray = JsonParser.parseString(jsonString).asJsonArray
            for (jsonElement in jsonArray) {
                list.add(gson.fromJson(jsonElement, T::class.java)) //cls
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }


    fun <T> jsonToListMaps(jsonString: String?): List<Map<String?, T?>?>? {

        var t: List<Map<String?, T?>?>? = null
        try {
            t = gson.fromJson(
                jsonString,
                object : TypeToken<List<Map<String?, T?>?>?>() {}.type
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return t
    }

    fun <T> jsonToMaps(jsonString: String?): LinkedTreeMap<String?, T?>? {

        var t: LinkedTreeMap<String?, T?>? = null
        try {
            t = gson.fromJson(jsonString, object : TypeToken<Map<String?, T?>?>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return t

    }

    fun mapToJson(params: Map<String, Any?>?): String? {
        return gson.toJson(params)
    }
    fun <T>  fromJson(json:String,clazz: Class<T>):T{
        return gson.fromJson(json,clazz)
    }




}

fun Any.toJson(): String {
    return GsonUtils.toJson(this)
}

inline fun <reified T> String?.jsonToList(): ArrayList<T>? {
    return GsonUtils.jsonToList(this)
}

inline fun <reified T> String.jsonToBean(): T? {
    return GsonUtils.jsonToBean(this)
}
inline fun <reified T> String.jsonToMap(): LinkedTreeMap<String?,T?>? {
    return GsonUtils.jsonToMaps<T>(this)
}