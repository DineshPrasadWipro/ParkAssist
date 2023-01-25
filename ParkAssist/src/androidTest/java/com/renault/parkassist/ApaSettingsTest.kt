package com.renault.parkassist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.repository.apa.DisplayState
import com.renault.parkassist.repository.apa.FeatureConfig
import com.renault.parkassist.repository.apa.ManeuverType
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsCompletelyDisplayed
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import io.mockk.verify
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

// TODO: Re-enable tests related to radio preferences when migration has been done
//  ref CCSEXT-71793
@LargeTest
@RunWith(AndroidJUnit4::class)
class ApaSettingsTest : OverlayActivityTest() {

    @Before
    fun setup() {
        automaticParkAssistRepository.featureConfiguration = FeatureConfig.HFP
        automaticParkAssistRepository.supportedManeuvers =
            listOf(ManeuverType.PARALLEL, ManeuverType.PERPENDICULAR, ManeuverType.PARKOUT)
        launchFullScreen()
        navigateFullscreen(R.id.sonarHfpScanningFragment)
    }

    @Ignore("Radio preference behavior was changed with RenaultUI library")
    @Test
    fun should_display_settings_when_settings_button_clicked() { // ktlint-disable max-line-length
        // GIVEN APA scanning or guidance screen are displayed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        // WHEN the user press the settings button
        clickOnView(R.id.toolbar_icon_settings)
        // onView(withId(R.id.toolbar_icon_settings)).perform(click())
        // THEN the APA setting screen is displayed
        assertViewIsCompletelyDisplayed(R.id.apa_settings)

        assertViewHasEffectiveVisibility(R.id.car_apa_settings)
        assertViewHasEffectiveVisibility(R.id.apa_settings_park)

        automaticParkAssistRepository.defaultManeuverType.postValue(
            ManeuverType.PERPENDICULAR
        )
        onView(withText(R.string.rlb_parkassist_apa_settings_perpendicular)).check(
            matches(
                isChecked()
            )
        )
        assertViewHasEffectiveVisibility(R.id.car_apa_settings)
        assertViewHasEffectiveVisibility(R.id.apa_settings_park)
    }

    @Ignore("Radio preference behavior was changed with RenaultUI library")
    @Test
    fun should_switch_to_default_perpendicular() { // ktlint-disable max-line-length
        // GIVEN APA settings screen is displayed with current default maneuver parallel
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        clickOnView(R.id.toolbar_icon_settings)
        automaticParkAssistRepository.defaultManeuverType.postValue(
            ManeuverType.PARALLEL
        )
        // WHEN the user selects the perpendicular maneuver
        onView(withText(R.string.rlb_parkassist_apa_settings_perpendicular)).perform(click())
        // THEN perpendicular maneuver button is highlighted
        onView(withText(R.string.rlb_parkassist_apa_settings_perpendicular)).check(
            matches(
                isChecked()
            )
        )
        // AND the default maneuver is changed to perpendicular
        verify {
            automaticParkAssistRepository.setDefaultManeuverType(
                ManeuverType.PERPENDICULAR
            )
        }
    }

    @Ignore("Radio preference behavior was changed with RenaultUI library")
    @Test
    fun should_switch_to_default_parallel() { // ktlint-disable max-line-length
        // GIVEN APA settings screen is displayed with current default maneuver perpendicular
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        clickOnView(R.id.toolbar_icon_settings)
        automaticParkAssistRepository.defaultManeuverType.postValue(
            ManeuverType.PERPENDICULAR
        )
        // WHEN the user selects the parallel maneuver
        onView(withText(R.string.rlb_parkassist_apa_settings_parallel)).perform(click())
        // THEN perpendicular maneuver button is highlighted
        onView(withText(R.string.rlb_parkassist_apa_settings_parallel)).check(matches(isChecked()))
        // AND the default maneuver is changed to parallel
        verify {
            automaticParkAssistRepository.setDefaultManeuverType(
                ManeuverType.PARALLEL
            )
        }
    }

    @Test
    fun should_go_back_to_previous_screen() { // ktlint-disable max-line-length
        // GIVEN APA settings screen is displayed
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)
        clickOnView(R.id.toolbar_icon_settings)
        // WHEN the user press the back button
        clickOnView(R.id.car_ui_toolbar_nav_icon)
        // THEN initial screen (scanning) is displayed
        assertViewHasEffectiveVisibility(R.id.apa_mainscanning)
    }
}