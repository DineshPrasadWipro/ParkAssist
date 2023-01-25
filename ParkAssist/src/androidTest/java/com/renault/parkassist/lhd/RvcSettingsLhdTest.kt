package com.renault.parkassist.lhd

import android.content.res.Configuration
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.renault.parkassist.R
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.utils.matchers.matchOrderLeftToRight
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

// TODO: Re-enable tests when RHD/LHD is usable
//  ref CCSEXT-71793
@Ignore("RHD/LHD is not implemented yet on Android 12")
class RvcSettingsLhdTest : LhdRhdTestBase() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        launchFullScreen()
    }

    @Test
    fun testRvcSettingsLhd() {
        setLhd()
        navigateFullscreen(R.id.rvcSettings)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.layout_rvc_settings_colorimetry,
                        R.id.layout_rvc_settings_camera
                    )
                )
            )
        } else {
            Espresso.onView(ViewMatchers.withId(R.id.video_layout)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.camera_settings_camera_fragment,
                        R.id.sonar
                    )
                )
            )
        }
    }

    @Test
    fun testRvcSettingsRhd() {
        setRhd()
        navigateFullscreen(R.id.rvcSettings)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.layout_rvc_settings_camera,
                        R.id.layout_rvc_settings_colorimetry
                    )
                )
            )
        } else {
            Espresso.onView(ViewMatchers.withId(R.id.video_layout)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.sonar,
                        R.id.camera_settings_camera_fragment
                    )
                )
            )
        }
    }
}