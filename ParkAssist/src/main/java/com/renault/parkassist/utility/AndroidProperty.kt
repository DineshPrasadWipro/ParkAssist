package com.renault.parkassist.utility

import alliancex.arch.core.logger.logD

object AndroidProperty {

    private fun getSystemProperty(key: String): String? {
        var value: String? = null
        try {
            value = Class.forName("android.os.SystemProperties")
                .getMethod("get", String::class.java).invoke(null, key) as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        logD { "Read '$value' for property '$key'" }
        return value
    }

    fun getBooleanSystemProperty(key: String, def: Boolean): Boolean {
        val str: String? = getSystemProperty(key)
        var value = def
        if (str != null && str.isNotEmpty()) {
            value = str.toBoolean()
        }
        logD { "Read '$value' for boolean property '$key'" }
        return value
    }

    fun getIntSystemProperty(key: String, def: Int): Int {
        val str: String? = getSystemProperty(key)
        var value = def
        if (str != null && str.isNotEmpty()) {
            value = str.toInt()
        }
        logD { "Read '$value' for integer property '$key'" }
        return value
    }
}