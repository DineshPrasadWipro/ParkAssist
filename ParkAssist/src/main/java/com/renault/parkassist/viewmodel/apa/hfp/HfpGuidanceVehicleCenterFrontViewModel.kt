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

class HfpGuidanceVehicleCenterFrontViewModel(application: Application) :
    HfpGuidanceVehicleCenterFrontViewModelBase(application),
    KoinComponent {

    private val apaRepository: IApaRepository by inject()

    private val maneuverMoveSet = Transformations.map(apaRepository.maneuverMove) { move ->
        when (move) {
            ManeuverMove.FIRST, ManeuverMove.BACKWARD, ManeuverMove.FORWARD -> true
            else -> false
        }
    }

    private val engageFrontActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, instruction) ->
        maneuverMoveSet && (instruction == Instruction.DRIVE_FORWARD)
    }

    private val engageFrontNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, instruction) ->
        maneuverMoveSet && (instruction == Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)
    }

    private val stopFrontVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        (maneuverMove == ManeuverMove.FORWARD) && (instruction == Instruction.STOP)
    }

    private val arrowStraightUpVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            engageFrontActiveVisible,
            engageFrontNotActiveVisible
        )
    ) { (engageFrontActiveVisible, engageFrontNotActiveVisible) ->
        engageFrontActiveVisible || engageFrontNotActiveVisible
    }

    override fun getEngageFrontActiveVisible(): LiveData<Boolean> = engageFrontActiveVisible

    override fun getStopFrontVisible(): LiveData<Boolean> = stopFrontVisible

    override fun getEngageFrontNotActiveVisible(): LiveData<Boolean> = engageFrontNotActiveVisible

    override fun getArrowStraightUpVisible(): LiveData<Boolean> = arrowStraightUpVisible
}