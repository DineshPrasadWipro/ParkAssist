package com.renault.parkassist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.repository.surroundview.ErrorState
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.repository.surroundview.View.REAR_VIEW
import com.renault.parkassist.repository.surroundview.View.SETTINGS_REAR_VIEW
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.dynamicLinesButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.staticLinesButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.trailerButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.zoomAutoButtonTag
import com.renault.parkassist.utils.EspressoTestUtils
import com.renault.parkassist.utils.EspressoTestUtils.assertTextIsDisplayed
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import com.renault.parkassist.utils.EspressoTestUtils.waitForView
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertNotNull

@LargeTest
@RunWith(AndroidJUnit4::class)
class RvcTest : OverlayActivityTest() {

    // TODO Split test case in two, one for each activities

    @Before
    fun setup() {
        clearAll()
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        launchFullScreen()
        navigateFullscreen(R.id.rvcFragment)
    }

    @After
    fun tearDown() {
        displayCameraView()
        sonarRepository.displayRequest.postValue(DisplayType.NONE)
    }

    @Test
    fun should_display_sonar_popup_when_obstacles_detection_occurs() { // ktlint-disable max-line-length
        launchPopUp()
        navigatePopUp(R.id.sonarPipFragment)
        sonarRepository.displayRequest.postValue(DisplayType.WIDGET)
        sonarRepository.closeAllowed.postValue(true)
        assertViewHasEffectiveVisibility(R.id.sonar_sensor_layout)
        assertViewHasEffectiveVisibility(R.id.upa_pip_close)
    }

    @Test
    fun should_hide_close_button() { // ktlint-disable max-line-length
        launchPopUp()
        navigatePopUp(R.id.sonarPipFragment)
        sonarRepository.displayRequest.postValue(DisplayType.WIDGET)
        assertViewHasNoEffectiveVisibility(R.id.upa_pip_close)
    }

    @Test
    fun should_display_rvc_only_main_screen_when_rear_gear_is_engaged() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, ButtonsMode.REGULATORY)
        assertViewHasEffectiveVisibility(R.id.rvc_main)
        assertViewHasEffectiveVisibility(R.id.rvc_camera_sonar_container)
        assertToolbarIsInRegulatoryState()
    }

    @Test
    fun should_display_rvc_and_sonar_main_screen_when_rear_gear_is_engaged() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, ButtonsMode.REGULATORY, forceCloseable = true)
        sonarRepository.closeAllowed.postValue(true)
        // assertViewHasEffectiveVisibility(R.id.sonar_view)
        assertViewHasEffectiveVisibility(R.id.rvc_camera_sonar_container)
        assertToolbarIsInRegulatoryState()
    }

    @Test
    fun should_navigate_to_or_from_rvc_camera_settings_screen_accordingly_to_click_on_settings_or_back() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, ButtonsMode.REGULATORY)
        assertViewHasEffectiveVisibility(R.id.rvc_main)
        assertViewHasEffectiveVisibility(R.id.rvc_camera_sonar_container)
        assertToolbarIsInRegulatoryState()

        // WHEN the user clicks on 'settings' icon
        clickOnView(R.id.toolbar_icon_settings)
        navigateFullscreen(R.id.rvcSettings)
        displayCameraView(SETTINGS_REAR_VIEW, ButtonsMode.REGULATORY)
        waitForView(R.id.elt_camera_view)
        assertViewHasEffectiveVisibility(R.id.camera_settings_camera_fragment)
        assertViewHasEffectiveVisibility(R.id.camera_settings_camera_fragment)
        assertViewHasEffectiveVisibility(R.id.luminosity_seek_bar)
        assertViewHasEffectiveVisibility(R.id.contrast_seek_bar)
        assertViewHasEffectiveVisibility(R.id.hue_seek_bar)
        assertViewWithTagHasEffectiveVisibility(dynamicLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(staticLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(trailerButtonTag)
        assertViewWithTagHasEffectiveVisibility(zoomAutoButtonTag)
        assertTextIsDisplayed(
            R.string.rlb_parkassist_camera_settings
        )

        // WHEN the user clicks on 'back' icon
        clickOnView(R.id.car_ui_toolbar_nav_icon)
        navigateFullscreen(R.id.rvcFragment)
        displayCameraView(REAR_VIEW, ButtonsMode.REGULATORY)
        assertViewHasEffectiveVisibility(R.id.rvc_main)
        assertViewHasEffectiveVisibility(R.id.rvc_camera_sonar_container)
        assertToolbarIsInRegulatoryState()
    }

    @Test
    fun hide_sonar_when_UPA_is_deactivate() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, ButtonsMode.REGULATORY)
        sonarRepository.frontState.postValue(GroupState.DISABLED)
        sonarRepository.rearState.postValue(GroupState.DISABLED)
        sonarRepository.flankState.postValue(GroupState.DISABLED)
        assertViewHasNoEffectiveVisibility(R.id.car)
    }

    @Test
    fun hide_sonar_when_UPA_is_activate_and_display_request_none() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, ButtonsMode.REGULATORY)
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        sonarRepository.displayRequest.postValue(DisplayType.NONE)
        assertViewHasNoEffectiveVisibility(R.id.car)
    }

    @Test
    fun should_show_error_overlay_when_camera_failure() {
        displayCameraView(REAR_VIEW, ButtonsMode.ALL)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_CAMERA_FAILURE)
        onView(ViewMatchers.withId(R.id.camera_error_overlay))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun should_hide_error_overlay_when_camera_failure_gone() {
        displayCameraView(View.PANORAMIC_FRONT_VIEW, ButtonsMode.ALL)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_CAMERA_FAILURE)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_NO_ERROR)
        EspressoTestUtils.assertViewIsGone(R.id.camera_error_overlay)
    }

    @Test
    fun show_sonar_when_UPA_is_activate_and_display_request_different_to_none() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, ButtonsMode.REGULATORY)
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        assertViewHasEffectiveVisibility(R.id.car)
    }

    @Test
    fun show_sonar_when_UPA_rear_flank_are_activate_and_front_deactive_and_display_request_different_to_none() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, ButtonsMode.REGULATORY)
        sonarRepository.frontState.postValue(GroupState.DISABLED)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        assertViewHasEffectiveVisibility(R.id.car)
    }

    private fun assertToolbarIsInRegulatoryState() {
        assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
        assertViewHasNoEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
    }
}