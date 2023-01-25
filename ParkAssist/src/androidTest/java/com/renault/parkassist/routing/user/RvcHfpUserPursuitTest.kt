package com.renault.parkassist.routing.user

import alliance.car.autopark.AutoPark
import com.renault.parkassist.R
import com.renault.parkassist.routing.cfg.RvcRoutingTest
import com.renault.parkassist.utils.waitForCondition
import junit.framework.TestCase
import org.junit.Test

class RvcHfpUserPursuitTest : RvcRoutingTest() {

    @Test
    fun should_send_switch_true_activation_request_when_user_click_apa_button() {
        clickApaLauncher()
        waitForCondition(timeOutMs = uiTimeout) {
            apaBridgeMock.switchActivation != null
        }
        TestCase.assertTrue(apaBridgeMock.switchActivation ?: false)
    }

    @Test
    fun should_send_switch_false_activation_when_users_clicks_outside_scanning_view() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_SCANNING)
        assertScreenPresent(R.id.apa_mainscanning)
        stopShadowFullscreen()
        waitForCondition(timeOutMs = uiTimeout) {
            apaBridgeMock.switchActivation != null
        }
        TestCase.assertFalse(apaBridgeMock.switchActivation ?: true)
    }

    @Test
    fun should_send_switch_activation_true_request_when_user_clicks_favorite_easy_park_button() {
        clickEasyParkFavoriteButton()
        waitForCondition(timeOutMs = uiTimeout) {
            apaBridgeMock.switchActivation != null
        }
        TestCase.assertTrue(apaBridgeMock.switchActivation ?: false)
    }
}