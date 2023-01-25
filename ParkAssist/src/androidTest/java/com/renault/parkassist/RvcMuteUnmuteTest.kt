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
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RvcMuteUnmuteTest : OverlayActivityTest() {

    @get:Rule
    val retryTestRule = RetryTestRule(5)

    @Before
    fun setup() {
        soundRepository.apply {
            every { mute(any()) } answers { muted.postValue(firstArg()) }
        }
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
    fun should_mute_sound_when_button_clicked_from_unmuted_state() { // ktlint-disable max-line-length
        // Sonar sound feature is present
        soundRepository.temporaryMuteControlPresence = true
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        // Sonar sound is not muted
        soundRepository.muted.postValue(false)
        // UPA not deactivate
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // GIVEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // WHEN the user selects the mute button
        onView(withId(R.id.sonar_mute_button))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        clickOnView(R.id.sonar_mute_button)
        // THEN the Sonar sound is muted
        onView(withId(R.id.sonar_mute_button))
            .check(matches(isChecked()))
    }

    @Test
    fun should_unmute_sound_when_button_clicked_from_muted_state() { // ktlint-disable max-line-length
        // Sonar sound feature is present
        soundRepository.temporaryMuteControlPresence = true
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        // UPA not deactivate
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        // Sonar sound is muted
        soundRepository.muted.postValue(true)
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // GIVEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // WHEN the user selects the mute button
        onView(withId(R.id.sonar_mute_button))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        clickOnView(R.id.sonar_mute_button)
        // THEN the Sonar sound is unmuted
        onView(withId(R.id.sonar_mute_button))
            .check(matches(isNotChecked()))
    }

    @Test
    fun should_hide_mute_button_when_upa_audio_not_present() { // ktlint-disable max-line-length
        // GIVEN UPA Audio not present
        soundRepository.temporaryMuteControlPresence = false
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        // WHEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // THEN Sonar mute button is not visible
        onView(withId(R.id.sonar_mute_button)).check(
            matches(withEffectiveVisibility(Visibility.GONE))
        )
    }

    @Test
    fun should_hide_button_when_sound_disabled() { // ktlint-disable max-line-length
        // GIVEN  UPA Audio is present with settings
        soundRepository.temporaryMuteControlPresence = true
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        // and Upa activate
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(false)
        // WHEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // THEN Sonar mute button is not displayed
        onView(withId(R.id.sonar_mute_button)).check(
            matches(withEffectiveVisibility(Visibility.GONE))
        )
    }

    @Test
    fun should_display_button_when_sound_enabled() { // ktlint-disable max-line-length
        // GIVEN  UPA Audio is present
        soundRepository.temporaryMuteControlPresence = true
        sonarRepository.displayRequest.postValue(DisplayType.FULLSCREEN)
        // and UPA not deactivate
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // WHEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // THEN Sonar mute button is visible
        onView(withId(R.id.sonar_mute_button)).check(
            matches(withEffectiveVisibility(Visibility.VISIBLE))
        )
    }

    @Test
    fun should_hide_mute_button_when_sound_enabled_and_no_display_request() { // ktlint-disable max-line-length
        // GIVEN  UPA Audio is present
        soundRepository.temporaryMuteControlPresence = true
        // and UPA not deactivate
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        sonarRepository.displayRequest.postValue(DisplayType.NONE)
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // WHEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // THEN Sonar mute button is not visible
        onView(withId(R.id.sonar_mute_button)).check(
            matches(withEffectiveVisibility(Visibility.GONE))
        )
    }

    @Test
    fun should_hide_mute_button_when_upa_deactivate() { // ktlint-disable max-line-length
        // GIVEN  UPA Audio is present
        soundRepository.temporaryMuteControlPresence = true
        // and UPA not deactivate
        sonarRepository.flankState.postValue(GroupState.DISABLED)
        sonarRepository.rearState.postValue(GroupState.DISABLED)
        sonarRepository.frontState.postValue(GroupState.DISABLED)
        // and Sonar sound is enabled
        soundRepository.soundEnabled.postValue(true)
        // WHEN Sonar View is displayed
        displayCameraView(REAR_VIEW)
        // THEN Sonar mute button is visible
        onView(withId(R.id.sonar_mute_button)).check(
            matches(withEffectiveVisibility(Visibility.GONE))
        )
    }
}