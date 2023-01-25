package com.renault.parkassist

import android.content.res.Configuration
import android.os.SystemClock.sleep
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.apa.*
import com.renault.parkassist.repository.sonar.SensorState
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DemoHfp : OverlayActivityTest() {
    @Before
    fun setup() {
        automaticParkAssistRepository.featureConfiguration = FeatureConfig.HFP
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
    }

    @Test
    fun should_display_correct_screen_when_rvc_present() { // ktlint-disable max-line-length
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_PORTRAIT)
        resetSonar()
        sonarNoPlaceRight()

        // Display scanning screen
        launchFullScreen()
        navigateFullscreen(R.id.RvcHfpGuidanceFragment)
        sleep(50) // BREAK HERE /!\

        // Display Instruction “Select side” & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_SIDE
        )
        sleep(50) // BREAK HERE /!\

        // Change parking mode to parallel
        automaticParkAssistRepository.parallelManeuverSelection.postValue(
            ManeuverSelection.SELECTED
        )
        sleep(50) // BREAK HERE /!\

        // Right is selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        sleep(50) // BREAK HERE /!\

        // Display Instruction “Drive forward” & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD
        )

        sleep(50) // BREAK HERE /!\

        sonarPlaceFar()
        sleep(1000)
        sonarPlaceDetected()
        sleep(1000)

        // Display of small P (slot detected) & tempo X seconds
        automaticParkAssistRepository.rightSuitable.postValue(true)

        // Display Instruction “Drive slightly forward” & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD
        )

        sleep(50) // BREAK HERE /!\

        sonarPlaceForward()
        sleep(1000)
        sonarPlaceForward2()
        sleep(1000)
        sonarPlaceForward3()
        sleep(1000)

        sonarPlaceForward4()

        // Display Instruction “Stop” & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(Instruction.STOP)

        sleep(50) // BREAK HERE /!\

        // Display of big P (slot selected) & tempo X seconds
        automaticParkAssistRepository.rightSelected.postValue(true)

        // Display Instruction “engage reverse gear“ & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.ENGAGE_REAR_GEAR
        )

        sleep(50) // BREAK HERE /!\

        automaticParkAssistRepository.maneuverMove.postValue(
            ManeuverMove.BACKWARD
        )

        // Display guidance screen
        automaticParkAssistRepository.displayState.postValue(
            DisplayState.DISPLAY_GUIDANCE
        )

        automaticParkAssistRepository.maneuverCompletion.postValue(0)

        // Display Instruction “drive backward” & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.REVERSE
        )

        sleep(50) // BREAK HERE /!\

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            clickOnView(R.id.elt_adas_camera_switch)

            sleep(50) // BREAK HERE /!\
        }

        sonarRearDirection1()
        automaticParkAssistRepository.maneuverCompletion.postValue(20)
        sleep(1000)
        sonarRearDirection2()
        automaticParkAssistRepository.maneuverCompletion.postValue(40)
        sleep(1000)
        sonarRearDirection3()
        automaticParkAssistRepository.maneuverCompletion.postValue(60)
        sleep(1000)
        sonarRearDirection4()
        automaticParkAssistRepository.maneuverCompletion.postValue(80)
        sleep(1000)

        // Display Instruction “Stop“ & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.STOP
        )
        automaticParkAssistRepository.maneuverCompletion.postValue(100)

        sleep(50) // BREAK HERE /!\

        // Display Instruction “Engage forward gear & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR
        )

        sleep(50) // BREAK HERE /!\

        // Display Instruction “Drive forward“ & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD
        )
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(0)

        sleep(50) // BREAK HERE /!\

        automaticParkAssistRepository.maneuverCompletion.postValue(20)
        sleep(1000)
        automaticParkAssistRepository.maneuverCompletion.postValue(40)
        sleep(1000)

        sonarForward1()
        automaticParkAssistRepository.maneuverCompletion.postValue(60)
        sleep(1000)
        automaticParkAssistRepository.maneuverCompletion.postValue(80)
        sleep(1000)

        // Display Instruction “Stop” & tempo X seconds
        automaticParkAssistRepository.extendedInstruction.postValue(Instruction.STOP)
        automaticParkAssistRepository.maneuverCompletion.postValue(100)

        sleep(50) // BREAK HERE /!\
    }

    private fun sonarForward1() {
        sonarRepository.frontLeft.postValue(SensorState(true, false, true, 2))
        sonarRepository.frontCenter.postValue(SensorState(true, false, true, 3))
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 3))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 3))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 4))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 4))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 4))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 3))
        sonarRepository.rearCenter.postValue(SensorState(true, false, true, 3))
        sonarRepository.rearLeft.postValue(SensorState(true, false, true, 2))
    }

    private fun sonarRearDirection4() {
        sonarRepository.frontLeft.postValue(SensorState(true, false, true, 1))
        sonarRepository.frontCenter.postValue(SensorState(true, false, true, 2))
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 4))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 4))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 4))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 4))
        sonarRepository.rearCenter.postValue(SensorState(true, false, true, 5))
        sonarRepository.rearLeft.postValue(SensorState(true, false, true, 2))
    }

    private fun sonarRearDirection3() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 2))
        sonarRepository.frontCenter.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 4))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 4))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 2))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 3))
        sonarRepository.rearCenter.postValue(SensorState(true, false, true, 2))
        sonarRepository.rearLeft.postValue(SensorState(true, false, true, 1))
    }

    private fun sonarRearDirection2() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 4))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 3))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 1))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 2))
    }

    private fun sonarRearDirection1() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 3))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 0))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 1))
    }

    private fun sonarPlaceForward4() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 3))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 0))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 0))
    }

    private fun sonarPlaceForward3() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 0))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 1))
    }

    private fun sonarPlaceForward2() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 1))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 2))
    }

    private fun sonarPlaceForward() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 2))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 2))
    }

    private fun sonarPlaceDetected() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 2))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 1))
    }

    private fun sonarPlaceFar() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 3))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 2))
    }

    private fun sonarNoPlaceRight() {
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 1))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 2))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 3))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 3))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 2))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 1))
    }

    private fun resetSonar() {
        // Sonar(int sonarId,boolean isHwSupported,boolean isHatched,boolean isScanned,boolean isEnabled,int level)
        sonarRepository.frontLeft.postValue(SensorState(true, false, true, 0))
        sonarRepository.frontCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.frontRight.postValue(SensorState(true, false, true, 0))
        sonarRepository.rearLeft.postValue(SensorState(true, false, true, 0))
        sonarRepository.rearCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.rearRight.postValue(SensorState(true, false, true, 0))
        sonarRepository.leftFront.postValue(SensorState(true, false, true, 0))
        sonarRepository.leftFrontCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.leftRearCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.leftRear.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightFront.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightFrontCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightRearCenter.postValue(SensorState(true, false, true, 0))
        sonarRepository.rightRear.postValue(SensorState(true, false, true, 0))
    }
}