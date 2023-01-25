package com.renault.parkassist.routing.vehicle.screen

import com.renault.parkassist.R
import com.renault.parkassist.repository.routing.ISonarRouting
import com.renault.parkassist.routing.cfg.RvcRoutingTest
import org.junit.Test

class RvcSonarScreenTest : RvcRoutingTest() {

    @Test
    fun should_display_sonar_fullscreen_when_rear_detection_from_vehicle() {
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = true,
                front = false,
                flank = false
            )
        )
        assertScreenPresent(R.id.rvc_main)
    }

    @Test
    fun should_exit_sonar_fullscreen_when_no_detection_from_vehicle() {
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = true,
                front = false,
                flank = false
            )
        )
        assertScreenPresent(R.id.rvc_main)
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = false,
                front = false,
                flank = false
            )
        )
        assertScreenExited(R.id.rvc_main)
    }

    @Test
    fun should_display_sonar_pop_up_when_no_more_rear_detection_from_vehicle() {
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = true,
                front = true,
                flank = true
            )
        )
        assertScreenPresent(R.id.rvc_main)
        sonarBridgeMock.request.onNext(
            ISonarRouting.Request(
                rear = false,
                front = true,
                flank = true
            )
        )
        assertScreenExited(R.id.rvc_main)
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