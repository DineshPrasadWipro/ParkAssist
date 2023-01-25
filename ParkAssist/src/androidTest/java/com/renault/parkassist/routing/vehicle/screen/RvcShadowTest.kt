package com.renault.parkassist.routing.vehicle.screen

import alliance.car.autopark.AutoPark
import alliance.car.surroundview.SurroundView
import com.renault.parkassist.repository.routing.ISurroundRouting
import com.renault.parkassist.routing.cfg.RvcRoutingTest
import org.junit.Test

class RvcShadowTest : RvcRoutingTest() {

    @Test
    fun should_set_shadow_activity_when_requested() {
        startUserService()

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true
            )
        )
        waitShadow()
        assertHasShadow()

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.NO_DISPLAY,
                false
            )
        )
        waitShadowKilled()
        assertHasNotShadow()

        apaBridgeMock.screenRequest.onNext(
            AutoPark.DISPLAY_SCANNING
        )
        waitShadow()
        assertHasShadow()
    }
}