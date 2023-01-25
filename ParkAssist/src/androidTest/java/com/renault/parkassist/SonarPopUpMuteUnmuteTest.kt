package com.renault.parkassist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import io.mockk.every
import org.junit.Before
import org.junit.Test

class SonarPopUpMuteUnmuteTest : OverlayActivityTest() {

    @Before
    fun setup() {
        soundRepository.apply {
            every { mute(any()) } answers { muted.postValue(firstArg()) }
        }
        setVehicleConfiguration(ParkAssistHwConfig.SONAR)
        launchPopUp()
        navigatePopUp(R.id.sonarPipFragment)
    }

    @Test
    fun should_enable_button_when_sound_enabled_in_sonar_popup() { // ktlint-disable max-line-length
        // GIVEN  UPA Audio is present with settings
        soundRepository.temporaryMuteControlPresence = true
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        // WHEN Sonar Popup View is displayed
        sonarRepository.displayRequest.postValue(DisplayType.WIDGET)
        // THEN Sonar mute button is visible
        onView(ViewMatchers.withId(R.id.sonar_mute_button)).check(
            ViewAssertions.matches(
                ViewMatchers
                    .withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )
        // AND Sonar mute button is disabled (greyed out)
        onView(ViewMatchers.withId(R.id.sonar_mute_button)).check(
            ViewAssertions.matches(ViewMatchers.isEnabled())
        )
    }

    @Test
    fun should_hide_button_when_sound_disabled_in_sonar_popup() { // ktlint-disable max-line-length
        // GIVEN  UPA Audio is present with settings
        soundRepository.temporaryMuteControlPresence = true
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(false)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        // WHEN Sonar Popup View is displayed
        sonarRepository.displayRequest.postValue(DisplayType.WIDGET)
        // THEN Sonar mute button is not visible
        onView(ViewMatchers.withId(R.id.sonar_mute_button)).check(
            ViewAssertions.matches(
                ViewMatchers
                    .withEffectiveVisibility(ViewMatchers.Visibility.GONE)
            )
        )
    }
}