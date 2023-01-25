package com.renault.parkassist.lhd

import android.content.res.Configuration
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.renault.parkassist.R
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.utils.matchers.matchOrderLeftToRight
import org.junit.Assume
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

// TODO: Re-enable tests when RHD/LHD is usable
//  ref CCSEXT-71793
@Ignore("RHD/LHD is not implemented yet on Android 12")
class AvmSettingsLhdTest : LhdRhdTestBase() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchFullScreen()
    }

    @Test
    fun testAvmSettingsLhd() {
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        setLhd()
        navigateFullscreen(R.id.avmSettings)

        Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.layout_settings,
                    R.id.camera_settings_camera_fragment
                )
            )
        )
    }

    @Test
    fun testAvmSettingsRhd() {
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        setRhd()
        navigateFullscreen(R.id.avmSettings)

        Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.camera_settings_camera_fragment,
                    R.id.layout_settings
                )
            )
        )
    }
}