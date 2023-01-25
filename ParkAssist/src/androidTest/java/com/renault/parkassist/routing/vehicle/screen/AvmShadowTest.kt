package com.renault.parkassist.routing.vehicle.screen

import alliance.car.surroundview.SurroundView
import com.renault.parkassist.koin.KoinTestApp
import com.renault.parkassist.repository.routing.ISurroundRouting
import com.renault.parkassist.routing.cfg.AvmHfpRoutingTest
import com.renault.parkassist.ui.FullscreenShadowActivity
import org.junit.Test

class AvmShadowTest : AvmHfpRoutingTest() {

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
                SurroundView.REAR_VIEW,
                false
            )
        )
        waitShadowKilled()
        assertHasNotShadow()

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.NO_DISPLAY,
                true
            )
        )
        waitShadow()
        assertHasNotShadow()
    }

    @Test
    fun should_launch_shadow_again_when_same_request_received_after_shadow_killed() {
        startUserService()

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true
            )
        )
        waitShadow()
        assertHasShadow()

        KoinTestApp.runningActivities.first { it is FullscreenShadowActivity }.finish()
        waitShadowKilled()
        assertHasNotShadow()

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true
            )
        )
        waitShadow()
        assertHasShadow()
    }

    @Test
    fun avm_then_apa_then_avm_then_no_display() {
        startUserService()

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.FRONT_VIEW,
                true
            )
        )
        waitShadow()
        assertHasShadow()

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.APA_FRONT_VIEW,
                true
            )
        )
        waitShadowKilled()
        assertHasShadow()

        KoinTestApp.runningActivities.first { it is FullscreenShadowActivity }.finish()
        waitShadowKilled()

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.FRONT_VIEW,
                true
            )
        )
        waitShadow()
        assertHasShadow()

        KoinTestApp.runningActivities.first { it is FullscreenShadowActivity }.finish()
        waitShadowKilled()

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.NO_DISPLAY,
                true
            )
        )
        assertHasNotShadow()
    }
}