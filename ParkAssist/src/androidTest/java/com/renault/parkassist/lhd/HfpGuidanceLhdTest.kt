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
class HfpGuidanceLhdTest : LhdRhdTestBase() {
    @Before
    fun setup() {
        launchFullScreen()
    }

    @Test
    fun testRvcHfpGuidanceLhd() {
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        setLhd()
        navigateFullscreen(R.id.RvcHfpGuidanceFragment)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.layout_maneuver_bar,
                        R.id.layout_guidance_illus,
                        R.id.sonar_full_fragment_container,
                        R.id.space_guidance
                    )
                )
            )
        } else {
            Espresso.onView(ViewMatchers.withId(R.id.elt_hfp_camera_view_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.elt_hfp_camera_view_fragment,
                        R.id.sonar_full_fragment_container
                    )
                )
            )
        }
    }

    @Test
    fun testRvcHfpGuidanceRhd() {
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        setRhd()
        navigateFullscreen(R.id.RvcHfpGuidanceFragment)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.space_guidance,
                        R.id.sonar_full_fragment_container,
                        R.id.layout_guidance_illus,
                        R.id.layout_maneuver_bar
                    )
                )
            )
        } else {
            Espresso.onView(ViewMatchers.withId(R.id.elt_hfp_camera_view_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.sonar_full_fragment_container,
                        R.id.elt_hfp_camera_view_fragment
                    )
                )
            )
        }
    }

    @Test
    fun testAvmHfpGuidanceLhd() {
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)

        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        setLhd()
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)

        Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.layout_maneuver_bar,
                    R.id.layout_guidance_illus,
                    R.id.space_guidance
                )
            )
        )

        Espresso.onView(ViewMatchers.withId(R.id.guidance_layout_renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.hfp_guidance_picture,
                    R.id.sonar_alerts_guidance_picture
                )
            )
        )
    }

    @Test
    fun testAvmHfpGuidanceRhd() {
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)

        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        setRhd()
        navigateFullscreen(R.id.AvmHfpGuidanceFragment)

        Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.space_guidance,
                    R.id.layout_guidance_illus,
                    R.id.layout_maneuver_bar
                )
            )
        )

        Espresso.onView(ViewMatchers.withId(R.id.guidance_layout_renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.sonar_alerts_guidance_picture,
                    R.id.hfp_guidance_picture
                )
            )
        )
    }
}