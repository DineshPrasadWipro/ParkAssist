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

class HfpParkoutVehicleCenterViewModel(application: Application) :
    HfpParkoutVehicleCenterViewModelBase(application),
    KoinComponent {

    private val apaRepository: IApaRepository by inject()

    private val leftSet = Transformations.map(apaRepository.scanningSide) { scanningSide ->
        scanningSide == ScanningSide.SCANNING_SIDE_LEFT
    }

    private val rightSet = Transformations.map(apaRepository.scanningSide) { scanningSide ->
        scanningSide == ScanningSide.SCANNING_SIDE_RIGHT
    }

    private val leftDoubleCurveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSet,
            apaRepository.extendedInstruction
        )
    ) { (leftSet, instruction) ->
        leftSet && (instruction == Instruction.GO_FORWARD_OR_REVERSE)
    }

    private val rightDoubleCurveVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightSet,
            apaRepository.extendedInstruction
        )
    ) { (rightSet, instruction) ->
        rightSet && (instruction == Instruction.GO_FORWARD_OR_REVERSE)
    }

    private val stopVisible = Transformations.map(apaRepository.extendedInstruction) {
        it == Instruction.STOP
    }

    private val arrowRightSideVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightSet,
            apaRepository.extendedInstruction
        )
    ) { (rightSet, instruction) ->
        rightSet && (instruction == Instruction.SELECT_SIDE)
    }

    private val arrowLeftSideVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSet,
            apaRepository.extendedInstruction
        )
    ) { (leftSet, instruction) ->
        leftSet && (instruction == Instruction.SELECT_SIDE)
    }

    override fun getLeftDoubleCurveVisible(): LiveData<Boolean> = leftDoubleCurveVisible

    override fun getRightDoubleCurveVisible(): LiveData<Boolean> = rightDoubleCurveVisible

    override fun getStopVisible(): LiveData<Boolean> = stopVisible

    override fun getArrowRightSideVisible(): LiveData<Boolean> = arrowRightSideVisible

    override fun getArrowLeftSideVisible(): LiveData<Boolean> = arrowLeftSideVisible
}