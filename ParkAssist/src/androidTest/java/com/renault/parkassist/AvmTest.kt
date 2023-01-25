package com.renault.parkassist

import android.content.res.Configuration
import android.widget.FrameLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.KoinTestBase.ButtonsMode.ALL
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.repository.surroundview.Action
import com.renault.parkassist.repository.surroundview.ErrorState
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.TrunkState.TRUNK_DOOR_OPENED
import com.renault.parkassist.repository.surroundview.View.*
import com.renault.parkassist.repository.surroundview.WarningState
import com.renault.parkassist.ui.avm.AvmFragment.Companion.panoramicViewButtonTag
import com.renault.parkassist.ui.avm.AvmFragment.Companion.sideViewButtonTag
import com.renault.parkassist.ui.avm.AvmFragment.Companion.standardViewButtonTag
import com.renault.parkassist.ui.avm.AvmFragment.Companion.threeDButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.dynamicLinesButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.staticLinesButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.trailerButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.zoomAutoButtonTag
import com.renault.parkassist.utils.EspressoTestUtils
import com.renault.parkassist.utils.EspressoTestUtils.assertTextIsDisplayed
import com.renault.parkassist.utils.EspressoTestUtils.assertViewDoesNotExist
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsCompletelyDisplayed
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsGone
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagIsChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagIsNotChecked
import com.renault.parkassist.utils.EspressoTestUtils.waitForView
import com.renault.parkassist.utils.EspressoTestUtils.withTagAsResourceId
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AvmTest : OverlayActivityTest() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchFullScreen()
        navigateFullscreen(R.id.avmFragment)
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
    }

    @After
    fun teardown() {
        displayCameraView()
    }

    @Test
    fun should_show_avm_screen_when_user_launch_avm_screen() { // ktlint-disable max-line-length
        displayCameraView(FRONT_VIEW, ALL)

        assertViewHasEffectiveVisibility(R.id.avm_main)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_camera_overlay_container)
        assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        assertViewWithTagHasEffectiveVisibility(standardViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(panoramicViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(sideViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(threeDButtonTag)
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            assertViewHasEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
    }

    @Test
    fun should_show_avm_camera_indicator_when_front_standard_or_panoramic_view() { // ktlint-disable max-line-length
        displayCameraView(FRONT_VIEW, ALL)

        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_front)))
    }

    @Test
    fun should_show_avm_camera_indicator_when_rear_standard_or_panoramic_view() { // ktlint-disable max-line-length
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_avm_rear)))
    }

    @Test
    fun should_show_avm_vertical_bars_when_bird_sides() { // ktlint-disable max-line-length
        displayCameraView(SIDES_VIEW, ALL)
        assertViewHasEffectiveVisibility(R.id.elt_avm_sides_mask_left)
        assertViewHasEffectiveVisibility(R.id.elt_avm_sides_mask_right)
    }

    @Test
    fun should_show_boot_open_when_user_launch_avm_rear_standard_view_and_trunk_door_open() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, ALL)
        // Trunk door opened
        surroundViewRepository.trunkState.postValue(TRUNK_DOOR_OPENED)
        // boot open disclaimer is displayed
        assertViewHasEffectiveVisibility(R.id.elt_boot_status_info)
    }

    @Test
    fun should_navigate_to_or_from_avm_camera_settings_screen_accordingly_to_click_on_settings_or_back() { // ktlint-disable max-line-length
        displayCameraView(FRONT_VIEW, ALL)
        // Use to check presence of sonar mute button in AVM settings screen
        // Sonar sound feature is present and sound enabled
        soundRepository.temporaryMuteControlPresence = true
        soundRepository.soundEnabled.postValue(true)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)

        assertViewHasEffectiveVisibility(R.id.avm_main)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            assertViewHasEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
        assertViewWithTagHasEffectiveVisibility(standardViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(panoramicViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(sideViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(threeDButtonTag)

        // WHEN the user clicks on 'settings' icon
        onView(withId(R.id.toolbar_icon_settings)).perform(click())
        navigateFullscreen(R.id.avmSettings)
        displayCameraView(SETTINGS_FRONT_VIEW, ALL)

        assertViewHasEffectiveVisibility(R.id.camera_settings_camera_fragment)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.luminosity_seek_bar)
        assertViewHasEffectiveVisibility(R.id.contrast_seek_bar)
        assertViewHasEffectiveVisibility(R.id.hue_seek_bar)
        assertViewWithTagHasEffectiveVisibility(dynamicLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(staticLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(trailerButtonTag)
        assertViewWithTagHasEffectiveVisibility(zoomAutoButtonTag)
        assertViewDoesNotExist(R.id.toolbar_icon_settings)
        assertViewHasEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_std_fragment)
        assertViewHasEffectiveVisibility(R.id.sonar_mute_button)

        assertTextIsDisplayed(R.string.rlb_parkassist_camera_settings)

        // WHEN the user clicks on 'back' icon
        onView(withId(R.id.car_ui_toolbar_nav_icon)).perform(click())
        navigateFullscreen(R.id.avmFragment)
        displayCameraView(FRONT_VIEW, ALL)

        assertViewHasEffectiveVisibility(R.id.avm_main)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            assertViewHasEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
        assertViewWithTagHasEffectiveVisibility(standardViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(panoramicViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(sideViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(threeDButtonTag)
    }

    @Test
    fun should_show_avm_3D_screen_when_user_launch_avm_3D_screen() { // ktlint-disable max-line-length
        displayCameraView(THREE_DIMENSION_VIEW, ALL)

        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_3d_fragment)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)

        onView(withId(R.id.elt_camera_indicator))
            .check(matches(withTagAsResourceId(R.drawable.ric_adas_360)))
        // 3D info is displayed
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            assertViewHasEffectiveVisibility(R.id.elt_avm_3d_info)
            assertViewHasEffectiveVisibility(R.id.elt_avm_disclaimer)
        }

        assertViewWithTagHasEffectiveVisibility(standardViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(panoramicViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(sideViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(threeDButtonTag)
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            assertViewHasEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
    }

    @Test
    fun should_not_show_dialog_when_warning_none_received() { // ktlint-disable max-line-length
        spyk(surroundViewRepository)
        surroundViewRepository.warningState.postValue(WarningState.WARNING_STATE_NONE)
        waitDialog()
        onView(withText(android.R.string.ok))
            .check(doesNotExist())
    }

    @Test
    fun should_show_error_overlay_when_camera_failure() {
        displayCameraView(REAR_VIEW, ALL)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_CAMERA_FAILURE)
        onView(withId(R.id.camera_error_overlay)).check(matches(isDisplayed()))
    }

    @Test
    fun should_show_mute_button_when_camera_failure() {
        displayCameraView(REAR_VIEW, ALL)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_CAMERA_FAILURE)
        soundRepository.temporaryMuteControlPresence = true
        soundRepository.soundEnabled.postValue(true)
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        soundRepository.volume.postValue(1)
        onView(withId(R.id.camera_error_overlay)).check(matches(isDisplayed()))
        assertViewIsCompletelyDisplayed(R.id.sonar_mute_button)
    }

    @Test
    fun should_hide_error_overlay_when_camera_failure_gone() {
        displayCameraView(PANORAMIC_FRONT_VIEW, ALL)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_CAMERA_FAILURE)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_NO_ERROR)
        assertViewIsGone(R.id.camera_error_overlay)
    }

    @Test
    fun should_not_change_camera_container_size_when_switching_from_sides_to_front_in_portrait() {
        // GIVEN portrait display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_PORTRAIT)
        waitForView(R.id.elt_avm_camera_view_container, true)
        displayCameraView(SIDES_VIEW)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        assertViewIsCompletelyDisplayed(R.id.elt_avm_camera_view_container)
        val cameraView = fullscreenActivityTestRule.activity
            .findViewById<FrameLayout>(R.id.elt_avm_camera_view_container)
        val previousSize = cameraView.height to cameraView.width
        displayCameraView(FRONT_VIEW)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        val nextHeight = cameraView.height to cameraView.width
        assertEquals(previousSize, nextHeight)
    }

    @Test
    fun should_not_change_camera_container_size_when_switching_from_3d_to_front_in_landscape() {
        // GIVEN landscape display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        waitForView(R.id.elt_avm_camera_view_container, true)
        displayCameraView(THREE_DIMENSION_VIEW)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        assertViewIsCompletelyDisplayed(R.id.elt_avm_camera_view_container)
        val cameraView = fullscreenActivityTestRule.activity
            .findViewById<FrameLayout>(R.id.elt_avm_camera_view_container)
        val previousSize = cameraView.height to cameraView.width
        displayCameraView(FRONT_VIEW)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        val nextHeight = cameraView.height to cameraView.width
        assertEquals(previousSize, nextHeight)
    }

    @Test
    fun should_not_show_three_d_info_during_view_transition() {
        // GIVEN portrait display
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_PORTRAIT)
        // AND AVM screen is displayed with FRONT_VIEW
        displayCameraView(FRONT_VIEW, ALL)
        // WHEN user selects 3D mode
        onView(withTagValue(`is`(threeDButtonTag)))
            .perform(EspressoTestUtils.setCheck(true))
        surroundViewRepository.surroundState.postValue(SurroundState(THREE_DIMENSION_VIEW, true))
        // THEN 3D info should not be displayed during transition
        assertViewIsGone(R.id.elt_avm_3d_info)
        displayCameraView(THREE_DIMENSION_VIEW, ALL)
        // AND the 3D info should be displayed once camera rendering is ongoing
        assertViewHasEffectiveVisibility(R.id.elt_avm_3d_info)
        surroundViewRepository.errorState.postValue(ErrorState.ERROR_STATE_CAMERA_FAILURE)
        // AND the 3D info should be displayed in case of camera error
        assertViewHasEffectiveVisibility(R.id.elt_avm_3d_info)
    }

    @Test
    fun should_highlight_selected_button_and_disable_listener_for_buttons_during_view_transitions() { // ktlint-disable max-line-length
        // GIVEN AVM screen is displayed with FRONT_VIEW
        displayCameraView(FRONT_VIEW, ALL)
        // WHEN user selects 3D mode
        onView(withTagValue(`is`(threeDButtonTag)))
            .perform(click())
        // THEN the repository receiver the correct value
        verify {
            surroundViewRepository.request(Action.SELECT_THREE_DIMENSION_VIEW)
        }
        surroundViewRepository.surroundState.postValue(SurroundState(THREE_DIMENSION_VIEW, true))
        // AND the 3D button should be selected
        assertViewWithTagIsChecked(threeDButtonTag)
        assertViewWithTagIsNotChecked(sideViewButtonTag)
        assertViewWithTagIsNotChecked(panoramicViewButtonTag)
        assertViewWithTagIsNotChecked(standardViewButtonTag)
        // AND buttons should not be clickable
        onView(withTagValue(`is`(threeDButtonTag)))
            .check(matches(not(isClickable())))
        onView(withTagValue(`is`(sideViewButtonTag)))
            .check(matches(not(isClickable())))
        onView(withTagValue(`is`(panoramicViewButtonTag)))
            .check(matches(not(isClickable())))
        onView(withTagValue(`is`(standardViewButtonTag)))
            .check(matches(not(isClickable())))

        // AND buttons should be clickable when surround request completed except the current one selected
        displayCameraView(THREE_DIMENSION_VIEW, ALL)
        assertViewWithTagIsChecked(threeDButtonTag)
        onView(withTagValue(`is`(threeDButtonTag)))
            .check(matches(isNotClickable()))
        assertViewWithTagIsNotChecked(sideViewButtonTag)
        onView(withTagValue(`is`(sideViewButtonTag)))
            .check(matches(isClickable()))

        assertViewWithTagIsNotChecked(panoramicViewButtonTag)
        onView(withTagValue(`is`(panoramicViewButtonTag)))
            .check(matches(isClickable()))

        assertViewWithTagIsNotChecked(standardViewButtonTag)
        onView(withTagValue(`is`(standardViewButtonTag)))
            .check(matches(isClickable()))
    }

    private fun waitDialog() = Thread.sleep(500)
}