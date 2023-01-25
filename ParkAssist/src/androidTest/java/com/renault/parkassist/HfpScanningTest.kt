package com.renault.parkassist

import android.content.res.Configuration
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.apa.*
import com.renault.parkassist.repository.apa.ManeuverSelection.NOT_SELECTED
import com.renault.parkassist.repository.apa.ManeuverSelection.SELECTED
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.utility.FADE_OUT_DURATION
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.waitFor
import com.renault.parkassist.utils.EspressoTestUtils.waitForView
import com.renault.parkassist.utils.EspressoTestUtils.withTagAsResourceId
import com.renault.parkassist.utils.assertRenautToggleIconButtonActive
import com.renault.parkassist.utils.assertRenautToggleIconButtonNotActive
import com.renault.parkassist.viewmodel.apa.Maneuver
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HfpScanningTest : OverlayActivityTest() {

    @Before
    fun setup() {
        automaticParkAssistRepository.apply {
            every { requestManeuverType(ManeuverType.PARALLEL) } answers {
                setManeuverTypeSelection(parallelManeuverSelection)
            }
            every { requestManeuverType(ManeuverType.PERPENDICULAR) } answers {
                setManeuverTypeSelection(perpendicularManeuverSelection)
            }
            every { requestManeuverType(ManeuverType.PARKOUT) } answers {
                setManeuverTypeSelection(parkOutManeuverSelection)
            }
        }
        automaticParkAssistRepository.supportedManeuvers =
            listOf(Maneuver.PARALLEL, Maneuver.PERPENDICULAR)
        automaticParkAssistRepository.featureConfiguration = FeatureConfig.HFP
        setManeuverTypeSelection(null)
        launchFullScreen()
    }

    @Test
    fun hide_sonar_view_when_display_state_none_in_avm() {
        // GIVEN AVM present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        sonarRepository.upaFkpVisualFeedbackFeaturePresent = true
        sonarRepository.upaRearFeaturePresent = true
        navigateFullscreen(R.id.avmHfpScanningFragment)
        // AND Scanning is displayed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        // WHEN Surround state is different from View.NO_DISPLAY
        displayCameraView(view = View.PANORAMIC_FRONT_VIEW)
        // THEN Masking area is not displayed
        assertViewHasNoEffectiveVisibility(R.id.sonar_view_no_camera)
        // AND Avm Fragment is displayed
        assertViewHasEffectiveVisibility(R.id.camera_container)

        // WHEN Surround state is equal to View.NO_DISPLAY
        displayCameraView(view = View.NO_DISPLAY)
        // THEN Masking area is displayed
        assertViewHasEffectiveVisibility(R.id.sonar_view_no_camera)
    }

    @Test
    fun hide_sonar_view_when_display_state_none_in_rvc() {
        // GIVEN RVC
        setVehicleConfiguration(ParkAssistHwConfig.RVC)

        // WHEN sonar repository is different from DisplayType.NONE
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)

        navigateFullscreen(R.id.sonarHfpScanningFragment)
        // AND Scanning is displayed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        waitForView(R.id.sonar_sensor_fragment, true)

        // THEN Masking area is not displayed
        assertViewHasNoEffectiveVisibility(R.id.sonar_view_no_camera)
        // AND Sonar fragment is displayed
        assertViewHasEffectiveVisibility(R.id.sonar_sensor_fragment)
        // WHEN sonar repository is DisplayType.NONE
        sonarRepository.displayRequest.postValue(DisplayType.NONE)
        // THEN Masking area is displayed
        assertViewHasEffectiveVisibility(R.id.sonar_view_no_camera)
    }

    @Test
    fun hide_sonar_view_when_upa_disabled_in_avm() {
        // GIVEN AVM present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        sonarRepository.upaFkpVisualFeedbackFeaturePresent = true
        sonarRepository.upaRearFeaturePresent = true
        navigateFullscreen(R.id.avmHfpScanningFragment)
        // AND Scanning is displayed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        displayCameraView(View.FRONT_VIEW)

        // AND UPA is supported
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = true
        sonarRepository.fkpFeaturePresent = false

        // WHEN UPA is enabled (front or rear sonar sensors group are active)
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        // THEN Sonar disabled view is not displayed
        assertViewHasNoEffectiveVisibility(R.id.sonar_disabled_view)
        // AND Avm Fragment is displayed
        assertViewHasEffectiveVisibility(R.id.camera_container)

        // WHEN UPA is disabled (front AND rear sonar sensors group are NOT active)
        sonarRepository.frontState.postValue(GroupState.DISABLED)
        sonarRepository.rearState.postValue(GroupState.DISABLED)
        // THEN Masking area is displayed
        assertViewHasEffectiveVisibility(R.id.sonar_disabled_view)
    }

    @Test
    fun hide_sonar_view_when_upa_disabled_in_rvc() {
        // GIVEN RVC
        setVehicleConfiguration(ParkAssistHwConfig.RVC)

        // WHEN sonar repository is different from DisplayType.NONE
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)

        navigateFullscreen(R.id.sonarHfpScanningFragment)
        // AND Scanning is displayed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        waitForView(R.id.sonar_sensor_fragment, true)

        // AND UPA is supported
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = true
        sonarRepository.fkpFeaturePresent = false

        // WHEN UPA is enabled (front or rear sonar sensors group are active)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        // THEN Sonar disabled view is not displayed
        assertViewHasNoEffectiveVisibility(R.id.sonar_disabled_view)
        // AND Sonar Fragment is displayed
        assertViewHasEffectiveVisibility(R.id.sonar_sensor_fragment)

        // WHEN UPA is disabled (front AND rear sonar sensors group are NOT active)
        sonarRepository.frontState.postValue(GroupState.DISABLED)
        sonarRepository.rearState.postValue(GroupState.DISABLED)
        // THEN Sonar disabled view is displayed
        assertViewHasEffectiveVisibility(R.id.sonar_disabled_view)
    }

    @Test
    fun should_display_correct_screen_when_rvc_present() { // ktlint-disable max-line-length
        // GIVEN AVM not present
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        sonarRepository.upaFkpVisualFeedbackFeaturePresent = true
        sonarRepository.upaRearFeaturePresent = true

        // WHEN easy park assist is launched
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        // THEN scanning screen (APA_main) is displayed
        assertViewHasEffectiveVisibility(R.id.apa_mainscanning)
        // that includes :
        // maneuver selection area
        assertViewHasEffectiveVisibility(R.id.maneuver_selection_area)
        // map area (including top view of the vehicle and sides indicators)
        assertViewHasEffectiveVisibility(R.id.map_area)
        // instruction area
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            assertViewHasEffectiveVisibility(R.id.elt_apa_scanning_instruction)
        // disclaimer area
        assertViewHasEffectiveVisibility(R.id.disclaimer_area)
        // sonar view (including mute button)
        assertViewHasEffectiveVisibility(R.id.sonar_sensor_fragment)
        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                ViewMatchers.isDescendantOfA(withId(R.id.sonar_mute_view))
            )
        ).check(matches(ViewMatchers.isNotChecked()))
        // settings button
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
    }

    @Test
    fun should_display_correct_screen_when_avm_present() { // ktlint-disable max-line-length
        // GIVEN AVM present
        setVehicleConfiguration(ParkAssistHwConfig.AVM)

        // WHEN easy park assist is launched
        navigateFullscreen(R.id.avmHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        // THEN scanning screen (APA_main) is displayed
        assertViewHasEffectiveVisibility(R.id.apa_mainscanning)
        // that includes :
        // maneuver selection area
        assertViewHasEffectiveVisibility(R.id.maneuver_selection_area)
        // map area (including top view of the vehicle and sides indicators)
        assertViewHasEffectiveVisibility(R.id.map_area)
        // instruction area
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            assertViewHasEffectiveVisibility(R.id.elt_apa_scanning_instruction)
        // disclaimer area
        assertViewHasEffectiveVisibility(R.id.disclaimer_area)

        // AVM display REAR_VIEW
        displayCameraView(View.REAR_VIEW)
        // AVM bird view with sonar indications (including mute button)
        assertViewHasEffectiveVisibility(R.id.camera_container)

        onView(
            Matchers.allOf(
                withId(R.id.sonar_mute_button),
                ViewMatchers.isDescendantOfA(withId(R.id.camera_container))
            )
        ).check(matches(ViewMatchers.isNotChecked()))

        // no sonar view
        onView(withId(R.id.sonar_sensor_fragment)).check(doesNotExist())

        // settings button
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
    }

    @Ignore("should we keep this test")
    @Test
    fun should_not_display_incorrect_views_when_neither_avm_nor_rvc_present() { // ktlint-disable max-line-length
        // GIVEN neither AVM nor RVC is present
        setVehicleConfiguration(ParkAssistHwConfig.NONE)

        // WHEN easy park assist is launched
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        // THEN scanning screen (APA_main) is displayed
        assertViewHasEffectiveVisibility(R.id.apa_mainscanning)
        // that includes :
        // maneuver selection area
        assertViewHasEffectiveVisibility(R.id.maneuver_selection_area)
        // map area (including top view of the vehicle and sides indicators)
        assertViewHasEffectiveVisibility(R.id.map_area)
        // instruction area
        assertViewHasEffectiveVisibility(R.id.elt_apa_scanning_instruction)
        // disclaimer area
        assertViewHasEffectiveVisibility(R.id.disclaimer_area)
        // neither sonar view nor avm bird view
        onView(withId(R.id.sonar_sensor_fragment)).check(doesNotExist())
        // settings button
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
    }

    @Test
    fun should_correctly_change_maneuver_mode_to_parallel_left() { // ktlint-disable max-line-length
        // GIVEN scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN the user select the scanning side
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // WHEN the user changes the maneuver mode
        onView(withId(R.id.maneuver_parallel)).perform(click())
        // THEN the maneuver button should be active
        assertManeuverButtonActive(R.id.maneuver_parallel)
    }

    @Test
    fun should_correctly_change_maneuver_mode_to_parallel_right() { // ktlint-disable max-line-length
        // GIVEN scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN the user select the scanning side
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // WHEN the user changes the maneuver mode
        onView(withId(R.id.maneuver_parallel)).perform(click())
        // THEN the maneuver button should be active
        assertManeuverButtonActive(R.id.maneuver_parallel)
    }

    @Test
    fun should_correctly_change_maneuver_mode_to_perpendicular_left() { // ktlint-disable max-line-length
        // GIVEN scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN the user select the scanning side
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // WHEN the user changes the maneuver mode
        onView(withId(R.id.maneuver_perpendicular)).perform(click())
        // THEN the perpendicular button should be active
        assertManeuverButtonActive(R.id.maneuver_perpendicular)
    }

    @Test
    fun should_correctly_change_maneuver_mode_to_perpendicular_right() { // ktlint-disable max-line-length
        // GIVEN scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN the user select the scanning side
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // WHEN the user changes the maneuver mode
        onView(withId(R.id.maneuver_perpendicular)).perform(click())
        // THEN the perpendicular button should be active
        assertManeuverButtonActive(R.id.maneuver_perpendicular)
    }

    @Test
    fun should_correctly_mute_and_unmute_sound() { // ktlint-disable max-line-length
        // GIVEN scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // TODO WHEN the user mutes/unmutes UPA/FKP sound
        // TODO THEN UPA/FKP sound is muted/unmuted accordingly
        // FIXME Stretched goal
    }

    @Test
    fun should_display_the_correct_resource_when_scanning_side_left_and_maneuver_perpendicular_selected() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN maneuver type selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // AND maneuver scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // THEN the straight background is displayed
        onView(withId(R.id.map_area)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_bckg_apa_scanning_straight)
            )
        )
    }

    @Test
    fun should_display_the_correct_resource_when_scanning_side_right_and_maneuver_perpendicular_selected() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN maneuver type selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // AND maneuver scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // THEN the straight background is displayed
        onView(withId(R.id.map_area)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_bckg_apa_scanning_straight)
            )
        )
    }

    @Test
    fun should_display_the_correct_resource_when_scanning_side_left_and_maneuver_parallel_selected() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN maneuver type selected
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // AND maneuver scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // THEN the straight background is displayed
        onView(withId(R.id.map_area)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_bckg_apa_scanning_straight)
            )
        )
    }

    @Test
    fun should_display_the_correct_resource_when_scanning_side_right_and_maneuver_parallel_selected() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN maneuver type selected
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // AND maneuver scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // THEN the straight background is displayed
        onView(withId(R.id.map_area)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_bckg_apa_scanning_straight)
            )
        )
    }

    @Test
    fun side_indicator_should_not_display_when_instruction_reverse() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // THEN the left indicator is displayed
        assertViewHasEffectiveVisibility(R.id.apa_scanning_car_bottom_left_placeholder)
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // THEN the right indicator is displayed
        assertViewHasEffectiveVisibility(R.id.apa_scanning_car_bottom_right_placeholder)
        automaticParkAssistRepository.extendedInstruction
            .postValue(Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        waitFor(FADE_OUT_DURATION)
        assertViewHasNoEffectiveVisibility(R.id.apa_scanning_car_bottom_left_placeholder)
        assertViewHasNoEffectiveVisibility(R.id.apa_scanning_car_bottom_right_placeholder)
    }

    @Test
    fun should_display_the_correct_resource_when_EXT_INSTRUCTION_DRIVE_FORWARD() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD
        )
        // THEN the front stop is not displayed
        assertViewHasNoEffectiveVisibility(R.id.apa_scanning_car_top_stop_placeholder)
        // AND the front arrow is displayed
        assertViewHasEffectiveVisibility(R.id.apa_scanning_car_top_arrow_placeholder)
    }

    @Test
    fun should_display_the_correct_resource_when_EXT_INSTRUCTION_DRIVE_FORWARD_TO_FIND_PARKING_SLOT() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD
        )
        // THEN the front stop is not displayed
        assertViewHasNoEffectiveVisibility(R.id.apa_scanning_car_top_stop_placeholder)
        // AND the front arrow is displayed
        assertViewHasEffectiveVisibility(R.id.apa_scanning_car_top_arrow_placeholder)
    }

    @Test
    fun should_display_the_correct_resource_when_EXT_INSTRUCTION_DRIVE_FORWARD_OR_BACKWARD() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.GO_FORWARD_OR_REVERSE
        )
        // THEN the front stop is not displayed
        assertViewHasNoEffectiveVisibility(R.id.apa_scanning_car_top_stop_placeholder)
        // AND the front arrow is displayed
        assertViewHasEffectiveVisibility(R.id.apa_scanning_car_top_arrow_placeholder)
    }

    @Test
    fun should_display_stop_front_area_resource_when_instruction_stop() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.STOP
        )
        // THEN the front stop is displayed
        assertViewHasEffectiveVisibility(R.id.apa_scanning_car_top_stop_placeholder)
        // AND the front arrow is not displayed
        assertViewHasNoEffectiveVisibility(R.id.apa_scanning_car_top_arrow_placeholder)
    }

    @Test
    fun should_display_a_small_left_parking_selected_indicator_when_left_slot_selected_in_parallel_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // AND left slot is detected
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // AND the user selects the slot
        automaticParkAssistRepository.leftSelected.postValue(false)
        // AND Maneuver parallel selected
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // THEN the a big P is displayed in the left parallel slot
        onView(withId(R.id.left_parking_slot_parallel)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // left slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_perpendicular)
        // right slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_parallel)
        // right slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_perpendicular)
    }

    @Test
    fun should_display_a_small_right_parking_selected_indicator_when_right_slot_selected_in_parallel_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // AND right slot is detected
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // AND the user selects the right slot
        automaticParkAssistRepository.rightSelected.postValue(false)
        // AND Maneuver parallel selected
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // THEN the a big P is displayed in the right slot
        onView(withId(R.id.right_parking_slot_parallel)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // right slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_perpendicular)
        // left slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_parallel)
        // left slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_perpendicular)
    }

    @Test
    fun should_display_a_small_right_and_left_parking_suitable_indicator_when_right_and_left_slot_suitable_in_parallel_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD_SLOT_SUITABLE
        )
        // AND right slot is detected
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // AND left slot is detected
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // AN the user does not select the right slot
        automaticParkAssistRepository.rightSelected.postValue(false)
        // AN the user does not select the left slot
        automaticParkAssistRepository.leftSelected.postValue(false)
        // AND Maneuver parallel selected
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // THEN the bckg slot parallel both is displayed
        onView(withId(R.id.map_area)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_bckg_apa_scanning_parallel_both)
            )
        )
        // AND a small P is displayed in the right slot
        onView(withId(R.id.right_parking_slot_parallel)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // AND a small P is displayed in the left slot
        onView(withId(R.id.left_parking_slot_parallel)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // right slot parking parallel is visible
        assertViewHasEffectiveVisibility(R.id.right_parking_slot_parallel)
        // left slot parking parallel is visible
        assertViewHasEffectiveVisibility(R.id.left_parking_slot_parallel)
        // right slot parking perpendicular is not visible
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_perpendicular)
        // left slot parking perpendicular is not visible
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_perpendicular)
    }

    @Test
    fun should_display_a_small_right_and_left_parking_suitable_indicator_when_right_and_left_slot_suitable_in_perpendicular_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD_SLOT_SUITABLE
        )
        // AND right slot is detected
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // AND left slot is detected
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // AN the user does not select the right slot
        automaticParkAssistRepository.rightSelected.postValue(false)
        // AN the user does not select the left slot
        automaticParkAssistRepository.leftSelected.postValue(false)
        // AND Maneuver perpendicular selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // THEN the bckg slot perpendicular both is displayed
        onView(withId(R.id.map_area)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_bckg_apa_scanning_perpendicular_both)
            )
        )
        // AND a small P is displayed in the right slot
        onView(withId(R.id.right_parking_slot_perpendicular)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // AND a small P is displayed in the left slot
        onView(withId(R.id.left_parking_slot_perpendicular)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // right slot parking perpendicular is visible
        assertViewHasEffectiveVisibility(R.id.right_parking_slot_perpendicular)
        // left slot parking perpendicular is visible
        assertViewHasEffectiveVisibility(R.id.left_parking_slot_perpendicular)
        // right slot parking parallel is not visible
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_parallel)
        // left slot parking parallel is not visible
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_parallel)
    }

    @Test
    fun should_display_a_small_left_parking_selected_indicator_when_left_slot_selected_in_perpendicular_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // AND left slot is detected
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // AND the user selects the slot
        automaticParkAssistRepository.leftSelected.postValue(false)
        // AND Maneuver perpendicular selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // THEN the a big P is displayed in the left parallel slot
        onView(withId(R.id.left_parking_slot_perpendicular)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // left slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_parallel)
        // right slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_parallel)
        // right slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_perpendicular)
    }

    @Test
    fun should_display_a_small_right_parking_selected_indicator_when_right_slot_selected_in_perpendicular_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // AND right slot is detected
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // AND the user selects the right slot
        automaticParkAssistRepository.rightSelected.postValue(false)
        // AND Maneuver perpendicular selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // THEN the a big P is displayed in the right slot
        onView(withId(R.id.right_parking_slot_perpendicular)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // right slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_parallel)
        // left slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_parallel)
        // left slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_perpendicular)
    }

    @Test
    fun should_display_a_big_left_parking_selected_indicator_when_left_slot_selected_in_parallel_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // AND left slot is detected
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // AND the user selects the slot
        automaticParkAssistRepository.leftSelected.postValue(true)
        // AND Maneuver parallel selected
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // THEN the a big P is displayed in the left parallel slot
        onView(withId(R.id.left_parking_slot_parallel)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking_large)
            )
        )
        // left slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_perpendicular)
        // right slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_parallel)
        // right slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_perpendicular)
    }

    @Test
    fun should_display_a_big_right_parking_selected_indicator_when_right_slot_selected_in_parallel_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // AND right slot is detected
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // AND the user selects the right slot
        automaticParkAssistRepository.rightSelected.postValue(true)
        // AND Maneuver parallel selected
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)

        // THEN the a big P is displayed in the right slot
        onView(withId(R.id.right_parking_slot_parallel)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking_large)
            )
        )

        // right slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_perpendicular)
        // left slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_parallel)
        // left slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_perpendicular)
    }

    @Test
    fun should_display_a_big_left_parking_selected_indicator_when_left_slot_selected_in_perpendicular_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // AND left slot is detected
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // AND the user selects the slot
        automaticParkAssistRepository.leftSelected.postValue(true)
        // AND Maneuver perpendicular selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // THEN the a big P is displayed in the left parallel slot
        onView(withId(R.id.left_parking_slot_perpendicular)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking_large)
            )
        )
        // left slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_parallel)
        // right slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_parallel)
        // right slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_perpendicular)
    }

    @Test
    fun should_display_a_big_right_parking_selected_indicator_when_right_slot_selected_in_perpendicular_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // AND right slot is detected
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // AND the user selects the right slot
        automaticParkAssistRepository.rightSelected.postValue(true)
        // AND Maneuver perpendicular selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // THEN the a big P is displayed in the right slot
        onView(withId(R.id.right_parking_slot_perpendicular)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking_large)
            )
        )
        // right slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_parallel)
        // left slot parking parallel
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_parallel)
        // left slot parking perpendicular
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_perpendicular)
    }

    @Test
    fun should_display_active_back_right_arrow_when_right_selected_and_engage_rear_gear_instruction_is_sent() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // AND right slot is detected
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // AND the user selects the right slot
        automaticParkAssistRepository.rightSelected.postValue(true)
        // AND Maneuver perpendicular selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)

        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        // THEN active backright arrow is present
        assertViewHasEffectiveVisibility(R.id.apa_scanning_car_bottom_center_placeholder)
    }

    @Test
    fun should_display_active_back_left_arrow_when_right_selected_and_engage_rear_gear_instruction_is_sent() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)
        // AND right slot is detected
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // AND the user selects the right slot
        automaticParkAssistRepository.leftSelected.postValue(true)
        // AND Maneuver perpendicular selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)

        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON
        )

        // THEN active backright arrow is present
        assertViewHasEffectiveVisibility(R.id.apa_scanning_car_bottom_center_placeholder)
    }

    @Test
    fun should_display_a_big_right_parking_selected_and_small_left_parking_suitable_indicator_when_right_slot_selected_and_left_slot_suitable_in_perpendicular_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD_SLOT_SUITABLE
        )
        // AND right slot is detected
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // AND left slot is detected
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // AND the user selects the right slot
        automaticParkAssistRepository.rightSelected.postValue(true)
        // AN the user does not select the left slot
        automaticParkAssistRepository.leftSelected.postValue(false)
        // AND Maneuver perpendicular selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // THEN the bckg slot perpendicular both is displayed
        onView(withId(R.id.map_area)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_bckg_apa_scanning_perpendicular_both)
            )
        )
        // THEN the a big P is displayed in the right slot
        onView(withId(R.id.right_parking_slot_perpendicular)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking_large)
            )
        )
        // AND a small P is displayed in the left slot
        onView(withId(R.id.left_parking_slot_perpendicular)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // right slot parking perpendicular is visible
        assertViewHasEffectiveVisibility(R.id.right_parking_slot_perpendicular)
        // left slot parking perpendicular is visible
        assertViewHasEffectiveVisibility(R.id.left_parking_slot_perpendicular)
        // right slot parking parallel is not visible
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_parallel)
        // left slot parking parallel is not visible
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_parallel)
    }

    @Test
    fun should_display_a_small_right_parking_selected_and_big_left_parking_suitable_indicator_when_right_slot_suitable_and_left_slot_selected_in_parallel_mode() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN scanning side selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)
        // WHEN instruction is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.DRIVE_FORWARD_SLOT_SUITABLE
        )
        // AND right slot is detected
        automaticParkAssistRepository.rightSuitable.postValue(true)
        // AND left slot is detected
        automaticParkAssistRepository.leftSuitable.postValue(true)
        // AND the user selects the right slot
        automaticParkAssistRepository.rightSelected.postValue(false)
        // AN the user does not select the left slot
        automaticParkAssistRepository.leftSelected.postValue(true)
        // AND Maneuver perpendicular selected
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // THEN the bckg slot parallel both is displayed
        onView(withId(R.id.map_area)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_bckg_apa_scanning_parallel_both)
            )
        )
        // THEN the a small P is displayed in the right slot
        onView(withId(R.id.right_parking_slot_parallel)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking)
            )
        )
        // AND a big P is displayed in the left slot
        onView(withId(R.id.left_parking_slot_parallel)).check(
            matches(
                withTagAsResourceId(R.drawable.rimg_adas_parking_large)
            )
        )
        // right slot parking parallel is visible
        assertViewHasEffectiveVisibility(R.id.right_parking_slot_parallel)
        // left slot parking parallel is visible
        assertViewHasEffectiveVisibility(R.id.left_parking_slot_parallel)
        // right slot parking perpendicular is not visible
        assertViewHasNoEffectiveVisibility(R.id.right_parking_slot_perpendicular)
        // left slot parking perpendicular is not visible
        assertViewHasNoEffectiveVisibility(R.id.left_parking_slot_perpendicular)
    }

    @Test
    fun should_display_the_correct_rss_when_maneuver_parkout_selected_left_side_and_SELECT_SIDE_EXT() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN maneuver type parkin parallel selected
        setManeuverTypeSelection(automaticParkAssistRepository.parallelManeuverSelection)
        // AND display State Parkout Confirmation requested
        automaticParkAssistRepository.displayState.postValue(
            DisplayState.DISPLAY_PARKOUT_CONFIRMATION
        )

        // AND instruction Select Side is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.SELECT_SIDE
        )
        // AND scanning side left selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(200)

        // THEN the illustration SELECT_SIDE / PARKOUT is displayed
        assertViewHasEffectiveVisibility(R.id.sides_background)
        assertViewHasEffectiveVisibility(R.id.sides_vehicle_center)
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_arrow_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_arrow_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_arrow_parkout,
            R.id.parallel_right_vehicle_center
        )
        assertViewHasEffectiveVisibility(
            R.id.center_left_arrow_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_arrow_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_arrow_parkout,
            R.id.parallel_right_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(R.id.parallel_left_background_parkout)
        assertViewHasNoEffectiveVisibility(R.id.parallel_left_vehicle_center)
        assertViewHasNoEffectiveVisibility(R.id.parallel_right_background_parkout)
        assertViewHasNoEffectiveVisibility(R.id.parallel_right_vehicle_center)
        assertViewHasNoEffectiveVisibility(R.id.parallel_left_vehicle_left_parkout)
        assertViewHasNoEffectiveVisibility(R.id.parallel_right_vehicle_right_parkout)

        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve_parkout,
            R.id.parallel_right_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve_parkout,
            R.id.parallel_right_vehicle_center
        )

        assertViewHasNoEffectiveVisibility(
            R.id.center_stop_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop_parkout,
            R.id.parallel_right_vehicle_center
        )
    }

    @Test
    fun should_display_the_correct_rss_when_maneuver_parkout_selected_right_side_and_STOP_EXT() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN maneuver type parkout selected
        setManeuverTypeSelection(automaticParkAssistRepository.parkOutManeuverSelection)
        // AND display State Parkout Confirmation requested
        automaticParkAssistRepository.displayState.postValue(
            DisplayState.DISPLAY_PARKOUT_CONFIRMATION
        )
        // AND scanning side left selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_RIGHT)

        // AND instruction Select Side is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.STOP
        )

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        // THEN the illustration LEFT / STOP / PARKOUT is displayed
        assertViewHasNoEffectiveVisibility(R.id.sides_background)
        assertViewHasNoEffectiveVisibility(R.id.sides_vehicle_center)
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_arrow_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_arrow_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_arrow_parkout,
            R.id.parallel_right_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_arrow_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_arrow_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_arrow_parkout,
            R.id.parallel_right_vehicle_center
        )
        assertViewHasEffectiveVisibility(R.id.parallel_left_background_parkout)
        assertViewHasEffectiveVisibility(R.id.parallel_left_vehicle_center)
        assertViewHasNoEffectiveVisibility(R.id.parallel_right_background_parkout)
        assertViewHasNoEffectiveVisibility(R.id.parallel_right_vehicle_center)
        assertViewHasNoEffectiveVisibility(R.id.parallel_left_vehicle_left_parkout)
        assertViewHasNoEffectiveVisibility(R.id.parallel_right_vehicle_right_parkout)

        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve_parkout,
            R.id.parallel_right_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve_parkout,
            R.id.parallel_right_vehicle_center
        )

        assertViewHasNoEffectiveVisibility(
            R.id.center_stop_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasEffectiveVisibility(
            R.id.center_stop_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop_parkout,
            R.id.parallel_right_vehicle_center
        )
    }

    @Test
    fun should_display_the_correct_rss_when_maneuver_parkout_selected_left_side_and_STOP_EXT() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        // WHEN maneuver type parkout selected
        setManeuverTypeSelection(automaticParkAssistRepository.perpendicularManeuverSelection)
        // AND display State Parkout Confirmation requested
        automaticParkAssistRepository.displayState.postValue(
            DisplayState.DISPLAY_PARKOUT_CONFIRMATION
        )
        // AND scanning side right selected
        automaticParkAssistRepository.scanningSide.postValue(ScanningSide.SCANNING_SIDE_LEFT)

        // AND instruction Select Side is given
        automaticParkAssistRepository.extendedInstruction.postValue(
            Instruction.STOP
        )

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        // THEN the illustration RIGHT / STOP / PARKOUT is displayed
        assertViewHasNoEffectiveVisibility(R.id.sides_background)
        assertViewHasNoEffectiveVisibility(R.id.sides_vehicle_center)
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_arrow_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_arrow_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_arrow_parkout,
            R.id.parallel_right_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_arrow_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_arrow_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_arrow_parkout,
            R.id.parallel_right_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(R.id.parallel_left_background_parkout)
        assertViewHasNoEffectiveVisibility(R.id.parallel_left_vehicle_center)
        assertViewHasEffectiveVisibility(R.id.parallel_right_background_parkout)
        assertViewHasEffectiveVisibility(R.id.parallel_right_vehicle_center)
        assertViewHasNoEffectiveVisibility(R.id.parallel_left_vehicle_left_parkout)
        assertViewHasNoEffectiveVisibility(R.id.parallel_right_vehicle_right_parkout)

        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_left_doublecurve_parkout,
            R.id.parallel_right_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_right_doublecurve_parkout,
            R.id.parallel_right_vehicle_center
        )

        assertViewHasNoEffectiveVisibility(
            R.id.center_stop_parkout,
            R.id.sides_vehicle_center
        )
        assertViewHasNoEffectiveVisibility(
            R.id.center_stop_parkout,
            R.id.parallel_left_vehicle_center
        )
        assertViewHasEffectiveVisibility(
            R.id.center_stop_parkout,
            R.id.parallel_right_vehicle_center
        )
    }

    @Test
    fun should_navigate_to_or_from_avm_camera_settings_screen_accordingly_to_click_on_settings_or_back() { // ktlint-disable max-line-length
        // GIVEN HFP scanning screen is displayed
        navigateFullscreen(R.id.sonarHfpScanningFragment)
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        automaticParkAssistRepository.parallelManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.maneuver_parallel)

        // WHEN user click on settings button
        onView(withId(R.id.toolbar_icon_settings)).perform(click())

        // THEN Auto Park Assist settings screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_settings)

        // WHEN user click on back button
        onView(withId(R.id.car_ui_toolbar_nav_icon)).perform(click())

        // THEN HFP screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_mainscanning)
        // AND parallel maneuver is active
        assertManeuverButtonActive(R.id.maneuver_parallel)

        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.maneuver_perpendicular)

        // WHEN user click on settings button
        onView(withId(R.id.toolbar_icon_settings)).perform(click())

        // THEN Auto Park Assist settings screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_settings)

        // WHEN user click on back button
        onView(withId(R.id.car_ui_toolbar_nav_icon)).perform(click())

        // THEN HFP screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_mainscanning)
        // AND parallel maneuver is active
        assertManeuverButtonActive(R.id.maneuver_perpendicular)

        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.maneuver_parkout)

        // WHEN user click on settings button
        onView(withId(R.id.toolbar_icon_settings)).perform(click())

        // THEN Auto Park Assist settings screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_settings)

        // WHEN user click on back button
        onView(withId(R.id.toolbar_nav)).perform(click())

        // THEN HFP screen is displayed
        assertViewHasEffectiveVisibility(R.id.apa_mainscanning)
        // AND parallel maneuver is active
        assertManeuverButtonActive(R.id.maneuver_parkout)
    }

    @Test
    fun selected_buttons_should_not_be_clickable() {
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        navigateFullscreen(R.id.sonarHfpScanningFragment)

        automaticParkAssistRepository.parallelManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.maneuver_parallel)

        onView(withId(R.id.maneuver_parallel))
            .perform(click())
        verify(exactly = 0) {
            automaticParkAssistRepository
                .requestManeuverType(ManeuverType.PARALLEL)
        }

        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.maneuver_perpendicular)

        onView(withId(R.id.maneuver_perpendicular))
            .perform(click())
        verify(exactly = 0) {
            automaticParkAssistRepository
                .requestManeuverType(ManeuverType.PERPENDICULAR)
        }

        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        assertManeuverButtonActive(R.id.maneuver_parkout)

        onView(withId(R.id.maneuver_parkout)).perform(click())
        verify(exactly = 0) {
            automaticParkAssistRepository
                .requestManeuverType(ManeuverType.PARKOUT)
        }
    }

    @Test
    fun click_on_not_selected_button_should_select_button_and_deselect_others() {
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        navigateFullscreen(R.id.avmHfpScanningFragment)

        automaticParkAssistRepository.parallelManeuverSelection
            .postValue(ManeuverSelection.SELECTED)
        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        assertManeuverButtonActive(R.id.maneuver_parallel)

        onView(withId(R.id.maneuver_parkout))
            .perform(click())

        assertManeuverButtonActive(R.id.maneuver_parkout)
        assertManeuverButtonNotActive(R.id.maneuver_parallel)
    }

    @Test
    fun click_on_selected_button_should_send_request_apa_service() {
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        navigateFullscreen(R.id.sonarHfpScanningFragment)

        automaticParkAssistRepository.parallelManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.perpendicularManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)
        automaticParkAssistRepository.parkOutManeuverSelection
            .postValue(ManeuverSelection.NOT_SELECTED)

        onView(withId(R.id.maneuver_parkout))
            .perform(click())
        verify { automaticParkAssistRepository.requestManeuverType(ManeuverType.PARKOUT) }
        onView(withId(R.id.maneuver_parallel))
            .perform(click())
        verify { automaticParkAssistRepository.requestManeuverType(ManeuverType.PARALLEL) }
        onView(withId(R.id.maneuver_perpendicular))
            .perform(click())
        verify { automaticParkAssistRepository.requestManeuverType(ManeuverType.PERPENDICULAR) }
    }

    private fun assertManeuverButtonActive(@IdRes buttonId: Int) {
        assertRenautToggleIconButtonActive(buttonId)
    }

    private fun assertManeuverButtonNotActive(@IdRes buttonId: Int) {
        assertRenautToggleIconButtonNotActive(buttonId)
    }

    @Test
    fun back_button_should_display_in_portrait_mode() {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
            navigateFullscreen(R.id.fapkFragment)
            assertViewHasEffectiveVisibility(R.id.toolbar_nav)
        }
    }

    private fun setManeuverTypeSelection(maneuverSelection: MutableLiveData<Int>?) {
        when (maneuverSelection) {
            automaticParkAssistRepository.parallelManeuverSelection -> {
                automaticParkAssistRepository.perpendicularManeuverSelection.postValue(NOT_SELECTED)
                automaticParkAssistRepository.parkOutManeuverSelection.postValue(NOT_SELECTED)
                automaticParkAssistRepository.parallelManeuverSelection.postValue(SELECTED)
            }
            automaticParkAssistRepository.perpendicularManeuverSelection -> {
                automaticParkAssistRepository.parallelManeuverSelection.postValue(NOT_SELECTED)
                automaticParkAssistRepository.parkOutManeuverSelection.postValue(NOT_SELECTED)
                automaticParkAssistRepository.perpendicularManeuverSelection.postValue(SELECTED)
            }
            automaticParkAssistRepository.parkOutManeuverSelection -> {
                automaticParkAssistRepository.parallelManeuverSelection.postValue(NOT_SELECTED)
                automaticParkAssistRepository.perpendicularManeuverSelection.postValue(NOT_SELECTED)
                automaticParkAssistRepository.parkOutManeuverSelection.postValue(SELECTED)
            }
            else -> {
                automaticParkAssistRepository.parallelManeuverSelection.postValue(NOT_SELECTED)
                automaticParkAssistRepository.perpendicularManeuverSelection.postValue(NOT_SELECTED)
                automaticParkAssistRepository.parkOutManeuverSelection.postValue(NOT_SELECTED)
            }
        }
    }
}