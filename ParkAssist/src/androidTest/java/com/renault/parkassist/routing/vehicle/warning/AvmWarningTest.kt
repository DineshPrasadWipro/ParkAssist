package com.renault.parkassist.routing.vehicle.warning

import com.renault.parkassist.R
import com.renault.parkassist.routing.cfg.AvmHfpRoutingTest
import org.junit.Test

class AvmWarningTest : AvmHfpRoutingTest() {

    @Test
    fun should_display_warning_dialog_when_requested_by_vehicle() {
        surroundBridgeMock.dialogRequest.onNext(true)
        assertScreenPresent(R.id.activity_warning)
    }

    @Test
    fun should_exit_warning_dialog_when_requested_by_vehicle() {
        surroundBridgeMock.dialogRequest.onNext(true)
        assertScreenPresent(R.id.activity_warning)
        surroundBridgeMock.dialogRequest.onNext(false)
        assertScreenExited(R.id.activity_warning)
    }
}