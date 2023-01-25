package com.renault.parkassist

import alliance.car.autopark.AutoPark
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.apa.DisplayState
import com.renault.parkassist.repository.apa.FeatureConfig
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FapkWarningTest : OverlayActivityTest() {
    @Before
    fun setup() {
        automaticParkAssistRepository.featureConfiguration = FeatureConfig.FAPK
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchApaWarning()
    }

    @Test
    fun should_stop_display_dialog_box_on_none_warning_reception() {
        // GIVEN HFP guidance screen is displayed
        automaticParkAssistRepository.displayState.postValue(AutoPark.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.warningMessage
            .postValue(AutoPark.MESSAGE_PRESS_OK_TO_START_MANEUVER)
        onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
        onView(withText(R.string.rlb_parkassist_apa_fapk_warning_press_ok_to_start_maneuver))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withText(R.string.rlb_ok))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withText(R.string.rlb_parkassist_apa_negative_button_label))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        waitDialog()
        automaticParkAssistRepository.warningMessage.postValue(AutoPark.MESSAGE_NONE)
        waitDialog()
        // Check the activity is back on top (dialog closed)
        onView(withId(R.id.activity_warning)).check(matches(isDisplayed()))
    }

    @Test
    fun should_display_dialog_box_with_ok_button_on_relevant_warning_reception_and_user_click_ok() {
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.warningMessage.postValue(
            AutoPark.MESSAGE_MANEUVER_CANCELED_PARKING_BRAKE
        )
        onView(withText(R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_parking_brake))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        waitDialog()
        onView(withText(R.string.rlb_ok))
            .inRoot(isDialog())
            .perform(ViewActions.click())
        waitDialog()
        verify {
            automaticParkAssistRepository.acknowledgeWarning(AutoPark.USER_ACKNOWLEDGEMENT_1)
        }
    }

    @Test
    fun should_display_dialog_box_with_ok_and_cancel_button_on_relevant_warning_reception_and_user_click_cancel() { // ktlint-disable max-line-length
        // GIVEN HFP guidance screen is displayed
        automaticParkAssistRepository.displayState.postValue(AutoPark.DISPLAY_GUIDANCE)
        automaticParkAssistRepository
            .warningMessage.postValue(AutoPark.MESSAGE_PRESS_OK_TO_START_MANEUVER)
        onView(withText(R.string.rlb_parkassist_apa_fapk_warning_press_ok_to_start_maneuver))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        waitDialog()
        onView(withText(R.string.rlb_parkassist_apa_negative_button_label))
            .inRoot(isDialog())
            .perform(ViewActions.click())
        waitDialog()
        verify {
            automaticParkAssistRepository.acknowledgeWarning(AutoPark.USER_ACKNOWLEDGEMENT_2)
        }
    }

    @Test
    fun should_display_dialog_box_with_ok_and_cancel_button_on_relevant_warning_reception_and_user_click_ok() { // ktlint-disable max-line-length
        // GIVEN HFP guidance screen is displayed
        automaticParkAssistRepository.displayState.postValue(AutoPark.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.warningMessage
            .postValue(AutoPark.MESSAGE_PRESS_OK_TO_START_MANEUVER)
        onView(withText(R.string.rlb_parkassist_apa_fapk_warning_press_ok_to_start_maneuver))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        waitDialog()
        onView(withText(R.string.rlb_ok))
            .inRoot(isDialog())
            .perform(ViewActions.click())
        waitDialog()
        verify {
            automaticParkAssistRepository.acknowledgeWarning(AutoPark.USER_ACKNOWLEDGEMENT_1)
        }
    }

    @Test
    fun should_display_dialog_box_with_cancel_button_on_relevant_warning_reception_and_user_click_cancel() { // ktlint-disable max-line-length
        automaticParkAssistRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        automaticParkAssistRepository.warningMessage.postValue(
            AutoPark.MESSAGE_MANEUVER_SUSPENDED_HAND_ON_WHEEL
        )
        onView(withText(R.string.rlb_parkassist_apa_fapk_warning_maneuver_suspended_hand_on_wheel))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        waitDialog()
        onView(withText(R.string.rlb_parkassist_apa_neutral_button_label))
            .inRoot(isDialog())
            .perform(ViewActions.click())
        waitDialog()
        verify {
            automaticParkAssistRepository.acknowledgeWarning(AutoPark.USER_ACKNOWLEDGEMENT_1)
        }
    }

    private fun waitDialog() = Thread.sleep(500)
}