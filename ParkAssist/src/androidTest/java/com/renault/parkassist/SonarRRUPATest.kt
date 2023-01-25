package com.renault.parkassist

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.utils.EspressoTestUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SonarRRUPATest : OverlayActivityTest() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.SONAR_RR)
        launchPopUp()
        navigatePopUp(R.id.sonarPipFragment)
        sonarRepository.displayRequest.postValue(DisplayType.WIDGET)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
    }

    @After
    fun tearDown() {
        sonarRepository.displayRequest.postValue(DisplayType.NONE)
    }

    @Test
    fun should_display_sonar_popup_when_rear_gear_is_engaged_in_rear_upa_config() { // ktlint-disable max-line-length
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.car)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.upa_pip_close)
    }
}