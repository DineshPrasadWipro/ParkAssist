package com.renault.parkassist.viewmodel.apa.hfp

import alliance.car.sonar.AllianceCarSonarManager
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.distinctUntilChanged
import com.renault.parkassist.R
import com.renault.parkassist.repository.apa.*
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.apa.ApaUtils
import com.renault.parkassist.viewmodel.filter
import com.renault.parkassist.viewmodel.filterNull
import com.renault.parkassist.viewmodel.map
import org.koin.core.KoinComponent
import org.koin.core.inject

class HfpGuidanceViewModel(application: Application) : HfpGuidanceViewModelBase(application),
    KoinComponent {

    private val apaRepository: IApaRepository by inject()

    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val sonarRepository: ISonarRepository by inject()

    private val cameraSwitch =
        MutableLiveData<Boolean>().apply { value = false }

    private val rearViews = listOf(
        View.REAR_VIEW,
        View.APA_REAR_VIEW,
        View.PANORAMIC_REAR_VIEW,
        View.SETTINGS_REAR_VIEW
    )

    private val raebSonarOffVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            sonarRepository.raebAlertState,
            sonarRepository.raebAlertEnabled,
            cameraSwitch,
            surroundRepository.surroundState.filter { !it.isRequest }
        )
    ) { (raebAlert, raebEnabled, cameraSwitch, surroundState) ->
        sonarRepository.raebFeaturePresent && !cameraSwitch && surroundState.view in rearViews &&
            (!raebEnabled || raebAlert == AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
    }

    private val extendedInstruction =
        Transformations.map(apaRepository.extendedInstruction) { instruction ->
            ApaUtils.getInstructionResource(
                instruction
            )
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

    private val gaugeVisible =
        Transformations.map(
            apaRepository.extendedInstruction
        ) {
            when (it) {
                Instruction.STOP,
                Instruction.DRIVE_FORWARD,
                Instruction.REVERSE -> true
                else -> false
            }
        }

    private val maneuverType =
        LiveDataUtils.combineNonNull(
            parallelManeuverSelected,
            perpendicularManeuverSelected,
            parkoutManeuverSelected
        )
            .map { (parallelManeuverSelected,
                       perpendicularManeuverSelected,
                       parkoutManeuverSelected) ->
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

    private val maneuverMoveSet =
        Transformations.map(apaRepository.maneuverMove) { move ->
            when (move) {
                ManeuverMove.FIRST, ManeuverMove.BACKWARD, ManeuverMove.FORWARD -> true
                else -> false
            }
        }

    private val driveForward = Transformations.map(apaRepository.extendedInstruction) {
        it == Instruction.DRIVE_FORWARD
    }

    private val driveBackward = Transformations.map(apaRepository.extendedInstruction) {
        it == Instruction.REVERSE
    }

    private val stop = Transformations.map(apaRepository.extendedInstruction) {
        it == Instruction.STOP
    }

    private val isForwardGauge = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            maneuverType,
            apaRepository.extendedInstruction
        ).filter { (maneuverMove, maneuverType, instruction) ->
            !((maneuverMove == ManeuverMove.FIRST) && (maneuverType == ManeuverType.PARKOUT) &&
                (instruction == Instruction.STOP))
        }
    ) { (maneuverMove, maneuverType, instruction) ->
        when {
            (maneuverMove == ManeuverMove.BACKWARD) -> false
            (maneuverMove == ManeuverMove.FORWARD) -> true
            (maneuverMove == ManeuverMove.FIRST) &&
                ((maneuverType == ManeuverType.PERPENDICULAR) ||
                    (maneuverType == ManeuverType.PARALLEL))
            -> false
            (maneuverMove == ManeuverMove.FIRST) && (maneuverType == ManeuverType.PARKOUT) &&
                (instruction == Instruction.DRIVE_FORWARD) -> true
            (maneuverMove == ManeuverMove.FIRST) && (maneuverType == ManeuverType.PARKOUT) &&
                (instruction == Instruction.REVERSE) -> false
            else -> {
                errorLog(
                    "autopark", "cannot compute gauge direction",
                    "fall back to forward gauge"
                )
                true
            }
        }
    }

    private val gaugeColor = Transformations.map(apaRepository.extendedInstruction) {
        when (it) {
            Instruction.STOP -> R.color.gauge_stop
            else -> R.color.gauge_normal
        }
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

    private val perpendicularRightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_RIGHT) &&
            (maneuverType == ManeuverType.PERPENDICULAR)
    }

    private val parallelLeftSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_LEFT) && (maneuverType == ManeuverType.PARALLEL)
    }

    private val instructionParallelSet =
        Transformations.map(apaRepository.extendedInstruction) { instruction ->
            ((instruction == Instruction.STOP) ||
                (instruction == Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR) ||
                (instruction == Instruction.DRIVE_FORWARD) ||
                (instruction == Instruction.REVERSE) ||
                (instruction == Instruction.GO_FORWARD_OR_REVERSE) ||
                (instruction == Instruction.MANEUVER_COMPLETE_OR_FINISHED) ||
                (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON))
        }

    private val instructionParkoutSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.extendedInstruction,
            instructionParallelSet
        )
    ) { (instruction, instructionParallelSet) ->
        (instruction == Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL) ||
            instructionParallelSet
    }

    private val parkoutRightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_LEFT) && (maneuverType == ManeuverType.PARKOUT)
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

    private val parkoutLeftSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        (scanningSide == ScanningSide.SCANNING_SIDE_RIGHT) &&
            (maneuverType == ManeuverType.PARKOUT)
    }

    private val perpendicularSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.scanningSide,
            maneuverType
        )
    ) { (scanningSide, maneuverType) ->
        ((scanningSide == ScanningSide.SCANNING_SIDE_RIGHT) ||
            (scanningSide == ScanningSide.SCANNING_SIDE_LEFT)) &&
            (maneuverType == ManeuverType.PERPENDICULAR)
    }

    private val instructionPerpendicularCenterSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, maneuverMove, instruction) ->
        when {
            maneuverMoveSet &&
                ((instruction == Instruction.STOP) ||
                    (instruction == Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR) ||
                    (instruction == Instruction.DRIVE_FORWARD) ||
                    (instruction == Instruction.MANEUVER_COMPLETE_OR_FINISHED)) -> true
            ((maneuverMove == ManeuverMove.BACKWARD) || (maneuverMove == ManeuverMove.FORWARD)) &&
                ((instruction == Instruction.REVERSE) ||
                    (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)) -> true
            else -> false
        }
    }

    private val firstReverseOrSelectReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        (maneuverMove == ManeuverMove.FIRST) &&
            ((instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON) ||
                (instruction == Instruction.REVERSE))
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

    private val backwardForwardSelectReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        ((maneuverMove == ManeuverMove.BACKWARD) || (maneuverMove == ManeuverMove.FORWARD)) &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val forwardStop = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        (maneuverMove == ManeuverMove.FORWARD) && (instruction == Instruction.STOP)
    }

    private val backgroundParallelLeftSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelLeftSet,
            maneuverMoveSet,
            instructionParallelSet
        )
    ) { (parallelLeftSet, maneuverMoveSet, instructionParallelSet) ->
        parallelLeftSet && maneuverMoveSet && instructionParallelSet
    }

    private val allForwardOrReverse = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, instruction) ->
        maneuverMoveSet && (instruction == Instruction.GO_FORWARD_OR_REVERSE)
    }

    private val backgroundParallelLeftForPerpendicularLeftSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularLeftSet,
            maneuverMoveSet,
            allForwardOrReverse
        )
    ) { (perpendicularLeftSet, maneuverMoveSet, allForwardOrReverse) ->
        perpendicularLeftSet && maneuverMoveSet && allForwardOrReverse
    }

    private val backgroundParallelRightForPerpendicularRightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularRightSet,
            maneuverMoveSet,
            allForwardOrReverse
        )
    ) { (perpendicularRightSet, maneuverMoveSet, allForwardOrReverse) ->
        perpendicularRightSet && maneuverMoveSet && allForwardOrReverse
    }

    private val backgroundParkoutLeftSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutLeftSet,
            maneuverMoveSet,
            instructionParkoutSet
        )
    ) { (parkoutLeftSet, maneuverMoveSet, instructionParkoutSet) ->
        parkoutLeftSet && maneuverMoveSet && instructionParkoutSet
    }

    private val backgroundParallelLeftVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            backgroundParallelLeftSet,
            backgroundParkoutLeftSet,
            backgroundParallelLeftForPerpendicularLeftSet
        )
    ) { (backgroundParallelLeftSet, backgroundParkoutLeftSet,
            backgroundParallelLeftForPerpendicularLeftSet) ->
        backgroundParallelLeftSet || backgroundParkoutLeftSet ||
            backgroundParallelLeftForPerpendicularLeftSet
    }

    private val backgroundParallelRightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelRightSet,
            maneuverMoveSet,
            instructionParallelSet
        )
    ) { (parallelRightSet, maneuverMoveSet, instructionParallelSet) ->
        parallelRightSet && maneuverMoveSet && instructionParallelSet
    }

    private val backgroundParkoutRightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutRightSet,
            maneuverMoveSet,
            instructionParkoutSet
        )
    ) { (parkoutRightSet, maneuverMoveSet, instructionParkoutSet) ->
        parkoutRightSet && maneuverMoveSet && instructionParkoutSet
    }

    private val backgroundParallelRightVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            backgroundParallelRightSet,
            backgroundParkoutRightSet,
            backgroundParallelRightForPerpendicularRightSet
        )
    ) { (backgroundParallelRightSet, backgroundParkoutRightSet,
            backgroundParallelRightForPerpendicularRightSet) ->
        backgroundParallelRightSet || backgroundParkoutRightSet ||
            backgroundParallelRightForPerpendicularRightSet
    }

    private val vehicleCenterFrontSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        when {
            ((maneuverMove == ManeuverMove.FIRST) || (maneuverMove == ManeuverMove.BACKWARD)) &&
                ((instruction == Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR) ||
                    (instruction == Instruction.DRIVE_FORWARD) ||
                    (instruction == Instruction.STOP)) -> true
            (maneuverMove == ManeuverMove.FORWARD) &&
                ((instruction == Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR) ||
                    (instruction == Instruction.DRIVE_FORWARD)) -> true
            else -> false
        }
    }

    private val parallelLeftVehicleCenterFrontVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelLeftSet,
            parkoutLeftSet,
            vehicleCenterFrontSet
        )
    ) { (parallelLeftSet, parkoutLeftSet, vehicleCenterFrontSet) ->
        ((parallelLeftSet || parkoutLeftSet) && vehicleCenterFrontSet)
    }

    private val firstBackwardStop = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMove, instruction) ->
        ((maneuverMove == ManeuverMove.FIRST) || (maneuverMove == ManeuverMove.BACKWARD)) &&
            (instruction == Instruction.STOP)
    }

    private val parallelRightVehicleCenterFrontVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelRightSet,
            parkoutRightSet,
            vehicleCenterFrontSet
        )
    ) { (parallelRightSet, parkoutRightSet, vehicleCenterFrontSet) ->
        ((parallelRightSet || parkoutRightSet) && vehicleCenterFrontSet)
    }

    private val parallelParkoutVehicleCenterBackSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            backwardForwardSelectReverse,
            backwardForwardReverse,
            forwardStop
        )
    ) { (backwardForwardSelectReverse, backwardForwardReverse, forwardStop) ->
        backwardForwardSelectReverse || backwardForwardReverse || forwardStop
    }

    private val parallelLeftVehicleCenterBackSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelLeftSet,
            parallelParkoutVehicleCenterBackSet
        )
    ) { (parallelLeftSet, parallelParkoutVehicleCenterBackSet) ->
        (parallelLeftSet && parallelParkoutVehicleCenterBackSet)
    }

    private val parkoutLeftVehicleCenterBackSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutLeftSet,
            parallelParkoutVehicleCenterBackSet,
            firstReverseOrSelectReverse
        )
    ) { (parkoutLeftSet, parallelParkoutVehicleCenterBackSet, firstReverseOrSelectReverse)
        ->
        (parkoutLeftSet &&
            (parallelParkoutVehicleCenterBackSet || firstReverseOrSelectReverse))
    }

    private val parallelLeftVehicleCenterBackVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelLeftVehicleCenterBackSet,
            parkoutLeftVehicleCenterBackSet
        )
    ) { (parallelLeftVehicleCenterBackSet, parkoutLeftVehicleCenterBackSet) ->
        parallelLeftVehicleCenterBackSet || parkoutLeftVehicleCenterBackSet
    }

    private val parallelRightVehicleCenterBackSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelRightSet,
            parallelParkoutVehicleCenterBackSet
        )
    ) { (parallelRightSet, parallelParkoutVehicleCenterBackSet) ->
        (parallelRightSet && parallelParkoutVehicleCenterBackSet)
    }

    private val parkoutRightVehicleCenterBackSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutRightSet,
            parallelParkoutVehicleCenterBackSet,
            firstReverseOrSelectReverse
        )
    ) { (parkoutRightSet, parallelParkoutVehicleCenterBackSet, firstReverseOrSelectReverse)
        ->
        (parkoutRightSet &&
            (parallelParkoutVehicleCenterBackSet || firstReverseOrSelectReverse))
    }

    private val parallelRightVehicleCenterBackVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelRightVehicleCenterBackSet,
            parkoutRightVehicleCenterBackSet
        )
    ) { (parallelRightVehicleCenterBackSet, parkoutRightVehicleCenterBackSet) ->
        (parallelRightVehicleCenterBackSet || parkoutRightVehicleCenterBackSet)
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

    private val rightSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelRightSet,
            parkoutRightSet,
            perpendicularRightSet
        )
    ) { (parallelRightSet, parkoutRightSet, perpendicularRightSet) ->
        (parallelRightSet || parkoutRightSet || perpendicularRightSet)
    }

    private val allComplete = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, instruction) ->
        maneuverMoveSet && (instruction == Instruction.MANEUVER_COMPLETE_OR_FINISHED)
    }

    private val parallelLeftParkVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelLeftSet,
            allComplete
        )
    ) { (parallelLeftSet, allComplete) ->
        (parallelLeftSet && allComplete)
    }

    private val parallelRightParkVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelRightSet,
            allComplete
        )
    ) { (parallelRightSet, allComplete) ->
        (parallelRightSet && allComplete)
    }

    private val parallelLeftVehicleCenterVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSet,
            allForwardOrReverse,
            parallelLeftParkVisible
        )
    ) { (leftSet, allForwardOrReverse, parallelLeftParkVisible) ->
        (leftSet && allForwardOrReverse) || parallelLeftParkVisible
    }

    private val parallelRightVehicleCenterVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightSet,
            allForwardOrReverse,
            parallelRightParkVisible
        )
    ) { (rightSet, allForwardOrReverse, parallelRightParkVisible) ->
        (rightSet && allForwardOrReverse) || parallelRightParkVisible
    }

    private val allCompleteOrFinished = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverMoveSet,
            apaRepository.extendedInstruction
        )
    ) { (maneuverMoveSet, instruction) ->
        maneuverMoveSet &&
            ((instruction == Instruction.MANEUVER_COMPLETE_OR_FINISHED) ||
                (instruction == Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL))
    }

    private val parkoutLeftVehicleLeftVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutLeftSet,
            allCompleteOrFinished
        )
    ) { (parkoutLeftSet, allCompleteOrFinished) ->
        parkoutLeftSet && allCompleteOrFinished
    }

    private val parkoutRightVehicleRightVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parkoutRightSet,
            allCompleteOrFinished
        )
    ) { (parkoutRightSet, allCompleteOrFinished) ->
        parkoutRightSet && allCompleteOrFinished
    }

    private val parallelLeftVehicleCenterCutVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelLeftSet,
            firstReverseOrSelectReverse
        )
    ) { (parallelLeftSet, firstReverseOrSelectReverse) ->
        parallelLeftSet && firstReverseOrSelectReverse
    }

    private val parallelRightVehicleCenterCutVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            parallelRightSet,
            firstReverseOrSelectReverse
        )
    ) { (parallelRightSet, firstReverseOrSelectReverse) ->
        parallelRightSet && firstReverseOrSelectReverse
    }

    private val backgroundPerpendicularCenterVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularSet,
            instructionPerpendicularCenterSet
        )
    ) { (perpendicularSet, instructionPerpendicularCenterSet) ->
        perpendicularSet && instructionPerpendicularCenterSet
    }

    private val perpendicularVehicleCenterParkVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularSet,
            allComplete
        )
    ) { (perpendicularSet, allComplete) ->
        perpendicularSet && allComplete
    }

    private val perpendicularVehicleCenterFrontVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularSet,
            maneuverMoveSet,
            apaRepository.extendedInstruction
        )
    ) { (perpendicularSet, maneuverMoveSet, instruction) ->
        perpendicularSet && maneuverMoveSet &&
            ((instruction == Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR) ||
                (instruction == Instruction.DRIVE_FORWARD))
    }

    private val perpendicularVehicleCenterBackVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularSet,
            apaRepository.maneuverMove,
            apaRepository.extendedInstruction
        )
    ) { (perpendicularSet, maneuverMove, instruction) ->
        perpendicularSet &&
            ((maneuverMove == ManeuverMove.FORWARD) || (maneuverMove == ManeuverMove.BACKWARD)) &&
            ((instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON) ||
                (instruction == Instruction.REVERSE))
    }

    private val perpendicularVehicleCenterBackStopBackVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularSet,
            firstBackwardStop
        )
    ) { (perpendicularSet, firstBackwardStop) ->
        perpendicularSet && firstBackwardStop
    }

    private val perpendicularVehicleCenterFrontStopFrontVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularSet,
            forwardStop
        )
    ) { (perpendicularSet, forwardStop) ->
        perpendicularSet && forwardStop
    }

    private val perpendicularLeftVehicleCenterCutVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularLeftSet,
            firstReverseOrSelectReverse
        )
    ) { (perpendicularLeftSet, firstReverseOrSelectReverse) ->
        perpendicularLeftSet && firstReverseOrSelectReverse
    }

    private val perpendicularRightVehicleCenterCutVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            perpendicularRightSet,
            firstReverseOrSelectReverse
        )
    ) { (perpendicularRightSet, firstReverseOrSelectReverse) ->
        perpendicularRightSet && firstReverseOrSelectReverse
    }

    private val isCameraVisible = surroundRepository.surroundState.map {
        !(it.view == View.NO_DISPLAY && !it.isRequest)
    }.distinctUntilChanged()

    override fun getBackgroundParallelLeftVisible(): LiveData<Boolean> =
        backgroundParallelLeftVisible

    override fun getBackgroundParallelRightVisible(): LiveData<Boolean> =
        backgroundParallelRightVisible

    override fun getParallelLeftVehicleCenterCutVisible(): LiveData<Boolean> =
        parallelLeftVehicleCenterCutVisible

    override fun getParallelRightVehicleCenterCutVisible(): LiveData<Boolean> =
        parallelRightVehicleCenterCutVisible

    override fun getParallelLeftVehicleCenterFrontVisible(): LiveData<Boolean> =
        parallelLeftVehicleCenterFrontVisible

    override fun getParallelRightVehicleCenterFrontVisible(): LiveData<Boolean> =
        parallelRightVehicleCenterFrontVisible

    override fun getParallelLeftVehicleCenterBackVisible(): LiveData<Boolean> =
        parallelLeftVehicleCenterBackVisible

    override fun getParallelRightVehicleCenterBackVisible(): LiveData<Boolean> =
        parallelRightVehicleCenterBackVisible

    override fun getParallelLeftVehicleCenterVisible(): LiveData<Boolean> =
        parallelLeftVehicleCenterVisible

    override fun getParallelRightVehicleCenterVisible(): LiveData<Boolean> =
        parallelRightVehicleCenterVisible

    override fun getParkoutLeftVehicleLeftVisible(): LiveData<Boolean> =
        parkoutLeftVehicleLeftVisible

    override fun getParkoutRightVehicleRightVisible(): LiveData<Boolean> =
        parkoutRightVehicleRightVisible

    override fun getBackgroundPerpendicularCenterVisible(): LiveData<Boolean> =
        backgroundPerpendicularCenterVisible

    override fun getPerpendicularVehicleCenterParkVisible(): LiveData<Boolean> =
        perpendicularVehicleCenterParkVisible

    override fun getPerpendicularVehicleCenterFrontVisible(): LiveData<Boolean> =
        perpendicularVehicleCenterFrontVisible

    override fun getPerpendicularVehicleCenterBackVisible(): LiveData<Boolean> =
        perpendicularVehicleCenterBackVisible

    override fun getPerpendicularVehicleCenterBackStopBackVisible(): LiveData<Boolean> =
        perpendicularVehicleCenterBackStopBackVisible

    override fun getPerpendicularVehicleCenterFrontStopFrontVisible(): LiveData<Boolean> =
        perpendicularVehicleCenterFrontStopFrontVisible

    override fun getPerpendicularLeftVehicleCenterCutVisible(): LiveData<Boolean> =
        perpendicularLeftVehicleCenterCutVisible

    override fun getPerpendicularRightVehicleCenterCutVisible(): LiveData<Boolean> =
        perpendicularRightVehicleCenterCutVisible

    override fun getParallelLeftParkVisible(): LiveData<Boolean> =
        parallelLeftParkVisible

    override fun getParallelRightParkVisible(): LiveData<Boolean> =
        parallelRightParkVisible

    override fun getExtendedInstruction(): LiveData<Int?> = extendedInstruction

    override fun getGaugeColor(): LiveData<Int> = gaugeColor
    override fun getIsCameraVisible(): LiveData<Boolean> =
        isCameraVisible

    override fun getRaebSonarOffVisible(): LiveData<Boolean> = raebSonarOffVisible

    override fun requestCameraSwitch(cameraOn: Boolean) {
        cameraSwitch.postValue(cameraOn)
    }

    override fun getManeuverCompletion(): LiveData<Int> =
        apaRepository.maneuverCompletion

    override fun getIsForwardGauge(): LiveData<Boolean> = isForwardGauge

    override fun getGaugeVisible(): LiveData<Boolean> = gaugeVisible
}