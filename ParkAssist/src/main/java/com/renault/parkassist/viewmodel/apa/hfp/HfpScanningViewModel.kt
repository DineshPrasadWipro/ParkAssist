package com.renault.parkassist.viewmodel.apa.hfp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.R
import com.renault.parkassist.repository.apa.*
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.ui.apa.AnticipatedManeuverButtons
import com.renault.parkassist.ui.apa.ManeuverButtonMapper
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.apa.ApaUtils
import com.renault.parkassist.viewmodel.map
import com.renault.parkassist.viewmodel.merge
import org.koin.core.KoinComponent
import org.koin.core.inject

class HfpScanningViewModel(app: Application) :
    HfpScanningViewModelBase(app), KoinComponent {

    private val apaRepository: IApaRepository by inject()
    private val surroundRepository: IExtSurroundViewRepository by inject()
    private val sonarRepository: ISonarRepository by inject()

    private val anticipatedManeuverButtons = AnticipatedManeuverButtons()
    private val maneuverButtonMapper: ManeuverButtonMapper by inject()

    private val sonarAvmVisible = Transformations.map(surroundRepository.surroundState) { state ->
        when (state.view) {
            View.NO_DISPLAY -> false
            else -> true
        }
    }

    private val sonarRvcVisible = Transformations.map(sonarRepository.displayRequest) { request ->
        when (request) {
            DisplayType.NONE -> false
            else -> true
        }
    }

    private val parallelManeuverEnabled =
        apaRepository.parallelManeuverSelection.map(maneuverButtonMapper::toEnabled)

    private val perpendicularManeuverEnabled =
        apaRepository.perpendicularManeuverSelection.map(maneuverButtonMapper::toEnabled)

    private val parkoutManeuverEnabled =
        apaRepository.parkOutManeuverSelection.map(maneuverButtonMapper::toEnabled)

    private val maneuverParallelButtonSelected =
        anticipatedManeuverButtons.parallelSelected.merge(
            apaRepository.parallelManeuverSelection
                .map(maneuverButtonMapper::toSelected)
        )

    private val maneuverPerpendicularButtonSelected =
        anticipatedManeuverButtons.perpendicularSelected.merge(
            apaRepository.perpendicularManeuverSelection
                .map(maneuverButtonMapper::toSelected)
        )

    private val parkoutManeuverSelected =
        anticipatedManeuverButtons.parkoutSelected.merge(
            apaRepository.parkOutManeuverSelection
                .map(maneuverButtonMapper::toSelected)
        )

    private val instruction =
        Transformations.map(apaRepository.extendedInstruction) { instruction ->
            ApaUtils.getInstructionResource(
                instruction
            )
        }

    private val displayParkout =
        Transformations.map(apaRepository.displayState) { displayState ->
            when (displayState) {
                DisplayState.DISPLAY_PARKOUT_CONFIRMATION -> true
                else -> false
            }
        }

    private val leftIndicatorSelected =
        Transformations.map(LiveDataUtils.combineNonNull
            (apaRepository.scanningSide, apaRepository.extendedInstruction)) {
                (side, instruction) -> side == ScanningSide.SCANNING_SIDE_LEFT &&
            instruction != Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        }

    private val rightIndicatorSelected =
        Transformations.map(LiveDataUtils.combineNonNull
            (apaRepository.scanningSide, apaRepository.extendedInstruction)) {
                (side, instruction) -> side == ScanningSide.SCANNING_SIDE_RIGHT &&
            instruction != Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        }

    private val leftSuitableOrSelected = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.leftSuitable,
            apaRepository.leftSelected
        )
    ) { (leftSuitable, leftSelected) ->
        leftSuitable || leftSelected
    }

    private val rightSuitableOrSelected = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.rightSuitable,
            apaRepository.rightSelected
        )
    ) { (rightSuitable, rightSelected) ->
        rightSuitable || rightSelected
    }

    private val sideSuitableOrSelected = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSuitableOrSelected,
            rightSuitableOrSelected
        )
    ) { (leftSuitableOrSelected, rightSuitableOrSelected) ->
        when {
            leftSuitableOrSelected && rightSuitableOrSelected -> Side.LEFT_RIGHT
            leftSuitableOrSelected && !rightSuitableOrSelected -> Side.LEFT
            !leftSuitableOrSelected && rightSuitableOrSelected -> Side.RIGHT
            else -> Side.STRAIGHT
        }
    }

    private val rearArrowResourceVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSuitableOrSelected,
            rightSuitableOrSelected,
            apaRepository.extendedInstruction
        )
    ) { (leftSuitableOrSelected, rightSuitableOrSelected, instruction) ->
        (leftSuitableOrSelected || rightSuitableOrSelected) &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val rearLeftShortArrowVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSuitableOrSelected,
            maneuverPerpendicularButtonSelected,
            apaRepository.extendedInstruction
        )
    ) { (leftSuitableOrSelected, maneuverPerpendicularButtonSelected, instruction) ->
        leftSuitableOrSelected && maneuverPerpendicularButtonSelected &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val rearLeftLongArrowVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            leftSuitableOrSelected,
            maneuverParallelButtonSelected,
            apaRepository.extendedInstruction
        )
    ) { (leftSuitableOrSelected, maneuverParallelButtonSelected, instruction) ->
        leftSuitableOrSelected && maneuverParallelButtonSelected &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val rearRightShortArrowVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightSuitableOrSelected,
            maneuverPerpendicularButtonSelected,
            apaRepository.extendedInstruction
        )
    ) { (rightSuitableOrSelected, maneuverPerpendicularButtonSelected, instruction) ->
        rightSuitableOrSelected && maneuverPerpendicularButtonSelected &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val rearRightLongArrowVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rightSuitableOrSelected,
            maneuverParallelButtonSelected,
            apaRepository.extendedInstruction
        )
    ) { (rightSuitableOrSelected, maneuverParallelButtonSelected, instruction) ->
        rightSuitableOrSelected && maneuverParallelButtonSelected &&
            (instruction == Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
    }

    private val carFrontStopResourceVisible = Transformations.map(
        apaRepository.extendedInstruction
    ) { instruction ->
        instruction == Instruction.STOP
    }

    private val carFrontArrowResourceVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            rearArrowResourceVisible,
            carFrontStopResourceVisible
        )
    ) { (rearArrowResourceVisible, carFrontStopResourceVisible) ->
        !rearArrowResourceVisible && !carFrontStopResourceVisible
    }

    private val leftParkingSlotPerpendicularVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverPerpendicularButtonSelected,
            leftSuitableOrSelected
        )
    ) { (maneuverPerpendicularButtonSelected, leftSuitableOrSelected) ->
        maneuverPerpendicularButtonSelected && leftSuitableOrSelected
    }

    private val rightParkingSlotPerpendicularVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverPerpendicularButtonSelected,
            rightSuitableOrSelected
        )
    ) { (maneuverPerpendicularButtonSelected, rightSuitableOrSelected) ->
        maneuverPerpendicularButtonSelected && rightSuitableOrSelected
    }

    private val leftParkingSlotParallelVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverParallelButtonSelected,
            leftSuitableOrSelected
        )
    ) { (maneuverParallelButtonSelected, leftSuitableOrSelected) ->
        maneuverParallelButtonSelected && leftSuitableOrSelected
    }

    private val leftSlotResource = Transformations.map(apaRepository.leftSelected) {
        if (it) R.drawable.rimg_adas_parking_large else R.drawable.rimg_adas_parking
    }

    private val rightSlotResource = Transformations.map(apaRepository.rightSelected) {
        if (it) R.drawable.rimg_adas_parking_large else R.drawable.rimg_adas_parking
    }

    private val rightParkingSlotParallelVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverParallelButtonSelected,
            rightSuitableOrSelected
        )
    ) { (maneuverParallelButtonSelected, rightSuitableOrSelected) ->
        maneuverParallelButtonSelected && rightSuitableOrSelected
    }

    private val backgroundResource = Transformations.map(
        LiveDataUtils.combineNonNull(
            maneuverParallelButtonSelected,
            maneuverPerpendicularButtonSelected,
            sideSuitableOrSelected
        )
    ) { (parallelButtonSelected, perpendicularButtonSelected, sideSuitableOrSelected) ->
        if (parallelButtonSelected)
            when (sideSuitableOrSelected) {
                Side.LEFT -> R.drawable.rimg_bckg_apa_scanning_parallel_left
                Side.RIGHT -> R.drawable.rimg_bckg_apa_scanning_parallel_right
                Side.LEFT_RIGHT -> R.drawable.rimg_bckg_apa_scanning_parallel_both
                else -> R.drawable.rimg_bckg_apa_scanning_straight
            } else if (perpendicularButtonSelected)
            when (sideSuitableOrSelected) {
                Side.LEFT -> R.drawable.rimg_bckg_apa_scanning_perpendicular_left
                Side.RIGHT -> R.drawable.rimg_bckg_apa_scanning_perpendicular_right
                Side.LEFT_RIGHT -> R.drawable.rimg_bckg_apa_scanning_perpendicular_both
                else -> R.drawable.rimg_bckg_apa_scanning_straight
            } else R.drawable.rimg_bckg_apa_scanning_straight
    }

    private val frontDisabled =
        Transformations.map(sonarRepository.frontState) { state ->
            groupStateToDeactivation(state)
        }

    private val rearDisabled = Transformations.map(sonarRepository.rearState) { state ->
        groupStateToDeactivation(state)
    }

    private val scanningDisplayState =
        Transformations.map(apaRepository.displayState) { displayState ->
            when (displayState) {
                DisplayState.DISPLAY_SCANNING -> true
                else -> false
            }
        }

    private val upaDisabledApaScanning = Transformations.map(
        LiveDataUtils.combineNonNull(
            scanningDisplayState,
            frontDisabled,
            rearDisabled
        )
    ) { (scanningDisplayState, frontDisabled, rearDisabled) ->
        when {
            scanningDisplayState && frontDisabled && rearDisabled -> true
            else -> false
        }
    }

    private fun groupStateToDeactivation(@GroupState state: Int): Boolean {
        return when (state) {
            GroupState.DISABLED -> true
            else /*GroupState.DISABLED*/ -> false
        }
    }

    override fun getManeuverParallelButtonEnabled() = parallelManeuverEnabled
    override fun getManeuverParkoutButtonEnabled() = parkoutManeuverEnabled
    override fun getManeuverPerpendicularButtonEnabled() = perpendicularManeuverEnabled

    override fun getManeuverParallelButtonSelected() = maneuverParallelButtonSelected
    override fun getManeuverPerpendicularButtonSelected() =
        maneuverPerpendicularButtonSelected

    override fun getManeuverParkoutButtonSelected() = parkoutManeuverSelected
    override fun getInstruction() = instruction

    override fun getLeftParkingSlotParallelVisible() = leftParkingSlotParallelVisible
    override fun getRightParkingSlotPerpendicularVisible() =
        rightParkingSlotPerpendicularVisible

    override fun getLeftParkingSlotPerpendicularVisible() =
        leftParkingSlotPerpendicularVisible

    override fun getRightParkingSlotParallelVisible() = rightParkingSlotParallelVisible

    override fun getRearArrowResourceVisible() = rearArrowResourceVisible
    override fun getCarFrontArrowResourceVisible(): LiveData<Boolean> =
        carFrontArrowResourceVisible

    override fun getCarFrontStopResourceVisible(): LiveData<Boolean> =
        carFrontStopResourceVisible

    override fun getUpaDisabledApaScanning() = upaDisabledApaScanning

    override fun getBackgroundResource() = backgroundResource
    override fun getLeftSlotResource() = leftSlotResource
    override fun getRightSlotResource() = rightSlotResource

    override fun getDisplayParkout() = displayParkout
    override fun getLeftIndicatorSelected() = leftIndicatorSelected

    override fun getRearLeftShortArrowVisible(): LiveData<Boolean> =
        rearLeftShortArrowVisible

    override fun getRearLeftLongArrowVisible(): LiveData<Boolean> =
        rearLeftLongArrowVisible

    override fun getRearRightShortArrowVisible(): LiveData<Boolean> =
        rearRightShortArrowVisible

    override fun getRearRightLongArrowVisible(): LiveData<Boolean> =
        rearRightLongArrowVisible

    override fun getRightIndicatorSelected() = rightIndicatorSelected

    override fun getSonarRvcVisible(): LiveData<Boolean> = sonarRvcVisible

    override fun getSonarAvmVisible(): LiveData<Boolean> = sonarAvmVisible

    override fun start() {}

    override fun stop() {}

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
                ) }
        }
    }
}