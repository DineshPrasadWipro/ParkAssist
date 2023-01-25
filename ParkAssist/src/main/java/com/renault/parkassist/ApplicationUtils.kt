package com.renault.parkassist

import android.app.Application
import android.content.Context
import android.content.Intent

inline fun <reified T> Application.launchActivity() {
    startActivity(
        Intent(
            this,
            T::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}

// TODO: Update when RHD/LHD is re-enabled
//  ref CCSEXT-71793
val Context.isLhd
    get() = false //UxConfig.get(this).lhd