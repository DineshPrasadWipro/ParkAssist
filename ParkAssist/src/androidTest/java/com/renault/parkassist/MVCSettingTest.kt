package com.renault.parkassist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.renault.parkassist.R
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.ui.mvc.MvcSettingsFragment
import com.renault.parkassist.utils.EspressoTestUtils
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsNotChecked
import io.mockk.every
import io.mockk.verify
import org.junit.*
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@LargeTest
@RunWith(AndroidJUnit4::class)
class MVCSettingTest : OverlayActivityTest() {

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

        setVehicleConfiguration(ParkAssistHwConfig.MVC)
        launchFullScreen()
        navigateFullscreen(R.id.mvcFragment)

        displayCameraView(View.REAR_VIEW, ButtonsMode.REGULATORY)
        assertViewHasEffectiveVisibility(R.id.mvc_main)
        assertViewHasEffectiveVisibility(R.id.fragment_camera_colorimetry)
        assertToolbarIsInRegulatoryState()

        launchFragmentInContainer<MvcSettingsFragment>()
    }

    @After
    fun teardown() {
        displayCameraView()
    }

    @Test
    fun should_display_camera_settings_when_clicked_settings_button_from_mvc_view() { // ktlint-disable max-line-length
        gotoCameraSettings()

        assertViewHasEffectiveVisibility(R.id.luminosity_seek_bar)
        assertViewHasEffectiveVisibility(R.id.contrast_seek_bar)
        assertViewHasEffectiveVisibility(R.id.hue_seek_bar)

        assertViewHasEffectiveVisibility(R.id.default_view_radio_pref_settings)

        assertViewHasEffectiveVisibility(R.id.mvcDynamicGuideline)
        assertViewHasEffectiveVisibility(R.id.mvcStaticGuideline)
        assertViewHasEffectiveVisibility(R.id.mvcTrailerGuideline)
        assertViewHasEffectiveVisibility(R.id.mvcAutoZoomGuideline)

        assertEquals<CharSequence>(
            fullscreenActivityTestRule.activity.renaultToolbar.title,
            context.resources.getString(R.string.rlb_parkassist_camera_settings)
        )
    }

    @Test
    fun should_close_settings_when_back_button_pressed() { // ktlint-disable max-line-length
        gotoCameraSettings()

        // WHEN the user selects the back button (or vehicle in nav bar if no back button)
        EspressoTestUtils.clickOnView(R.id.car_ui_toolbar_nav_icon)
        navigateFullscreen(R.id.mvcFragment)
        // THEN we get back to mvc screen
        assertViewHasEffectiveVisibility(R.id.mvc_main)
        EspressoTestUtils.assertViewDoesNotExist(R.id.camera_settings_camera_fragment, null)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
        assertToolbarIsInRegulatoryState()
    }

    private fun gotoCameraSettings() {
        // WHEN the user clicks on 'settings' icon
        EspressoTestUtils.clickOnView(R.id.toolbar_icon_settings)
        navigateFullscreen(R.id.mvcSettings)
        displayCameraView(View.SETTINGS_REAR_VIEW, ButtonsMode.REGULATORY)
        EspressoTestUtils.waitForView(R.id.elt_camera_view)

        assertViewHasEffectiveVisibility(R.id.camera_settings_camera_fragment)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_camera_overlay_container)
    }

    private fun assertToolbarIsInRegulatoryState() {
        assertNotNull(fullscreenActivityTestRule.activity.renaultToolbar)
        assertViewHasEffectiveVisibility(R.id.toolbar_icon_settings)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
    }

    @Test
    fun should_update_brightness_seekbar_value_when_clicked() { // ktlint-disable max-line-length
        gotoCameraSettings()

        EspressoTestUtils.setPercentageProgressOnSlider(R.id.luminosity_seek_bar, 100)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(200)
        verify(exactly = 1) { surroundViewRepository.setBrightness(100) }

        EspressoTestUtils.assertRenaultSliderValue(R.id.luminosity_seek_bar, 100, true)
    }

    @Test
    fun should_update_contrast_seekbar_value_when_clicked() { // ktlint-disable max-line-length
        gotoCameraSettings()

        EspressoTestUtils.setPercentageProgressOnSlider(R.id.contrast_seek_bar, 40)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(200)
        verify(exactly = 1) { surroundViewRepository.setContrast(40) }

        EspressoTestUtils.assertRenaultSliderValue(R.id.contrast_seek_bar, 40, true)
    }

    @Test
    fun should_update_color_seekbar_value_when_clicked() { // ktlint-disable max-line-length
        gotoCameraSettings()

        EspressoTestUtils.setPercentageProgressOnSlider(R.id.hue_seek_bar, 20)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(200)
        verify { surroundViewRepository.setColor(20) }

        EspressoTestUtils.assertRenaultSliderValue(R.id.hue_seek_bar, 20, true)
    }

    @Test
    fun should_toggle_mvc_settings_auto_zoom_when_clicked_and_active() {
        displayCameraView(View.REAR_VIEW, ButtonsMode.ALL)
        surroundViewRepository.autoZoomRearViewActivation.postValue(false)

        onView(ViewMatchers.withId(R.id.mvcAutoZoomGuideline))
            .perform(EspressoTestUtils.setCheck(true))

        assertViewIsChecked(R.id.mvcAutoZoomGuideline)

        onView(ViewMatchers.withId(R.id.mvcAutoZoomGuideline))
            .perform(EspressoTestUtils.setCheck(false))

        assertViewIsNotChecked(R.id.mvcAutoZoomGuideline)
    }

    @Test
    fun should_toggle_mvc_settings_dynamic_guideline_when_clicked_and_active() {
        displayCameraView(View.REAR_VIEW, ButtonsMode.ALL)
        surroundViewRepository.dynamicGuidelinesActivation.postValue(false)

        onView(ViewMatchers.withId(R.id.mvcDynamicGuideline))
            .perform(EspressoTestUtils.setCheck(true))

        assertViewIsChecked(R.id.mvcDynamicGuideline)

        onView(ViewMatchers.withId(R.id.mvcDynamicGuideline))
            .perform(EspressoTestUtils.setCheck(false))

        assertViewIsNotChecked(R.id.mvcDynamicGuideline)
    }

    @Test
    fun should_toggle_mvc_settings_static_guideline_when_clicked_and_active() {
        displayCameraView(View.REAR_VIEW, ButtonsMode.ALL)
        surroundViewRepository.staticGuidelinesActivation.postValue(false)

        onView(ViewMatchers.withId(R.id.mvcStaticGuideline))
            .perform(EspressoTestUtils.setCheck(true))

        assertViewIsChecked(R.id.mvcStaticGuideline)

        onView(ViewMatchers.withId(R.id.mvcStaticGuideline))
            .perform(EspressoTestUtils.setCheck(false))

        assertViewIsNotChecked(R.id.mvcStaticGuideline)
    }

    @Test
    fun should_toggle_mvc_settings_trailer_when_clicked_and_active() {
        displayCameraView(View.REAR_VIEW, ButtonsMode.ALL)
        surroundViewRepository.trailerGuidelinesActivation.postValue(false)

        onView(ViewMatchers.withId(R.id.mvcTrailerGuideline))
            .perform(EspressoTestUtils.setCheck(true))

        assertViewIsChecked(R.id.mvcTrailerGuideline)

        onView(ViewMatchers.withId(R.id.mvcTrailerGuideline))
            .perform(EspressoTestUtils.setCheck(false))

        assertViewIsNotChecked(R.id.mvcTrailerGuideline)
    }
}