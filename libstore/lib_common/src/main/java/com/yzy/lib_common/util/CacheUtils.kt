package com.yzy.lib_common.util


import com.tencent.mmkv.MMKV


object CacheUtils {
   private val prefs: MMKV by lazy {
        MMKV.defaultMMKV()!!
    }

    /**
     * 获取存放数据
     * @return 值
     */
    private fun getValue(key: String, default: Any): Any = with(prefs) {
        return when (default) {
            is Int -> decodeInt(key, default)
            is String -> decodeString(key, default).toString()
            is Long -> decodeLong(key, default)
            is Float -> decodeFloat(key, default)
            is Boolean -> decodeBool(key, default)
            else -> throw IllegalArgumentException("类型错误")
        }
    }

    fun getString(key: String, default: String = ""): String{
        return getValue(key, default) as String
    }

    fun getInt(key: String, default: Int = 0): Int {
        return getValue(key, default) as Int
    }

    fun getLong(key: String, default: Long = 0): Long {
        return getValue(key, default) as Long
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return getValue(key, default) as Boolean
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return getValue(key, default) as Float
    }

    /**
     * 存放SharedPreferences
     * @param key 键
     * @param value 值
     */
    fun saveValue(key: String, value: Any) = with(prefs) {
        when (value) {
            is Long -> encode(key, value)
            is Int -> encode(key, value)
            is String -> encode(key, value)
            is Float -> encode(key, value)
            is Boolean -> encode(key, value)
            else -> throw IllegalArgumentException("类型错误")
        }
    }

    /**
     * 清除
     */
    fun clear() {
        prefs.clearAll()
    }

    /**
     * 删除某Key的值
     */
    fun remove(key: String) {
        prefs.removeValueForKey(key)
    }
}