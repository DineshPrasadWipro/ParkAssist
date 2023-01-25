package com.renault.parkassist.viewmodel.apa.hfp

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.R
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.apa.*
import com.renault.parkassist.repository.apa.mock.ApaRepositoryMock
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.sonar.mock.SonarRepositoryMock
import com.renault.parkassist.repository.surroundview.*
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class HfpGuidanceViewModelTest : TestBase() {

    private lateinit var apaRepository: ApaRepositoryMock
    private lateinit var sonarRepository: SonarRepositoryMock
    private lateinit var surroundViewRepository: SurroundViewRepository

    private lateinit var vm: HfpGuidanceViewModel
    private lateinit var vmVehicleCenterBack: HfpGuidanceVehicleCenterBackViewModel
    private lateinit var vmVehicleCenterCut: HfpGuidanceVehicleCenterCutViewModel
    private lateinit var vmVehicleCenterFront: HfpGuidanceVehicleCenterFrontViewModel
    private lateinit var vmVehicleCenter: HfpGuidanceVehicleCenterViewModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private val mockedSurroundState = MutableLiveData<SurroundState>()

    override val koinModule = module {
        single<IApaRepository>(override = true) { apaRepository }
        single<ISonarRepository>(override = true) { sonarRepository }
        single<IExtSurroundViewRepository>(override = true) { surroundViewRepository }
    }

    @Before
    override fun setup() {
        super.setup()
        apaRepository = ApaRepositoryMock(mockk(relaxed = true))
        sonarRepository = SonarRepositoryMock(mockk(relaxed = true))
        surroundViewRepository = mockk(relaxed = true) {
            every { surroundState } returns mockedSurroundState
        }

        lifecycleOwner = TestUtils.mockLifecycleOwner()
        vm = HfpGuidanceViewModel(mockk())
        vmVehicleCenterBack = HfpGuidanceVehicleCenterBackViewModel(mockk())
        vmVehicleCenterCut = HfpGuidanceVehicleCenterCutViewModel(mockk())
        vmVehicleCenterFront = HfpGuidanceVehicleCenterFrontViewModel(mockk())
        vmVehicleCenter = HfpGuidanceVehicleCenterViewModel(mockk())
    }

    @Test
    fun `should have gauge oriented accordingly to current move and current maneuver type and instruction`() { // ktlint-disable max-line-length
        var forward = false
        vm.isForwardGauge.observe(
            lifecycleOwner,
            Observer { value: Boolean -> forward = value })

        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)
        Assert.assertEquals(true, forward)

        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        Assert.assertEquals(false, forward)

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        Assert.assertEquals(false, forward)

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        Assert.assertEquals(false, forward)

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)
        Assert.assertEquals(true, forward)

        apaRepository.extendedInstruction.postValue(Instruction.STOP)
        Assert.assertEquals(true, forward)

        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)
        Assert.assertEquals(false, forward)

        apaRepository.extendedInstruction.postValue(Instruction.STOP)
        Assert.assertEquals(false, forward)
    }

    @Test
    fun `should have gauge color set accordingly of instruction`() {
        var gaugeColor = 0
        vm.gaugeColor.observe(
            lifecycleOwner,
            Observer {
                gaugeColor = it
            }
        )
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)
        assertEquals(R.color.gauge_normal, gaugeColor)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)
        assertEquals(R.color.gauge_stop, gaugeColor)
    }

    @Test
    fun `should have gauge visibility set accordingly of status and completion`() {
        var gaugeVisible = false
        vm.gaugeVisible.observe(
            lifecycleOwner,
            Observer {
                gaugeVisible = it
            }
        )

        apaRepository.extendedInstruction.postValue(Instruction.STOP)
        assertTrue(gaugeVisible)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)
        assertTrue(gaugeVisible)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)
        assertTrue(gaugeVisible)
        apaRepository.extendedInstruction
            .postValue(Instruction.ACCELERATE_AND_HOLD_THE_PEDAL_PRESSED)
        assertFalse(gaugeVisible)
    }

    /**
     * LEFT - PARALLEL
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PARALLEL side LEFT in FIRST_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var leftStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { leftStopVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(leftStopVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
    }

    @Test
    fun `SHOULD display car, left angled arrow inactive and parkin available icon for parkin PARALLEL side LEFT in FIRST_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterCutVisible = false
        vm.parallelLeftVehicleCenterCutVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterCutVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenterCut.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageLeftNotActiveVisible = false
        vmVehicleCenterCut.engageLeftNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageLeftNotActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterCutVisible)
        assertTrue(arrowCurveDownVisible)
        assertTrue(engageLeftNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PARALLEL side LEFT in FIRST_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )

        var leftCurveFrontNotActiveVisible = false
        vmVehicleCenter.leftCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontNotActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(leftCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PARALLEL side LEFT in FIRST_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var leftCurveFrontActiveVisible = false
        vmVehicleCenter.leftCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(leftCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car, left angled arrow active and parkin available icon for parkin PARALLEL side LEFT in FIRST_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterCutVisible = false
        vm.parallelLeftVehicleCenterCutVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterCutVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenterCut.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageLeftActiveVisible = false
        vmVehicleCenterCut.engageLeftActiveVisible.observe(
            lifecycleOwner,
            Observer { engageLeftActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterCutVisible)
        assertTrue(arrowCurveDownVisible)
        assertTrue(engageLeftActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PARALLEL side LEFT in FIRST_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PARALLEL side LEFT in FIRST_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftParkVisible = false
        vm.parallelLeftParkVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftParkVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftParkVisible)
    }

    /**
     * BACKWARD_MOVE
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PARALLEL side LEFT in BACKWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var leftStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { leftStopVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(leftStopVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PARALLEL side LEFT in BACKWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowStraightDownVisible = false
        vmVehicleCenter.arrowStraightDownVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowStraightDownVisible)
        assertTrue(engageBackNotActiveVisible)
        assertFalse(leftCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PARALLEL side LEFT in BACKWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var leftCurveFrontNotActiveVisible = false
        vmVehicleCenter.leftCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontNotActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(leftCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PARALLEL side LEFT in BACKWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var leftCurveFrontActiveVisible = false
        vmVehicleCenter.leftCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(leftCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PARALLEL side LEFT in BACKWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowStraightDownVisible = false
        vmVehicleCenter.arrowStraightDownVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowStraightDownVisible)
        assertTrue(engageBackActiveVisible)
        assertFalse(leftCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PARALLEL side LEFT in BACKWARD_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PARALLEL side LEFT in BACKWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftParkVisible = false
        vm.parallelLeftParkVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftParkVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftParkVisible)
    }

    /**
     *  FORWARD_MOVE
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PARALLEL side LEFT in FORWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var leftStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { leftStopVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(leftStopVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PARALLEL side LEFT in FORWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowStraightDownVisible = false
        vmVehicleCenter.arrowStraightDownVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowStraightDownVisible)
        assertTrue(engageBackNotActiveVisible)
        assertFalse(leftCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PARALLEL side LEFT in FORWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var leftCurveFrontNotActiveVisible = false
        vmVehicleCenter.leftCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontNotActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(leftCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PARALLEL side LEFT in FORWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var leftCurveFrontActiveVisible = false
        vmVehicleCenter.leftCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(leftCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PARALLEL side LEFT in FORWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowStraightDownVisible = false
        vmVehicleCenter.arrowStraightDownVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowStraightDownVisible)
        assertTrue(engageBackActiveVisible)
        assertFalse(leftCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PARALLEL side LEFT in FORWARD_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PARALLEL side LEFT in FORWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftParkVisible = false
        vm.parallelLeftParkVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftParkVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftParkVisible)
    }

    /**
     * LEFT - PERPENDICULAR
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PERPENDICULAR side LEFT in FIRST_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackStopBackVisible = false
        vm.perpendicularVehicleCenterBackStopBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackStopBackVisible = it }
        )
        var stopBackVisible = false
        vmVehicleCenterBack.stopBackVisible.observe(
            lifecycleOwner,
            Observer { stopBackVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackStopBackVisible)
        assertTrue(stopBackVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PERPENDICULAR side LEFT in FIRST_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var perpendicularLeftVehicleCenterCutVisible = false
        vm.perpendicularLeftVehicleCenterCutVisible.observe(
            lifecycleOwner,
            Observer { perpendicularLeftVehicleCenterCutVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenterCut.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageLeftNotActiveVisible = false
        vmVehicleCenterCut.engageLeftNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageLeftNotActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(perpendicularLeftVehicleCenterCutVisible)
        assertTrue(arrowCurveDownVisible)
        assertTrue(engageLeftNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PERPENDICULAR side LEFT in FIRST_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontNotActiveVisible = false
        vmVehicleCenterFront.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertFalse(stopFrontVisible)
        assertTrue(engageFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PERPENDICULAR side LEFT in FIRST_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontActiveVisible = false
        vmVehicleCenterFront.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PERPENDICULAR side LEFT in FIRST_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var perpendicularLeftVehicleCenterCutVisible = false
        vm.perpendicularLeftVehicleCenterCutVisible.observe(
            lifecycleOwner,
            Observer { perpendicularLeftVehicleCenterCutVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenterCut.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageLeftActiveVisible = false
        vmVehicleCenterCut.engageLeftActiveVisible.observe(
            lifecycleOwner,
            Observer { engageLeftActiveVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(perpendicularLeftVehicleCenterCutVisible)
        assertTrue(arrowCurveDownVisible)
        assertTrue(engageLeftActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PERPENDICULAR side LEFT in FIRST_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
        assertTrue(backgroundParallelLeftVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PERPENDICULAR side LEFT in FIRST_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterParkVisible = false
        vm.perpendicularVehicleCenterParkVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterParkVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterParkVisible)
    }

    /**
     * BACKWARD_MOVE
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PERPENDICULAR side LEFT in BACKWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackStopBackVisible = false
        vm.perpendicularVehicleCenterBackStopBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackStopBackVisible = it }
        )
        var stopBackVisible = false
        vmVehicleCenterBack.stopBackVisible.observe(
            lifecycleOwner,
            Observer { stopBackVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackStopBackVisible)
        assertTrue(stopBackVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PERPENDICULAR side LEFT in BACKWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackVisible = false
        vm.perpendicularVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackVisible = it }
        )

        var engageBackNotActiveVisible = false
        vmVehicleCenterBack.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var engageBackVisible = false
        vmVehicleCenterBack.engageBackVisible.observe(
            lifecycleOwner,
            Observer { engageBackVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackVisible)
        assertTrue(engageBackVisible)
        assertTrue(engageBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PERPENDICULAR side LEFT in BACKWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontNotActiveVisible = false
        vmVehicleCenterFront.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PERPENDICULAR side LEFT in BACKWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontActiveVisible = false
        vmVehicleCenterFront.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PERPENDICULAR side LEFT in BACKWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackVisible = false
        vm.perpendicularVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackVisible = it }
        )

        var engageBackActiveVisible = false
        vmVehicleCenterBack.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )

        var engageBackVisible = false
        vmVehicleCenterBack.engageBackVisible.observe(
            lifecycleOwner,
            Observer { engageBackVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackVisible)
        assertTrue(engageBackVisible)
        assertTrue(engageBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PERPENDICULAR side LEFT in BACKWARD_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PERPENDICULAR side LEFT in BACKWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterParkVisible = false
        vm.perpendicularVehicleCenterParkVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterParkVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterParkVisible)
    }

    /**
     * FORWARD MOVE
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PERPENDICULAR side LEFT in FORWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontStopFrontVisible = false
        vm.perpendicularVehicleCenterFrontStopFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontStopFrontVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenterFront.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontStopFrontVisible)
        assertFalse(engageFrontActiveVisible)
        assertTrue(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PERPENDICULAR side LEFT in FORWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackVisible = false
        vm.perpendicularVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackVisible = it }
        )

        var engageBackNotActiveVisible = false
        vmVehicleCenterBack.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var engageBackVisible = false
        vmVehicleCenterBack.engageBackVisible.observe(
            lifecycleOwner,
            Observer { engageBackVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackVisible)
        assertTrue(engageBackVisible)
        assertTrue(engageBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PERPENDICULAR side LEFT in FORWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontNotActiveVisible = false
        vmVehicleCenterFront.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PERPENDICULAR side LEFT in FORWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontActiveVisible = false
        vmVehicleCenterFront.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PERPENDICULAR side LEFT in FORWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackVisible = false
        vm.perpendicularVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackVisible = it }
        )

        var engageBackActiveVisible = false
        vmVehicleCenterBack.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var engageBackVisible = false
        vmVehicleCenterBack.engageBackVisible.observe(
            lifecycleOwner,
            Observer { engageBackVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackVisible)
        assertTrue(engageBackActiveVisible)
        assertTrue(engageBackVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PERPENDICULAR side LEFT in FORWARD_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PERPENDICULAR side LEFT in FORWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterParkVisible = false
        vm.perpendicularVehicleCenterParkVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterParkVisible = it }
        )

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterParkVisible)
    }

    /**
     * RIGHT - PARALLEL
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PARALLEL side RIGHT in FIRST_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var rightStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { rightStopVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(rightStopVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PARALLEL side RIGHT in FIRST_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterCutVisible = false
        vm.parallelRightVehicleCenterCutVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterCutVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenterCut.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )

        var engageRightNotActiveVisible = false
        vmVehicleCenterCut.engageRightNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageRightNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterCutVisible)
        assertTrue(arrowCurveDownVisible)
        assertTrue(engageRightNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PARALLEL side RIGHT in FIRST_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var leftCurveFrontNotActiveVisible = false
        vmVehicleCenter.leftCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(leftCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PARALLEL side RIGHT in FIRST_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var leftCurveFrontActiveVisible = false
        vmVehicleCenter.leftCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(leftCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PARALLEL side RIGHT in FIRST_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterCutVisible = false
        vm.parallelRightVehicleCenterCutVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterCutVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenterCut.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )

        var engageRightActiveVisible = false
        vmVehicleCenterCut.engageRightActiveVisible.observe(
            lifecycleOwner,
            Observer { engageRightActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterCutVisible)
        assertTrue(arrowCurveDownVisible)
        assertTrue(engageRightActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PARALLEL side RIGHT in FIRST_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(leftDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PARALLEL side RIGHT in FIRST_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightParkVisible = false
        vm.parallelRightParkVisible.observe(
            lifecycleOwner,
            Observer { parallelRightParkVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightParkVisible)
    }

    /**
     * BACKWARD_MOVE
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PARALLEL side RIGHT in BACKWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var rightStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { rightStopVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(rightStopVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PARALLEL side RIGHT in BACKWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowStraightDownVisible = false
        vmVehicleCenter.arrowStraightDownVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowStraightDownVisible)
        assertTrue(engageBackNotActiveVisible)
        assertFalse(leftCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PARALLEL side RIGHT in BACKWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var leftCurveFrontNotActiveVisible = false
        vmVehicleCenter.leftCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(leftCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PARALLEL side RIGHT in BACKWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var leftCurveFrontActiveVisible = false
        vmVehicleCenter.leftCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(leftCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PARALLEL side RIGHT in BACKWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowStraightDownVisible = false
        vmVehicleCenter.arrowStraightDownVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowStraightDownVisible)
        assertTrue(engageBackActiveVisible)
        assertFalse(leftCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PARALLEL side RIGHT in BACKWARD_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(leftDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PARALLEL side RIGHT in BACKWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightParkVisible = false
        vm.parallelRightParkVisible.observe(
            lifecycleOwner,
            Observer { parallelRightParkVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightParkVisible)
    }

    /**
     * FORWARD_MOVE
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PARALLEL side RIGHT in FORWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var rightStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { rightStopVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(rightStopVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PARALLEL side RIGHT in FORWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowStraightDownVisible = false
        vmVehicleCenter.arrowStraightDownVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowStraightDownVisible)
        assertTrue(engageBackNotActiveVisible)
        assertFalse(leftCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PARALLEL side RIGHT in FORWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var leftCurveFrontNotActiveVisible = false
        vmVehicleCenter.leftCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(leftCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PARALLEL side RIGHT in FORWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowStraightUpVisible = false
        vmVehicleCenter.arrowStraightUpVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var leftCurveFrontActiveVisible = false
        vmVehicleCenter.leftCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowStraightUpVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(leftCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PARALLEL side RIGHT in FORWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowStraightDownVisible = false
        vmVehicleCenter.arrowStraightDownVisible.observe(
            lifecycleOwner,
            Observer { arrowStraightDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowStraightDownVisible)
        assertTrue(engageBackActiveVisible)
        assertFalse(leftCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PARALLEL side RIGHT in FORWARD_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(leftDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PARALLEL side RIGHT in FORWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightParkVisible = false
        vm.parallelRightParkVisible.observe(
            lifecycleOwner,
            Observer { parallelRightParkVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightParkVisible)
    }

    /**
     * RIGHT - PERPENDICULAR
     */

    @Test
    fun `SHOULD display car and warning icon for parkin PERPENDICULAR side RIGHT in FIRST_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackStopBackVisible = false
        vm.perpendicularVehicleCenterBackStopBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackStopBackVisible = it }
        )
        var stopBackVisible = false
        vmVehicleCenterBack.stopBackVisible.observe(
            lifecycleOwner,
            Observer { stopBackVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackStopBackVisible)
        assertTrue(stopBackVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PERPENDICULAR side RIGHT in FIRST_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var perpendicularRightVehicleCenterCutVisible = false
        vm.perpendicularRightVehicleCenterCutVisible.observe(
            lifecycleOwner,
            Observer { perpendicularRightVehicleCenterCutVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenterCut.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )

        var engageRightNotActiveVisible = false
        vmVehicleCenterCut.engageRightNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageRightNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(perpendicularRightVehicleCenterCutVisible)
        assertTrue(arrowCurveDownVisible)
        assertTrue(engageRightNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PERPENDICULAR side RIGHT in FIRST_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontNotActiveVisible = false
        vmVehicleCenterFront.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PERPENDICULAR side RIGHT in FIRST_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontActiveVisible = false
        vmVehicleCenterFront.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PERPENDICULAR side RIGHT in FIRST_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var perpendicularRightVehicleCenterCutVisible = false
        vm.perpendicularRightVehicleCenterCutVisible.observe(
            lifecycleOwner,
            Observer { perpendicularRightVehicleCenterCutVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenterCut.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )

        var engageRightActiveVisible = false
        vmVehicleCenterCut.engageRightActiveVisible.observe(
            lifecycleOwner,
            Observer { engageRightActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(perpendicularRightVehicleCenterCutVisible)
        assertTrue(arrowCurveDownVisible)
        assertTrue(engageRightActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PERPENDICULAR side RIGHT in FIRST_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(leftDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PERPENDICULAR side RIGHT in FIRST_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterParkVisible = false
        vm.perpendicularVehicleCenterParkVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterParkVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterParkVisible)
    }

    /**
     * BACKWARD_MOVE
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PERPENDICULAR side RIGHT in BACKWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackStopBackVisible = false
        vm.perpendicularVehicleCenterBackStopBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackStopBackVisible = it }
        )
        var stopBackVisible = false
        vmVehicleCenterBack.stopBackVisible.observe(
            lifecycleOwner,
            Observer { stopBackVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackStopBackVisible)
        assertTrue(stopBackVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PERPENDICULAR side RIGHT in BACKWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackVisible = false
        vm.perpendicularVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackVisible = it }
        )

        var engageBackNotActiveVisible = false
        vmVehicleCenterBack.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var engageBackVisible = false
        vmVehicleCenterBack.engageBackVisible.observe(
            lifecycleOwner,
            Observer { engageBackVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackVisible)
        assertTrue(engageBackVisible)
        assertTrue(engageBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PERPENDICULAR side RIGHT in BACKWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontNotActiveVisible = false
        vmVehicleCenterFront.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PERPENDICULAR side RIGHT in BACKWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontActiveVisible = false
        vmVehicleCenterFront.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PERPENDICULAR side RIGHT in BACKWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackVisible = false
        vm.perpendicularVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackVisible = it }
        )

        var engageBackActiveVisible = false
        vmVehicleCenterBack.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var engageBackVisible = false
        vmVehicleCenterBack.engageBackVisible.observe(
            lifecycleOwner,
            Observer { engageBackVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackVisible)
        assertTrue(engageBackVisible)
        assertTrue(engageBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PERPENDICULAR side RIGHT in BACKWARD_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(leftDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PERPENDICULAR side RIGHT in BACKWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterParkVisible = false
        vm.perpendicularVehicleCenterParkVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterParkVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterParkVisible)
    }

    /**
     * FORWARD_MOVE
     */
    @Test
    fun `SHOULD display car and warning icon for parkin PERPENDICULAR side RIGHT in FORWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontStopFrontVisible = false
        vm.perpendicularVehicleCenterFrontStopFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontStopFrontVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenterFront.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontStopFrontVisible)
        assertFalse(engageFrontActiveVisible)
        assertTrue(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow inactive for parkin PERPENDICULAR side RIGHT in FORWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackVisible = false
        vm.perpendicularVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackVisible = it }
        )

        var engageBackNotActiveVisible = false
        vmVehicleCenterBack.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var engageBackVisible = false
        vmVehicleCenterBack.engageBackVisible.observe(
            lifecycleOwner,
            Observer { engageBackVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackVisible)
        assertTrue(engageBackVisible)
        assertTrue(engageBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and front arrow inactive for parkin PERPENDICULAR side RIGHT in FORWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontNotActiveVisible = false
        vmVehicleCenterFront.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontNotActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and front arrow active for parkin PERPENDICULAR side RIGHT in FORWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterFrontVisible = false
        vm.perpendicularVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterFrontVisible = it }
        )

        var engageFrontActiveVisible = false
        vmVehicleCenterFront.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var stopFrontVisible = false
        vmVehicleCenterFront.stopFrontVisible.observe(
            lifecycleOwner,
            Observer { stopFrontVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterFrontVisible)
        assertTrue(engageFrontActiveVisible)
        assertFalse(stopFrontVisible)
    }

    @Test
    fun `SHOULD display car and back arrow active for parkin PERPENDICULAR side RIGHT in FORWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterBackVisible = false
        vm.perpendicularVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterBackVisible = it }
        )

        var engageBackActiveVisible = false
        vmVehicleCenterBack.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var engageBackVisible = false
        vmVehicleCenterBack.engageBackVisible.observe(
            lifecycleOwner,
            Observer { engageBackVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterBackVisible)
        assertTrue(engageBackVisible)
        assertTrue(engageBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and double curve for parkin PERPENDICULAR side RIGHT in FORWARD_MOVE with instruction PO_GUIDANCE`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

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
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(leftDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and parked icon for parkin PERPENDICULAR side RIGHT in FORWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        var backgroundPerpendicularCenterVisible = false
        vm.backgroundPerpendicularCenterVisible.observe(
            lifecycleOwner,
            Observer { backgroundPerpendicularCenterVisible = it }
        )
        var perpendicularVehicleCenterParkVisible = false
        vm.perpendicularVehicleCenterParkVisible.observe(
            lifecycleOwner,
            Observer { perpendicularVehicleCenterParkVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundPerpendicularCenterVisible)
        assertTrue(perpendicularVehicleCenterParkVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side RIGHT in FIRST_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var rightCurveBackNotActiveVisible = false
        vmVehicleCenter.rightCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackNotActiveVisible)
        assertFalse(rightCurveBackNotActiveVisible)
        assertTrue(leftCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side RIGHT in FIRST_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var rightCurveFrontNotActiveVisible = false
        vmVehicleCenter.rightCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveFrontNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontNotActiveVisible)
        assertTrue(rightCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout side RIGHT in FIRST_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var rightCurveFrontActiveVisible = false
        vmVehicleCenter.rightCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveFrontActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontActiveVisible)
        assertTrue(rightCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout_side RIGHT in FIRST_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var rightCurveBackActiveVisible = false
        vmVehicleCenter.rightCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackActiveVisible)
        assertFalse(rightCurveBackActiveVisible)
        assertTrue(leftCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side RIGHT in FIRST_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var leftStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { leftStopVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(leftStopVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side RIGHT in FIRST_MOVE with instruction PO GUIDANCE`() { // ktlint-disable max-line-length
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

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side RIGHT in FIRST_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parkoutLeftVehicleLeftVisible = false
        vm.parkoutLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parkoutLeftVehicleLeftVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parkoutLeftVehicleLeftVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side RIGHT in FIRST_MOVE with instruction FINISHED TAKE BACK CONTROL`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parkoutLeftVehicleLeftVisible = false
        vm.parkoutLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parkoutLeftVehicleLeftVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parkoutLeftVehicleLeftVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side RIGHT in BACKWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var rightCurveBackNotActiveVisible = false
        vmVehicleCenter.rightCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackNotActiveVisible)
        assertFalse(rightCurveBackNotActiveVisible)
        assertTrue(leftCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side RIGHT in BACKWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var rightCurveFrontNotActiveVisible = false
        vmVehicleCenter.rightCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveFrontNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontNotActiveVisible)
        assertTrue(rightCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout side RIGHT in BACKWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var rightCurveFrontActiveVisible = false
        vmVehicleCenter.rightCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveFrontActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontActiveVisible)
        assertTrue(rightCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout_side RIGHT in BACKWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var rightCurveBackActiveVisible = false
        vmVehicleCenter.rightCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackActiveVisible)
        assertFalse(rightCurveBackActiveVisible)
        assertTrue(leftCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side RIGHT in BACKWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var leftStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { leftStopVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(leftStopVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side RIGHT in BACKWARD_MOVE with instruction PO GUIDANCE`() { // ktlint-disable max-line-length
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

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side RIGHT in BACKWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parkoutLeftVehicleLeftVisible = false
        vm.parkoutLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parkoutLeftVehicleLeftVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parkoutLeftVehicleLeftVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side RIGHT in BACKWARD_MODE with instruction FINISHED TAKE BACK CONTROL`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parkoutLeftVehicleLeftVisible = false
        vm.parkoutLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parkoutLeftVehicleLeftVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parkoutLeftVehicleLeftVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout side RIGHT in FORWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var rightCurveBackNotActiveVisible = false
        vmVehicleCenter.rightCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackNotActiveVisible)
        assertFalse(rightCurveBackNotActiveVisible)
        assertTrue(leftCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout side RIGHT in FORWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var rightCurveFrontNotActiveVisible = false
        vmVehicleCenter.rightCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveFrontNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontNotActiveVisible)
        assertTrue(rightCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout side RIGHT in FORWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterFrontVisible = false
        vm.parallelLeftVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var rightCurveFrontActiveVisible = false
        vmVehicleCenter.rightCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveFrontActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontActiveVisible)
        assertTrue(rightCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout_side RIGHT in FORWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var rightCurveBackActiveVisible = false
        vmVehicleCenter.rightCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackActiveVisible)
        assertFalse(rightCurveBackActiveVisible)
        assertTrue(leftCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout side RIGHT in FORWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parallelLeftVehicleCenterBackVisible = false
        vm.parallelLeftVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelLeftVehicleCenterBackVisible = it }
        )

        var leftStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { leftStopVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(leftStopVisible)
        assertTrue(parallelLeftVehicleCenterBackVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side RIGHT in FORWARD_MOVE with instruction PO GUIDANCE`() { // ktlint-disable max-line-length
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

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parallelLeftVehicleCenterVisible)
        assertTrue(rightDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout side RIGHT in FORWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parkoutLeftVehicleLeftVisible = false
        vm.parkoutLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parkoutLeftVehicleLeftVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parkoutLeftVehicleLeftVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side RIGHT in FORWARD_MOVE with instruction FINISHED TAKE BACK CONTROL`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        var backgroundParallelLeftVisible = false
        vm.backgroundParallelLeftVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelLeftVisible = it }
        )
        var parkoutLeftVehicleLeftVisible = false
        vm.parkoutLeftVehicleLeftVisible.observe(
            lifecycleOwner,
            Observer { parkoutLeftVehicleLeftVisible = it }
        )

        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(true)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL)

        assertTrue(backgroundParallelLeftVisible)
        assertTrue(parkoutLeftVehicleLeftVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side LEFT in FIRST_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )
        var rightCurveBackNotActiveVisible = false
        vmVehicleCenter.rightCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackNotActiveVisible)
        assertFalse(leftCurveBackNotActiveVisible)
        assertTrue(rightCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side LEFT in FIRST_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var leftCurveFrontNotActiveVisible = false
        vmVehicleCenter.leftCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontNotActiveVisible)
        assertTrue(leftCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout side LEFT in FIRST_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var leftCurveFrontActiveVisible = false
        vmVehicleCenter.leftCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontActiveVisible)
        assertTrue(leftCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout_side LEFT in FIRST_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )
        var rightCurveBackActiveVisible = false
        vmVehicleCenter.rightCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackActiveVisible)
        assertFalse(leftCurveBackActiveVisible)
        assertTrue(rightCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side LEFT in FIRST_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var rightStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { rightStopVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(rightStopVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side LEFT in FIRST_MOVE with instruction PO GUIDANCE`() { // ktlint-disable max-line-length
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
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(leftDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side LEFT in FIRST_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parkoutRightVehicleRightVisible = false
        vm.parkoutRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parkoutRightVehicleRightVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parkoutRightVehicleRightVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side LEFT in FIRST_MOVE with instruction FINISHED TAKE BACK CONTROL`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parkoutRightVehicleRightVisible = false
        vm.parkoutRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parkoutRightVehicleRightVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parkoutRightVehicleRightVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side LEFT in BACKWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )
        var rightCurveBackNotActiveVisible = false
        vmVehicleCenter.rightCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackNotActiveVisible)
        assertFalse(leftCurveBackNotActiveVisible)
        assertTrue(rightCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side LEFT in BACKWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var leftCurveFrontNotActiveVisible = false
        vmVehicleCenter.leftCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontNotActiveVisible)
        assertTrue(leftCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout side LEFT in BACKWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var leftCurveFrontActiveVisible = false
        vmVehicleCenter.leftCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontActiveVisible)
        assertTrue(leftCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout_side LEFT in BACKWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )
        var rightCurveBackActiveVisible = false
        vmVehicleCenter.rightCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackActiveVisible)
        assertFalse(leftCurveBackActiveVisible)
        assertTrue(rightCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side LEFT in BACKWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var rightStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { rightStopVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(rightStopVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side LEFT in BACKWARD_MOVE with instruction PO GUIDANCE`() { // ktlint-disable max-line-length
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
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(leftDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side LEFT in BACKWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parkoutRightVehicleRightVisible = false
        vm.parkoutRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parkoutRightVehicleRightVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parkoutRightVehicleRightVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side LEFT in BACKWARD_MOVE with instruction FINISHED TAKE BACK CONTROL`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parkoutRightVehicleRightVisible = false
        vm.parkoutRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parkoutRightVehicleRightVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parkoutRightVehicleRightVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side LEFT in FORWARD_MOVE with instruction ENGAGE_REAR_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackNotActiveVisible = false
        vmVehicleCenter.engageBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackNotActiveVisible = it }
        )
        var leftCurveBackNotActiveVisible = false
        vmVehicleCenter.leftCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackNotActiveVisible = it }
        )
        var rightCurveBackNotActiveVisible = false
        vmVehicleCenter.rightCurveBackNotActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackNotActiveVisible)
        assertFalse(leftCurveBackNotActiveVisible)
        assertTrue(rightCurveBackNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual for parkout_side LEFT in FORWARD_MOVE with instruction ENGAGE_FORWARD_GEAR`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontNotActiveVisible = false
        vmVehicleCenter.engageFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontNotActiveVisible = it }
        )
        var leftCurveFrontNotActiveVisible = false
        vmVehicleCenter.leftCurveFrontNotActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontNotActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontNotActiveVisible)
        assertTrue(leftCurveFrontNotActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout side LEFT in FORWARD_MOVE with instruction DRIVE_FORWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterFrontVisible = false
        vm.parallelRightVehicleCenterFrontVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterFrontVisible = it }
        )

        var arrowCurveUpVisible = false
        vmVehicleCenter.arrowCurveUpVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveUpVisible = it }
        )
        var engageFrontActiveVisible = false
        vmVehicleCenter.engageFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { engageFrontActiveVisible = it }
        )
        var leftCurveFrontActiveVisible = false
        vmVehicleCenter.leftCurveFrontActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveFrontActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterFrontVisible)
        assertTrue(arrowCurveUpVisible)
        assertFalse(engageFrontActiveVisible)
        assertTrue(leftCurveFrontActiveVisible)
    }

    @Test
    fun `SHOULD display car and arrow curve visual active for parkout_side LEFT in FORWARD_MOVE with instruction DRIVE_BACKWARD`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var arrowCurveDownVisible = false
        vmVehicleCenter.arrowCurveDownVisible.observe(
            lifecycleOwner,
            Observer { arrowCurveDownVisible = it }
        )
        var engageBackActiveVisible = false
        vmVehicleCenter.engageBackActiveVisible.observe(
            lifecycleOwner,
            Observer { engageBackActiveVisible = it }
        )
        var leftCurveBackActiveVisible = false
        vmVehicleCenter.leftCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { leftCurveBackActiveVisible = it }
        )
        var rightCurveBackActiveVisible = false
        vmVehicleCenter.rightCurveBackActiveVisible.observe(
            lifecycleOwner,
            Observer { rightCurveBackActiveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
        assertTrue(arrowCurveDownVisible)
        assertFalse(engageBackActiveVisible)
        assertFalse(leftCurveBackActiveVisible)
        assertTrue(rightCurveBackActiveVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side LEFT in FORWARD_MOVE with instruction STOP`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parallelRightVehicleCenterBackVisible = false
        vm.parallelRightVehicleCenterBackVisible.observe(
            lifecycleOwner,
            Observer { parallelRightVehicleCenterBackVisible = it }
        )

        var rightStopVisible = false
        vmVehicleCenter.stopVisible.observe(
            lifecycleOwner,
            Observer { rightStopVisible = it }
        )

        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.STOP)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(rightStopVisible)
        assertTrue(parallelRightVehicleCenterBackVisible)
    }

    @Test
    fun `SHOULD display car and stop icon for parkout_side LEFT in FORWARD_MOVE with instruction PO GUIDANCE`() { // ktlint-disable max-line-length
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
        var leftDoubleCurveVisible = false
        vmVehicleCenter.leftDoubleCurveVisible.observe(
            lifecycleOwner,
            Observer { leftDoubleCurveVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.GO_FORWARD_OR_REVERSE)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parallelRightVehicleCenterVisible)
        assertTrue(leftDoubleCurveVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side LEFT in FORWARD_MOVE with instruction FINISHED`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parkoutRightVehicleRightVisible = false
        vm.parkoutRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parkoutRightVehicleRightVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_COMPLETE_OR_FINISHED)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parkoutRightVehicleRightVisible)
    }

    @Test
    fun `SHOULD display car and easypark parked icon for parkout_side LEFT in FORWARD_MOVE with instruction FINISHED TAKE BACK CONTROL`() { // ktlint-disable max-line-length
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        apaRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        var backgroundParallelRightVisible = false
        vm.backgroundParallelRightVisible.observe(
            lifecycleOwner,
            Observer { backgroundParallelRightVisible = it }
        )
        var parkoutRightVehicleRightVisible = false
        vm.parkoutRightVehicleRightVisible.observe(
            lifecycleOwner,
            Observer { parkoutRightVehicleRightVisible = it }
        )

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        apaRepository.extendedInstruction.postValue(Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL)

        assertTrue(backgroundParallelRightVisible)
        assertTrue(parkoutRightVehicleRightVisible)
    }

    @Test
    fun `should hide avm display when no display request`() {

        var avmDisplay = true
        mockedSurroundState.postValue(SurroundState(View.NO_DISPLAY, false))
        vm.isCameraVisible.observe(
            lifecycleOwner,
            Observer { avmDisplay = it }

        )
        assertFalse(avmDisplay)

        mockedSurroundState.postValue(SurroundState(View.FRONT_VIEW, false))

        assertTrue(avmDisplay)

        mockedSurroundState.postValue(SurroundState(View.FRONT_VIEW, true))

        assertTrue(avmDisplay)
    }
}