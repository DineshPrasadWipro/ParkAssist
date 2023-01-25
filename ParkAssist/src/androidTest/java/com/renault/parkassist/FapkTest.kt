package com.renault.parkassist

import alliance.car.autopark.AutoPark
import alliance.car.autopark.AutoPark.DISPLAY_GUIDANCE
import alliance.car.autopark.AutoPark.MESSAGE_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE
import alliance.car.sonar.AllianceCarSonarManager
import alliancex.arch.core.logger.logD
import android.content.res.Configuration
import androidx.annotation.IdRes
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
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.TrunkState
import com.renault.parkassist.repository.surroundview.View.APA_FRONT_VIEW
import com.renault.parkassist.repository.surroundview.View.APA_REAR_VIEW
import com.renault.parkassist.utility.extractLocationOnScreen
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsDisabled
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsNotSelected
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import com.renault.parkassist.utils.EspressoTestUtils.withTagAsResourceId
import com.renault.parkassist.utils.actions.TouchDownAndUp
import com.renault.parkassist.utils.assertRenautToggleIconButtonActive
import com.renault.parkassist.utils.assertRenautToggleIconButtonNotActive
import com.renault.parkassist.viewmodel.avm.Finger
import io.mockk.every
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_camera.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertNotNull

@LargeTest
@RunWith(AndroidJUnit4::class)
class FapkTest : OverlayActivityTest() {

    @Before
    fun setup() {
        every { soundRepository.mute(any()) } answers {
            soundRepository.muted.postValue(firstArg())
        }
        automaticParkAssistRepository.apply {
            every { requestManeuverType(ManeuverType.PARALLEL) } answers {
                parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
                perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
                parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
            }
            every { requestManeuverType(ManeuverType.PERPENDICULAR) } answers {
                parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
                perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
                parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
            }
            every { requestManeuverType(ManeuverType.PARKOUT) } answers {
                parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
                perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
                parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
            }
        }

        automaticParkAssistRepository.featureConfiguration = FeatureConfig.FAPK
        setVehicleConfiguration(ParkAssistHwConfig.AVM)

        // WHEN FAPK scanning screen is displayed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // and Sonar sound feature is present
        soundRepository.soundActivationControlPresence = true
        soundRepository.temporaryMuteControlPresence = true
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        launchFullScreen()
        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.parallelManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.supportedManeuvers.add(0, ManeuverType.PARALLEL)
        automaticParkAssistRepository.supportedManeuvers.add(1, ManeuverType.PERPENDICULAR)
        automaticParkAssistRepository.supportedManeuvers.add(2, ManeuverType.PARKOUT)
    }

    @Test
    fun should_display_correct_screen_when_avm_present() { // ktlint-disable max-line-length
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // Maneuver Start Switch display
        automaticParkAssistRepository.maneuverSwitchSelection.postValue(
            ManeuverStartSwitch.DISPLAY_START
        )

        // and avm display FRONT_VIEW
        displayCameraView(APA_FRONT_VIEW)
        // THEN fapk scanning screen (APA_main) is displayed
        assertViewHasEffectiveVisibility(R.id.fapk_main)
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            assertViewHasEffectiveVisibility(R.id.fapk_guidance_instruction)
            assertViewHasEffectiveVisibility(R.id.toolbar_nav)
        }

        assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_parallel)
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_perpendicular)
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_park_out)
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_switch_start)
        assertViewHasNoEffectiveVisibility(R.id.elt_fapk_maneuver_switch_stop)
        // easy park assist pictogram is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_camera_easypark)

        assertViewHasEffectiveVisibility(R.id.avm_camera_view)
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)

        // Settings icon is present
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)

        // RCTA and mute in rear view
        displayCameraView(APA_REAR_VIEW)
        sonarRepository.collisionAlertSide.postValue(AllianceCarSonarManager.SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(
            AllianceCarSonarManager.LEVEL_HIGH_COLLISION_RISK
        )
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_sonar_alerts_fragment)

        // and Upa activated
        sonarRepository.frontState.postValue(GroupState.ENABLED)

        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        assertViewHasEffectiveVisibility(R.id.sonar_mute_button)

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
    fun should_display_correct_active_button_when_user_select_maneuver() { // ktlint-disable max-line-length
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // Maneuver Start Switch display
        automaticParkAssistRepository.maneuverSwitchSelection.postValue(
            ManeuverStartSwitch.DISPLAY_START
        )
        // AND all buttons are enable but not selected
        automaticParkAssistRepository.parallelManeuverSelection.postValue(
            ManeuverSelection.NOT_SELECTED
        )
        automaticParkAssistRepository.perpendicularManeuverSelection.postValue(
            ManeuverSelection.SELECTED
        )
        automaticParkAssistRepository.parkOutManeuverSelection.postValue(
            ManeuverSelection.NOT_SELECTED
        )
        automaticParkAssistRepository.maneuverSwitchSelection.postValue(
            ManeuverStartSwitch.DISPLAY_CANCEL
        )
        // and avm display REAR_VIEW
        displayCameraView(APA_REAR_VIEW)

        // WHEN user select maneuver parallel
        onView(withId(R.id.elt_fapk_maneuver_parallel)).perform(click())
        // THEN parallel button is active
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_parallel)

        // WHEN user select maneuver perpendicular
        onView(withId(R.id.elt_fapk_maneuver_perpendicular)).perform(click())
        // THEN perpendicular button is active
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_perpendicular)

        // WHEN user select maneuver parkout
        onView(withId(R.id.elt_fapk_maneuver_park_out)).perform(click())
        // THEN parkout button is active
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_park_out)
        // AND easy park icon is not present
        assertViewHasNoEffectiveVisibility(R.id.elt_camera_easypark)

        // WHEN user select STOP maneuver
        onView(withId(R.id.elt_fapk_maneuver_switch_stop)).perform(click())
        // THEN START maneuver button is active
        onView(withId(R.id.elt_fapk_maneuver_switch_stop))
            .check(matches(isSelected()))
        automaticParkAssistRepository.automaticManeuver.postValue(true)

        // AND easy park icon is not present
        assertViewHasNoEffectiveVisibility(R.id.elt_camera_easypark)

        assertViewHasEffectiveVisibility(R.id.avm_camera_view)
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)

        // Settings icon is not present (rear view)
        assertViewHasNoEffectiveVisibility(R.id.toolbar_icon_settings)

        // RCTA and mute
        sonarRepository.collisionAlertSide.postValue(AllianceCarSonarManager.SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(
            AllianceCarSonarManager.LEVEL_HIGH_COLLISION_RISK
        )
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_sonar_alerts_fragment)
    }

    @Test
    fun should_display_mute_unmute_button_on_user_action() { // ktlint-disable max-line-length
        soundRepository.temporaryMuteControlPresence = true
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.automaticManeuver.postValue(false)

        // WHEN Sonar sound is not muted
        soundRepository.muted.postValue(false)
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Upa activate
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)

        // and avm display REAR_VIEW
        displayCameraView(APA_REAR_VIEW)

        assertViewHasEffectiveVisibility(R.id.sonar_mute_button)

        //  THEN sonar sound is unmuted
        onView(
            withId(R.id.sonar_mute_button)
        ).check(matches(isNotChecked()))

        // WHEN the user selects the mute button
        clickOnView(R.id.sonar_mute_button)
        // THEN the sonar sound is muted
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button)
            )
        ).check(matches(isChecked()))

        assertViewHasEffectiveVisibility(R.id.avm_camera_view)
        assertViewHasEffectiveVisibility(R.id.elt_easypark_disclaimer)

        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)

        // easy park assist pictogram is displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_camera_easypark)
        // boot open disclaimer is displayed
        surroundViewRepository.trunkState.postValue(TrunkState.TRUNK_DOOR_OPENED)
        assertViewHasEffectiveVisibility(R.id.elt_boot_status_info)

        sonarRepository.collisionAlertSide.postValue(AllianceCarSonarManager.SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(
            AllianceCarSonarManager.LEVEL_HIGH_COLLISION_RISK
        )
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        // THEN right RCTA is displayed
        assertViewHasEffectiveVisibility(R.id.rcta_right, R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.rcta_left, R.id.camera_container)
    }

    @Test
    fun should_display_rcta_when_rcta_events_occur() { // ktlint-disable max-line-length
        sonarRepository.rctaFeaturePresent = true
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        // When scanning phase for parallel park-in maneuver
        automaticParkAssistRepository.parallelManeuverSelection.postValue(
            ManeuverSelection.SELECTED
        )
        // and has detected slot (left)sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        automaticParkAssistRepository.rightSuitable.postValue(true)
        automaticParkAssistRepository.rightSelected.postValue(true)

        // and avm display REAR_VIEW
        displayCameraView(APA_REAR_VIEW)

        // WHEN RCTA are detected
        setCollisionAlert(
            AllianceCarSonarManager.SIDE_RIGHT_ALERT,
            AllianceCarSonarManager.LEVEL_HIGH_COLLISION_RISK
        )
        // and RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)

        // THEN rcta icons are displayed
        assertViewHasEffectiveVisibility(R.id.rcta_right)
    }

    @Test
    fun should_update_indication_icon_when_camera_direction_change() { // ktlint-disable max-line-length
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        // WHEN Front view request
        displayCameraView(APA_FRONT_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)

        // THEN the front camera direction icon is displayed
        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_front)))

        // WHEN Rear view request
        displayCameraView(APA_REAR_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        // THEN the rear camera direction icon is displayed
        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_rear)))
    }

    @Test
    fun should_update_instruction_label_when_scanning_step_procedure_changes() { // ktlint-disable max-line-length
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            assertViewHasEffectiveVisibility(R.id.fapk_guidance_instruction)
        // WHEN guidance instruction 'select side' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_SIDE
        )
        // THEN guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_fapk_select_side)).check(
            matches(isDisplayed())
        )

        // WHEN guidance instruction 'drive forward' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD
        )
        // THEN guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_fapk_drive_forward)).check(
            matches(isDisplayed())
        )

        // WHEN guidance instruction 'stopCamera' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.STOP
        )

        // THEN guidance instruction is displayed
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            onView(
                allOf(
                    withText(R.string.rlb_parkassist_fapk_stop),
                    withParent(withId(R.id.fapk_main))
                )
            ).check(
                matches(isDisplayed())
            ) else
            onView(
                allOf(
                        withText(R.string.rlb_parkassist_fapk_stop)
                )
            ).check(
                matches(isDisplayed())
            )

        // WHEN guidance instruction 'engage rear gear or press provideSurface' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )
        // THEN guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_fapk_press_start_button))
            .check(
                matches(isDisplayed())
            )

        // WHEN guidance instruction 'engage forward gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR
        )
        // THEN guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_fapk_engage_forward_gear)).check(
            matches(isDisplayed())
        )

        // WHEN guidance instruction 'finished' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.MANEUVER_COMPLETE_OR_FINISHED
        )
        // THEN guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_fapk_maneuver_finished)).check(
            matches(isDisplayed())
        )

        // WHEN guidance instruction 'engage rear gear' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.ENGAGE_REAR_GEAR
        )
        // THEN guidance instruction is displayed
        onView(withText(R.string.rlb_parkassist_fapk_engage_rear_gear)).check(
            matches(isDisplayed())
        )

        // WHEN guidance instruction 'accelerate and hold the pedal pressed' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.ACCELERATE_AND_HOLD_THE_PEDAL_PRESSED
        )
        // THEN guidance instruction is displayed
        onView(
            withText(
                R.string.rlb_parkassist_fapk_accelerate_and_hold_the_pedal_pressed
            )
        ).check(
            matches(isDisplayed())
        )

        // WHEN guidance instruction 'maneuver finished' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.MANEUVER_FINISHED_RELEASE_THE_ACCELERATOR_PEDAL
        )
        // THEN guidance instruction is displayed
        onView(
            withText(
                R.string.rlb_parkassist_fapk_maneuver_finished_release_the_accelerator_pedal
            )
        ).check(
            matches(isDisplayed())
        )

        // WHEN guidance instruction 'maneuver finished, take back control' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL
        )
        // THEN guidance instruction is displayed
        onView(
            withText(
                R.string.rlb_parkassist_fapk_maneuver_finished_take_back_control
            )
        ).check(
            matches(isDisplayed())
        )

        // WHEN guidance instruction 'hold the accelerator pressed' sent
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.HOLD_THE_ACCELERATOR_PEDAL_PRESSED
        )
        // THEN guidance instruction is displayed
        onView(
            withText(
                R.string.rlb_parkassist_fapk_hold_the_accelerator_pedal_pressed
            )
        ).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun should_display_raeb_when_raeb_events_occur() { // ktlint-disable max-line-length
        sonarRepository.raebFeaturePresent = true
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)

        // and avm display REAR_VIEW
        displayCameraView(APA_REAR_VIEW)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        // THEN RAEB is displayed
        assertViewHasEffectiveVisibility(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
        // THEN RAEB OFF is displayed
        assertViewHasNoEffectiveVisibility(R.id.raeb_left)
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        // THEN RAEB is displayed
        assertViewHasEffectiveVisibility(R.id.raeb_left)
    }

    @Test
    fun should_display_raeb_off_when_raeb_disabled() { // ktlint-disable max-line-length
        sonarRepository.raebFeaturePresent = true
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        // GIVEN RAEB is disabled
        sonarRepository.raebAlertEnabled.postValue(false)

        // and avm display REAR_VIEW
        displayCameraView(APA_REAR_VIEW)

        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        assertViewHasNoEffectiveVisibility(R.id.raeb_left)
    }

    @Test
    fun should_send_x_y_value_to_service_when_user_select_slot() { // ktlint-disable max-line-length
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        surroundViewRepository.surroundState.postValue(
            SurroundState(APA_FRONT_VIEW)
        )

        // WHEN touch event occurs on avm camera view
        onView(withId(R.id.camera_container)).perform(TouchDownAndUp(50f, 50f))
        val locationContainer =
            fullscreenActivityTestRule.activity.camera_container.extractLocationOnScreen()

        val x0 = locationContainer.first + 50f
        val y0 = locationContainer.second + 50f
        logD { "expected screenPress location x0:$x0, y0:$y0" }
        // THEN verify values are sent to repository
        verify { surroundViewRepository.screenPress(Finger.FIRST, x0, y0) }
    }

    @Test
    fun should_navigate_to_or_from_avm_camera_settings_screen_accordingly_to_click_on_settings_or_back() { // ktlint-disable max-line-length
        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.parallelManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // and avm display FRONT_VIEW
        displayCameraView(APA_FRONT_VIEW)

        assertManeuverButtonActive(R.id.elt_fapk_maneuver_parallel)

        // WHEN user click on settings button
        onView(withId(R.id.toolbar_icon_settings)).perform(click())

        // THEN Auto Park Assist settings screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_settings)

        // WHEN user click on back button
        onView(withId(R.id.toolbar_nav)).perform(click())

        // THEN FAPK screen is displayed
        assertViewHasEffectiveVisibility(R.id.fapk_main)
        // AND parallel maneuver is active
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_parallel)

        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_perpendicular)

        // WHEN user click on settings button
        onView(withId(R.id.toolbar_icon_settings)).perform(click())

        // THEN Auto Park Assist settings screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_settings)

        // WHEN user click on back button
        onView(withId(R.id.toolbar_nav)).perform(click())

        // THEN FAPK screen is displayed
        assertViewHasEffectiveVisibility(R.id.fapk_main)
        // AND perpendicular maneuver is active
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_perpendicular)

        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_park_out)

        // WHEN user click on settings button
        onView(withId(R.id.toolbar_icon_settings)).perform(click())

        // THEN Auto Park Assist settings screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_settings)

        // WHEN user click on back button
        onView(withId(R.id.toolbar_nav)).perform(click())

        // THEN FAPK screen is displayed
        assertViewHasEffectiveVisibility(R.id.fapk_main)
        // AND parkout maneuver is active
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_park_out)
    }

    @Test
    fun should_not_display_dialog_box_on_relevant_warning_reception() {
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        navigateFullscreen(R.id.fapkFragment)
        // GIVEN HFP guidance screen is displayed
        automaticParkAssistRepository.displayState.postValue(DISPLAY_GUIDANCE)
        automaticParkAssistRepository.warningMessage.postValue(
            MESSAGE_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE
        )
        waitDialog()
        onView(withText(R.string.rlb_ok)).check(doesNotExist())
        onView(withText(R.string.rlb_parkassist_apa_neutral_button_label)).check(doesNotExist())
        onView(withText(R.string.rlb_parkassist_apa_negative_button_label)).check(doesNotExist())
    }

    @Test
    fun should_display_correct_switch_button_when_the_button_is_being_pushed_by_the_user() { // ktlint-disable max-line-length
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // Maneuver Start Switch no display
        automaticParkAssistRepository.maneuverSwitchSelection.postValue(
            ManeuverStartSwitch.NONE
        )
        // SWITCH START Maneuver Button is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_fapk_maneuver_switch_start)
        // SWITCH STOP Maneuver Button is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_fapk_maneuver_switch_stop)

        // Maneuver Start Switch Unusable
        automaticParkAssistRepository.maneuverSwitchSelection.postValue(
            ManeuverStartSwitch.UNUSABLE_START
        )
        // SWITCH START Maneuver Button is not displayed
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_switch_start)
        // AND SWITCH START Maneuver Button is not enabled
        assertViewIsDisabled(R.id.elt_fapk_maneuver_switch_start)
        // AND SWITCH STOP Maneuver Button is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_fapk_maneuver_switch_stop)

        // WHEN AVM sends Maneuver Switch Start
        automaticParkAssistRepository.maneuverSwitchSelection.postValue(
            ManeuverStartSwitch.DISPLAY_START
        )
        // SWITCH START Maneuver Button is displayed
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_switch_start)
        // AND SWITCH START Maneuver Button is enabled
        onView(withId(R.id.elt_fapk_maneuver_switch_start))
            .check(matches(isEnabled()))
        // AND SWITCH START Maneuver Button is not selected
        assertViewIsNotSelected(R.id.elt_fapk_maneuver_switch_start)
        // AND SWITCH Maneuver Button text is start
        onView(withText(R.string.rlb_parkassist_fapk_start)).check(
            matches(
                isDisplayed()
            )
        )
        // AND SWITCH STOP Maneuver Button is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_fapk_maneuver_switch_stop)

        // WHEN user pushes in SWITCH START Maneuver Button
        onView(withId(R.id.elt_fapk_maneuver_switch_start)).perform(click())

        // THEN SWITCH Maneuver Button is selected
        onView(withId(R.id.elt_fapk_maneuver_switch_start))
            .check(matches(isSelected()))
        // AND SWITCH Maneuver Button text is start
        onView(withText(R.string.rlb_parkassist_fapk_start)).check(
            matches(
                isDisplayed()
            )
        )

        // WHEN AVM sends Maneuver Switch Cancel
        automaticParkAssistRepository.maneuverSwitchSelection.postValue(
            ManeuverStartSwitch.DISPLAY_CANCEL
        )

        // SWITCH STOP Maneuver Button is displayed
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_switch_stop)
        // AND SWITCH STOP Maneuver Button is enabled
        onView(withId(R.id.elt_fapk_maneuver_switch_stop))
            .check(matches(isEnabled()))
        // AND SWITCH STOP Maneuver Button is not selected
        assertViewIsNotSelected(R.id.elt_fapk_maneuver_switch_stop)
        // AND SWITCH START Maneuver Button is not displayed
        assertViewHasNoEffectiveVisibility(R.id.elt_fapk_maneuver_switch_start)

        // AND SWITCH Maneuver Button text is stop
        onView(withText(R.string.rlb_parkassist_fapk_stop_button)).check(
            matches(
                isDisplayed()
            )
        )

        // WHEN user pushes in SWITCH STOP Maneuver Button
        onView(withId(R.id.elt_fapk_maneuver_switch_stop)).perform(click())

        // THEN SWITCH STOP Maneuver Button is selected
        onView(withId(R.id.elt_fapk_maneuver_switch_stop))
            .check(matches(isSelected()))
        // AND SWITCH Maneuver Button text is stop
        onView(withText(R.string.rlb_parkassist_fapk_stop_button)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun should_navigate_to_fapk_scan_from_settings_when_rear_gear_is_engaged() {
        // GIVEN current screen is FAPK settings
        navigateFullscreen(R.id.fapkSettingsFragment)
        // WHEN user engage Rear Gear
        surroundViewRepository.surroundState.postValue(
            SurroundState(APA_REAR_VIEW)
        )
        // AND extended instruction is not to engage rear gear
        automaticParkAssistRepository.extendedInstruction
            .postValue(AutoPark.EXT_INSTRUCTION_DRIVE_FORWARD)
        // THEN screen should be FAPK scanning
        assertViewHasEffectiveVisibility(R.id.fapk_main)
    }

    @Test
    fun should_not_show_settings_buttons_in_fapk_scan_with_rear_view() {
        // GIVEN current screen is FAPK scanning +RV
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        surroundViewRepository.surroundState.postValue(
            SurroundState(APA_REAR_VIEW)
        )
        // THEN settings button should not be visible
        assertViewHasNoEffectiveVisibility(R.id.toolbar_icon_settings)
    }

    @Test
    fun should_not_display_settings_icon_in_fapk_guidance_screen() { // ktlint-disable max-line-length
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        // Settings icon is not present
        assertViewHasNoEffectiveVisibility(R.id.toolbar_icon_settings)
    }

    @Test
    fun maneuver_button_should_not_be_available_when_not_selected_in_guidance_screen() {
        navigateFullscreen(R.id.fapkFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)

        // AND all buttons are enable but not selected
        automaticParkAssistRepository.parallelManeuverSelection.postValue(
            ManeuverSelection.NOT_SELECTED
        )
        automaticParkAssistRepository.perpendicularManeuverSelection.postValue(
            ManeuverSelection.NOT_SELECTED
        )
        automaticParkAssistRepository.parkOutManeuverSelection.postValue(
            ManeuverSelection.NOT_SELECTED
        )

        onView(withId(R.id.elt_fapk_maneuver_parallel))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.elt_fapk_maneuver_perpendicular))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.elt_fapk_maneuver_park_out))
            .check(matches(not(isEnabled())))

        onView(withId(R.id.elt_fapk_maneuver_parallel))
            .perform(click())
        verify(exactly = 0) {
            automaticParkAssistRepository.requestManeuverType(ManeuverType.PARALLEL)
        }
        onView(withId(R.id.elt_fapk_maneuver_perpendicular))
            .perform(click())
        verify(exactly = 0) {
            automaticParkAssistRepository.requestManeuverType(ManeuverType.PERPENDICULAR)
        }
        onView(withId(R.id.elt_fapk_maneuver_park_out))
            .perform(click())
        verify(exactly = 0) {
            automaticParkAssistRepository.requestManeuverType(ManeuverType.PARKOUT)
        }
    }

    @Test
    fun selected_buttons_should_not_be_clickable() {
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        navigateFullscreen(R.id.fapkFragment)

        automaticParkAssistRepository.parallelManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_parallel)

        onView(withId(R.id.elt_fapk_maneuver_parallel))
            .perform(click())
        verify(exactly = 0) {
            automaticParkAssistRepository
                .requestManeuverType(ManeuverType.PARALLEL)
        }

        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_perpendicular)

        onView(withId(R.id.elt_fapk_maneuver_perpendicular))
            .perform(click())
        verify(exactly = 0) {
            automaticParkAssistRepository
                .requestManeuverType(ManeuverType.PERPENDICULAR)
        }

        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_park_out)

        onView(withId(R.id.elt_fapk_maneuver_park_out)).perform(click())
        verify(exactly = 0) {
            automaticParkAssistRepository
                .requestManeuverType(ManeuverType.PARKOUT)
        }
    }

    @Test
    fun click_on_not_selected_button_should_select_button_and_deselect_others() {
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        navigateFullscreen(R.id.fapkFragment)

        automaticParkAssistRepository.parallelManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        assertManeuverButtonActive(R.id.elt_fapk_maneuver_parallel)

        onView(withId(R.id.elt_fapk_maneuver_park_out))
            .perform(click())

        assertManeuverButtonActive(R.id.elt_fapk_maneuver_park_out)
        assertManeuverButtonNotActive(R.id.elt_fapk_maneuver_parallel)
    }

    @Test
    fun click_on_selected_button_should_send_request_apa_service() {
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        navigateFullscreen(R.id.fapkFragment)

        automaticParkAssistRepository.parallelManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)

        onView(withId(R.id.elt_fapk_maneuver_park_out))
            .perform(click())
        verify { automaticParkAssistRepository.requestManeuverType(ManeuverType.PARKOUT) }
        onView(withId(R.id.elt_fapk_maneuver_parallel))
            .perform(click())
        verify { automaticParkAssistRepository.requestManeuverType(ManeuverType.PARALLEL) }
        onView(withId(R.id.elt_fapk_maneuver_perpendicular))
            .perform(click())
        verify { automaticParkAssistRepository.requestManeuverType(ManeuverType.PERPENDICULAR) }
    }

    @Test
    fun back_button_should_display_in_portrait_mode() {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
            navigateFullscreen(R.id.fapkFragment)
            assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        }
    }

    @Test
    fun should_display_camera_mask_in_fapk_scanning_screen_when_view_mask_requested() { // ktlint-disable max-line-length
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        navigateFullscreen(R.id.fapkFragment)
        // and avm display FRONT_VIEW with std overlay
        displayCameraView(APA_FRONT_VIEW)
        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_apa_fragment)
        automaticParkAssistRepository.viewMask
            .postValue(ViewMask.REQUESTED)
        // Camera mask is present (only in scanning mode)
        assertViewHasEffectiveVisibility(R.id.elt_camera_mask, R.id.camera_ovl_avm_apa_fragment)

        automaticParkAssistRepository.viewMask
            .postValue(ViewMask.UNAVAILABLE)
        // Camera mask is not present
        assertViewHasNoEffectiveVisibility(R.id.elt_camera_mask, R.id.camera_ovl_avm_apa_fragment)
    }

    @Test
    fun should_not_display_camera_mask_in_fapk_guidance_screen() { // ktlint-disable max-line-length
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        navigateFullscreen(R.id.fapkFragment)
        // and avm display FRONT_VIEW
        displayCameraView(APA_FRONT_VIEW)
        automaticParkAssistRepository.viewMask
            .postValue(ViewMask.REQUESTED)
        // Camera mask is not present (only present in scanning mode)
        assertViewHasNoEffectiveVisibility(R.id.elt_camera_mask, R.id.camera_ovl_avm_apa_fragment)
    }

    @Test
    fun maneuver_buttons_should_not_display_when_feature_not_present() {
        automaticParkAssistRepository.supportedManeuvers.clear()
        navigateFullscreen(R.id.fapkFragment)
        assertViewHasNoEffectiveVisibility(R.id.elt_fapk_maneuver_parallel)
        assertViewHasNoEffectiveVisibility(R.id.elt_fapk_maneuver_perpendicular)
        assertViewHasNoEffectiveVisibility(R.id.elt_fapk_maneuver_park_out)
    }

    @Test
    fun maneuver_buttons_should_display_only_in_available_config() {
        navigateFullscreen(R.id.fapkFragment)
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_parallel)
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_perpendicular)
        assertViewHasEffectiveVisibility(R.id.elt_fapk_maneuver_park_out)
    }

    private fun assertManeuverButtonActive(@IdRes buttonId: Int) {
        assertRenautToggleIconButtonActive(buttonId)
    }

    private fun assertManeuverButtonNotActive(@IdRes buttonId: Int) {
        assertRenautToggleIconButtonNotActive(buttonId)
    }

    private fun setCollisionAlert(
        @AllianceCarSonarManager.Side side: Int,
        @AllianceCarSonarManager.Level level: Int
    ) {
        sonarRepository.collisionAlertSide.postValue(side)
        sonarRepository.collisionAlertLevel.postValue(level)
    }

    private fun waitDialog() = Thread.sleep(500)
}