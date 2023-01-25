package com.renault.parkassist

import alliance.car.sonar.AllianceCarSonarManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.KoinTestBase.ButtonsMode.ALL
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.repository.surroundview.ErrorState
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.utils.EspressoTestUtils
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsNotChecked
import com.renault.parkassist.utils.EspressoTestUtils.checkToolbarTitle
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import io.mockk.every
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@LargeTest
@RunWith(AndroidJUnit4::class)
class MvcTest : OverlayActivityTest() {
    @Before
    fun setup() {
        clearAll()
        surroundViewRepository.apply {
            every { setAutoZoomRearViewActivation(any()) } answers {
                autoZoomRearViewActivation.postValue(firstArg())
            }
        }

        surroundViewRepository.isAutoZoomSupported = true
        surroundViewRepository.autoZoomRearViewActivation.postValue(true)

        setVehicleConfiguration(ParkAssistHwConfig.MVC)
        launchFullScreen()
        navigateFullscreen(R.id.mvcFragment)
    }

    @After
    fun teardown() {
        displayCameraView()
        sonarRepository.displayRequest.postValue(DisplayType.NONE)
    }

    private fun assertViewMvc() {
        assertViewHasEffectiveVisibility(R.id.mvc_main)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        Assert.assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_easypark)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_apps_monitor)
        assertViewHasEffectiveVisibility(R.id.sonar_fragment)
        assertViewHasEffectiveVisibility(R.id.car)
        checkToolbarTitle(
            R.id.car_ui_toolbar,
            R.string.rlb_parkassist_disclaimer
        )
        assertViewHasEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
        assertViewHasEffectiveVisibility(R.id.mvc_auto_zoom)
        assertViewHasEffectiveVisibility(R.id.mvc_rear)
        assertViewHasEffectiveVisibility(R.id.mvc_front)
        assertViewHasEffectiveVisibility(R.id.mvc_left)
        assertViewHasEffectiveVisibility(R.id.mvc_right)
    }

    @Test
    fun should_show_mvc_default_view_when_user_launch_mvc_screen() {
        //TODO: Implement Default View as per condition, Considered FRONT_VIEW as Default View here
        displayCameraView(View.FRONT_VIEW, ALL)
        assertViewMvc()
    }

    @Test
    fun should_show_mvc_front_view_with_camera_indicator_when_launch_front_view() {
        displayCameraView(View.FRONT_VIEW, ALL)
        clickOnView(R.id.mvc_front)
        onView(ViewMatchers.withId(R.id.elt_camera_indicator))
            .check(ViewAssertions.matches(EspressoTestUtils.withTagAsResourceId(
                R.drawable.ric_adas_avm_front)))
        assertViewHasEffectiveVisibility(R.id.camera_ovl_rvc_std_fragment)
        assertViewMvc()
    }

    @Test
    fun should_show_mvc_rear_view_with_camera_indicator_when_launch_rear_view_or_auto_view() {
        displayCameraView(View.REAR_VIEW, ALL)
        clickOnView(R.id.mvc_rear)
        onView(ViewMatchers.withId(R.id.elt_camera_indicator))
            .check(ViewAssertions.matches(EspressoTestUtils.withTagAsResourceId(
                R.drawable.ric_adas_avm_rear)))
        assertViewHasEffectiveVisibility(R.id.camera_ovl_rvc_std_fragment)
        assertViewMvc()
    }

    @Test
    fun should_show_mvc_left_view_with_camera_indicator_when_launch_left_screen() {
        displayCameraView(View.LEFT_VIEW, ALL)
        clickOnView(R.id.mvc_left)
        onView(ViewMatchers.withId(R.id.elt_camera_indicator))
            .check(ViewAssertions.matches(EspressoTestUtils.withTagAsResourceId(
                R.drawable.ric_adas_avm_left_side)))
        assertViewHasEffectiveVisibility(R.id.camera_ovl_rvc_std_fragment)
        assertViewMvc()
    }

    @Test
    fun should_show_mvc_right_view_with_camera_indicator_when_launch_right_screen() {
        displayCameraView(View.RIGHT_VIEW, ALL)
        clickOnView(R.id.mvc_right)
        onView(ViewMatchers.withId(R.id.elt_camera_indicator))
            .check(ViewAssertions.matches(EspressoTestUtils.withTagAsResourceId(
                R.drawable.ric_adas_avm_right_side)))
        assertViewHasEffectiveVisibility(R.id.camera_ovl_rvc_std_fragment)
        assertViewMvc()
    }

    @Test
    fun should_toggle_auto_zoom_when_clicked_and_active() {
        displayCameraView(View.REAR_VIEW, ALL)
        surroundViewRepository.autoZoomRearViewActivation.postValue(false)

        onView(ViewMatchers.withId(R.id.mvc_auto_zoom))
            .perform(EspressoTestUtils.setCheck(true))

        assertViewIsChecked(R.id.mvc_auto_zoom)

        onView(ViewMatchers.withId(R.id.mvc_auto_zoom))
            .perform(EspressoTestUtils.setCheck(false))

        assertViewIsNotChecked(R.id.mvc_auto_zoom)
    }

    @Test
    fun should_navigate_to_or_from_mvc_camera_settings_screen_by_click_on_settings_or_back() {
        displayCameraView(View.FRONT_VIEW, ALL)
        assertViewMvc()

        // WHEN the user clicks on 'settings' icon to navigate to MVC camera settings screen
        onView(ViewMatchers.withId(R.id.toolbar_icon_settings))
            .perform(ViewActions.click())
        navigateFullscreen(R.id.mvcSettings)
        displayCameraView(View.SETTINGS_FRONT_VIEW, ALL)

        assertViewHasEffectiveVisibility(R.id.rvc_standard_camera_fragment)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.luminosity_seek_bar)
        assertViewHasEffectiveVisibility(R.id.contrast_seek_bar)
        assertViewHasEffectiveVisibility(R.id.hue_seek_bar)
        assertViewHasEffectiveVisibility(R.id.mvcDynamicGuideline)
        assertViewHasEffectiveVisibility(R.id.mvcStaticGuideline)
        assertViewHasEffectiveVisibility(R.id.mvcTrailerGuideline)
        assertViewHasEffectiveVisibility(R.id.mvcAutoZoomGuideline)
        EspressoTestUtils.assertViewDoesNotExist(R.id.toolbar_icon_settings)
        assertViewHasEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
        assertEquals<CharSequence>(
            fullscreenActivityTestRule.activity.renaultToolbar.title,
            context.resources.getString(R.string.rlb_parkassist_camera_settings)
        )

        // WHEN the user clicks on 'back' icon to navigate to MVC main screen
        onView(ViewMatchers.withId(R.id.car_ui_toolbar_nav_icon))
            .perform(ViewActions.click())
        navigateFullscreen(R.id.mvcFragment)
        displayCameraView(View.FRONT_VIEW, ALL)
        assertViewMvc()
    }

    @Test
    fun should_show_error_overlay_when_camera_failure() {
        displayCameraView(View.FRONT_VIEW, ALL)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_CAMERA_FAILURE)
        onView(ViewMatchers.withId(R.id.camera_error_overlay))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun should_hide_error_overlay_when_camera_failure_gone() {
        displayCameraView(View.FRONT_VIEW, ALL)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_CAMERA_FAILURE)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_NO_ERROR)
        EspressoTestUtils.assertViewIsGone(R.id.camera_error_overlay)
    }

    @Test
    fun should_display_mvc_and_sonar_main_screen() {
        displayCameraView(View.REAR_VIEW, ButtonsMode.REGULATORY, forceCloseable = true)
        sonarRepository.closeAllowed.postValue(true)
        assertViewHasEffectiveVisibility(R.id.sonar_fragment)
        assertViewHasNoEffectiveVisibility(R.id.car)
    }

    @Test
    fun hide_sonar_when_UPA_is_deactivate() { // ktlint-disable max-line-length
        displayCameraView(View.REAR_VIEW, ButtonsMode.REGULATORY)
        sonarRepository.frontState.postValue(GroupState.DISABLED)
        sonarRepository.rearState.postValue(GroupState.DISABLED)
        sonarRepository.flankState.postValue(GroupState.DISABLED)
        assertViewHasNoEffectiveVisibility(R.id.car)
    }

    @Test
    fun hide_sonar_when_UPA_is_activate_and_display_request_none() {
        displayCameraView(View.REAR_VIEW, ButtonsMode.REGULATORY)
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        sonarRepository.displayRequest.postValue(DisplayType.NONE)
        assertViewHasNoEffectiveVisibility(R.id.car)
    }

    @Test
    fun show_sonar_when_UPA_is_activate_and_display_request_different_to_none() {
        displayCameraView(View.REAR_VIEW, ButtonsMode.REGULATORY)
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        assertViewHasEffectiveVisibility(R.id.car)
    }

    @Test
    fun show_sonar_when_UPA_rear_flank_are_activate_and_front_deactive_and_display_request_different_to_none() {
        displayCameraView(View.REAR_VIEW, ButtonsMode.REGULATORY)
        sonarRepository.frontState.postValue(GroupState.DISABLED)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        assertViewHasEffectiveVisibility(R.id.car)
    }


    @Test
    fun should_not_display_raeb_when_no_alert() {
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(View.REAR_VIEW, ALL)
        // AND current view is MVC Auto or Rear view
        clickOnView(R.id.mvc_rear)
        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_NO_ALERT)
        // THEN RAEB is displayed
        EspressoTestUtils.assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_raeb_when_alert_received() {
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(View.REAR_VIEW, ALL)
        // AND current view is MVC Auto or Rear view
        clickOnView(R.id.mvc_rear)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        // THEN RAEB is displayed
        EspressoTestUtils.assertViewIsCompletelyDisplayed(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        // THEN RAEB is displayed
        EspressoTestUtils.assertViewIsCompletelyDisplayed(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
        // THEN RAEB is displayed
        EspressoTestUtils.assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_raeb_off_when_raeb_disabled() {
        sonarRepository.raebFeaturePresent = true
        // GIVEN RAEB is disabled
        sonarRepository.raebAlertEnabled.postValue(false)

        // AND the rear gear is engaged
        displayCameraView(View.REAR_VIEW, ALL)

        // AND current view is MVC Rear or Auto view
        clickOnView(R.id.mvc_rear)

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

        // WHEN RAEB alert not operational
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // AND current view is MVC Settings view
        clickOnView(R.id.toolbar_icon_settings)

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

        // GIVEN RAEB is enabled
        sonarRepository.raebAlertEnabled.postValue(true)

        // WHEN RAEB alert not operational
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        assertViewHasNoEffectiveVisibility(R.id.raeb_left)
    }

    @Test
    fun should_not_display_raeb_when_user_deactivates_raeb_and_raeb_alert() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebFeaturePresent = true
        // AND the rear gear is engaged
        displayCameraView(View.REAR_VIEW, ALL)
        // AND current view is MVC auto or rear view
        clickOnView(R.id.mvc_rear)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        // AND user deactivates RAEB
        sonarRepository.raebAlertEnabled.postValue(false)
        // THEN RAEB is NOT displayed
        EspressoTestUtils.assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_not_display_raeb_when_obstacle_detected_from_the_left_and_the_right_AND_no_rear_view_ongoing() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND the MVC front view is displayed
        displayCameraView(View.FRONT_VIEW, ALL)
        // AND current view is MVC front view
        clickOnView(R.id.mvc_front)
        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        // THEN RAEB is not displayed
        EspressoTestUtils.assertViewIsNotDisplayed(R.id.raeb_left)
        EspressoTestUtils.assertViewIsNotDisplayed(R.id.raeb_right)
    }

    @Test
    fun should_display_left_and_right_sonar_and_camera_rcta_when_obstacle_detected_from_the_left_and_the_right() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(View.REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left and right
        sonarRepository.collisionAlertSide.postValue(AllianceCarSonarManager.SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(
            AllianceCarSonarManager.LEVEL_HIGH_COLLISION_RISK)
        // THEN the Left and Right Rear Cross Trafic Alert is displayed in both
        // AVM camera and sonar views

        EspressoTestUtils.assertViewIsCompletelyDisplayed(R.id.rcta_right)
        EspressoTestUtils.assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_not_display_left_and_right_sonar_and_camera_rcta_when_obstacle_detected_from_the_left_and_the_right_AND_user_deactivates_rcta_in_standard_mode() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND the rear gear is engaged
        displayCameraView(View.REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left and right
        sonarRepository.collisionAlertSide.postValue(AllianceCarSonarManager.SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(
            AllianceCarSonarManager.LEVEL_HIGH_COLLISION_RISK)
        // AND user deactivates RCTA
        sonarRepository.collisionAlertEnabled.postValue(false)
        // THEN left and right RCTA are NOT displayed in both AVM camera and sonar views
        EspressoTestUtils.assertViewIsNotDisplayed(R.id.rcta_right)
        EspressoTestUtils.assertViewIsNotDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_not_display_left_and_right_sonar_and_camera_rcta_when_obstacle_detected_from_the_left_and_the_right_AND_no_rear_view_ongoing() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND MVC front view is displayed
        displayCameraView(View.FRONT_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left and right
        sonarRepository.collisionAlertSide.postValue(AllianceCarSonarManager.SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(AllianceCarSonarManager.LEVEL_HIGH_COLLISION_RISK)
        // AND user activate RCTA
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN left and right RCTA are NOT displayed in both AVM camera and sonar views
        EspressoTestUtils.assertViewIsNotDisplayed(R.id.rcta_right)
        EspressoTestUtils.assertViewIsNotDisplayed(R.id.rcta_left)
    }

}