package com.renault.parkassist

import android.content.res.Configuration
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.KoinTestBase.ButtonsMode.REGULATORY
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.repository.surroundview.View.*
import com.renault.parkassist.ui.avm.AvmFragment
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.checkToolbarTitle
import com.renault.parkassist.utils.EspressoTestUtils.setCheck
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AvmRegulatoryTest : OverlayActivityTest() {

    @Before
    fun setup() {
        clearAll()
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchFullScreen()
        navigateFullscreen(R.id.avmFragment)

        // Sonar sound feature is present
        soundRepository.soundActivationControlPresence = true
        soundRepository.temporaryMuteControlPresence = true
        sonarRepository.displayRequest.postValue(DisplayType.WIDGET)
    }

    @After
    fun teardown() {
        displayCameraView()
    }

    @Test
    fun should_display_avm_popup_when_obstacles_detection_occurs() { // ktlint-disable max-line-length
        launchPopUp()
        navigatePopUp(R.id.avmPipFragment)
        displayCameraView(POP_UP_VIEW)
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)

        assertViewHasEffectiveVisibility(R.id.avm_popup)
        assertViewHasEffectiveVisibility(R.id.avm_pip_camera)
        assertViewHasEffectiveVisibility(R.id.avm_pip_close)
        assertViewHasEffectiveVisibility(R.id.avm_popup_camera_fragment)
        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_popup_fragment)
        assertViewHasEffectiveVisibility(R.id.camera_sonar_mute_fragment)
        assertViewHasEffectiveVisibility(R.id.sonar_mute_button)
    }

    @Test
    fun should_display_avm_main_screen_when_rear_gear_is_engaged() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, REGULATORY)

        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_std_fragment)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        assertViewAvm()
    }

    @Test
    fun should_display_avm_side_view_when_user_selects_the_side_view() {
        displayCameraView(REAR_VIEW, REGULATORY)

        onView(withTagValue(`is`(AvmFragment.sideViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(SIDES_VIEW, REGULATORY)

        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_sides_fragment)
        assertViewAvm()
    }

    @Test
    fun should_display_avm_3d_view_when_user_selects_the_3d_view() {
        displayCameraView(REAR_VIEW, REGULATORY)

        onView(withTagValue(`is`(AvmFragment.threeDButtonTag)))
            .perform(setCheck(true))
        displayCameraView(THREE_DIMENSION_VIEW, REGULATORY)

        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_3d_fragment)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        assertViewAvm()
    }

    @Test
    fun should_display_avm_panoramic_view_when_user_selects_the_panoramic_view() { // ktlint-disable max-line-length
        displayCameraView(REAR_VIEW, REGULATORY)

        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_std_fragment)

        onView(withTagValue(`is`(AvmFragment.panoramicViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, REGULATORY)

        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_pano_fragment)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        assertViewAvm()
    }

    @Test
    fun should_display_avm_standard_view_when_user_selects_the_standard_view() { // ktlint-disable max-line-length
        displayCameraView(PANORAMIC_REAR_VIEW, REGULATORY)
        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_pano_fragment)

        onView(withTagValue(`is`(AvmFragment.standardViewButtonTag)))
            .perform(setCheck(true))

        displayCameraView(REAR_VIEW, REGULATORY)

        assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_std_fragment)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        assertViewAvm()
    }

    private fun assertViewAvm() {
        assertViewHasEffectiveVisibility(R.id.avm_main)
        assertViewHasEffectiveVisibility(R.id.elt_avm_camera_view_container)

        Assert.assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            checkToolbarTitle(R.id.car_ui_toolbar, R.string.rlb_parkassist_avm)
            assertViewHasEffectiveVisibility(R.id.elt_avm_disclaimer)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            checkToolbarTitle(R.id.car_ui_toolbar, R.string.rlb_parkassist_avm_disclaimer)
        }
        assertViewWithTagHasEffectiveVisibility(AvmFragment.standardViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(AvmFragment.panoramicViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(AvmFragment.sideViewButtonTag)
        assertViewWithTagHasEffectiveVisibility(AvmFragment.threeDButtonTag)

        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
        assertViewHasNoEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
    }
}