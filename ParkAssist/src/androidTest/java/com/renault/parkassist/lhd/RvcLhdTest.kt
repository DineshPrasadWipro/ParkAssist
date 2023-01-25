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
class RvcLhdTest : LhdRhdTestBase() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        launchFullScreen()
    }

    @Test
    fun testRvcLhd() {
        setLhd()
        navigateFullscreen(R.id.rvcFragment)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Espresso.onView(ViewMatchers.withId(R.id.rvc_camera_sonar_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.camera_space_start,
                        R.id.rvc_standard_camera_fragment,
                        R.id.sonar,
                        R.id.camera_space_end
                    )
                )
            )
        } else {
            Espresso.onView(ViewMatchers.withId(R.id.rvc_camera_sonar_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.rvc_standard_camera_fragment,
                        R.id.sonar
                    )
                )
            )
        }
    }

    @Test
    fun testRvcRhd() {
        setRhd()
        navigateFullscreen(R.id.rvcFragment)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Espresso.onView(ViewMatchers.withId(R.id.rvc_camera_sonar_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.camera_space_end,
                        R.id.sonar,
                        R.id.rvc_standard_camera_fragment,
                        R.id.camera_space_start
                    )
                )
            )
        } else {
            Espresso.onView(ViewMatchers.withId(R.id.rvc_camera_sonar_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.sonar,
                        R.id.rvc_standard_camera_fragment
                    )
                )
            )
        }
    }
}