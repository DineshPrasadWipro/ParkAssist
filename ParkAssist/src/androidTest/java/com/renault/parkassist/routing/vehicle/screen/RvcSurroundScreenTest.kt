package com.renault.parkassist.routing.vehicle.screen

import alliance.car.surroundview.SurroundView
import com.renault.parkassist.R
import com.renault.parkassist.repository.routing.ISurroundRouting
import com.renault.parkassist.routing.cfg.RvcRoutingTest
import org.junit.Test

class RvcSurroundScreenTest : RvcRoutingTest() {

    @Test
    fun should_display_rear_view_when_requested_by_vehicle() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.rvc_main)
    }

    @Test
    fun should_display_trailer_when_requested_by_vehicle() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.TRAILER_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.fragment_trailer)
    }

    @Test
    fun should_display_dealer_when_requested_by_vehicle() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.DEALER_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.dealer_camera_fragment)
    }

    @Test
    fun should_exit_rear_view_when_requested_by_vehicle() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.rvc_main)
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.NO_DISPLAY,
                true
            )
        )
        assertScreenExited(R.id.rvc_main)
    }

    @Test
    fun should_go_to_settings_then_go_back_to_rear_view() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.rvc_main)
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.SETTINGS_FRONT_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.fragment_rvc_settings)
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true
            )
        )
        assertScreenPresent(R.id.rvc_main)
    }

    @Test
    fun should_display_rear_view_when_requested_by_vehicle_only_when_display_mode_is_visible() {
        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true,
                SurroundView.DISPLAY_MODE_HIDDEN
            )
        )
        assertScreenNotVisible(R.id.rvc_main)

        surroundBridgeMock.screenRequest.onNext(
            ISurroundRouting.Request(
                SurroundView.REAR_VIEW,
                true,
                SurroundView.DISPLAY_MODE_VISIBLE
            )
        )
        assertScreenPresent(R.id.rvc_main)
    }
}