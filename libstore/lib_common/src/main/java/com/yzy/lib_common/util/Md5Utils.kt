package com.yzy.lib_common.util

import android.content.pm.PackageManager
import com.yzy.lib_common.base.Ktx
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateFactory


object Md5Utils {
    fun md5(plainText: String): String {
        var secretBytes: ByteArray? = null
        secretBytes = try {
            MessageDigest.getInstance("md5").digest(
                plainText.toByteArray()
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("There is no md5 algorithm！")
        }
        var md5code: String = BigInteger(1, secretBytes).toString(16)
        for (i in 0 until 32 - md5code.length) {
            md5code = "0$md5code"
        }
        return md5code
    }

    /**
     * 32位MD5加密的大写字符串
     * @param s
     * @return
     */
    fun MD5(s: String): String {
        val hexDigits = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
        )
        return try {
            val btInput = s.toByteArray()
            // 获得MD5摘要算法的 MessageDigest 对象
            val mdInst = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            mdInst.update(btInput)
            // 获得密文
            val md = mdInst.digest()
            // 把密文转换成十六进制的字符串形式
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = md[i]
                str[k++] = hexDigits[byte0.toInt() ushr 4 and 0xf]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            String(str)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    fun getFileMD5(file: File?): String? {
        if (!FileUtils.isFileExist(file)) {
            return ""
        }
        var fis: InputStream? = null
        try {
            val digest = MessageDigest.getInstance("MD5")
            fis = FileInputStream(file)
            val buffer = ByteArray(8192)
            var len: Int
            while (fis.read(buffer).also { len = it } != -1) {
                digest.update(buffer, 0, len)
            }
            return bytes2Hex(digest.digest())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            FileUtils.closeIOQuietly(fis)
        }
        return ""
    }

    /**
     * 一个byte转为2个hex字符
     *
     * @param src byte数组
     * @return 16进制大写字符串
     */
    private fun bytes2Hex(src: ByteArray): String? {
        val res = CharArray(src.size shl 1)
        val hexDigits = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F'
        )
        var i = 0
        var j = 0
        while (i < src.size) {
            res[j++] = hexDigits[src[i].toInt() ushr 4 and 0x0F]
            res[j++] = hexDigits[src[i].toInt() and 0x0F]
            i++
        }
        return String(res)
    }


    fun getSignMd5Str(): String? {
        try {

            val packageManager = Ktx.app.packageManager;
            val packageInfo = packageManager.getPackageInfo(
                Ktx.app.getPackageName(),
                PackageManager.GET_SIGNATURES
            );
            val signs = packageInfo.signatures;
            val sign = signs[0];
            val certFactory = CertificateFactory.getInstance("X.509")
            val cert = certFactory.generateCertificate(ByteArrayInputStream(sign.toByteArray()))
            val md = MessageDigest.getInstance("SHA1");
            val b = md.digest(cert.getEncoded());
            return byte2HexFormatted(b);
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return null

    }


    private fun byte2HexFormatted(arr: ByteArray): String {
        val str = StringBuilder(arr.size * 2);
        arr.forEachIndexed { index, c ->
            var h = Integer.toHexString(c.toInt());
            val l = h.length
            if (l == 1)
                h = "0$h";
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (index < (arr.size - 1))
                str.append(':');
        }
        return str.toString();

    }
}