package com.renault.parkassist.viewmodel.apa.hfp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.Instruction
import com.renault.parkassist.repository.apa.ManeuverMove
import com.renault.parkassist.repository.apa.ScanningSide
import com.renault.parkassist.viewmodel.LiveDataUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class HfpGuidanceVehicleCenterCutViewModel(application: Application) :
    HfpGuidanceVehicleCenterCutViewModelBase(application),
    KoinComponent {

    private val apaRepository: IApaRepository by inject()

    private val firstReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        (maneuverMove == ManeuverMove.FIRST) && (instruction == Instruction.REVERSE)
    }

    private val firstSelectReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        (maneuverMove == ManeuverMove.FIRST) &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val engageLeftActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            firstReverse
        )
    ) { (scanningSide, firstReverse) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_LEFT) && firstReverse
    }

    private val engageLeftNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            firstSelectReverse
        )
    ) { (scanningSide, firstSelectReverse) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_LEFT) && firstSelectReverse
    }

    private val engageRightActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            firstReverse
        )
    ) { (scanningSide, firstReverse) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_RIGHT) && firstReverse
    }

    private val engageRightNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            firstSelectReverse
        )
    ) { (scanningSide, firstSelectReverse) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_RIGHT) && firstSelectReverse
    }

    private val engageRight = Transformations.map(
        LiveDataUtils.combineNonNull(
            engageRightActiveVisible,
            engageRightNotActiveVisible
        )
    ) { (engageRightActiveVisible, engageRightNotActiveVisible) ->
        engageRightActiveVisible || engageRightNotActiveVisible
    }

    private val engageLeft = Transformations.map(
        LiveDataUtils.combineNonNull(
            engageLeftActiveVisible,
            engageLeftNotActiveVisible
        )
    ) { (engageLeftActiveVisible, engageLeftNotActiveVisible) ->
        engageLeftActiveVisible || engageLeftNotActiveVisible
    }

    private val arrowCurveDownVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            engageRight,
            engageLeft
        )
    ) { (engageRight, engageLeft) -> engageRight || engageLeft
    }

    override fun getEngageLeftNotActiveVisible(): LiveData<Boolean> = engageLeftNotActiveVisible

    override fun getEngageLeftActiveVisible(): LiveData<Boolean> = engageLeftActiveVisible

    override fun getEngageRightActiveVisible(): LiveData<Boolean> = engageRightActiveVisible

    override fun getEngageRightNotActiveVisible(): LiveData<Boolean> = engageRightNotActiveVisible

    override fun getArrowCurveDownVisible(): LiveData<Boolean> = arrowCurveDownVisible
}