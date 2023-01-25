package com.renault.parkassist.routing.vehicle.screen

import com.renault.parkassist.R
import com.renault.parkassist.repository.routing.ISonarRouting
import com.renault.parkassist.routing.cfg.UpaRoutingTest
import org.junit.Test

class UpaSonarScreenTest : UpaRoutingTest() {

    @Test
    fun should_display_sonar_popup_when_rear_detection_from_vehicle() {
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = true,
                front = false,
                flank = false
            )
        )
        assertScreenPresent(R.id.upa_popup)
    }

    @Test
    fun should_display_sonar_pop_up_when_front_detection_from_vehicle() {
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = false,
                front = true,
                flank = false
            )
        )
        assertScreenPresent(R.id.upa_popup)
    }

    @Test
    fun should_exit_sonar_popup_when_no_detection_from_vehicle() {
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = true,
                front = false,
                flank = false
            )
        )
        assertScreenPresent(R.id.upa_popup)
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = false,
                front = false,
                flank = false
            )
        )
        assertScreenExited(R.id.upa_popup)
    }

    @Test
    fun should_exit_sonar_pop_up_when_no_detection_from_vehicle() {
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = false,
                front = true,
                flank = false
            )
        )
        assertScreenPresent(R.id.upa_popup)
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = false,
                front = false,
                flank = false
            )
        )
        assertScreenExited(R.id.upa_popup)
    }
}