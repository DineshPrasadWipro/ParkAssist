package com.renault.parkassist.viewmodel.apa.fapk

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.apa.DisplayState
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.ManeuverStartSwitch
import com.renault.parkassist.repository.apa.ManeuverType
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.ui.apa.AnticipatedManeuverButtons
import com.renault.parkassist.ui.apa.ManeuverButtonMapper
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.apa.ApaUtils
import com.renault.parkassist.viewmodel.filter
import com.renault.parkassist.viewmodel.map
import com.renault.parkassist.viewmodel.merge
import org.koin.core.KoinComponent
import org.koin.core.inject

class FapkViewModel(application: Application) : FapkViewModelBase(application),
    KoinComponent {

    private val apaRepository: IApaRepository by inject()
    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val anticipatedManeuverButtons = AnticipatedManeuverButtons()

    private val userManeuverStartSwitchSelected =
        MutableLiveData<Boolean>().apply { value = false }

    private val userManeuverStopSwitchSelected =
        MutableLiveData<Boolean>().apply { value = false }

    private val maneuverButtonMapper: ManeuverButtonMapper by inject()

    private val parallelManeuverSelected =
        anticipatedManeuverButtons.parallelSelected.merge(
            apaRepository.parallelManeuverSelection
                .map(maneuverButtonMapper::toSelected)
        )

    private val perpendicularManeuverSelected =
        anticipatedManeuverButtons.perpendicularSelected.merge(
            apaRepository.perpendicularManeuverSelection
                .map(maneuverButtonMapper::toSelected)
        )

    private val parkoutManeuverSelected =
        anticipatedManeuverButtons.parkoutSelected.merge(
            apaRepository.parkOutManeuverSelection
                .map(maneuverButtonMapper::toSelected)
        )

    private val parallelManeuverEnabled =
        LiveDataUtils.combineNonNull(
            apaRepository.parallelManeuverSelection,
            apaRepository.displayState
        ).map(maneuverButtonMapper::toEnabled)

    private val perpendicularManeuverEnabled =
        LiveDataUtils.combineNonNull(
            apaRepository.perpendicularManeuverSelection,
            apaRepository.displayState
        ).map(maneuverButtonMapper::toEnabled)

    private val parkoutManeuverEnabled =
        LiveDataUtils.combineNonNull(
            apaRepository.parkOutManeuverSelection,
            apaRepository.displayState
        ).map(maneuverButtonMapper::toEnabled)

    private val maneuverStartSwitchButtonVisible =
        Transformations.map(apaRepository.maneuverSwitchSelection) {
            (it != ManeuverStartSwitch.NONE) && (it != ManeuverStartSwitch.DISPLAY_CANCEL)
        }

    private val maneuverStopSwitchButtonVisible =
        Transformations.map(apaRepository.maneuverSwitchSelection) {
            it == ManeuverStartSwitch.DISPLAY_CANCEL
        }

    private val maneuverStartSwitchButtonEnabled =
        Transformations.map(apaRepository.maneuverSwitchSelection) {
            when (it) {
                ManeuverStartSwitch.DISPLAY_START,
                ManeuverStartSwitch.DISPLAY_START_AUTO_MODE,
                ManeuverStartSwitch.DISPLAY_START_PARALLEL,
                ManeuverStartSwitch.DISPLAY_START_PERPENDICULAR,
                ManeuverStartSwitch.DISPLAY_START_PARKOUT -> {
                    true
                }
                else -> false
            }
        }

    private val maneuverStartSwitchButtonSelected =
        apaRepository.maneuverSwitchSelection.map { false }
            .merge(userManeuverStartSwitchSelected)

    private val maneuverStopSwitchButtonSelected =
        apaRepository.maneuverSwitchSelection.map { false }
            .merge(userManeuverStopSwitchSelected)

    private val settingsVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.displayState,
            surroundRepository.surroundState.filter { !it.isRequest }
        )
    ) { (displayState, surroundState) ->
        (displayState != DisplayState.DISPLAY_GUIDANCE) &&
            (surroundState.view != View.APA_REAR_VIEW) &&
            maneuverParallelVisible && maneuverPerpendicularVisible
    }

    // Send Start Maneuver to repository
    override fun maneuverStart() {
        apaRepository.switchManeuverStartActivation()
        userManeuverStartSwitchSelected.postValue(true)
    }

    // Send Stop Maneuver to repository
    override fun maneuverStop() {
        apaRepository.switchManeuverStartActivation()
        userManeuverStopSwitchSelected.postValue(true)
    }

    override fun getInstruction(): LiveData<Int?> =
        Transformations.map(apaRepository.extendedInstruction) { instruction ->
            ApaUtils.getFakpInstructionResource(instruction)
        }

    override fun getManeuverStartSwitchButtonVisible(): LiveData<Boolean> =
        maneuverStartSwitchButtonVisible

    override fun getManeuverStartSwitchButtonEnabled(): LiveData<Boolean> =
        maneuverStartSwitchButtonEnabled

    override fun getManeuverStartSwitchButtonSelected(): LiveData<Boolean> =
        maneuverStartSwitchButtonSelected

    override fun getManeuverStopSwitchButtonSelected(): LiveData<Boolean> =
        maneuverStopSwitchButtonSelected

    override fun getManeuverStopSwitchButtonVisible(): LiveData<Boolean> =
        maneuverStopSwitchButtonVisible

    override fun getManeuverParallelButtonSelected(): LiveData<Boolean> =
        parallelManeuverSelected

    override fun getManeuverParallelButtonEnabled(): LiveData<Boolean> =
        parallelManeuverEnabled

    override fun getManeuverPerpendicularButtonSelected(): LiveData<Boolean> =
        perpendicularManeuverSelected

    override fun getManeuverPerpendicularButtonEnabled(): LiveData<Boolean> =
        perpendicularManeuverEnabled

    override fun getManeuverParkoutButtonSelected(): LiveData<Boolean> =
        parkoutManeuverSelected

    override fun getManeuverParallelVisible(): Boolean =
        apaRepository.supportedManeuvers.contains(ManeuverType.PARALLEL)

    override fun getManeuverPerpendicularVisible(): Boolean =
        apaRepository.supportedManeuvers.contains(ManeuverType.PERPENDICULAR)

    override fun getManeuverParkoutVisible(): Boolean =
        apaRepository.supportedManeuvers.contains(ManeuverType.PARKOUT)

    override fun getManeuverParkoutButtonEnabled(): LiveData<Boolean> =
        parkoutManeuverEnabled

    override fun getSettingsVisible(): LiveData<Boolean> = settingsVisible

    override fun setManeuver(@ManeuverType maneuverType: Int) {
        triggerAnticipatedSelection(maneuverType)
        apaRepository.requestManeuverType(maneuverType)
    }

    private fun triggerAnticipatedSelection(@ManeuverType maneuverType: Int) {
        when (maneuverType) {
            ManeuverType.PARALLEL -> {
                anticipatedManeuverButtons.parallelSelected.postValue(true)
                anticipatedManeuverButtons.perpendicularSelected.postValue(false)
                anticipatedManeuverButtons.parkoutSelected.postValue(false)
            }
            ManeuverType.PERPENDICULAR -> {
                anticipatedManeuverButtons.parallelSelected.postValue(false)
                anticipatedManeuverButtons.perpendicularSelected.postValue(true)
                anticipatedManeuverButtons.parkoutSelected.postValue(false)
            }
            ManeuverType.PARKOUT -> {
                anticipatedManeuverButtons.parallelSelected.postValue(false)
                anticipatedManeuverButtons.perpendicularSelected.postValue(false)
                anticipatedManeuverButtons.parkoutSelected.postValue(true)
            }
            else -> {
                errorLog(
                    "autopark",
                    "Unsupported maneuver mode received",
                    "discarding"
                )
            }
        }
    }
}