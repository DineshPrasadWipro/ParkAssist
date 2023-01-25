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
class HfpScanningLhdTest : LhdRhdTestBase() {
    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchFullScreen()
    }

    @Test
    fun testHfpScanningLhd() {
        setLhd()
        navigateFullscreen(R.id.avmHfpScanningFragment)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Espresso.onView(ViewMatchers.withId(R.id.elt_hfp_scanning_view_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.maneuver_selection_area,
                        R.id.layout_scanning,
                        R.id.space_scanning
                    )
                )
            )

            Espresso.onView(ViewMatchers.withId(R.id.elt_hfp_scanning)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.layout_scanning_illustration,
                        R.id.layout_scanning_sonar
                    )
                )
            )
        } else {
            Espresso.onView(ViewMatchers.withId(R.id.elt_hfp_scanning_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.elt_hfp_scanning_picture_container,
                        R.id.layout_sonar
                    )
                )
            )
        }
    }

    @Test
    fun testHfpScanningRhd() {
        setRhd()
        navigateFullscreen(R.id.avmHfpScanningFragment)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Espresso.onView(ViewMatchers.withId(R.id.elt_hfp_scanning_view_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.space_scanning,
                        R.id.layout_scanning,
                        R.id.maneuver_selection_area
                    )
                )
            )

            Espresso.onView(ViewMatchers.withId(R.id.elt_hfp_scanning)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.layout_scanning_sonar,
                        R.id.layout_scanning_illustration
                    )
                )
            )
        } else {
            Espresso.onView(ViewMatchers.withId(R.id.elt_hfp_scanning_container)).check(
                ViewAssertions.matches(
                    matchOrderLeftToRight(
                        R.id.layout_sonar,
                        R.id.elt_hfp_scanning_picture_container
                    )
                )
            )
        }
    }
}