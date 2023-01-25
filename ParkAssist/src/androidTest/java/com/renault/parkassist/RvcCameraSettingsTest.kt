package com.renault.parkassist

import alliancex.renault.ui.RenaultToggleIconButton
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.repository.surroundview.View.SETTINGS_REAR_VIEW
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.dynamicLinesButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.staticLinesButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.trailerButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.zoomAutoButtonTag
import com.renault.parkassist.utils.DisableAnimationsRule
import com.renault.parkassist.utils.EspressoTestUtils
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSliderValue
import com.renault.parkassist.utils.EspressoTestUtils.assertViewDoesNotExist
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagIsChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagIsNotChecked
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import com.renault.parkassist.utils.EspressoTestUtils.setPercentageProgressOnSlider
import com.renault.parkassist.utils.EspressoTestUtils.waitForView
import io.mockk.every
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RvcCameraSettingsTest : OverlayActivityTest() {

    @get:Rule
    var disableAnimationsRule = DisableAnimationsRule()

    @Before
    fun setup() {
        clearAll()
        surroundViewRepository.apply {
            every { setStaticGuidelinesActivation(any()) } answers {
                staticGuidelinesActivation.postValue(firstArg())
            }
            every { setDynamicGuidelinesActivation(any()) } answers {
                dynamicGuidelinesActivation.postValue(firstArg())
            }
            every { setTrailerGuidelinesActivation(any()) } answers {
                trailerGuidelinesActivation.postValue(firstArg())
            }
            every { setAutoZoomRearViewActivation(any()) } answers {
                autoZoomRearViewActivation.postValue(firstArg())
            }
        }

        surroundViewRepository.isDynamicGuidelinesSupported = true
        surroundViewRepository.isStaticGuidelinesSupported = true
        surroundViewRepository.isTrailerGuidelinesSupported = true
        surroundViewRepository.isAutoZoomSupported = true
        surroundViewRepository.dynamicGuidelinesActivation.postValue(true)
        surroundViewRepository.autoZoomRearViewActivation.postValue(true)
        surroundViewRepository.trailerGuidelinesActivation.postValue(true)
        surroundViewRepository.staticGuidelinesActivation.postValue(true)

        surroundViewRepository.brightnessMin = 0
        surroundViewRepository.brightnessMax = 100
        surroundViewRepository.colorMin = 0
        surroundViewRepository.colorMax = 100
        surroundViewRepository.contrastMin = 0
        surroundViewRepository.contrastMax = 100

        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        launchFullScreen()
        navigateFullscreen(R.id.rvcFragment)

        displayCameraView(View.REAR_VIEW, ButtonsMode.REGULATORY)
        assertViewHasEffectiveVisibility(R.id.rvc_main)
        assertViewHasEffectiveVisibility(R.id.rvc_camera_sonar_container)
        assertToolbarIsInRegulatoryState()
    }

    @After
    fun teardown() {
        displayCameraView()
    }

    @Test
    fun should_display_camera_settings_when_clicked_settings_button_from_rvc_view() { // ktlint-disable max-line-length
        gotoCameraSettings()

        assertViewHasEffectiveVisibility(R.id.luminosity_seek_bar)
        assertViewHasEffectiveVisibility(R.id.contrast_seek_bar)
        assertViewHasEffectiveVisibility(R.id.hue_seek_bar)
        assertViewWithTagHasEffectiveVisibility(dynamicLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(staticLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(trailerButtonTag)
        assertViewWithTagHasEffectiveVisibility(zoomAutoButtonTag)
        assertEquals<CharSequence>(
            fullscreenActivityTestRule.activity.renaultToolbar.title,
            context.resources.getString(R.string.rlb_parkassist_camera_settings)
        )
    }

    @Test
    fun should_hide_dynamic_guidelines_toggle_buttons_when_feature_is_unavailable() { // ktlint-disable max-line-length
        surroundViewRepository.isDynamicGuidelinesSupported = false
        gotoCameraSettings()

        assertNull(findButton(dynamicLinesButtonTag))
        assertViewWithTagHasEffectiveVisibility(staticLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(trailerButtonTag)
        assertViewWithTagHasEffectiveVisibility(zoomAutoButtonTag)
    }

    @Test
    fun should_hide_static_guidelines_toggle_buttons_when_feature_is_unavailable() { // ktlint-disable max-line-length
        surroundViewRepository.isStaticGuidelinesSupported = false
        gotoCameraSettings()

        assertViewWithTagHasEffectiveVisibility(dynamicLinesButtonTag)
        assertNull(findButton(staticLinesButtonTag))
        assertViewWithTagHasEffectiveVisibility(trailerButtonTag)
        assertViewWithTagHasEffectiveVisibility(zoomAutoButtonTag)
    }

    @Test
    fun should_hide_trailer_guidelines_toggle_buttons_when_feature_is_unavailable() { // ktlint-disable max-line-length
        surroundViewRepository.isTrailerGuidelinesSupported = false
        gotoCameraSettings()

        assertViewWithTagHasEffectiveVisibility(dynamicLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(staticLinesButtonTag)
        assertNull(findButton(trailerButtonTag))
        assertViewWithTagHasEffectiveVisibility(zoomAutoButtonTag)
    }

    @Test
    fun should_hide_autozoom_toggle_buttons_when_feature_is_unavailable() { // ktlint-disable max-line-length
        surroundViewRepository.isAutoZoomSupported = false
        gotoCameraSettings()

        assertViewWithTagHasEffectiveVisibility(dynamicLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(staticLinesButtonTag)
        assertViewWithTagHasEffectiveVisibility(trailerButtonTag)
        assertNull(findButton(zoomAutoButtonTag))
    }

    @Test
    fun should_toggle_fixed_guidelines_when_clicked() { // ktlint-disable max-line-length
        gotoCameraSettings()
        surroundViewRepository.staticGuidelinesActivation.postValue(false)

        onView(withTagValue(`is`(staticLinesButtonTag)))
            .perform(EspressoTestUtils.setCheck(true))
        assertCameraDisplaysFixedGuidelines()
        assertViewWithTagIsChecked(staticLinesButtonTag)

        onView(withTagValue(`is`(staticLinesButtonTag)))
            .perform(EspressoTestUtils.setCheck(false))
        assertCameraDisplaysFixedGuidelines()
        assertViewWithTagIsNotChecked(staticLinesButtonTag)
    }

    @Test
    fun should_toggle_dynamic_guidelines_when_clicked() { // ktlint-disable max-line-length
        gotoCameraSettings()
        surroundViewRepository.dynamicGuidelinesActivation.postValue(false)

        onView(withTagValue(`is`(dynamicLinesButtonTag)))
            .perform(EspressoTestUtils.setCheck(true))

        assertCameraDisplaysDynamicGuidelines()
        assertViewWithTagIsChecked(dynamicLinesButtonTag)

        onView(withTagValue(`is`(dynamicLinesButtonTag)))
            .perform(EspressoTestUtils.setCheck(false))

        assertCameraDisplaysDynamicGuidelines()
        assertViewWithTagIsNotChecked(dynamicLinesButtonTag)
    }

    @Test
    fun should_toggle_trailer_guidelines_when_clicked() { // ktlint-disable max-line-length
        gotoCameraSettings()
        surroundViewRepository.trailerGuidelinesActivation.postValue(false)

        onView(withTagValue(`is`(trailerButtonTag)))
            .perform(EspressoTestUtils.setCheck(true))

        assertCameraDisplaysTrailerGuidelines()
        assertViewWithTagIsChecked(trailerButtonTag)

        onView(withTagValue(`is`(trailerButtonTag)))
            .perform(EspressoTestUtils.setCheck(false))

        assertCameraDisplaysTrailerGuidelines()
        assertViewWithTagIsNotChecked(trailerButtonTag)
    }

    @Test
    fun should_toggle_auto_zoom_when_clicked_and_active() { // ktlint-disable max-line-length
        gotoCameraSettings()
        surroundViewRepository.autoZoomRearViewActivation.postValue(false)

        onView(withTagValue(`is`(zoomAutoButtonTag)))
            .perform(EspressoTestUtils.setCheck(true))

        assertViewWithTagIsChecked(zoomAutoButtonTag)

        onView(withTagValue(`is`(zoomAutoButtonTag)))
            .perform(EspressoTestUtils.setCheck(false))

        assertViewWithTagIsNotChecked(zoomAutoButtonTag)
    }

    @Test
    fun should_update_brightness_seekbar_value_when_clicked() { // ktlint-disable max-line-length
        gotoCameraSettings()

        setPercentageProgressOnSlider(R.id.luminosity_seek_bar, 100)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(200)
        verify(exactly = 1) { surroundViewRepository.setBrightness(100) }

        assertRenaultSliderValue(R.id.luminosity_seek_bar, 100, true)
    }

    @Test
    fun should_update_contrast_seekbar_value_when_clicked() { // ktlint-disable max-line-length
        gotoCameraSettings()

        setPercentageProgressOnSlider(R.id.contrast_seek_bar, 40)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(200)
        verify(exactly = 1) { surroundViewRepository.setContrast(40) }

        assertRenaultSliderValue(R.id.contrast_seek_bar, 40, true)
    }

    @Test
    fun should_update_color_seekbar_value_when_clicked() { // ktlint-disable max-line-length
        gotoCameraSettings()

        setPercentageProgressOnSlider(R.id.hue_seek_bar, 20)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(200)
        verify { surroundViewRepository.setColor(20) }

        assertRenaultSliderValue(R.id.hue_seek_bar, 20, true)
    }

    @Test
    fun should_close_settings_when_back_button_pressed() { // ktlint-disable max-line-length
        gotoCameraSettings()

        // WHEN the user selects the back button (or vehicle in nav bar if no back button)
        clickOnView(R.id.car_ui_toolbar_nav_icon)
        navigateFullscreen(R.id.rvcFragment)
        // THEN we get back to rvc screen
        assertViewHasEffectiveVisibility(R.id.rvc_main)
        assertViewHasEffectiveVisibility(R.id.rvc_camera_sonar_container)
        assertViewDoesNotExist(R.id.camera_settings_camera_fragment, null)
        assertViewHasNoEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
        assertToolbarIsInRegulatoryState()
    }

    private fun gotoCameraSettings() {
        // WHEN the user clicks on 'settings' icon
        clickOnView(R.id.toolbar_icon_settings)
        navigateFullscreen(R.id.rvcSettings)
        displayCameraView(SETTINGS_REAR_VIEW, ButtonsMode.REGULATORY)
        waitForView(R.id.elt_camera_view)

        assertViewHasEffectiveVisibility(R.id.camera_settings_camera_fragment)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_camera_overlay_container)

        assertViewHasEffectiveVisibility(R.id.camera_ovl_rvc_settings_fragment)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        assertViewHasEffectiveVisibility(R.id.camera_sonar_alerts_fragment)
    }

    private fun assertToolbarIsInRegulatoryState() {
        assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
        assertViewHasNoEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
    }

    private fun findButton(tag: String): RenaultToggleIconButton? =
        fullscreenActivityTestRule
            .activity
            .renaultButtonBar
            .findButtonWithTag(tag)

    private fun assertCameraDisplaysFixedGuidelines() {
        // TODO Check that the camera displays the fixed guidelines
    }

    private fun assertCameraDisplaysDynamicGuidelines() {
        // TODO Check that the camera displays the dynamic guidelines
    }

    private fun assertCameraDisplaysTrailerGuidelines() {
        // TODO Check that the camera displays the dynamic guidelines
    }
}