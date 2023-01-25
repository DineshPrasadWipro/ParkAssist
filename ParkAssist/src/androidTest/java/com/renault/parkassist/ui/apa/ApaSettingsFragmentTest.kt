package com.renault.parkassist.ui.apa

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.filters.MediumTest
import com.renault.parkassist.R
import com.renault.parkassist.repository.apa.ManeuverType
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultRadioPreferenceIsChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultRadioPreferenceIsNotChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.setRenaultRadioPreferenceChecked
import com.renault.parkassist.viewmodel.apa.ApaSettingsViewModelBase
import com.renault.parkassist.viewmodel.apa.Maneuver
import com.renault.parkassist.viewmodel.apa.mock.ApaSettingsViewModelMock
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinComponent
import org.koin.dsl.module

// TODO: Update when radio preference behavior is changed in RenaultUI
//  ref CCSEXT-71793
@Ignore("Radio preference behavior is changed in RenaultUI")
@MediumTest
class ApaSettingsFragmentTest: KoinComponent {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ApaSettingsViewModelMock

    @Before
    fun setup() {
        viewModel = spyk(
            ApaSettingsViewModelMock(
                mockk()
            )
        )
        viewModel.maneuverSelectorVisible = true
        viewModel.maneuvers =
            listOf(ManeuverType.PARALLEL, ManeuverType.PERPENDICULAR, ManeuverType.PARKOUT)

        getKoin().loadModules(
            listOf(module {
                viewModel<ApaSettingsViewModelBase>(override = true) { viewModel }
            })
        )
    }

    @Test
    fun should_update_radio_button_state_and_sample_image_when_switch_to_perpendicular() { // ktlint-disable max-line-length
        launchFragmentInContainer<ApaSettingsFragment>(themeResId = R.style.RenaultTheme)
        viewModel.defaultManeuver.postValue(Maneuver.PERPENDICULAR)
        assertRenaultRadioPreferenceIsNotChecked(R.string.rlb_parkassist_apa_settings_parallel)
        assertRenaultRadioPreferenceIsChecked(R.string.rlb_parkassist_apa_settings_perpendicular)
        assertViewHasEffectiveVisibility(R.id.car_apa_settings)
        assertViewHasEffectiveVisibility(R.id.apa_settings_park)
    }

    @Test
    fun should_update_radio_button_state_and_sample_image_when_switch_to_parallel() { // ktlint-disable max-line-length
        launchFragmentInContainer<ApaSettingsFragment>(themeResId = R.style.RenaultTheme)
        viewModel.defaultManeuver.postValue(Maneuver.PARALLEL)
        assertRenaultRadioPreferenceIsChecked(R.string.rlb_parkassist_apa_settings_parallel)
        assertRenaultRadioPreferenceIsNotChecked(R.string.rlb_parkassist_apa_settings_perpendicular)
        assertViewHasEffectiveVisibility(R.id.car_apa_settings)
        assertViewHasEffectiveVisibility(R.id.apa_settings_park)
    }

    @Test
    fun should_switch_to_parallel_when_clicked() { // ktlint-disable max-line-length
        launchFragmentInContainer<ApaSettingsFragment>(themeResId = R.style.RenaultTheme)
        setRenaultRadioPreferenceChecked(R.string.rlb_parkassist_apa_settings_perpendicular, true)
        setRenaultRadioPreferenceChecked(R.string.rlb_parkassist_apa_settings_parallel, true)
        verify { viewModel.setDefaultManeuver(Maneuver.PARALLEL) }
    }

    @Test
    fun should_switch_to_perpendicular_when_clicked() { // ktlint-disable max-line-length
        launchFragmentInContainer<ApaSettingsFragment>(themeResId = R.style.RenaultTheme)
        setRenaultRadioPreferenceChecked(R.string.rlb_parkassist_apa_settings_perpendicular, true)
        verify { viewModel.setDefaultManeuver(Maneuver.PERPENDICULAR) }
    }
}