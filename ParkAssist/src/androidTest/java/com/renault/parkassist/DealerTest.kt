package com.renault.parkassist

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DealerTest : OverlayActivityTest() {

    @Before
    fun setup() {
        clearAll()
    }

    @Test
    fun should_display_dealer_screen_in_rvc() { // ktlint-disable max-line-length
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        launchFullScreen()
        displayCameraView(view = View.DEALER_VIEW)
        navigateFullscreen(R.id.dealerFragment)
        assertViewHasEffectiveVisibility(R.id.elt_camera_view)
    }

    @Test
    fun should_display_dealer_screen_in_avm() { // ktlint-disable max-line-length
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchFullScreen()
        displayCameraView(view = View.DEALER_VIEW)
        navigateFullscreen(R.id.dealerFragment)
        assertViewHasEffectiveVisibility(R.id.elt_camera_view)
    }
}