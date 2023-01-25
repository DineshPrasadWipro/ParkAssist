package com.renault.parkassist.utility

object Conversion {
    fun rangeToPercentage(value: Int, min: Int, max: Int): Int {
        // Represents the range amplitude
        val amp = (max - min)

        return (((value - min).toFloat() * 100 / amp) + 0.5).toInt()
    }

    fun percentageToRange(value: Int, min: Int, max: Int): Int {
        // Represents the range amplitude
        val amp = (max - min)

        return (value.toFloat() * amp / 100 + 0.5).toInt() + min
    }

    fun percentageToRangeToPercentage(value: Int, min: Int, max: Int): Int {
        // Represents the range amplitude
        val amp = (max - min)

        return ((value.toFloat() * amp / 100 + 0.5).toInt().toFloat() * 100 / amp + 0.5).toInt()
    }
}