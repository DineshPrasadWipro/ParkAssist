package com.renault.parkassist.viewmodel.apa.hfp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.Instruction
import com.renault.parkassist.repository.apa.ScanningSide
import com.renault.parkassist.viewmodel.LiveDataUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class HfpParkoutViewModel(app: Application) :
    HfpParkoutViewModelBase(app), KoinComponent {

    private val apaRepository: IApaRepository by inject()

    private val backgroundSidesVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.extendedInstruction,
            apaRepository.scanningSide
        )
    ) { (instruction, scanningSide) ->
        (instruction == Instruction.SELECT_SIDE) ||
            ((instruction == Instruction.STOP) &&
                (scanningSide != ScanningSide.SCANNING_SIDE_LEFT) &&
                (scanningSide != ScanningSide.SCANNING_SIDE_RIGHT)
                )
    }

    private val instructionVehicleCenterSet =
        Transformations.map(apaRepository.extendedInstruction) { instruction ->
            ((instruction == Instruction.STOP) ||
                (instruction == Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR) ||
                (instruction == Instruction.DRIVE_FORWARD) ||
                (instruction == Instruction.REVERSE) ||
                (instruction == Instruction.GO_FORWARD_OR_REVERSE) ||
                (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON))
        }

    private val instructionVehicleLeftRightSet =
        Transformations.map(apaRepository.extendedInstruction) { instruction ->
            (instruction == Instruction.MANEUVER_COMPLETE_OR_FINISHED) ||
                (instruction == Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL)
        }

    private val instructionParallelForParkoutSet = Transformations.map(
        LiveDataUtils.combineNonNull(
            instructionVehicleCenterSet,
            instructionVehicleLeftRightSet
        )
    ) { (instructionVehicleCenterSet, instructionVehicleLeftRightSet) ->
        instructionVehicleCenterSet || instructionVehicleLeftRightSet
    }

    private val rightSet = Transformations.map(apaRepository.scanningSide) {
        scanningSide -> scanningSide == ScanningSide.SCANNING_SIDE_LEFT
    }

    private val leftSet = Transformations.map(apaRepository.scanningSide) {
            scanningSide -> scanningSide == ScanningSide.SCANNING_SIDE_RIGHT
    }

    private val backgroundParallelLeftVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSet,
            instructionParallelForParkoutSet
        )
    ) { (leftSet, instructionParallelForParkoutSet) ->
        leftSet && instructionParallelForParkoutSet
    }

    private val parallelLeftVehicleCenterVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSet,
            instructionVehicleCenterSet
        )
    ) { (leftSet, instructionVehicleCenterSet) ->
        leftSet && instructionVehicleCenterSet
    }

    private val backgroundParallelRightVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightSet,
            instructionParallelForParkoutSet
        )
    ) { (rightSet, instructionParallelForParkoutSet) ->
        rightSet && instructionParallelForParkoutSet
    }

    private val parallelRightVehicleCenterVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightSet,
            instructionVehicleCenterSet
        )
    ) { (rightSet, instructionVehicleCenterSet) ->
        rightSet && instructionVehicleCenterSet
    }

    private val parallelLeftVehicleLeftVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSet,
            instructionVehicleLeftRightSet
        )
    ) { (leftSet, instructionVehicleLeftRightSet) ->
        leftSet && instructionVehicleLeftRightSet
    }

    private val parallelRightVehicleRightVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightSet,
            instructionVehicleLeftRightSet
        )
    ) { (rightSet, instructionVehicleLeftRightSet) ->
        rightSet && instructionVehicleLeftRightSet
    }

    override fun getBackgroundSidesVisible(): LiveData<Boolean> = backgroundSidesVisible

    override fun getBackgroundParallelLeftVisible(): LiveData<Boolean> =
        backgroundParallelLeftVisible

    override fun getParallelLeftVehicleCenterVisible(): LiveData<Boolean> =
        parallelLeftVehicleCenterVisible

    override fun getBackgroundParallelRightVisible(): LiveData<Boolean> =
        backgroundParallelRightVisible

    override fun getParallelRightVehicleCenterVisible(): LiveData<Boolean> =
        parallelRightVehicleCenterVisible

    override fun getParallelLeftVehicleLeftVisible(): LiveData<Boolean> =
        parallelLeftVehicleLeftVisible

    override fun getParallelRightVehicleRightVisible(): LiveData<Boolean> =
        parallelRightVehicleRightVisible
}