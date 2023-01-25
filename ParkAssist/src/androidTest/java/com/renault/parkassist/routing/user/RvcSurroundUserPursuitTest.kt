package com.renault.parkassist.routing.user

import alliance.car.surroundview.SurroundView
import com.renault.parkassist.R
import com.renault.parkassist.repository.routing.ISurroundRouting
import com.renault.parkassist.repository.surroundview.Action
import com.renault.parkassist.repository.surroundview.ManeuverAvailability
import com.renault.parkassist.repository.surroundview.TrailerPresence
import com.renault.parkassist.routing.cfg.RvcRoutingTest
import com.renault.parkassist.utils.waitForCondition
import junit.framework.TestCase
import org.junit.Test

class RvcSurroundUserPursuitTest : RvcRoutingTest() {

    @Test
    fun should_send_activate_trailer_view_request_when_user_clicks_camera_button() {
        surroundBridgeMock.maneuverAvailability.onNext(ManeuverAvailability.READY)
        surroundBridgeMock.trailerPresence.onNext(TrailerPresence.TRAILER_PRESENCE_DETECTED)
        clickCameraLauncher()
        waitForCondition(timeOutMs = uiTimeout) {
            surroundBridgeMock.requestedActions.isNotEmpty()
        }
        TestCase.assertEquals(
            Action.ACTIVATE_TRAILER_VIEW,
            surroundBridgeMock.lastActionRequested
        )
    }

    @Test
    fun should_send_close_view_when_users_clicks_outside_trailer_view() {
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
}