package com.renault.parkassist.routing.vehicle.screen

import alliance.car.autopark.AutoPark
import alliance.car.surroundview.SurroundView
import com.renault.parkassist.R
import com.renault.parkassist.repository.routing.ISurroundRouting
import com.renault.parkassist.routing.cfg.AvmFapkRoutingTest
import org.junit.Test

class FapkScreenTest : AvmFapkRoutingTest() {

    @Test
    fun should_display_fapk_screen_when_apa_scanning_requested_by_vehicle() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_SCANNING)
        assertScreenPresent(R.id.fapk_main)
    }

    @Test
    fun should_display_fapk_screen_when_apa_guidance_requested_by_vehicle() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_GUIDANCE)
        assertScreenPresent(R.id.fapk_main)
    }

    @Test
    fun should_exit_fapk_screen_when_requested_by_vehicle() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_SCANNING)
        assertScreenPresent(R.id.fapk_main)
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_NONE)
        assertScreenExited(R.id.fapk_main)
    }

    @Test
    fun should_navigate_fapk_scanning_parkout_none_screens_when_requested_by_vehicle() {
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_SCANNING)
        assertScreenPresent(R.id.fapk_main)
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_PARKOUT_CONFIRMATION)
        assertScreenPresent(R.id.fapk_main)
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_NONE)
        assertScreenExited(R.id.fapk_main)
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_PARKOUT_CONFIRMATION)
        assertScreenPresent(R.id.fapk_main)
    }

    @Test
    fun should_navigate_fapk_scanning_from_surround_pop_up_when_requested_by_vehicle() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.POP_UP_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.avm_popup)
        apaBridgeMock.screenRequest.onNext(AutoPark.DISPLAY_SCANNING)
        assertScreenPresent(R.id.fapk_main)
    }
}