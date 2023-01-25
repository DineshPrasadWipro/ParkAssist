package com.renault.parkassist.viewmodel.apa.hfp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.apa.*
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.filterNull
import org.koin.core.KoinComponent
import org.koin.core.inject

class HfpGuidanceVehicleCenterViewModel(application: Application) :
    HfpGuidanceVehicleCenterViewModelBase(application),
    KoinComponent {

    private val apaRepository: IApaRepository by inject()

    private val maneuverMoveSet = Transformations.map(apaRepository.maneuverMove) { move ->
        when (move) {
            ManeuverMove.FIRST, ManeuverMove.BACKWARD, ManeuverMove.FORWARD -> true
            else -> false
        }
    }

    private val parallelManeuverSelected =
        Transformations.map(apaRepository.parallelManeuverSelection) {
            when (it) {
                ManeuverSelection.SELECTED -> true
                ManeuverSelection.NOT_SELECTED -> false
                else -> false
            }
        }

    private val perpendicularManeuverSelected =
        Transformations.map(apaRepository.perpendicularManeuverSelection) {
            when (it) {
                ManeuverSelection.SELECTED -> true
                ManeuverSelection.NOT_SELECTED -> false
                else -> false
            }
        }

    private val parkoutManeuverSelected =
        Transformations.map(apaRepository.parkOutManeuverSelection) {
            when (it) {
                ManeuverSelection.SELECTED -> true
                ManeuverSelection.NOT_SELECTED -> false
                else -> false
            }
        }

    private val maneuverType = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelManeuverSelected,
            perpendicularManeuverSelected,
            parkoutManeuverSelected
        )
    ) { (parallelManeuverSelected, perpendicularManeuverSelected, parkoutManeuverSelected) ->
        when {
            (listOf(
                parallelManeuverSelected, perpendicularManeuverSelected,
                parkoutManeuverSelected
            ).filter { it }.count() > 1) -> {
                errorLog(
                    "autopark", "Multiple Maneuver selected not authorized",
                    "discarding"
                )
                null
            }
            parallelManeuverSelected ->
                ManeuverType.PARALLEL
            perpendicularManeuverSelected ->
                ManeuverType.PERPENDICULAR
            parkoutManeuverSelected ->
                ManeuverType.PARKOUT
            else -> null
        }
    }.filterNull()

    private val parkoutRightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_LEFT) && (maneuverType == ManeuverType.PARKOUT)
    }

    private val parkoutLeftSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_RIGHT) &&
            (maneuverType == ManeuverType.PARKOUT)
    }

    private val allForward = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, instruction) ->
        maneuverMoveSet && (instruction == Instruction.DRIVE_FORWARD)
    }

    private val allSelectForward = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, instruction) ->
        maneuverMoveSet && (instruction == Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)
    }

    private val rightCurveFrontActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutLeftSet,
            allForward
        )
    ) { (parkoutLeftSet, allForward) ->
        parkoutLeftSet && allForward
    }

    private val rightCurveFrontNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutLeftSet,
            allSelectForward
        )
    ) { (parkoutLeftSet, allSelectForward) ->
        parkoutLeftSet && allSelectForward
    }

    private val rightCurveFrontVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightCurveFrontActiveVisible,
            rightCurveFrontNotActiveVisible
        )
    ) { (rightCurveFrontActiveVisible, rightCurveFrontNotActiveVisible) ->
        rightCurveFrontActiveVisible || rightCurveFrontNotActiveVisible
    }

    private val leftCurveFrontActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutRightSet,
            allForward
        )
    ) { (parkoutRightSet, allForward) ->
        parkoutRightSet && allForward
    }

    private val leftCurveFrontNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutRightSet,
            allSelectForward
        )
    ) { (parkoutRightSet, allSelectForward) ->
        parkoutRightSet && allSelectForward
    }

    private val leftCurveFrontVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftCurveFrontActiveVisible,
            leftCurveFrontNotActiveVisible
        )
    ) { (leftCurveFrontActiveVisible, leftCurveFrontNotActiveVisible) ->
        leftCurveFrontActiveVisible || leftCurveFrontNotActiveVisible
    }

    private val engageFrontNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverType,
            allSelectForward
        )
    ) { (maneuverType, allSelectForward) ->
        (maneuverType != ManeuverType.PARKOUT) && allSelectForward
    }

    private val engageFrontActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverType,
            allForward
        )
    ) { (maneuverType, allForward) ->
        (maneuverType != ManeuverType.PARKOUT) && allForward
    }

    private val arrowStraightUpVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            engageFrontNotActiveVisible,
            engageFrontActiveVisible
        )
    ) { (engageFrontNotActiveVisible, engageFrontActiveVisible) ->
        engageFrontNotActiveVisible || engageFrontActiveVisible
    }

    private val arrowCurveUpVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftCurveFrontVisible,
            rightCurveFrontVisible
        )
    ) { (leftCurveFrontVisible, rightCurveFrontVisible) ->
        leftCurveFrontVisible || rightCurveFrontVisible
    }

    private val backwardForwardReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        ((maneuverMove == ManeuverMove.BACKWARD) || (maneuverMove == ManeuverMove.FORWARD)) &&
            (instruction == Instruction.REVERSE)
    }

    private val engageBackActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverType,
            backwardForwardReverse
        )
    ) { (maneuverType, backwardForwardReverse) ->
        (maneuverType != ManeuverType.PARKOUT) && backwardForwardReverse
    }

    private val backwardForwardSelectReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        ((maneuverMove == ManeuverMove.BACKWARD) || (maneuverMove == ManeuverMove.FORWARD)) &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val engageBackNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverType,
            backwardForwardSelectReverse
        )
    ) { (maneuverType, backwardForwardSelectReverse) ->
        (maneuverType != ManeuverType.PARKOUT) && backwardForwardSelectReverse
    }

    val firstReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        (maneuverMove == ManeuverMove.FIRST) && (instruction == Instruction.REVERSE)
    }

    private val allReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            firstReverse,
            backwardForwardReverse
        )
    ) { (firstReverse, backwardForwardReverse) ->
        firstReverse || backwardForwardReverse
    }

    private val rightCurveBackActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutRightSet,
            allReverse
        )
    ) { (parkoutLeftSet, allReverse) ->
        parkoutLeftSet && allReverse
    }

    val firstSelectReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        (maneuverMove == ManeuverMove.FIRST) &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val allSelectReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            firstSelectReverse,
            backwardForwardSelectReverse
        )
    ) { (firstSelectReverse, backwardForwardSelectReverse) ->
        firstSelectReverse || backwardForwardSelectReverse
    }

    private val rightCurveBackNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutRightSet,
            allSelectReverse
        )
    ) { (parkoutRightSet, allSelectReverse) ->
        parkoutRightSet && allSelectReverse
    }

    private val leftCurveBackActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutLeftSet,
            allReverse
        )
    ) { (parkoutLeftSet, allReverse) ->
        parkoutLeftSet && allReverse
    }

    private val leftCurveBackNotActiveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutLeftSet,
            allSelectReverse
        )
    ) { (parkoutRightSet, allSelectReverse) ->
        parkoutRightSet && allSelectReverse
    }

    private val arrowStraightDownVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            engageBackNotActiveVisible,
            engageBackActiveVisible
        )
    ) { (engageBackNotActiveVisible, engageBackActiveVisible) ->
        engageBackNotActiveVisible || engageBackActiveVisible
    }

    private val leftCurveBackVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftCurveBackActiveVisible,
            leftCurveBackNotActiveVisible
        )
    ) { (leftCurveBackActiveVisible, leftCurveBackNotActiveVisible) ->
        leftCurveBackActiveVisible || leftCurveBackNotActiveVisible
    }

    private val rightCurveBackVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightCurveBackActiveVisible,
            rightCurveBackNotActiveVisible
        )
    ) { (rightCurveBackActiveVisible, rightCurveBackNotActiveVisible) ->
        rightCurveBackActiveVisible || rightCurveBackNotActiveVisible
    }

    private val arrowCurveDownVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftCurveBackVisible,
            rightCurveBackVisible
        )
    ) { (leftCurveBackVisible, rightCurveBackVisible) ->
        leftCurveBackVisible || rightCurveBackVisible
    }

    private val allForwardOrReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, instruction) ->
        maneuverMoveSet && (instruction == Instruction.GO_FORWARD_OR_REVERSE)
    }

    private val parallelRightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_RIGHT) &&
            (maneuverType == ManeuverType.PARALLEL)
    }

    private val perpendicularRightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_RIGHT) &&
            (maneuverType == ManeuverType.PERPENDICULAR)
    }

    private val rightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelRightSet,
            parkoutRightSet,
            perpendicularRightSet
        )
    ) { (parallelRightSet, parkoutRightSet, perpendicularRightSet) ->
        (parallelRightSet || parkoutRightSet || perpendicularRightSet)
    }

    private val leftDoubleCurveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightSet,
            allForwardOrReverse
        )
    ) { (rightSet, allForwardOrReverse) ->
        (rightSet && allForwardOrReverse)
    }

    private val parallelLeftSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_LEFT) && (maneuverType == ManeuverType.PARALLEL)
    }

    private val perpendicularLeftSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_LEFT) &&
            (maneuverType == ManeuverType.PERPENDICULAR)
    }

    private val leftSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelLeftSet,
            parkoutLeftSet,
            perpendicularLeftSet
        )
    ) { (parallelLeftSet, parkoutLeftSet, perpendicularLeftSet) ->
        (parallelLeftSet || parkoutLeftSet || perpendicularLeftSet)
    }

    private val rightDoubleCurveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSet,
            allForwardOrReverse
        )
    ) { (leftSet, allForwardOrReverse) ->
        (leftSet && allForwardOrReverse)
    }

    private val stopVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            maneuverType,
            apaRepository.scanningSide,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, maneuverType, scanningSide, instruction) ->
        maneuverMoveSet &&
            (maneuverType == ManeuverType.PARALLEL || maneuverType == ManeuverType.PARKOUT) &&
            (instruction == Instruction.STOP) &&
            (scanningSide == ScanningSide.SCANNING_SIDE_RIGHT ||
                scanningSide == ScanningSide.SCANNING_SIDE_LEFT)
    }

    override fun getEngageFrontNotActiveVisible(): LiveData<Boolean> = engageFrontNotActiveVisible

    override fun getEngageFrontActiveVisible(): LiveData<Boolean> = engageFrontActiveVisible

    override fun getRightCurveFrontActiveVisible(): LiveData<Boolean> = rightCurveFrontActiveVisible

    override fun getRightCurveFrontNotActiveVisible(): LiveData<Boolean> =
        rightCurveFrontNotActiveVisible

    override fun getLeftCurveFrontActiveVisible(): LiveData<Boolean> = leftCurveFrontActiveVisible

    override fun getLeftCurveFrontNotActiveVisible(): LiveData<Boolean> =
        leftCurveFrontNotActiveVisible

    override fun getEngageBackActiveVisible(): LiveData<Boolean> = engageBackActiveVisible

    override fun getEngageBackNotActiveVisible(): LiveData<Boolean> = engageBackNotActiveVisible

    override fun getRightCurveBackActiveVisible(): LiveData<Boolean> = rightCurveBackActiveVisible

    override fun getRightCurveBackNotActiveVisible(): LiveData<Boolean> =
        rightCurveBackNotActiveVisible

    override fun getLeftCurveBackActiveVisible(): LiveData<Boolean> = leftCurveBackActiveVisible

    override fun getLeftCurveBackNotActiveVisible(): LiveData<Boolean> =
        leftCurveBackNotActiveVisible

    override fun getLeftDoubleCurveVisible(): LiveData<Boolean> = leftDoubleCurveVisible

    override fun getRightDoubleCurveVisible(): LiveData<Boolean> = rightDoubleCurveVisible

    override fun getStopVisible(): LiveData<Boolean> = stopVisible

    override fun getArrowStraightUpVisible(): LiveData<Boolean> = arrowStraightUpVisible

    override fun getArrowCurveUpVisible(): LiveData<Boolean> = arrowCurveUpVisible

    override fun getArrowStraightDownVisible(): LiveData<Boolean> = arrowStraightDownVisible

    override fun getArrowCurveDownVisible(): LiveData<Boolean> = arrowCurveDownVisible
}