package com.renault.parkassist.ui.sonar

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility.INVISIBLE
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.renault.parkassist.R
import com.renault.parkassist.ui.settings.MainSettingsFragment
import com.renault.parkassist.utils.EspressoTestUtils.assertProgressBarMaxValue
import com.renault.parkassist.utils.EspressoTestUtils.assertProgressBarMinValue
import com.renault.parkassist.utils.EspressoTestUtils.assertProgressBarValue
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultPreferenceHasSummary
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultRadioPreferenceDoesNotExist
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultRadioPreferenceIsChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultRadioPreferenceIsDisplayed
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultRadioPreferenceIsNotChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSeekBarPreferenceDoesNotExist
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSeekBarPreferenceHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSwitchPreferenceDoesNotExist
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSwitchPreferenceHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSwitchPreferenceIsChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSwitchPreferenceIsNotChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.clickRenaultSwitchPreference
import com.renault.parkassist.utils.EspressoTestUtils.setPercentageProgressOnPreferenceSeekBar
import com.renault.parkassist.utils.EspressoTestUtils.setRenaultRadioPreferenceChecked
import com.renault.parkassist.utils.EspressoTestUtils.toggleRenaultSwitchPreference
import com.renault.parkassist.viewmodel.settings.MainSettingsViewModelBase
import com.renault.parkassist.viewmodel.settings.mock.MainSettingsViewModelMock
import com.renault.parkassist.viewmodel.sonar.SonarSettingsViewModelBase
import com.renault.parkassist.viewmodel.sonar.mock.SonarSettingsViewModelMock
import com.renault.parkassist.viewmodel.sound.SoundDescriptor
import com.renault.parkassist.viewmodel.sound.SoundSettingsViewModelBase
import com.renault.parkassist.viewmodel.sound.mock.SoundSettingsViewModelMock
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

@MediumTest
class MainSettingsFragmentTest : KoinComponent {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var sonarSettingsViewModel: SonarSettingsViewModelMock
    private lateinit var mainSettingsViewModel: MainSettingsViewModelMock
    private lateinit var soundSettingsViewModel: SoundSettingsViewModelMock
    private lateinit var module: Module
    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        sonarSettingsViewModel = spyk(SonarSettingsViewModelMock(mockk()))
        mainSettingsViewModel = MainSettingsViewModelMock(mockk())
        soundSettingsViewModel = spyk(SoundSettingsViewModelMock(mockk()))
        module = module {
            viewModel<SonarSettingsViewModelBase>(override = true) { sonarSettingsViewModel }
            viewModel<MainSettingsViewModelBase>(override = true) { mainSettingsViewModel }
            viewModel<SoundSettingsViewModelBase>(override = true) { soundSettingsViewModel }
        }
        sonarSettingsViewModel.reset()
        soundSettingsViewModel.reset()
        soundSettingsViewModel.sounds =
            listOf(
                SoundDescriptor("Type1", 1),
                SoundDescriptor("Type2", 2),
                SoundDescriptor("Type3", 3)
            )
        soundSettingsViewModel.soundId.value = 2
        soundSettingsViewModel.minVolume = 10
        soundSettingsViewModel.maxVolume = 50
        soundSettingsViewModel.volume.postValue(40)
        getKoin().loadModules(listOf(module))
    }

    @After
    fun tearDown() {
        getKoin().unloadModules(listOf(module))
    }

    private fun getTitle(resId: Int): String {
        return context.resources.getString(resId)
    }

    // Toggle Text title is used for identifying each RenaultSwitch inside RenaultSwitchPreference.
    private var frontToggle = getTitle(R.string.rlb_parkassist_main_settings_sonar_front)
    private var sideToggle = getTitle(R.string.rlb_parkassist_main_settings_sonar_side)
    private var rearToggle = getTitle(R.string.rlb_parkassist_main_settings_sonar_rear)
    private var soundToggle = getTitle(R.string.rlb_parkassist_sound)
    private var soundVolume = getTitle(R.string.rlb_parkassist_sound_settings_volume)
    private var rctaToggle = getTitle(R.string.rlb_parkassist_main_settings_rcta)
    private var raebToggle = getTitle(R.string.rlb_parkassist_main_settings_raeb)
    private var oseToggle = getTitle(R.string.rlb_parkassist_main_settings_ose)

    @Test
    fun should_show_activated_front_toggle_when_front_enabled() { // ktlint-disable max-line-length
        sonarSettingsViewModel.frontVisible = true
        sonarSettingsViewModel.frontEnabled.postValue(true)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceIsChecked(frontToggle)
    }

    @Test
    fun should_show_unactivated_front_toggle_when_front_disabled() { // ktlint-disable max-line-length
        sonarSettingsViewModel.frontVisible = true
        sonarSettingsViewModel.frontEnabled.postValue(false)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceIsNotChecked(frontToggle)
    }

    @Test
    fun should_show_activated_flank_toggle_when_flanks_enabled() { // ktlint-disable max-line-length
        sonarSettingsViewModel.flankVisible = true
        sonarSettingsViewModel.flankEnabled.postValue(true)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceIsChecked(sideToggle)
        assertViewHasEffectiveVisibility(R.id.side_toggle)
    }

    @Test
    fun should_show_unactivated_flank_toggle_when_flanks_disabled() { // ktlint-disable max-line-length
        sonarSettingsViewModel.flankVisible = true
        sonarSettingsViewModel.flankEnabled.postValue(false)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceIsNotChecked(sideToggle)
    }

    @Test
    fun should_show_activated_rear_toggle_when_rear_enabled() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rearToggleVisible = true
        sonarSettingsViewModel.rearVisible = true
        sonarSettingsViewModel.rearEnabled.postValue(true)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceIsChecked(rearToggle)
    }

    @Test
    fun should_show_unactivated_rear_toggle_when_rear_disabled() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rearToggleVisible = true
        sonarSettingsViewModel.rearVisible = true
        sonarSettingsViewModel.rearEnabled.postValue(false)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceIsNotChecked(rearToggle)
    }

    @Test
    fun should_hide_front_toggle_and_arc_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.frontVisible = false
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceDoesNotExist(frontToggle)
        onView(withId(R.id.front_arc)).check(matches(withEffectiveVisibility(INVISIBLE)))
    }

    @Test
    fun should_show_front_toggle_and_arc_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.frontVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(frontToggle)
        assertViewHasEffectiveVisibility(R.id.front_arc)
    }

    @Test
    fun should_hide_lateral_toggle_and_arcs_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.flankVisible = false
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceDoesNotExist(sideToggle)
        onView(withId(R.id.middle_arc)).check(matches(withEffectiveVisibility(INVISIBLE)))
    }

    @Test
    fun should_show_lateral_toggle_and_arcs_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.flankVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(sideToggle)
        assertViewHasEffectiveVisibility(R.id.middle_arc)
    }

    @Test
    fun should_hide_rear_toggle_when_unavailable() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rearToggleVisible = false
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceDoesNotExist(rearToggle)
    }

    @Test
    fun should_hide_rear_arc_when_sonar_rear_feature_not_present() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rearVisible = false
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        onView(withId(R.id.rear_arc)).check(matches(withEffectiveVisibility(INVISIBLE)))
    }

    @Test
    fun should_show_rear_toggle_when_available() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rearVisible = true
        sonarSettingsViewModel.rearToggleVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(rearToggle)
    }

    @Test
    fun should_show_rear_arc_when_sonar_rear_feature_present() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rearVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertViewHasEffectiveVisibility(R.id.rear_arc)
    }

    @Test
    fun should_hide_RCTA_toggle_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rctaVisible = false
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        // Ensure preference screen is fully inflated as rctaToggle rely on a livedata
        assertRenaultSwitchPreferenceDoesNotExist(rctaToggle)
    }

    @Test
    fun should_show_RCTA_toggle_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rctaVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        // Ensure preference screen is fully inflated as rctaToggle rely on a livedata
        assertRenaultSwitchPreferenceHasEffectiveVisibility(rctaToggle)
    }

    @Test
    fun should_hide_OSE_toggle_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.oseVisible = false
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceDoesNotExist(oseToggle)
    }

    @Test
    fun should_show_OSE_toggle_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.oseVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(oseToggle)
    }

    @Test
    fun should_hide_RAEB_toggle_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.raebVisible = false
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceDoesNotExist(raebToggle)
    }

    @Test
    fun should_show_RAEB_toggle_when_asked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.raebVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(raebToggle)
    }

    @Test
    fun should_set_viewmodel_RCTA_to_true_when_RCTA_disabled_and_RCTA_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rctaVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(rctaToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableRcta(true) }
    }

    @Test
    fun should_set_viewmodel_RCTA_to_false_when_RCTA_enabled_and_RCTA_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rctaVisible = true
        sonarSettingsViewModel.rctaEnabled.postValue(true)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(rctaToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableRcta(false) }
    }

    @Test
    fun should_set_viewmodel_OSE_to_true_when_OSE_disabled_and_OSE_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.oseVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(oseToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableOse(true) }
    }

    @Test
    fun should_set_viewmodel_OSE_to_false_when_OSE_enabled_and_OSE_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.oseVisible = true
        sonarSettingsViewModel.oseEnabled.postValue(true)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(oseToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableOse(false) }
    }

    @Test
    fun should_set_viewmodel_RAEB_to_true_when_RAEB_disabled_and_RAEB_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.raebVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(raebToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableRaeb(true) }
    }

    @Test
    fun should_set_viewmodel_RAEB_to_false_when_RAEB_enabled_and_RAEB_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.raebVisible = true
        sonarSettingsViewModel.raebEnabled.postValue(true)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(raebToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableRaeb(false) }
    }

    @Test
    fun should_set_viewmodel_front_to_true_when_front_disabled_and_front_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.frontVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(frontToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableFront(true) }
    }

    @Test
    fun should_set_viewmodel_front_to_false_when_front_enabled_and_front_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.frontVisible = true
        sonarSettingsViewModel.frontEnabled.postValue(true)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(frontToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableFront(false) }
    }

    @Test
    fun should_set_viewmodel_flank_to_true_when_flank_disabled_and_side_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.flankVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(sideToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableFlank(true) }
    }

    @Test
    fun should_set_viewmodel_flank_to_false_when_flank_enabled_and_side_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.flankVisible = true
        sonarSettingsViewModel.flankEnabled.postValue(true)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(sideToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableFlank(false) }
    }

    @Test
    fun should_set_viewmodel_rear_to_true_when_rear_disabled_and_rear_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rearToggleVisible = true
        sonarSettingsViewModel.rearVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(rearToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableRear(true) }
    }

    @Test
    fun should_set_viewmodel_rear_to_false_when_rear_enabled_and_rear_toggle_clicked() { // ktlint-disable max-line-length
        sonarSettingsViewModel.rearToggleVisible = true
        sonarSettingsViewModel.rearVisible = true
        sonarSettingsViewModel.rearEnabled.postValue(true)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        clickRenaultSwitchPreference(rearToggle)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { sonarSettingsViewModel.enableRear(false) }
    }

    @Test
    fun should_show_volume_bar_when_requested() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        soundSettingsViewModel.soundEnabled.postValue(true)
        soundSettingsViewModel.volumeVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(soundToggle)
        assertRenaultSeekBarPreferenceHasEffectiveVisibility()
    }

    @Test
    fun should_hide_volume_bar_when_requested() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        soundSettingsViewModel.soundEnabled.postValue(true)
        soundSettingsViewModel.volumeVisible = false
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(soundToggle)
        assertRenaultSeekBarPreferenceDoesNotExist()
    }

    // TODO: Re-enable when radio button preferences behavior is restored
    //  ref CCSEXT-71793
    @Ignore("Radio button preference behavior is changed")
    @Test
    fun should_show_sound_selector_when_requested() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        soundSettingsViewModel.soundEnabled.postValue(true)
        soundSettingsViewModel.soundTypeVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(soundToggle)
        assertRenaultRadioPreferenceIsDisplayed(R.string.rlb_parkassist_sound_type_1)
        assertRenaultRadioPreferenceIsDisplayed(R.string.rlb_parkassist_sound_type_2)
        assertRenaultRadioPreferenceIsDisplayed(R.string.rlb_parkassist_sound_type_3)
    }

    @Test
    fun should_hide_sound_selector_when_requested() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        soundSettingsViewModel.soundEnabled.postValue(true)
        soundSettingsViewModel.soundTypeVisible = false
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(soundToggle)
        assertRenaultRadioPreferenceDoesNotExist(R.string.rlb_parkassist_sound_type_1)
        assertRenaultRadioPreferenceDoesNotExist(R.string.rlb_parkassist_sound_type_2)
        assertRenaultRadioPreferenceDoesNotExist(R.string.rlb_parkassist_sound_type_3)
    }

    @Test
    fun should_show_correct_sound_type_summary_when_requested() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        soundSettingsViewModel.soundEnabled.postValue(false)
        soundSettingsViewModel.soundTypeVisible = true
        soundSettingsViewModel.soundId.postValue(3)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(soundToggle)
        assertRenaultPreferenceHasSummary(
            R.string.rlb_parkassist_sound_settings_sound_type,
            R.string.rlb_parkassist_sound_type_3
        )
    }

    @Test
    fun should_show_activated_sound_toggle_when_sound_enabled() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        soundSettingsViewModel.soundEnabled.postValue(true)
        assertRenaultSwitchPreferenceIsChecked(soundToggle)
    }

    @Test
    fun should_show_unactivated_sound_toggle_when_sound_disabled() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        soundSettingsViewModel.soundEnabled.postValue(false)
        assertRenaultSwitchPreferenceIsNotChecked(soundToggle)
    }

    @Test
    fun should_call_the_viewmodel_method_to_disable_sound_when_sound_active_and_toggle_clicked() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        soundSettingsViewModel.soundEnabled.postValue(true)
        toggleRenaultSwitchPreference(soundToggle, false)
        verify { soundSettingsViewModel.enableSound(false) }
    }

    @Test
    fun should_call_the_viewmodel_method_to_enable_sound_when_sound_disabled_and_toggle_clicked() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        soundSettingsViewModel.soundEnabled.postValue(false)
        toggleRenaultSwitchPreference(soundToggle, true)
        verify { soundSettingsViewModel.enableSound(true) }
    }

    @Test
    fun should_show_correct_min_and_max_volume() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundEnabled.postValue(true)
        soundSettingsViewModel.volumeVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        // Check min/max values 10-50 moved to 0-40 on slider bar
        assertProgressBarMinValue(0)
        assertProgressBarMaxValue(40)
        assertRenaultPreferenceHasSummary(
            R.string.rlb_parkassist_sound_settings_volume,
            "40"
        )
    }

    @Test
    fun should_select_the_correct_volume() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundEnabled.postValue(true)
        soundSettingsViewModel.volumeVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        soundSettingsViewModel.volume.postValue(30)
        assertRenaultPreferenceHasSummary(
            R.string.rlb_parkassist_sound_settings_volume,
            "30"
        )
        // min/max values 10-50 shifted to 0-40 on slider bar, so slider value should be 20
        assertProgressBarValue(20)
    }

    @Test
    fun should_set_the_volume_when_selected_by_the_user() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundEnabled.postValue(true)
        soundSettingsViewModel.volumeVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        soundSettingsViewModel.volume.postValue(40)
        setPercentageProgressOnPreferenceSeekBar(21)
        verify { soundSettingsViewModel.setVolume(31) }
    }

    // TODO: Re-enable when radio button preferences behavior is restored
    //  ref CCSEXT-71793
    @Ignore("Radio button preference behavior is changed")
    @Test
    fun should_select_correct_sound_type() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundEnabled.postValue(true)
        soundSettingsViewModel.soundTypeVisible = true
        soundSettingsViewModel.volumeVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        soundSettingsViewModel.soundId.postValue(3)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        assertRenaultRadioPreferenceIsNotChecked(R.string.rlb_parkassist_sound_type_1)
        assertRenaultRadioPreferenceIsNotChecked(R.string.rlb_parkassist_sound_type_2)
        assertRenaultRadioPreferenceIsChecked(R.string.rlb_parkassist_sound_type_3)
    }

    // TODO: Re-enable when radio button preferences behavior is restored
    //  ref CCSEXT-71793
    @Ignore("Radio button preference behavior is changed")
    @Test
    fun should_set_the_sound_type_when_selected_by_the_user() { // ktlint-disable max-line-length
        mainSettingsViewModel.soundMenuVisible = true
        soundSettingsViewModel.soundEnabled.postValue(true)
        soundSettingsViewModel.soundTypeVisible = true
        soundSettingsViewModel.volumeVisible = true
        soundSettingsViewModel.soundSwitchVisible = true
        soundSettingsViewModel.soundId.postValue(3)
        launchFragmentInContainer<MainSettingsFragment>(themeResId = R.style.RenaultTheme)
        setRenaultRadioPreferenceChecked(R.string.rlb_parkassist_sound_type_1, true)
        verify { soundSettingsViewModel.setSound(1) }
    }
}