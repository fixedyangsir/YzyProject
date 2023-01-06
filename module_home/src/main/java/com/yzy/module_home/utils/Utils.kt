package com.yzy.module_home.utils

import android.content.Context
import android.content.res.AssetManager
import androidx.annotation.NonNull
import org.json.JSONException
import org.json.JSONObject
import java.io.*


/**
 * Created by yzy on 2023/1/6.
 */
object Utils {
    /**
     * 获取assets目录下文件的字节数组
     *
     * @param context  上下文
     * @param fileName assets目录下的文件
     * @return assets目录下文件的字节数组
     */
    fun getAssetsFile(context: Context, fileName: String?): ByteArray? {
        val inputStream: InputStream
        val assetManager: AssetManager = context.getAssets()
        try {
            inputStream = assetManager.open(fileName!!)
            var bis: BufferedInputStream? = null
            val length: Int
            try {
                bis = BufferedInputStream(inputStream)
                length = bis.available()
                val data = ByteArray(length)
                bis.read(data)
                return data
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (bis != null) {
                    try {
                        bis.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return null
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取assets目录下json文件的数据
     *
     * @param context 上下文
     * @param name    assets目录下的json文件名
     * @return json数据对象
     */
    fun getJSONDataFromAsset(@NonNull context: Context, name: String?): JSONObject? {
        try {
            val inputStream = context.assets.open(name!!)
            val inputStreamReader = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuilder()
            var str: String?
            while (inputStreamReader.readLine().also { str = it } != null) {
                sb.append(str)
            }
            inputStreamReader.close()
            return JSONObject(sb.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }
}