package com.renault.parkassist.viewmodel.apa

import android.app.Application
import androidx.lifecycle.LiveData
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.ManeuverType
import com.renault.parkassist.viewmodel.filter
import com.renault.parkassist.viewmodel.map
import org.koin.core.KoinComponent
import org.koin.core.inject

class ApaSettingsViewModel(app: Application) :
    ApaSettingsViewModelBase(app), KoinComponent {

    private val apaRepository: IApaRepository by inject()
    private val _supportedManeuvers = apaRepository.supportedManeuvers.filter {
        listOf(
            ManeuverType.PERPENDICULAR,
            ManeuverType.PARALLEL
        ).contains(it)
    }
    private val _defaultManeuver: LiveData<Int> = apaRepository.defaultManeuverType.filter {
        maneuvers.contains(it)
    }.map {
        when (it) {
            ManeuverType.PARALLEL -> Maneuver.PARALLEL
            else /* ManeuverType.PERPENDICULAR */ -> Maneuver.PERPENDICULAR
        }
    }

    override fun getManeuverSelectorVisible(): Boolean = maneuvers.isNotEmpty()

    override fun getDefaultManeuver(): LiveData<Int> = _defaultManeuver

    override fun getManeuvers(): List<Int> = _supportedManeuvers

    override fun setDefaultManeuver(@Maneuver maneuver: Int) {
        if (maneuvers.contains(maneuver)) {
            val maneuverType = when (maneuver) {
                Maneuver.PARALLEL -> ManeuverType.PARALLEL
                Maneuver.PERPENDICULAR -> ManeuverType.PERPENDICULAR
                else -> throw UnsupportedOperationException("Unsupported Maneuver $maneuver")
            }
            apaRepository.setDefaultManeuverType(maneuverType)
        }
    }
}