package com.renault.parkassist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.repository.surroundview.View.REAR_VIEW
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import com.renault.parkassist.utils.RetryTestRule
import io.mockk.every
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AvmSonarMuteUnmuteTest : OverlayActivityTest() {

    @get:Rule
    val retry = RetryTestRule(5)

    @Before
    fun setup() {
        every { soundRepository.mute(any()) } answers {
            soundRepository.muted.postValue(firstArg())
        }
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
    fun should_mute_sound_when_button_clicked_from_unmuted_state() { // ktlint-disable max-line-length
        // Sonar sound feature is present
        soundRepository.temporaryMuteControlPresence = true
        // and Sonar sound is not muted
        soundRepository.muted.postValue(false)
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)

        // GIVEN Sonar View is displayed
        displayCameraView(REAR_VIEW)

        // WHEN the user selects the mute button
        onView(withId(R.id.sonar_mute_button)).check(
            matches(
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        )
        clickOnView(R.id.sonar_mute_button)
        // THEN the Sonar sound is muted
        onView(withId(R.id.sonar_mute_button))
            .check(matches(isChecked()))
    }

    @Test
    fun should_unmute_sound_when_button_clicked_from_muted_state() { // ktlint-disable max-line-length
        // Sonar sound is muted
        soundRepository.muted.postValue(true)
        // and Sonar sound feature is present
        soundRepository.temporaryMuteControlPresence = true
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        // and Sonar Sound volume > 0
        soundRepository.volume.postValue(1)

        // GIVEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        onView(withId(R.id.sonar_mute_button)).check(
            matches(
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        )
        // WHEN the user selects the mute button
        clickOnView(R.id.sonar_mute_button)
        // THEN the Sonar sound is unmuted
        onView(withId(R.id.sonar_mute_button))
            .check(matches(isNotChecked()))
    }

    @Test
    fun should_hide_mute_button_when_upa_audio_not_present() { // ktlint-disable max-line-length
        // GIVEN UPA Audio not present
        soundRepository.soundActivationControlPresence = false
        // WHEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // THEN Sonar sound is not visible
        onView(withId(R.id.sonar_mute_button)).check(
            matches(withEffectiveVisibility(Visibility.GONE))
        )
    }

    @Test
    fun should_hide_button_when_sound_disabled() { // ktlint-disable max-line-length
        // GIVEN  UPA Audio is present
        soundRepository.temporaryMuteControlPresence = true
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(false)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        // WHEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // THEN Sonar sound is not visible
        onView(withId(R.id.sonar_mute_button)).check(
            matches(withEffectiveVisibility(Visibility.GONE))
        )
    }

    @Test
    fun should_display_button_when_audio_present_and_sound_enabled_and_upda_activate() { // ktlint-disable max-line-length
        // GIVEN  UPA Audio is present
        soundRepository.temporaryMuteControlPresence = true
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        // WHEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // THEN Sonar sound is visible
        onView(withId(R.id.sonar_mute_button)).check(
            matches(withEffectiveVisibility(Visibility.VISIBLE))
        )
        // AND Sonar sound is enabled
        onView(withId(R.id.sonar_mute_button)).check(
            matches(isEnabled())
        )
    }
}