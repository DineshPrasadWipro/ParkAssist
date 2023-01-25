package com.renault.parkassist.viewmodel.apa.hfp

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.Instruction
import com.renault.parkassist.repository.apa.ManeuverSelection
import com.renault.parkassist.repository.apa.ScanningSide
import com.renault.parkassist.repository.apa.mock.ApaRepositoryMock
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class HfpParkoutViewModelTest : TestBase() {

    private lateinit var apaRepository: ApaRepositoryMock
    private lateinit var vm: HfpParkoutViewModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var vmVehicleCenter: HfpParkoutVehicleCenterViewModel

    override val koinModule = module {
        single<IApaRepository>(override = true) { apaRepository }
    }

    @Before
    override fun setup() {
        super.setup()
        apaRepository = ApaRepositoryMock(io.mockk.mockk(relaxed = true))
        lifecycleOwner = TestUtils.mockLifecycleOwner()
        vm = HfpParkoutViewModel(mockk())
        vmVehicleCenter = HfpParkoutVehicleCenterViewModel(mockk())
    }

        @Test
        fun should_set_background_sides_visible_for_parkout_NONE_side_with_instruction_SELECT_SIDE() { // ktlint-disable max-line-length
            apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
            apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
            apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
            apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_NONE)

            var backgroundSidesVisible = false
            vm.backgroundSidesVisible.observe(
                lifecycleOwner,
                Observer { backgroundSidesVisible = it }
            )
            var arrowRightSideVisible = false
            vmVehicleCenter.arrowRightSideVisible.observe(
                lifecycleOwner,
                Observer { arrowRightSideVisible = it }
            )
            var arrowLeftSideVisible = false
            vmVehicleCenter.arrowLeftSideVisible.observe(
                lifecycleOwner,
                Observer { arrowLeftSideVisible = it }
            )

            apaRepository.extendedInstruction.postValue(Instruction.SELECT_SIDE)

            assertTrue(backgroundSidesVisible)
            assertFalse(arrowRightSideVisible)
            assertFalse(arrowLeftSideVisible)
        }

    @Test
    fun should_set_background_sides_visible_and_arrow_Left_side_visible_for_parkout_LEFT_side_with_instruction_SELECT_SIDE() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundSidesVisible = false
        vm.backgroundSidesVisible.observe(
            lifecycleOwner,
            Observer { backgroundSidesVisible = it }
        )
        var arrowRightSideVisible = false
        vmVehicleCenter.arrowRightSideVisible.observe(
            lifecycleOwner,
            Observer { arrowRightSideVisible = it }
        )
        var arrowLeftSideVisible = false
        vmVehicleCenter.arrowLeftSideVisible.observe(
            lifecycleOwner,
            Observer { arrowLeftSideVisible = it }
        )

        apaRepository.extendedInstruction.postValue(Instruction.SELECT_SIDE)

        assertTrue(backgroundSidesVisible)
        assertFalse(arrowRightSideVisible)
        assertTrue(arrowLeftSideVisible)
    }

    @Test
    fun should_set_background_sides_visible_and_arrow_Right_side_visible_for_parkout_RIGHT_side_with_instruction_SELECT_SIDE() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundSidesVisible = false
        vm.backgroundSidesVisible.observe(
            lifecycleOwner,
            Observer { backgroundSidesVisible = it }
        )
        var arrowRightSideVisible = false
        vmVehicleCenter.arrowRightSideVisible.observe(
            lifecycleOwner,
            Observer { arrowRightSideVisible = it }
        )
        var arrowLeftSideVisible = false
        vmVehicleCenter.arrowLeftSideVisible.observe(
            lifecycleOwner,
            Observer { arrowLeftSideVisible = it }
        )

        apaRepository.extendedInstruction.postValue(Instruction.SELECT_SIDE)

        assertTrue(backgroundSidesVisible)
        assertTrue(arrowRightSideVisible)
        assertFalse(arrowLeftSideVisible)
    }

    @Test
    fun should_set_background_sides_visible_for_parkout_NONE_side_with_instruction_STOP() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_NONE)

        var backgroundSidesVisible = false
        vm.backgroundSidesVisible.observe(
            lifecycleOwner,
            Observer { backgroundSidesVisible = it }
        )
        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundSidesVisible)
        assertTrue(stopVisible)
    }

    @Test
    fun should_set_parallel_left_background_visible_and_Left_STOP_visible_for_parkout_RIGHT_side_with_instruction_STOP() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )

        var parallelLeftVehicleCenterVisible = false
        vm.parallelLeftVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(stopVisible)
    }

    @Test
    fun should_set_parallel_right_background_visible_and_Right_STOP_visible_for_parkout_LEFT_side_with_instruction_STOP() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )

        var parallelRightVehicleCenterVisible = false
        vm.parallelRightVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )

        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(stopVisible)
    }

    @Test
    fun should_set_parallel_left_background_visible_for_parkout_RIGHT_side_with_instruction_ENGAGE_REAR_GEAR() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )

        var parallelLeftVehicleCenterVisible = false
        vm.parallelLeftVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertFalse(stopVisible)
    }

    @Test
    fun should_set_parallel_right_background_visible_for_parkout_LEFT_side_with_instruction_ENGAGE_REAR_GEAR() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )

        var parallelRightVehicleCenterVisible = false
        vm.parallelRightVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertFalse(stopVisible)
    }

    @Test
    fun should_set_parallel_left_background_visible_for_parkout_RIGHT_side_with_instruction_ENGAGE_FORWARD_GEAR() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )

        var parallelLeftVehicleCenterVisible = false
        vm.parallelLeftVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertFalse(stopVisible)
    }

    @Test
    fun should_set_parallel_right_background_visible_for_parkout_LEFT_side_with_instruction_ENGAGE_FORWARD_GEAR() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )

        var parallelRightVehicleCenterVisible = false
        vm.parallelRightVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertFalse(stopVisible)
    }

    @Test
    fun should_set_parallel_left_background_visible_for_parkout_RIGHT_side_with_instruction_REVERSE() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )

        var parallelLeftVehicleCenterVisible = false
        vm.parallelLeftVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertFalse(stopVisible)
    }

    @Test
    fun should_set_parallel_right_background_visible_for_parkout_LEFT_side_with_instruction_REVERSE() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )

        var parallelRightVehicleCenterVisible = false
        vm.parallelRightVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertFalse(stopVisible)
    }

    @Test
    fun should_set_parallel_left_background_visible_for_parkout_RIGHT_side_with_instruction_DRIVE_FORWARD() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )

        var parallelLeftVehicleCenterVisible = false
        vm.parallelLeftVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertFalse(stopVisible)
    }

    @Test
    fun should_set_parallel_right_background_visible_for_parkout_LEFT_side_with_instruction_DRIVE_FORWARD() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )

        var parallelRightVehicleCenterVisible = false
        vm.parallelRightVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterVisible = it }
        )

        var stopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { stopVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertFalse(stopVisible)
    }

    @Test
    fun should_set_parallel_left_background_and_vehicle_left_visible_for_parkout_RIGHT_side_with_instruction_FINISHED() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )

        var parallelLeftVehicleLeftVisible = false
        vm.parallelLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleLeftVisible = it }
        )
        var parallelRightVehicleRightVisible = false
        vm.parallelRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleRightVisible = it }
        )

        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleLeftVisible)
        assertFalse(parallelRightVehicleRightVisible)
    }

    @Test
    fun should_set_parallel_right_background_and_vehicle_right_visible_for_parkout_LEFT_side_with_instruction_FINISHED() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )

        var parallelLeftVehicleLeftVisible = false
        vm.parallelLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleLeftVisible = it }
        )
        var parallelRightVehicleRightVisible = false
        vm.parallelRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleRightVisible = it }
        )

        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelRightVisible)
        assertFalse(parallelLeftVehicleLeftVisible)
        assertTrue(parallelRightVehicleRightVisible)
    }

    @Test
    fun should_set_parallel_left_background_and_vehicle_left_visible_for_parkout_RIGHT_side_with_instruction_FINISHED_TAKE_BACK_CONTROL() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )

        var parallelLeftVehicleLeftVisible = false
        vm.parallelLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleLeftVisible = it }
        )
        var parallelRightVehicleRightVisible = false
        vm.parallelRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleRightVisible = it }
        )

        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleLeftVisible)
        assertFalse(parallelRightVehicleRightVisible)
    }

    @Test
    fun should_set_parallel_right_background_and_vehicle_right_visible_for_parkout_LEFT_side_with_instruction_FINISHED_TAKE_BACK_CONTROL() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )

        var parallelLeftVehicleLeftVisible = false
        vm.parallelLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleLeftVisible = it }
        )
        var parallelRightVehicleRightVisible = false
        vm.parallelRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleRightVisible = it }
        )

        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL)

        assertTrue(backgroundParallelRightVisible)
        assertFalse(parallelLeftVehicleLeftVisible)
        assertTrue(parallelRightVehicleRightVisible)
    }

    @Test
    fun should_set_parallel_left_background_and_right_doublecurve_visible_for_parkout_RIGHT_side_with_instruction_PO_GUIDANCE() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )

        var parallelLeftVehicleCenterVisible = false
        vm.parallelLeftVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterVisible = it }
        )

        var rightDoubleCurveVisible = false
        vmVehicleCenter.rightDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { rightDoubleCurveVisible = it }
        )
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
        assertFalse(leftDoubleCurveVisible)
    }

    @Test
    fun should_set_parallel_right_background_and_left_doublecurve_visible_for_parkout_LEFT_side_with_instruction_PO_GUIDANCE() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )

        var parallelRightVehicleCenterVisible = false
        vm.parallelRightVehicleCenterVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterVisible = it }
        )

        var rightDoubleCurveVisible = false
        vmVehicleCenter.rightDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { rightDoubleCurveVisible = it }
        )
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertFalse(rightDoubleCurveVisible)
        assertTrue(leftDoubleCurveVisible)
    }
}