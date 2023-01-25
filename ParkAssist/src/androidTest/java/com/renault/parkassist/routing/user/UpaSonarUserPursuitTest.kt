package com.renault.parkassist.routing.user

import com.renault.parkassist.R
import com.renault.parkassist.repository.routing.ISonarRouting
import com.renault.parkassist.routing.cfg.UpaRoutingTest
import com.renault.parkassist.utils.waitForCondition
import junit.framework.TestCase
import org.junit.Test

class UpaSonarUserPursuitTest : UpaRoutingTest() {

    @Test
    fun should_send_close_sonar_when_user_clicks_outside_sonar_fullscreen_view() {
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = true,
                front = false,
                flank = false
            )
        )
        sonarBridgeMock.closable.onNext(true)
        assertScreenPresent(R.id.upa_popup)
        stopShadowFullscreen()
        waitForCondition(timeOutMs = uiTimeout) {
            sonarBridgeMock.isClosed
        }
        TestCase.assertTrue(sonarBridgeMock.isClosed)
    }

    @Test
    fun should_send_close_sonar_when_user_clicks_outside_sonar_popup_view() {
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = true,
                front = false,
                flank = false
            )
        )
        sonarBridgeMock.closable.onNext(true)
        assertScreenPresent(R.id.upa_popup)
        stopShadowFullscreen()
        waitForCondition(timeOutMs = uiTimeout) {
            sonarBridgeMock.isClosed
        }
        TestCase.assertTrue(sonarBridgeMock.isClosed)
    }

    @Test
    fun should_send_close_sonar_when_user_clicks_sonar_close_button() {
        sonarBridgeMock.closable.onNext(true)
        sonarRepository.closeAllowed.postValue(true)
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = true,
                front = false,
                flank = false
            )
        )
        assertScreenPresent(R.id.upa_popup)
        clickOnView(R.id.upa_pip_close)
        waitForCondition(timeOutMs = uiTimeout) {
            sonarBridgeMock.isClosed
        }
        TestCase.assertTrue(sonarBridgeMock.isClosed)
    }
}