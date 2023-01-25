package com.renault.parkassist.routing.user

import alliance.car.surroundview.SurroundView
import com.renault.parkassist.R
import com.renault.parkassist.repository.routing.ISurroundRouting
import com.renault.parkassist.repository.surroundview.Action
import com.renault.parkassist.repository.surroundview.ManeuverAvailability
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.routing.cfg.AvmHfpRoutingTest
import com.renault.parkassist.utils.waitForCondition
import junit.framework.TestCase
import org.junit.Test

class AvmSurroundUserPursuitsTest : AvmHfpRoutingTest() {

    @Test
    fun should_send_activate_maneuver_view_when_user_clicks_camera_button() {
        surroundBridgeMock.maneuverAvailability.onNext(ManeuverAvailability.READY)
        clickCameraLauncher()
        waitForCondition(timeOutMs = uiTimeout) {
            surroundBridgeMock.requestedActions.isNotEmpty()
        }
        TestCase.assertEquals(
            Action.ACTIVATE_MANEUVER_VIEW,
            surroundBridgeMock.lastActionRequested
        )
    }

    @Test
    fun should_send_close_view_when_users_clicks_outside_avm_screen() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.avm_main)
        stopShadowFullscreen()
        waitForCondition(timeOutMs = uiTimeout) {
            surroundBridgeMock.requestedActions.isNotEmpty()
        }
        TestCase.assertEquals(
            Action.CLOSE_VIEW,
            surroundBridgeMock.lastActionRequested
        )
    }

    @Test
    fun should_send_close_view_when_users_clicks_outside_trailer_screen() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.TRAILER_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.fragment_trailer)
        stopShadowFullscreen()
        waitForCondition(timeOutMs = uiTimeout) {
            surroundBridgeMock.requestedActions.isNotEmpty()
        }
        TestCase.assertEquals(
            Action.CLOSE_VIEW,
            surroundBridgeMock.lastActionRequested
        )
    }

    @Test
    fun should_send_close_view_when_users_clicks_on_avm_pop_up_close() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.POP_UP_VIEW,
                true
            )
        )
        surroundViewRepository.surroundState.postValue(
            SurroundState(
                SurroundView.POP_UP_VIEW
            )
        )
        surroundViewRepository.authorizedActions.postValue(
            listOf(Action.CLOSE_VIEW)
        )
        assertScreenPresent(R.id.avm_popup)
        clickOnView(R.id.avm_pip_close)
        waitForCondition(timeOutMs = uiTimeout) {
            surroundBridgeMock.requestedActions.isNotEmpty()
        }
        TestCase.assertEquals(
            Action.CLOSE_VIEW,
            surroundBridgeMock.lastActionRequested
        )
    }

    @Test
    fun should_send_activate_maneuver_when_user_clicks_on_camera_button() {
        surroundBridgeMock.maneuverAvailability.onNext(ManeuverAvailability.READY)
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.POP_UP_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.avm_popup)
        clickOnView(R.id.avm_pip_camera)
        waitForCondition(timeOutMs = uiTimeout) {
            surroundBridgeMock.requestedActions.isNotEmpty() &&
                apaBridgeMock.switchActivation != null
        }
        TestCase.assertEquals(
            Action.ACTIVATE_MANEUVER_VIEW,
            surroundBridgeMock.lastActionRequested
        )
    }

    @Test
    fun should_send_activate_maneuver_view_when_user_clicks_favorite_avm_button() {
        surroundBridgeMock.maneuverAvailability.onNext(ManeuverAvailability.READY)
        clickAvmFavoriteButton()
        waitForCondition(timeOutMs = uiTimeout) {
            surroundBridgeMock.requestedActions.isNotEmpty()
        }
        TestCase.assertEquals(
            Action.ACTIVATE_MANEUVER_VIEW,
            surroundBridgeMock.lastActionRequested
        )
    }
}