package com.renault.parkassist.routing.vehicle.screen

import com.renault.parkassist.repository.routing.ISonarRouting
import com.renault.parkassist.routing.cfg.UpaRoutingTest
import org.junit.Test

class UpaShadowTest : UpaRoutingTest() {

    @Test
    fun should_set_shadow_activity_when_requested() {
        startUserService()

        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = true,
                front = false,
                flank = false
            )
        )
        waitShadow()
        assertHasNotShadow()
    }
}