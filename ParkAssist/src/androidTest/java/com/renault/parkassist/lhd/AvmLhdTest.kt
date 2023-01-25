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
class AvmLhdTest : LhdRhdTestBase() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchFullScreen()
    }

    @Test
    fun testAvmLhd() {
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        setLhd()
        navigateFullscreen(R.id.avmFragment)

        Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.avm_menu_bar,
                    R.id.camera_container_landscape
                )
            )
        )
    }

    @Test
    fun testAvmRhd() {
        Assume.assumeTrue(orientation == Configuration.ORIENTATION_LANDSCAPE)
        setRhd()
        navigateFullscreen(R.id.avmFragment)

        Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.camera_container_landscape,
                    R.id.avm_menu_bar
                )
            )
        )
    }
}