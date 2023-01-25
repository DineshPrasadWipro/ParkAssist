package com.renault.parkassist.routing.vehicle.screen

import alliance.car.autopark.AutoPark
import com.renault.parkassist.R
import com.renault.parkassist.routing.cfg.RvcRoutingTest
import org.junit.Test

class RvcHfpScreenTest : RvcRoutingTest() {

    @Test
    fun should_display_hfp_scanning_when_apa_scanning_requested_by_vehicle() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_SCANNING)
        assertScreenPresent(R.id.apa_mainscanning)
    }

    @Test
    fun should_display_hfp_guidance_when_apa_guidance_requested_by_vehicle() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_GUIDANCE)
        assertScreenPresent(R.id.apa_sonarwithguidance)
    }

    @Test
    fun should_exit_hfp_scanning_when_apa_requested_by_vehicle() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_SCANNING)
        assertScreenPresent(R.id.apa_mainscanning)
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_NONE)
        assertScreenExited(R.id.apa_mainscanning)
    }

    @Test
    fun should_navigate_hfp_scanning_parkout_none_screens_when_requested_by_vehicle() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_SCANNING)
        assertScreenPresent(R.id.apa_mainscanning)
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_PARKOUT_CONFIRMATION)
        assertScreenPresent(R.id.apa_mainscanning)
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_NONE)
        assertScreenExited(R.id.apa_mainscanning)
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_PARKOUT_CONFIRMATION)
        assertScreenPresent(R.id.apa_mainscanning)
    }
}