package com.renault.parkassist.viewmodel.apa.hfp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.Instruction
import com.renault.parkassist.repository.apa.ManeuverMove
import com.renault.parkassist.viewmodel.LiveDataUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class HfpGuidanceVehicleCenterBackViewModel(application: Application) :
    HfpGuidanceVehicleCenterBackViewModelBase(application),
    KoinComponent {

    private val apaRepository: IApaRepository by inject()

    private val engageBackActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        ((maneuverMove == ManeuverMove.BACKWARD) || (maneuverMove == ManeuverMove.FORWARD)) &&
            (instruction == Instruction.REVERSE)
    }

    private val engageBackNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        ((maneuverMove == ManeuverMove.BACKWARD) || (maneuverMove == ManeuverMove.FORWARD)) &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val engageBackVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            engageBackActiveVisible,
            engageBackNotActiveVisible
        )
    ) { (engageBackActiveVisible, engageBackNotActiveVisible) ->
        engageBackActiveVisible || engageBackNotActiveVisible
    }

    private val stopBackVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        ((maneuverMove == ManeuverMove.FIRST) || (maneuverMove == ManeuverMove.BACKWARD)) &&
            (instruction == Instruction.STOP)
    }

    override fun getEngageBackActiveVisible(): LiveData<Boolean> = engageBackActiveVisible

    override fun getEngageBackVisible(): LiveData<Boolean> = engageBackVisible

    override fun getStopBackVisible(): LiveData<Boolean> = stopBackVisible

    override fun getEngageBackNotActiveVisible(): LiveData<Boolean> = engageBackNotActiveVisible
}