package com.renault.parkassist.routing.user

import alliance.car.autopark.AutoPark
import alliance.car.surroundview.SurroundView
import com.renault.parkassist.R
import com.renault.parkassist.repository.apa.FeatureConfig as ApaFeatureConfig
import com.renault.parkassist.repository.routing.ISurroundRouting
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.routing.cfg.AvmHfpRoutingTest
import com.renault.parkassist.utils.waitForCondition
import junit.framework.TestCase
import org.junit.Test

class AvmHfpUserPursuitTest : AvmHfpRoutingTest() {

    @Test
    fun should_send_switch_activation_true_request_when_user_click_apa_button() {
        clickApaLauncher()
        waitForCondition(timeOutMs = uiTimeout) {
            apaBridgeMock.switchActivation != null
        }
        TestCase.assertTrue(apaBridgeMock.switchActivation ?: false)
    }

    @Test
    fun should_send_switch_activation_false_when_users_clicks_outside_scanning_view() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_SCANNING)
        assertScreenPresent(R.id.apa_mainscanning)
        stopShadowFullscreen()
        waitForCondition(timeOutMs = uiTimeout) {
            apaBridgeMock.switchActivation != null
        }
        TestCase.assertFalse(apaBridgeMock.switchActivation ?: true)
    }

    @Test
    fun should_send_switch_activation_true_when_user_click_on_apa_toolbar_menu() {
        automaticParkAssistRepository.featureConfiguration = ApaFeatureConfig.HFP
        surroundViewRepository.surroundState.postValue(SurroundState(View.REAR_VIEW))
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.avm_main)
        surroundViewRepository.startRendering()
        clickOnView(R.id.toolbar_icon_easypark)
        waitForCondition(timeOutMs = uiTimeout) {
                apaBridgeMock.switchActivation != null
        }

        TestCase.assertTrue(apaBridgeMock.switchActivation ?: false)
    }
}