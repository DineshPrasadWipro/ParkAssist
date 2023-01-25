package com.renault.parkassist.lhd

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.renault.parkassist.R
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.ui.settings.MainSettingsFragment
import com.renault.parkassist.utils.matchers.matchOrderLeftToRight
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

// TODO: Re-enable tests when RHD/LHD is usable
//  ref CCSEXT-71793
@Ignore("RHD/LHD is not implemented yet on Android 12")
class MainSettingsLhdTest : LhdRhdTestBase() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchFullScreen()
    }

    @Test
    fun testMainSettingsLhd() {
        setLhd()
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)

        Espresso.onView(ViewMatchers.withId(R.id.carsettings_assist_parking)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.layout_main_setting_preference,
                    R.id.main_settings_sonar_pref_fragment
                )
            )
        )
    }

    @Test
    fun testMainSettingsRhd() {
        setRhd()
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)

        Espresso.onView(ViewMatchers.withId(R.id.carsettings_assist_parking)).check(
            ViewAssertions.matches(
                matchOrderLeftToRight(
                    R.id.main_settings_sonar_pref_fragment,
                    R.id.layout_main_setting_preference
                )
            )
        )
    }
}