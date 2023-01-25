package com.renault.parkassist

import alliance.car.autopark.AutoPark.MESSAGE_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL
import alliance.car.sonar.AllianceCarSonarManager.*
import android.content.res.Configuration
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.apa.*
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.repository.sonar.SensorState
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.TrunkState.TRUNK_DOOR_CLOSED
import com.renault.parkassist.repository.surroundview.TrunkState.TRUNK_DOOR_OPENED
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.repository.surroundview.View.*
import com.renault.parkassist.utils.EspressoTestUtils
import com.renault.parkassist.utils.EspressoTestUtils.assertManeuverProgressBarColor
import com.renault.parkassist.utils.EspressoTestUtils.assertManeuverProgressBarMove
import com.renault.parkassist.utils.EspressoTestUtils.assertManeuverProgressBarValue
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import com.renault.parkassist.utils.EspressoTestUtils.withTagAsResourceId
import io.mockk.every
import java.lang.Thread.sleep
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HfpGuidanceTest : OverlayActivityTest() {

    @Before
    fun setup() {
        sonarRepository.rctaFeaturePresent = true
        automaticParkAssistRepository.featureConfiguration = FeatureConfig.HFP
        every { soundRepository.mute(any()) } answers {
            soundRepository.muted.postValue(firstArg())
        }

        // GIVEN scanning screen is displayed
        launchFullScreen()

        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // and Sonar sound feature is present
        soundRepository.soundActivationControlPresence = true
        soundRepository.temporaryMuteControlPresence = true
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
    }

    @Test
    fun should_display_portrait_avm_guidance_parallel_left_screen_when_the_user_selects_the_slot() { // ktlint-disable max-line-length
        // GIVEN portrait display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_PORTRAIT)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)
        // and scanning phase for parallel park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // and scanning side is set to left
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // and has detected slot (right)
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.rightSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // guidance instruction 'engage rear gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        // Navigation back button should be visible
        assertViewHasEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)

        // THEN gauge is not visible
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // THEN the guidance screen is displayed (guidance instruction, parkassist pictogram, gauge, parking direction (left / right, parallel maneuver), AVM front or rear view + bird view and disclaimer text)
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        Thread.sleep(200)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_left_background)
        assertViewHasEffectiveVisibility(R.id.parallel_left_parking_large)
        assertViewHasEffectiveVisibility(
            R.id.center_cut_arrow_curve_down,
            R.id.parallel_left_vehicle_center_cut
        )
        assertViewHasEffectiveVisibility(R.id.center_cut_car, R.id.parallel_left_vehicle_center_cut)

        // when the gauge completion is set to 0 in backward move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(0)

        val forward = false
        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        Thread.sleep(200)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_left_background)
        assertViewHasEffectiveVisibility(R.id.center_car, R.id.parallel_left_vehicle_center__back)
        assertViewHasEffectiveVisibility(
            R.id.center_arrow_straight_down,
            R.id.parallel_left_vehicle_center__back
        )

        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_straight_up,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_down,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_up,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop,
            R.id.parallel_left_vehicle_center__back
        )

        assertViewHasEffectiveVisibility(R.id.elt_hfp_camera_view_fragment)
        assertViewHasNoEffectiveVisibility(R.id.sonar_full_fragment_container)
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        displayCameraView(REAR_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        // easy park assist pictogram is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_easypark)
        // RCTA and mute
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_sonar_alerts_fragment)

        sonarRepository.raebFeaturePresent = true
        // and RAEB is disabled
        sonarRepository.raebAlertEnabled.postValue(false)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)

        // THEN the Sonar sound is unmuted
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                isDescendantOfA(withId(R.id.camera_container))
            )
        )
            .check(matches(isNotChecked()))
    }

    @Test
    fun should_display_portrait_avm_guidance_perpendicular_right_screen_when_the_user_selects_the_slot() { // ktlint-disable max-line-length
        // GIVEN portrait display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_PORTRAIT)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)
        // and scanning phase for parallel park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // and scanning side is set to right
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // and has detected slot (left)
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.leftSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // guidance instruction 'engage rear gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )
        // THEN gauge is not visible
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // THEN the guidance screen is displayed (guidance instruction, parkassist pictogram, gauge, parking direction (left / right, parallel maneuver), AVM front or rear view + bird view and disclaimer text)
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        Thread.sleep(200)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_right_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_right_parking_large)
        assertViewHasEffectiveVisibility(
            R.id.center_cut_arrow_curve_down,
            R.id.perpendicular_right_vehicle_center_cut
        )
        assertViewHasEffectiveVisibility(
            R.id.center_cut_car,
            R.id.perpendicular_right_vehicle_center_cut
        )

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_rear_gear)).check(matches(isDisplayed()))

        // when the gauge completion is set to 20 in backward move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(20)

        val forward = false
        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.2f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        Thread.sleep(200)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_center_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_vehicle_center_back)
        assertViewHasEffectiveVisibility(
            R.id.center_back_car,
            R.id.perpendicular_vehicle_center_back
        )
        assertViewHasEffectiveVisibility(
            R.id.center_back_arrow_straight_down,
            R.id.perpendicular_vehicle_center_back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_back_stop_back,
            R.id.perpendicular_vehicle_center_back
        )

        assertViewHasEffectiveVisibility(R.id.elt_hfp_camera_view_fragment)
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        displayCameraView(REAR_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasNoEffectiveVisibility(R.id.sonar_full_fragment_container)
        // easy park assist pictogram is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_easypark)
        // boot open disclaimer is displayed
        surroundViewRepository.trunkState.postValue(TRUNK_DOOR_OPENED)
        assertViewHasEffectiveVisibility(R.id.elt_boot_status_info)

        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        // THEN right RCTA is displayed
        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.rcta_left, R.id.camera_container)

        // Trunk door opened
        surroundViewRepository.trunkState.postValue(TRUNK_DOOR_OPENED)

        // and RAEB is enabled
        sonarRepository.raebAlertEnabled.postValue(true)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // THEN RAEB is displayed
        EspressoTestUtils.assertViewIsCompletelyDisplayed(R.id.raeb_left)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.elt_raeb_off)
    }

    @Test
    fun should_display_portrait_rvc_guidance_parallel_right_screen_when_the_user_selects_the_slot() { // ktlint-disable max-line-length
        // GIVEN portrait display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_PORTRAIT)
        // and RVC is present
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        navigateFullscreen(R.id.RvcHfpGuidanceFragment)
        // and scanning phase for parallel park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // and scanning side is set to right
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        // and has detected slot (left)
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.leftSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.FIRST)

        // guidance instruction 'engage rear gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )
        // THEN gauge is not visible
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // THEN the guidance screen is displayed (guidance instruction, parkassist pictogram, gauge, parking direction (left / right, parallel maneuver), AVM front or rear view + bird view and disclaimer text)
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        Thread.sleep(200)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_right_background)
        assertViewHasEffectiveVisibility(R.id.parallel_right_parking_large)
        assertViewHasEffectiveVisibility(
            R.id.center_cut_arrow_curve_down,
            R.id.parallel_right_vehicle_center_cut
        )
        assertViewHasEffectiveVisibility(
            R.id.center_cut_car,
            R.id.parallel_right_vehicle_center_cut
        )

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_rear_gear)).check(matches(isDisplayed()))

        assertViewHasEffectiveVisibility(R.id.elt_sonar_easypark)

        // when the gauge completion is set to 0 in backward move
        automaticParkAssistRepository.maneuverCompletion.postValue(80)

        val forward = false
        // guidance instruction 'drive backward' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.REVERSE
        )
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)

        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.8f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)
        assertViewHasEffectiveVisibility(R.id.elt_adas_gauge)

        Thread.sleep(200)
        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_right_background)
        assertViewHasEffectiveVisibility(R.id.parallel_right_vehicle_center__back)
        assertViewHasEffectiveVisibility(
            R.id.center_car,
            R.id.parallel_right_vehicle_center__back
        )
        assertViewHasEffectiveVisibility(
            R.id.center_arrow_straight_down,
            R.id.parallel_right_vehicle_center__back
        )

        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_straight_up,
            R.id.parallel_right_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_up,
            R.id.parallel_right_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_down,
            R.id.parallel_right_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve,
            R.id.parallel_right_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve,
            R.id.parallel_right_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop,
            R.id.parallel_right_vehicle_center__back
        )

        onView(withId(R.id.elt_avm_camera_view_container)).check(doesNotExist())
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)
        assertViewHasEffectiveVisibility(R.id.sonar_full_fragment_container)
        displayCameraView(REAR_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)

        // RCTA and mute
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        assertViewHasEffectiveVisibility(R.id.rcta_left, R.id.camera_container)

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)
        // WHEN the user selects the mute button
        onView(withId(R.id.sonar_mute_button)).perform(click())
        // THEN the Sonar sound is muted
        onView(withId(R.id.sonar_mute_button)).check(matches(isChecked()))

        sonarRepository.frontCenter.postValue(SensorState(true, false, true, 4))
        // and RAEB is enabled
        sonarRepository.raebAlertEnabled.postValue(true)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_2)
        // THEN RAEB is displayed
        assertViewHasEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )
        assertViewHasNoEffectiveVisibility(R.id.elt_raeb_off)
    }

    @Test
    fun should_display_portrait_rvc_guidance_perpendicular_left_screen_when_the_user_selects_the_slot() { // ktlint-disable max-line-length
        // GIVEN portrait display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_PORTRAIT)
        // and RVC is present
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        navigateFullscreen(R.id.RvcHfpGuidanceFragment)
        // and scanning phase for parallel park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // and scanning side is set to left
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // and has detected slot (right)
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.rightSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // guidance instruction 'engage rear gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )
        // THEN gauge is not visible
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // THEN the guidance screen is displayed (guidance instruction, parkassist pictogram, gauge, parking direction (left / right, parallel maneuver), AVM front or rear view + bird view and disclaimer text)
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        Thread.sleep(200)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_left_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_left_parking_large)
        assertViewHasEffectiveVisibility(
            R.id.center_cut_arrow_curve_down,
            R.id.perpendicular_left_vehicle_center_cut
        )
        assertViewHasEffectiveVisibility(
            R.id.center_cut_car,
            R.id.perpendicular_left_vehicle_center_cut
        )

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_rear_gear)).check(matches(isDisplayed()))

        // when the gauge completion is set to 20 in backward move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(60)

        val forward = false
        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.6f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        Thread.sleep(200)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_center_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_vehicle_center_back)
        assertViewHasEffectiveVisibility(
            R.id.center_back_car,
            R.id.perpendicular_vehicle_center_back
        )
        assertViewHasEffectiveVisibility(
            R.id.center_back_arrow_straight_down,
            R.id.perpendicular_vehicle_center_back
        )

        assertViewHasNoEffectiveVisibility(
            R.id.center_back_stop_back,
            R.id.perpendicular_vehicle_center_back
        )

        onView(withId(R.id.elt_avm_camera_view_container)).check(doesNotExist())
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)
        assertViewHasEffectiveVisibility(R.id.sonar_full_fragment_container)
        displayCameraView(REAR_VIEW)
        // easy park assist pictogram is displayed
        assertViewHasEffectiveVisibility(R.id.elt_sonar_easypark)

        assertViewHasEffectiveVisibility(R.id.camera_container)

        sonarRepository.raebFeaturePresent = true
        // and RAEB is disabled
        sonarRepository.raebAlertEnabled.postValue(false)
        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(RAEB_NOT_OPERATIONAL)
        // THEN RAEB OFF is displayed
        assertViewHasNoEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
    }

    @Test
    fun should_display_landscape_avm_guidance_parallel_left_screen_when_the_user_selects_the_slot() { // ktlint-disable max-line-length
        // GIVEN landscape display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)
        // and scanning phase for parallel park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // and scanning side is set to left
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // and has detected slot (right)
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.rightSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // and guidance instruction 'engage forward gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR
        )
        // Then gauge is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // THEN the guidance screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        clickOnView(R.id.elt_adas_camera_switch)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_left_background)
        assertViewHasEffectiveVisibility(R.id.center_car, R.id.parallel_left_vehicle_center__front)
        assertViewHasEffectiveVisibility(
            R.id.center_arrow_straight_up,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_up,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_straight_down,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_down,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop,
            R.id.parallel_left_vehicle_center__front
        )

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_forward_gear)).check(
            matches(
                isDisplayed()
            )
        )

        // when the gauge completion is set to 10 in backward move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(10)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_left_background)
        assertViewHasEffectiveVisibility(R.id.center_car, R.id.parallel_left_vehicle_center__front)
        assertViewHasEffectiveVisibility(
            R.id.center_arrow_straight_up,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_up,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_straight_down,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_down,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve,
            R.id.parallel_left_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop,
            R.id.parallel_left_vehicle_center__front
        )

        clickOnView(R.id.elt_adas_camera_switch)

        val forward = true
        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.1f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        displayCameraView(REAR_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasNoEffectiveVisibility(R.id.sonar_full_fragment_container)
        // AVM rear view + bird view is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        // easy park assist pictogram is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_easypark)

        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_rear)))
        // Camera switched is checked
        onView(Matchers.allOf(withId(R.id.elt_adas_camera_switch))).check(matches(isSelected()))

        // and RCTA right alert sent
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        // avm sonar_alerts right is displayed on camera view
        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_sonar_alerts_fragment)

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)

        // sonar sound is unmuted
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                isDescendantOfA(withId(R.id.elt_camera_overlay_container))
            )
        ).check(matches(isNotChecked()))

        // and disclaimer text is displayed
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        // Trunk door closed
        surroundViewRepository.trunkState.postValue(TRUNK_DOOR_CLOSED)
        // boot open warning is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_boot_status_info)

        // and RAEB is enabled
        sonarRepository.raebAlertEnabled.postValue(true)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // THEN RAEB is displayed
        assertViewHasEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )
        assertViewHasNoEffectiveVisibility(
            R.id.elt_raeb_off,
            R.id.camera_ovl_avm_apa_fragment
        )
    }

    @Test
    fun should_display_landscape_avm_guidance_perpendicular_right_screen_when_the_user_selects_the_slot() { // ktlint-disable max-line-length
        // GIVEN landscape display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)
        // and scanning phase for parallel park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // and scanning side is set to right
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // and has detected slot (left)
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.leftSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // guidance instruction 'engage forward gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR
        )
        // gauge is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // THEN the guidance screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        clickOnView(R.id.elt_adas_camera_switch)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_center_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_vehicle_center_front)
        assertViewHasEffectiveVisibility(
            R.id.center_front_car,
            R.id.perpendicular_vehicle_center_front
        )
        assertViewHasEffectiveVisibility(
            R.id.center_front_arrow_straight_up,
            R.id.perpendicular_vehicle_center_front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_front_stop_front,
            R.id.perpendicular_vehicle_center_front
        )

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_forward_gear)).check(
            matches(
                isDisplayed()
            )
        )

        // when the gauge completion is set to 40 in backward move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(40)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_center_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_vehicle_center_front)
        assertViewHasEffectiveVisibility(
            R.id.center_front_car,
            R.id.perpendicular_vehicle_center_front
        )
        assertViewHasEffectiveVisibility(
            R.id.center_front_arrow_straight_up,
            R.id.perpendicular_vehicle_center_front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_front_stop_front,
            R.id.perpendicular_vehicle_center_front
        )

        clickOnView(R.id.elt_adas_camera_switch)

        val forward = true
        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.4f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        // Front view request
        displayCameraView(FRONT_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasNoEffectiveVisibility(R.id.sonar_full_fragment_container)
        // AVM front view + bird view is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        // easy park assist pictogram is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_easypark)
        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_front)))
        // Camera switched is checked
        onView(Matchers.allOf(withId(R.id.elt_adas_camera_switch))).check(matches(isSelected()))

        // and RCTA right alert sent
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        // display apa rear view
        displayCameraView(REAR_VIEW)
        // avm sonar_alerts right is displayed on camera view
        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_container)

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)

        // sonar sound is unmuted
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                isDescendantOfA(withId(R.id.camera_container))
            )
        ).check(matches(isNotChecked()))

        // and disclaimer text is displayed
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        // Trunk door opened
        surroundViewRepository.trunkState.postValue(TRUNK_DOOR_OPENED)
        // boot open disclaimer is displayed
        assertViewHasEffectiveVisibility(R.id.elt_boot_status_info)

        // and RAEB is enabled
        sonarRepository.raebAlertEnabled.postValue(true)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_2)
        // THEN RAEB is displayed
        assertViewHasEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )
        assertViewHasNoEffectiveVisibility(
            R.id.elt_raeb_off,
            R.id.camera_ovl_avm_apa_fragment
        )
    }

    @Test
    fun should_display_landscape_rvc_guidance_parallel_right_screen_when_the_user_selects_the_slot() { // ktlint-disable max-line-length
        // GIVEN lanscape display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        navigateFullscreen(R.id.RvcHfpGuidanceFragment)
        // and scanning phase for parallel park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // and scanning side is set to right
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // and has detected slot (left)
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.leftSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // and guidance instruction 'engage forward gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR
        )
        // gauge is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // THEN the guidance screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        clickOnView(R.id.elt_adas_camera_switch)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_right_background)
        assertViewHasEffectiveVisibility(R.id.center_car, R.id.parallel_right_vehicle_center__front)
        assertViewHasEffectiveVisibility(
            R.id.center_arrow_straight_up,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_up,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_straight_down,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_down,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop,
            R.id.parallel_right_vehicle_center__front
        )

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_forward_gear)).check(
            matches(
                isDisplayed()
            )
        )

        // when the gauge completion is set to 10 in backward move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(10)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_right_background)
        assertViewHasEffectiveVisibility(
            R.id.center_car,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasEffectiveVisibility(
            R.id.center_arrow_straight_up,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_up,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_straight_down,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_down,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve,
            R.id.parallel_right_vehicle_center__front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop,
            R.id.parallel_right_vehicle_center__front
        )

        clickOnView(R.id.elt_adas_camera_switch)

        val forward = true
        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.1f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        displayCameraView(REAR_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        // easy park assist pictogram is displayed
        assertViewHasEffectiveVisibility(R.id.elt_sonar_easypark)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_rear)))

        assertViewHasEffectiveVisibility(R.id.sonar_full_fragment_container)
        assertViewHasEffectiveVisibility(R.id.sonar_sensor_fragment)
        // Camera switched is checked
        onView(Matchers.allOf(withId(R.id.elt_adas_camera_switch))).check(matches(isSelected()))

        // and RCTA left and right alert sent
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        sonarRepository.raebFeaturePresent = true
        // and RAEB is disabled
        sonarRepository.raebAlertEnabled.postValue(false)
        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(
            R.id.elt_raeb_off,
            R.id.camera_ovl_rvc_apa_fragment
        )
        assertViewHasNoEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )
        assertViewHasNoEffectiveVisibility(
            R.id.elt_sonar_raeb_off,
            R.id.sonar_full_fragment_container
        )

        clickOnView(R.id.elt_adas_camera_switch)
        // avm sonar_alerts left and right are displayed on  sonar view
        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.sonar_full_fragment_container)
        assertViewHasEffectiveVisibility(R.id.rcta_left, R.id.sonar_full_fragment_container)

        // THEN RAEB OFF is displayed in sonar view
        assertViewHasEffectiveVisibility(
            R.id.elt_sonar_raeb_off,
            R.id.sonar_full_fragment_container
        )
        assertViewHasNoEffectiveVisibility(
            R.id.raeb_left,
            R.id.sonar_full_fragment_container
        )

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)

        // WHEN the user selects the mute button
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                isDescendantOfA(withId(R.id.sonar_full_fragment_container))
            )
        ).perform(click())
        // THEN the Sonar sound is muted
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                isDescendantOfA(withId(R.id.sonar_full_fragment_container))
            )
        ).check(matches(isChecked()))

        // and disclaimer text is displayed
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        // Trunk door closed
        surroundViewRepository.trunkState.postValue(TRUNK_DOOR_CLOSED)
        // boot open warning is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_boot_status_info)
    }

    @Test
    fun should_display_landscape_rvc_guidance_perpendicular_left_screen_when_the_user_selects_the_slot() { // ktlint-disable max-line-length
        // GIVEN landscape display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        navigateFullscreen(R.id.RvcHfpGuidanceFragment)
        // and scanning phase for perpendicular park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // and scanning side is set to left
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // and guidance instruction 'engage forward gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR
        )
        // gauge is displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // THEN the guidance screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        clickOnView(R.id.elt_adas_camera_switch)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_center_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_vehicle_center_front)
        assertViewHasEffectiveVisibility(
            R.id.center_front_car,
            R.id.perpendicular_vehicle_center_front
        )
        assertViewHasEffectiveVisibility(
            R.id.center_front_arrow_straight_up,
            R.id.perpendicular_vehicle_center_front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_front_stop_front,
            R.id.perpendicular_vehicle_center_front
        )

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_forward_gear)).check(
            matches(
                isDisplayed()
            )
        )

        // when the gauge completion is set to 80 in forward move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.FORWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(80)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_center_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_vehicle_center_front)
        assertViewHasEffectiveVisibility(
            R.id.center_front_car,
            R.id.perpendicular_vehicle_center_front
        )
        assertViewHasEffectiveVisibility(
            R.id.center_front_arrow_straight_up,
            R.id.perpendicular_vehicle_center_front
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_front_stop_front,
            R.id.perpendicular_vehicle_center_front
        )

        clickOnView(R.id.elt_adas_camera_switch)

        val forward = true
        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.8f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        // TODO apa_state management with a binder
        displayCameraView(REAR_VIEW)

        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_rear)))
        assertViewHasEffectiveVisibility(R.id.sonar_full_fragment_container)
        assertViewHasEffectiveVisibility(R.id.sonar_sensor_fragment)

        // Trunk door opened
        surroundViewRepository.trunkState.postValue(TRUNK_DOOR_OPENED)
        // boot open warning is displayed
        assertViewHasEffectiveVisibility(R.id.elt_boot_status_info)

        // rvc sonar view is displayed
        assertViewHasEffectiveVisibility(R.id.sonar_full_fragment_container)
        // Camera switched is checked
        onView(Matchers.allOf(withId(R.id.elt_adas_camera_switch))).check(matches(isSelected()))

        // and RCTA left alert sent
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        // avm sonar_alerts left is displayed on both camera and sonar views
        clickOnView(R.id.elt_adas_camera_switch)
        assertViewHasEffectiveVisibility(R.id.rcta_left, R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.rcta_left, R.id.sonar_full_fragment_container)

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)

        // and sonar sound is unmuted
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                isDescendantOfA(withId(R.id.sonar_full_fragment_container))
            )
        ).check(matches(isNotChecked()))

        // and disclaimer text is displayed
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)
    }

    @Test
    fun should_switch_from_landscape_avm_guidance_camera_view_to_parking_picture_view() { // ktlint-disable max-line-length
        // GIVEN landscape display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)
        // and scanning phase for parallel park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // and scanning side is set to left
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // and has detected slot (right)
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.rightSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // guidance instruction 'engage rear gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )
        // THEN gauge color is green
        assertManeuverProgressBarColor(R.id.elt_adas_gauge, R.color.gauge_normal)

        // THEN the guidance screen is displayed (guidance instruction, parkassist pictogram, gauge,
        // parking direction (left / right, parallel maneuver),
        // AVM front or rear view + bird view and disclaimer text)
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_rear_gear)).check(matches(isDisplayed()))

        // when the gauge completion is set to 90 in backward move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(90)

        Thread.sleep(200)

        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.9f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, false)

        surroundViewRepository.surroundState.postValue(SurroundState(View.REAR_VIEW, false))

        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        displayCameraView(REAR_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasNoEffectiveVisibility(R.id.sonar_full_fragment_container)

        // easy park assist pictogram is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_easypark)
        // RCTA and mute
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_sonar_alerts_fragment)

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)

        // THEN the Sonar sound is unmuted
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                isDescendantOfA(withId(R.id.camera_ovl_avm_apa_fragment))
            )
        ).check(matches(isNotChecked()))

        sonarRepository.raebFeaturePresent = true
        // and RAEB is disabled
        sonarRepository.raebAlertEnabled.postValue(false)
        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(
            R.id.elt_raeb_off,
            R.id.camera_ovl_avm_apa_fragment
        )
        assertViewHasNoEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )

        clickOnView(R.id.elt_adas_camera_switch)

        // THEN RAEB OFF is displayed in sonar view
        assertViewHasEffectiveVisibility(
            R.id.elt_raeb_off,
            R.id.sonar_alerts_guidance_picture
        )
        assertViewHasNoEffectiveVisibility(
            R.id.raeb_left,
            R.id.sonar_alerts_guidance_picture
        )

        onView(Matchers.allOf(withId(R.id.elt_adas_camera_switch))).check(
            matches(
                not(isSelected())
            )
        )

        assertViewHasEffectiveVisibility(R.id.hfp_guidance_picture)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_left_background)
        assertViewHasEffectiveVisibility(R.id.center_car, R.id.parallel_left_vehicle_center__back)
        assertViewHasEffectiveVisibility(
            R.id.center_arrow_straight_down,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_straight_up,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_up,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_arrow_curve_down,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve,
            R.id.parallel_left_vehicle_center__back
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop,
            R.id.parallel_left_vehicle_center__back
        )
    }

    @Test
    fun should_switch_from_landscape_guidance_picture_view_to_avm_guidance_camera_view() { // ktlint-disable max-line-length
        // GIVEN landscape display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)
        // and scanning phase for parallel park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // and scanning side is set to left
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // and has detected slot (right)
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.rightSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // guidance instruction 'engage rear gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )
        // gauge is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // guidance picture view selected
        clickOnView(R.id.elt_adas_camera_switch)
        onView(Matchers.allOf(withId(R.id.elt_adas_camera_switch))).check(
            matches(
                not(isSelected())
            )
        )
        assertViewHasEffectiveVisibility(R.id.hfp_guidance_picture)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.parallel_left_background)
        assertViewHasEffectiveVisibility(R.id.parallel_left_parking_large)
        assertViewHasEffectiveVisibility(
            R.id.center_cut_arrow_curve_down,
            R.id.parallel_left_vehicle_center_cut
        )
        assertViewHasEffectiveVisibility(R.id.center_cut_car, R.id.parallel_left_vehicle_center_cut)

        // when the user selects the camera view
        clickOnView(R.id.elt_adas_camera_switch)

        // THEN the guidance screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)
        assertViewHasNoEffectiveVisibility(R.id.hfp_guidance_picture)

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_rear_gear)).check(matches(isDisplayed()))

        // when the gauge completion is set to 10 in backward move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.BACKWARD)
        automaticParkAssistRepository.maneuverCompletion.postValue(10)

        val forward = false
        // then the gauge display is updated with backward move
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.1f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        displayCameraView(REAR_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasNoEffectiveVisibility(R.id.sonar_full_fragment_container)
        // easy park assist pictogram is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_easypark)
        // AVM rear view + bird view is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_rear)))

        // Camera switched is checked
        onView(Matchers.allOf(withId(R.id.elt_adas_camera_switch))).check(matches(isSelected()))

        // and RCTA right alert sent
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        // avm sonar_alerts right is displayed on camera view
        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_sonar_alerts_fragment)

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)

        // sonar sound is unmuted
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                isDescendantOfA(withId(R.id.camera_sonar_mute_fragment))
            )
        ).check(matches(isNotChecked()))

        // and disclaimer text is displayed
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        // Trunk door closed
        surroundViewRepository.trunkState.postValue(TRUNK_DOOR_CLOSED)
        // boot open warning is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_boot_status_info)
    }

    @Test
    fun should_display_landscape_avm_guidance_instruction_update() { // ktlint-disable max-line-length
        // GIVEN landscape display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)
        // and scanning phase for perpendicular park-in maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // and scanning side is set to right
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // and has detected slot (left)
        automaticParkAssistRepository.leftSuitable.postValue(false)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.leftSelected.postValue(false)

        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // guidance instruction 'engage rear gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )
        // THEN gauge color is green
        assertManeuverProgressBarColor(R.id.elt_adas_gauge, R.color.gauge_normal)

        // guidance screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        // guidance picture view selected
        clickOnView(R.id.elt_adas_camera_switch)
        onView(Matchers.allOf(withId(R.id.elt_adas_camera_switch))).check(
            matches(
                not(isSelected())
            )
        )
        assertViewHasEffectiveVisibility(R.id.hfp_guidance_picture)
        Thread.sleep(200)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_right_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_right_parking_large)
        assertViewHasEffectiveVisibility(
            R.id.center_cut_arrow_curve_down,
            R.id.perpendicular_right_vehicle_center_cut
        )
        assertViewHasEffectiveVisibility(
            R.id.center_cut_car,
            R.id.perpendicular_right_vehicle_center_cut
        )

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_rear_gear)).check(matches(isDisplayed()))
        // when the maneuver guidance instruction 'Stop' is sent
        automaticParkAssistRepository.extendedInstruction.postValue(Instruction.STOP)

        Thread.sleep(200)

        // guidance illustration check
        assertViewHasEffectiveVisibility(R.id.perpendicular_center_background)
        assertViewHasEffectiveVisibility(R.id.perpendicular_vehicle_center_back__stop)
        assertViewHasEffectiveVisibility(
            R.id.center_back_car,
            R.id.perpendicular_vehicle_center_back__stop
        )
        assertViewHasEffectiveVisibility(
            R.id.center_back_stop_back,
            R.id.perpendicular_vehicle_center_back__stop
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_back_arrow_straight_down,
            R.id.perpendicular_vehicle_center_back__stop
        )

        assertViewHasNoEffectiveVisibility(R.id.perpendicular_right_background)
        assertViewHasNoEffectiveVisibility(R.id.perpendicular_right_parking_large)
        assertViewHasNoEffectiveVisibility(
            R.id.center_cut_arrow_curve_down,
            R.id.perpendicular_right_vehicle_center_cut
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_cut_car,
            R.id.perpendicular_right_vehicle_center_cut
        )

        // THEN gauge color is red
        assertManeuverProgressBarColor(R.id.elt_adas_gauge, R.color.gauge_stop)
        // THEN the instruction label 'Stop after rear gear engaged'
        // is displayed and udpated accordingly
        onView(withText(R.string.rlb_parkassist_apa_stop)).check(matches(isDisplayed()))

        // gauge is displayed
        assertViewHasEffectiveVisibility(R.id.elt_adas_gauge)

        clickOnView(R.id.elt_adas_camera_switch)

        // Front view request
        displayCameraView(FRONT_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)

        // AVM front view + bird view is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        // easy park assist pictogram is displayed
        assertViewHasEffectiveVisibility(R.id.elt_camera_easypark)
        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_front)))
        // Camera switched is checked
        onView(Matchers.allOf(withId(R.id.elt_adas_camera_switch))).check(matches(isSelected()))

        // and RCTA right alert sent
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        // display apa rear view
        displayCameraView(REAR_VIEW)

        // avm sonar_alerts right is displayed on camera view
        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_sonar_alerts_fragment)

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)

        // sonar sound is unmuted
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                isDescendantOfA(withId(R.id.camera_container))
            )
        ).check(matches(isNotChecked()))

        // and disclaimer text is displayed
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        // Trunk door opened
        surroundViewRepository.trunkState.postValue(TRUNK_DOOR_OPENED)
        // boot open disclaimer is displayed
        assertViewHasEffectiveVisibility(R.id.elt_boot_status_info)
    }

    @Test
    fun should_display_rcta_flags_in_sonar_view_from_rvc_and_landscape() { // ktlint-disable max-line-length

        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        navigateFullscreen(R.id.RvcHfpGuidanceFragment)
        // GIVEN HFP guidance screen is displayed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        // and Orientation is landscape

        // display apa rear view
        displayCameraView(REAR_VIEW)

        // WHEN a coming vehicle or obstacle is detected
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        // THEN sonar alerts are not displayed
        assertViewHasNoEffectiveVisibility(
            R.id.alerts_sonar_view,
            R.id.sonar_full_fragment_container
        )

        // WHEN the user selects the camera switch
        clickOnView(R.id.elt_adas_camera_switch)
        // THEN the right sonar_alerts flag is display
        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.sonar_full_fragment_container)
        assertViewHasEffectiveVisibility(
            R.id.alerts_sonar_view,
            R.id.sonar_full_fragment_container
        )

        // WHEN the user selects the camera switch
        clickOnView(R.id.elt_adas_camera_switch)
        // THEN sonar alerts are not displayed
        assertViewHasNoEffectiveVisibility(
            R.id.alerts_sonar_view,
            R.id.sonar_full_fragment_container
        )
    }

    @Test
    fun should_hide_maneuver_progress_bar_when_instruction_accelerate_and_hold_pedal() {
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)

        // and has detected slot (right)
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // WHEN the user selects the slot through turn indicators
        automaticParkAssistRepository.rightSelected.postValue(true)

        // and scanning phase is completed
        automaticParkAssistRepository.perpendicularManeuverSelection.postValue(
            ManeuverSelection.SELECTED
        )
        /*setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)*/

        automaticParkAssistRepository.extendedInstruction.postValue(Instruction.DRIVE_FORWARD)
        sleep(5000)
        assertViewHasEffectiveVisibility(R.id.elt_adas_gauge)

        // WHEN instruction is accelerate and hold the pedal
        automaticParkAssistRepository.extendedInstruction
            .postValue(Instruction.ACCELERATE_AND_HOLD_THE_PEDAL_PRESSED)
        // THEN maneuver progress bar disappear
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)
    }

    @Test
    fun should_not_display_dialog_box_on_relevant_warning_reception() {
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        navigateFullscreen(R.id.RvcHfpGuidanceFragment)
        // GIVEN HFP guidance screen is displayed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.warningMessage.postValue(
            MESSAGE_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL
        )
        waitDialog()
        onView(withText(R.string.rlb_ok)).check(doesNotExist())
        onView(withText(R.string.rlb_parkassist_apa_neutral_button_label)).check(doesNotExist())
        onView(withText(R.string.rlb_parkassist_apa_negative_button_label)).check(doesNotExist())
    }

    @Test
    fun should_display_gauge_in_portrait_avm_guidance_parkout_first_move() { // ktlint-disable max-line-length
        // GIVEN portrait display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_PORTRAIT)
        // and AVM is present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)
        // and scanning phase for parkout maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.parkOutManeuverSelection)
        // and scanning phase is completed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // guidance instruction 'engage rear gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )
        // THEN gauge is not visible
        assertViewHasNoEffectiveVisibility(R.id.elt_adas_gauge)

        // THEN the guidance screen is displayed (guidance instruction, parkassist pictogram, gauge, parking direction (left / right, parallel maneuver), AVM front or rear view + bird view and disclaimer text)
        assertViewHasEffectiveVisibility(R.id.apa_sonarwithguidance)

        // guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_apa_engage_rear_gear)).check(matches(isDisplayed()))

        // when the gauge completion is set to 20 in 1st move
        automaticParkAssistRepository.maneuverMove.postValue(ManeuverMove.FIRST)
        automaticParkAssistRepository.maneuverCompletion.postValue(20)
        // and guidance instruction 'reverse' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.REVERSE
        )
        Thread.sleep(200)

        val forward = false
        // THEN gauge is visible
        assertViewHasEffectiveVisibility(R.id.elt_adas_gauge)
        // and the gauge display is updated with first move and reverse instruction
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.2f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        // when the gauge completion is set to 80
        automaticParkAssistRepository.maneuverCompletion.postValue(80)
        // and guidance instruction 'stop' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.STOP
        )
        Thread.sleep(200)

        // THEN gauge is visible
        assertViewHasEffectiveVisibility(R.id.elt_adas_gauge)
        // and the gauge display is kept with same direction
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.8f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, forward)

        // when the gauge completion is set to 20
        automaticParkAssistRepository.maneuverCompletion.postValue(20)
        // and guidance instruction 'drive forward' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD
        )
        Thread.sleep(200)

        // THEN gauge is visible
        assertViewHasEffectiveVisibility(R.id.elt_adas_gauge)
        // and the gauge display is updated with first move and drive forward instruction
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 0.2f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, true)

        // when the gauge completion is set to 100
        automaticParkAssistRepository.maneuverCompletion.postValue(100)
        // and guidance instruction 'stop' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.STOP
        )
        Thread.sleep(200)

        // THEN gauge is visible
        assertViewHasEffectiveVisibility(R.id.elt_adas_gauge)
        // and the gauge display is kept with same direction
        assertManeuverProgressBarValue(R.id.elt_adas_gauge, 1.0f)
        assertManeuverProgressBarMove(R.id.elt_adas_gauge, true)
    }

    private fun waitDialog() = Thread.sleep(500)

    private fun setManeuverTypeSelection(maneuverSelection: MutableLiveData<Int>?) {
        when (maneuverSelection) {
            automaticParkAssistRepository.parallelManeuverSelection -> {
                automaticParkAssistRepository.perpendicularManeuverSelection.postValue(
                    ManeuverSelection.NOT_SELECTED
                )
                automaticParkAssistRepository.parkOutManeuverSelection.postValue(
                    ManeuverSelection.NOT_SELECTED
                )
                automaticParkAssistRepository.parallelManeuverSelection.postValue(
                    ManeuverSelection.SELECTED
                )
            }
            automaticParkAssistRepository.perpendicularManeuverSelection -> {
                automaticParkAssistRepository.parallelManeuverSelection.postValue(
                    ManeuverSelection.NOT_SELECTED
                )
                automaticParkAssistRepository.parkOutManeuverSelection.postValue(
                    ManeuverSelection.NOT_SELECTED
                )
                automaticParkAssistRepository.perpendicularManeuverSelection.postValue(
                    ManeuverSelection.SELECTED
                )
            }
            automaticParkAssistRepository.parkOutManeuverSelection -> {
                automaticParkAssistRepository.parallelManeuverSelection.postValue(
                    ManeuverSelection.NOT_SELECTED
                )
                automaticParkAssistRepository.perpendicularManeuverSelection.postValue(
                    ManeuverSelection.NOT_SELECTED
                )
                automaticParkAssistRepository.parkOutManeuverSelection.postValue(
                    ManeuverSelection.SELECTED
                )
            }
            else -> {
                automaticParkAssistRepository.parallelManeuverSelection.postValue(
                    ManeuverSelection.NOT_SELECTED
                )
                automaticParkAssistRepository.perpendicularManeuverSelection.postValue(
                    ManeuverSelection.NOT_SELECTED
                )
                automaticParkAssistRepository.parkOutManeuverSelection.postValue(
                    ManeuverSelection.NOT_SELECTED
                )
            }
        }
    }

    @Test
    fun should_not_display_camera_in_hfp_when_avm_display_off() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)
        // and scanning phase for parkout maneuver
        setManeuverTypeSelection(automaticParkAssistRepository.parkOutManeuverSelection)
        // and scanning phase is completed
        surroundViewRepository.surroundState.postValue(SurroundState(View.FRONT_VIEW, false))
        assertViewHasEffectiveVisibility(R.id.camera_container)
        surroundViewRepository.surroundState.postValue(SurroundState(View.NO_DISPLAY, false))
        assertViewHasNoEffectiveVisibility(R.id.camera_container)
    }
}