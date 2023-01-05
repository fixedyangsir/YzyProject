package com.yzy.lib_common.util

import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.Build
import androidx.annotation.Nullable
import com.yzy.lib_common.base.Ktx
import java.io.*


/**
 * Created by koo
 */
object FileUtils {
    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExist(file: File?): Boolean {
        if (file == null) {
            return false
        }
        return if (file.exists()) {
            true
        } else isFileExists(file.absolutePath)
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(filePath: String): Boolean {
        val file: File = getFileByPath(filePath) ?: return false
        return if (file.exists()) {
            true
        } else isFileExistsApi29(filePath)
    }

    /**
     * Android 10判断文件是否存在的方法
     *
     * @param filePath 文件路径
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    private fun isFileExistsApi29(filePath: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            var afd: AssetFileDescriptor? = null
            try {
                val uri: Uri = Uri.parse(filePath)
                afd = openAssetFileDescriptor(uri)
                afd?.let { closeIOQuietly(it) } ?: return false
            } catch (e: FileNotFoundException) {
                return false
            } finally {
                closeIOQuietly(afd)
            }
            return true
        }
        return false
    }

    /**
     * 从uri资源符中读取文件描述
     *
     * @param uri 文本资源符
     * @return AssetFileDescriptor
     */
    @Throws(FileNotFoundException::class)
    private fun openAssetFileDescriptor(uri: Uri): AssetFileDescriptor? {
        return Ktx.app.contentResolver.openAssetFileDescriptor(uri, "r")
    }


    /**
     * 安静关闭 IO
     *
     * @param closeables closeables
     */
    fun closeIOQuietly(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (ignored: IOException) {
                }
            }
        }
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    @Nullable
    private fun getFileByPath(filePath: String?): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) {
            return true
        }
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    /**
     * 删除文件
     * @param path 文件绝对路径
     */
    fun deleteFetusFile(path: String) {
        val file = File(path)
        if (file.exists() && file.isFile) {
            file.delete()
        }
    }

    fun clearDirFile(path: String) {
        val dirFile = File(path)
        if (dirFile.exists()) {
            val files: Array<File> = dirFile.listFiles()
            if (!files.isNullOrEmpty()) {
                for (file in files) {
                    if (file.exists() && file.isFile) {
                        file.delete()
                    }
                }
            }
        }
    }
}