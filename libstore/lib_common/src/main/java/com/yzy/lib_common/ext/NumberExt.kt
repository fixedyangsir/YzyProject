package com.yzy.lib_common.ext


import java.math.BigDecimal
import java.math.BigInteger
import java.util.regex.Pattern


/**
 * 加法
 */
fun String.add(addNum: String, scale: Int = 8, round_mode: Int = BigDecimal.ROUND_HALF_UP,keepZero:Boolean=false): String {
    if (!isNumber() || !addNum.isNumber()) {
        return "0"
    }
    val b = toBigDecimal().add(addNum.toBigDecimal())
    if (keepZero){
        return b.setScale(scale, round_mode).toPlainString()
    }

    return b.setScale(scale, round_mode).stripTrailingZeros().toPlainString().trimZero()

}


/**
 * 减法
 */
fun String.subtract(
    subtractNum: String,
    scale: Int = 2,
    round_mode: Int = BigDecimal.ROUND_HALF_UP,
    keepZero:Boolean=false
): String {
    if (!isNumber() || !subtractNum.isNumber()) {
        return "0"
    }
    val b = toBigDecimal().subtract(subtractNum.toBigDecimal())

    if (keepZero){
        return b.setScale(scale, round_mode).toPlainString()
    }
    return b.setScale(scale, round_mode).stripTrailingZeros().toPlainString().trimZero()
}

/**
 *
 * 乘法
 */
fun String.multiply(
    multiplyNum: String,
    scale: Int? = 2,
    round_mode: Int = BigDecimal.ROUND_HALF_UP,
    keepZero:Boolean=false
): String {
    if (!isNumber() || !multiplyNum.isNumber()) {
        return "0"
    }
    scale?.let {
        val result = toBigDecimal().multiply(multiplyNum.toBigDecimal()).setScale(scale, round_mode)
        if (keepZero){
            return result.toPlainString()
        }

        return result.stripTrailingZeros().toPlainString().trimZero()
    }

    val b=toBigDecimal().multiply(multiplyNum.toBigDecimal())

    if (keepZero){
        return b.toPlainString()
    }

    return b.stripTrailingZeros().toPlainString()
        .trimZero()
}


fun String.multiplyPow(
    multiplyNum: String,
    scale: Int? = 2,
): String {
    if (!isNumber() || !multiplyNum.isNumber()) {
        return "0"
    }
    return multiply(BigDecimal.TEN.pow(multiplyNum.toInt()).toPlainString(), scale)

}

fun String.trimZero(): String {
    var value = this
    if (this.indexOf(".") > 0) {
// 去掉多余的0
        value = this.replace(Regex("0+?$"), "")
// 如最后一位是.则去掉
        value = value.replace(Regex("[.]$"), "")
    }
    return value
}

/**
 * 除法
 */
fun String.divide(
    divideNum: String,
    scale: Int = 2,
    round_mode: Int = BigDecimal.ROUND_HALF_UP,
    keepZero:Boolean=false
): String {
    if (!isNumber() || !divideNum.isNumber() || divideNum.toDouble() == 0.0) {
        return "0"
    }
    val b = toBigDecimal().divide(divideNum.toBigDecimal(), scale, round_mode)

    if (keepZero){
        return b.toPlainString()
    }

    return b.stripTrailingZeros().toPlainString().trimZero()
}


/**
 * 除法
 */
fun String.dividePow(
    divideNum: String,
    scale: Int = 2,
    round_mode: Int = BigDecimal.ROUND_HALF_UP,
): String {

    return divide(BigDecimal.TEN.pow(divideNum.toInt()).toPlainString(), scale, round_mode)

}


fun String.safeHexToBigInteger(): BigInteger {
    if (this.startsWith("0x")) {
        return this.substring(2).toBigInteger(16)
    }
    return toBigInteger(16)
}

fun String.showAmount(scale: Int = 8): String {
    if (!isNumber()) {
        return ""
    }
    return this.toBigDecimal().setScale(scale, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
        .toPlainString().trimZero()
}


/**
 * 保留小数
 */
fun String.formatSize(scale: Int = 2,trimZero:Boolean=true): String {
    if (!isNumber()) {
        return "0"
    }
    if (trimZero){
        return toBigDecimal().setScale(scale, BigDecimal.ROUND_HALF_UP)
            .stripTrailingZeros()
            .toPlainString().trimZero()
    }else{
        return toBigDecimal().setScale(scale, BigDecimal.ROUND_HALF_UP)
            .toPlainString()
    }

}


/**
 * 科学计数法
 * @receiver String
 * @return Boolean
 */
fun String.isENumber(): Boolean {
    val regx = "^[+-]?[\\d]+([.][\\d]*)?([Ee][+-]?[\\d]+)?\$"
    val pattern = Pattern.compile(regx)
    return pattern.matcher(this).matches()
}


fun String.multiplyZeros(
    multiplyNum: String,
    scale: Int? = 2,
    round_mode: Int = BigDecimal.ROUND_HALF_UP,
): String {
    if (!isNumber() || !multiplyNum.isNumber()) {
        return "0"
    }
    scale?.let {
        val result = toBigDecimal().multiply(multiplyNum.toBigDecimal()).setScale(scale, round_mode)
        return result.toPlainString()
    }
    return toBigDecimal().multiply(multiplyNum.toBigDecimal()).toPlainString()
}

fun String.toBigPainString(scale: Int = 2, big: Int = BigDecimal.ROUND_HALF_UP): String {
    try {
        //.stripTrailingZeros().
        return this.toBigDecimal().setScale(scale, big).toPlainString()
    } catch (e: Exception) {
        return this
    }
}





