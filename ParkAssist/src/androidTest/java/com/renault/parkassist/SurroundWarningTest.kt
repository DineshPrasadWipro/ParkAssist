package com.renault.parkassist

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.surroundview.UserAcknowledgement
import com.renault.parkassist.repository.surroundview.WarningState
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SurroundWarningTest : OverlayActivityTest() {
    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchSurroundViewWarning()
    }

    @Test
    fun should_show_dialog_when_warning_received_then_ack_when_user_click_positive_button() { // ktlint-disable max-line-length
        spyk(surroundViewRepository)
        surroundViewRepository.warningState.postValue(WarningState.WARNING_STATE_CAMERA_SOILED)
        Espresso.onView(ViewMatchers.withText(R.string.rlb_parkassist_warning_camera_soiled))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        waitDialog()
        Espresso.onView(ViewMatchers.withText(android.R.string.ok))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click())
        waitDialog()
        verify {
            surroundViewRepository.acknowledgeWarning(UserAcknowledgement.ACK_OK)
        }
    }

    @Test
    fun should_dismiss_previous_warning_when_warning_none_received() { // ktlint-disable max-line-length
        surroundViewRepository.warningState.postValue(WarningState.WARNING_STATE_CAMERA_SOILED)
        Espresso.onView(ViewMatchers.withText(R.string.rlb_parkassist_warning_camera_soiled))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        waitDialog()
        surroundViewRepository.warningState.postValue(WarningState.WARNING_STATE_NONE)
        waitDialog()
        // Check the activity is back on top (dialog closed)
        Espresso.onView(ViewMatchers.withId(R.id.activity_warning))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun waitDialog() = Thread.sleep(500)
}