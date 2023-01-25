package com.renault.parkassist.utility

import android.content.res.Resources
import android.util.TypedValue

object AndroidResource {

    fun getFloatDimensionValue(resources: Resources, resId: Int): Float {
        val typedValue = TypedValue()
        resources.getValue(resId, typedValue, true)
        return typedValue.float
    }
}