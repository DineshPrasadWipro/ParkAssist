package com.renault.parkassist.lhd

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.renault.parkassist.R
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.utils.matchers.matchOrderLeftToRight
import com.renault.parkassist.viewmodel.apa.Maneuver
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

// TODO: Re-enable tests when RHD/LHD is usable
//  ref CCSEXT-71793
@Ignore("RHD/LHD is not implemented yet on Android 12")
class ApaSettingsLhdTest : LhdRhdTestBase() {
    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        automaticParkAssistRepository.supportedManeuvers =
            listOf(Maneuver.PARALLEL, Maneuver.PERPENDICULAR)
        launchFullScreen()
    }

    @Test
    fun testApaSettingsLhd() {
        setLhd()
        navigateFullscreen(R.id.action_to_avmHfpSettingsFragment)

        Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.layout_apa_settings_preference,
                    R.id.layout_apa_settings_maneuveur
                )
            )
        )
    }

    @Test
    fun testApaSettingsRhd() {
        setRhd()
        navigateFullscreen(R.id.action_to_avmHfpSettingsFragment)

        Espresso.onView(ViewMatchers.withId(R.id.renault_oriented)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.layout_apa_settings_maneuveur,
                    R.id.layout_apa_settings_preference
                )
            )
        )
    }
}