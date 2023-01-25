package com.renault.parkassist.routing.pursuit

import androidx.lifecycle.ViewModel
import com.renault.parkassist.routing.IDisplayManager
import com.renault.parkassist.routing.Pursuit
import org.koin.core.KoinComponent
import org.koin.core.inject

class PursuitViewModel : ViewModel(), KoinComponent {
    private val manager: IDisplayManager by inject()

    fun startManeuver() = manager.startPursuit(Pursuit.MANEUVER)
    fun startEasyPark() = manager.startPursuit(Pursuit.PARK)
    fun stop() = manager.stopCurrentPursuit()
}