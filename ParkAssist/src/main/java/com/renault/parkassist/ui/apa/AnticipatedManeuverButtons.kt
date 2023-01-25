package com.renault.parkassist.ui.apa

import androidx.lifecycle.MutableLiveData
import org.koin.core.KoinComponent

class AnticipatedManeuverButtons : KoinComponent {
    val parallelSelected =
        MutableLiveData<Boolean>().apply { value = false }
    val perpendicularSelected =
        MutableLiveData<Boolean>().apply { value = false }
    val parkoutSelected =
        MutableLiveData<Boolean>().apply { value = false }
}