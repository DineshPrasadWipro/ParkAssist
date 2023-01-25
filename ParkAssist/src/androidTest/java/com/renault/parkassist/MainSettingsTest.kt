package com.renault.parkassist

import alliance.car.sonar.AllianceCarSonarManager.*
import android.content.Intent
import android.content.res.Configuration
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.renault.parkassist.koin.KoinTestBase
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.settings.SoundType
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.ui.settings.SettingsActivity
import com.renault.parkassist.utils.DisableAnimationsRule
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultPreferenceHasSummary
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultPreferenceIsDisabled
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultPreferenceIsEnabled
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultRadioPreferenceDoesNotExist
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultRadioPreferenceHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultRadioPreferenceIsEnabled
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSeekBarPreferenceDoesNotExist
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSeekBarPreferenceHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSeekBarPreferenceIsEnabled
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSwitchPreferenceDoesNotExist
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSwitchPreferenceHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSwitchPreferenceIsChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSwitchPreferenceIsNotChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import com.renault.parkassist.utils.EspressoTestUtils.clickRenaultSwitchPreference
import io.mockk.every
import io.mockk.verify
import kotlin.test.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainSettingsTest : KoinTestBase() {
    @get:Rule
    var settingsActivityTestRule = ActivityTestRule(SettingsActivity::class.java, true, false)

    @get:Rule
    var disableAnimationsRule = DisableAnimationsRule()

    @Before
    fun setup() {
        every { sonarRepository.enableCollisionAlert(any()) } answers {
            sonarRepository.collisionAlertEnabled.postValue(
                firstArg()
            )
        }
        every { sonarRepository.setSonarGroup(any(), any()) } answers {
            val group = firstArg<Int>()
            val state = if (secondArg()) GroupState.ENABLED else GroupState.DISABLED
            when (group) {
                SONAR_GROUP_FRONT -> sonarRepository.frontState.postValue(state)
                SONAR_GROUP_REAR -> sonarRepository.rearState.postValue(state)
                SONAR_GROUP_FLANK -> sonarRepository.flankState.postValue(state)
            }
        }
        every { soundRepository.enableOse(any()) } answers {
            soundRepository.oseEnabled.postValue(firstArg())
        }
        every { sonarRepository.enableRearAutoEmergencyBreak(any()) } answers {
            sonarRepository.raebAlertEnabled.postValue(firstArg())
        }

        every { soundRepository.enableSound(any()) } answers {
            soundRepository.soundEnabled.postValue(firstArg())
        }
        soundRepository.soundTypes = listOf(
            SoundType(1, "bipTest"),
            SoundType(2, "cordeTest"),
            SoundType(3, "sonarTest")
        )
        soundRepository.soundType.postValue(2)
        soundRepository.minVolume = 0
        soundRepository.maxVolume = 10
        // GIVEN UPA/FKP is supported in SONAR configuration
        setVehicleConfiguration(ParkAssistHwConfig.SONAR)
    }

    private fun getTitle(resId: Int): String {
        return context.resources.getString(resId)
    }

    // Toggle Text title is used for identifying each RenaultSwitch inside RenaultSwitchPreference.
    private var frontToggle = getTitle(R.string.rlb_parkassist_main_settings_sonar_front)
    private var rearToggle = getTitle(R.string.rlb_parkassist_main_settings_sonar_rear)
    private var sideToggle = getTitle(R.string.rlb_parkassist_main_settings_sonar_side)

    private var soundExpandableCategory = getTitle(R.string.rlb_parkassist_sound)
    private var soundToggle = getTitle(R.string.rlb_parkassist_sound)
    private var soundTypeResume = getTitle(R.string.rlb_parkassist_sound_settings_sound_type)
    private var soundVolume = getTitle(R.string.rlb_parkassist_sound_settings_volume)

    private var rctaToggle = getTitle(R.string.rlb_parkassist_main_settings_rcta)
    private var raebToggle = getTitle(R.string.rlb_parkassist_main_settings_raeb)
    private var oseToggle = getTitle(R.string.rlb_parkassist_main_settings_ose)

    @Test
    fun should_show_RCTA_option_and_flags_when_RCTA_hardware_is_present() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        //  AND the RCTA toggle is displayed
        assertRenaultSwitchPreferenceHasEffectiveVisibility(rctaToggle)
    }

    @Test
    fun should_hide_RCTA_option_and_flags_when_RCTA_hardware_is_not_present() { // ktlint-disable max-line-length
        // GIVEN RCTA is not supported
        sonarRepository.rctaFeaturePresent = false
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        //  AND the RCTA toggle is not displayed
        assertRenaultSwitchPreferenceDoesNotExist(rctaToggle)
        assertViewHasNoEffectiveVisibility(R.id.rcta_flag)
    }

    @Test
    fun should_show_OSE_option_when_OSE_hardware_is_present() { // ktlint-disable max-line-length
        // GIVEN OSE is supported
        soundRepository.oseControlPresence = true
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        //  AND the OSE toggle is displayed
        assertRenaultSwitchPreferenceHasEffectiveVisibility(oseToggle)
        assertViewHasEffectiveVisibility(R.id.ose_flag)
    }

    @Test
    fun should_hide_OSE_option_when_OSE_hardware_is_not_present() { // ktlint-disable max-line-length
        // GIVEN OSE is not supported
        soundRepository.oseControlPresence = false
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        //  AND the OSE toggle is not displayed
        assertRenaultSwitchPreferenceDoesNotExist(oseToggle)
        assertViewHasNoEffectiveVisibility(R.id.ose_flag)
    }

    @Test
    fun should_show_RAEB_option_when_RAEB_hardware_is_present() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebFeaturePresent = true
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        //  AND the RAEB toggle is displayed
        assertRenaultSwitchPreferenceHasEffectiveVisibility(raebToggle)
        assertViewHasEffectiveVisibility(R.id.raeb_flag)
    }

    @Test
    fun should_hide_RAEB_option_when_RAEB_hardware_is_not_present() { // ktlint-disable max-line-length
        // GIVEN RAEB is not supported
        sonarRepository.raebFeaturePresent = false
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        //  AND the RAEB toggle is not displayed
        assertRenaultSwitchPreferenceDoesNotExist(raebToggle)
        assertViewHasNoEffectiveVisibility(R.id.raeb_flag)
    }

    @Test
    fun should_show_sound_menu_when_UPA_audio_config_supported() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        sonarRepository.upaRearFeaturePresent = true
        //   AND UPA Audio config is supported with settings
        soundRepository.soundActivationControlPresence = true
        soundRepository.volumeControlPresence = true
        soundRepository.soundSelectionControlPresence = true
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        //  AND the sound menu items are displayed even if disabled
        assertRenaultSwitchPreferenceHasEffectiveVisibility(soundToggle)
        assertViewHasEffectiveVisibility(soundTypeResume)
        assertViewHasEffectiveVisibility(soundVolume)
    }

    @Test
    fun should_hide_sound_menu_when_UPA_audio_config_not_supported() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        sonarRepository.upaRearFeaturePresent = true
        //   AND UPA Audio config is not supported
        soundRepository.soundActivationControlPresence = false
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        //  AND the sound menu item is not displayed
        assertRenaultSwitchPreferenceDoesNotExist(soundToggle)
    }

    @Test
    fun should_hide_sonar_options_when_UPA_hardware_is_not_present() { // ktlint-disable max-line-length
        // GIVEN UPA is not supported
        sonarRepository.upaRearFeaturePresent = false
        sonarRepository.upaFrontFeaturePresent = false
        sonarRepository.fkpFeaturePresent = false
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        //  AND it contains an ego car not surrounded with sonar zones
        //  AND it contains neither sonar zones nor toggle buttons (front/rear/side)
        assertFrontSensorGroupHidden()
        assertViewHasNoEffectiveVisibility(R.id.rear_arc)
        assertRenaultSwitchPreferenceDoesNotExist(rearToggle)
        assertSideSensorGroupHidden()
    }

    @Test
    fun should_show_sonar_options_when_UPA_rear_hardware_is_present() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        sonarRepository.rearUpaActivationSettingPresent = true
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = false
        sonarRepository.fkpFeaturePresent = false
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        //  AND it contains an ego car surrounded with 1 rear sonar zone
        //  AND it contains 1 toggle `rear` button, but no 'front/side' buttons
        assertFrontSensorGroupHidden()
        assertRearSensorGroupActive()
        assertSideSensorGroupHidden()
    }

    @Test
    fun should_show_sonar_options_when_UPA_hardware_is_present() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        sonarRepository.rearUpaActivationSettingPresent = true
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = true
        sonarRepository.fkpFeaturePresent = false
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        //  AND it contains an ego car surrounded with 1 front & 1 rear sonar zones
        //  AND it contains 2 toggle buttons `front` & `rear``
        assertFrontSensorGroupActive()
        assertRearSensorGroupActive()
        assertSideSensorGroupHidden()
    }

    @Test
    fun should_show_sonar_options_when_UPA_and_FKP_hardware_is_present() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        //   AND FKP is supported
        sonarRepository.rearUpaActivationSettingPresent = true
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = true
        sonarRepository.fkpFeaturePresent = true
        // WHEN the user selects the settings button of Parking Assistance
        settingsActivityTestRule.launchActivity(Intent())
        // THEN the main settings screen is displayed
        assertMainSettingsIsDisplayed()
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        //  AND it contains an ego car surrounded with 4 sonar zones
        //  AND it contains 3 toggle buttons `front`, `rear` & `side`
        assertFrontSensorGroupActive()
        assertRearSensorGroupActive()
        assertSideSensorGroupActive()
    }

    @Test
    fun should_activate_sonar_front_detection_when_user_clicks_front_toggle() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = true
        sonarRepository.fkpFeaturePresent = false
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        //   AND front sonar sensors group is inactive
        sonarRepository.frontState.postValue(GroupState.DISABLED)
        assertFrontSensorGroupInactive()
        // WHEN the user clicks the `front` toggle button
        clickRenaultSwitchPreference(frontToggle)
        // THEN front sonar sensors group activation state toggles
        verify(exactly = 1) { sonarRepository.setSonarGroup(SONAR_GROUP_FRONT, true) }
        assertFrontSensorGroupActive()
    }

    @Test
    fun should_deactivate_sonar_front_detection_when_user_clicks_front_toggle() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = true
        sonarRepository.fkpFeaturePresent = false
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        //   AND front sonar sensors group is active
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        assertFrontSensorGroupActive()
        // WHEN the user clicks the `front` toggle button
        clickRenaultSwitchPreference(frontToggle)
        // THEN front sonar sensors group activation state toggles
        verify(exactly = 1) { sonarRepository.setSonarGroup(SONAR_GROUP_FRONT, false) }
        assertFrontSensorGroupInactive()
    }

    @Test
    fun should_activate_sonar_rear_detection_when_user_clicks_rear_toggle() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        sonarRepository.rearUpaActivationSettingPresent = true
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = false
        sonarRepository.fkpFeaturePresent = false
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        //   AND rear sonar sensors group is inactive
        sonarRepository.rearState.postValue(GroupState.DISABLED)
        assertRearSensorGroupInactive()
        // WHEN the user clicks the `rear` toggle button
        clickRenaultSwitchPreference(rearToggle)
        // THEN rear sonar sensors group activation state toggles
        verify(exactly = 1) { sonarRepository.setSonarGroup(SONAR_GROUP_REAR, true) }
        assertRearSensorGroupActive()
    }

    @Test
    fun should_deactivate_sonar_rear_detection_when_user_clicks_rear_toggle() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        sonarRepository.rearUpaActivationSettingPresent = true
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = false
        sonarRepository.fkpFeaturePresent = false
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        //   AND rear sonar sensors group is active
        sonarRepository.rearState.postValue(GroupState.ENABLED)
        assertRearSensorGroupActive()
        // WHEN the user clicks the `rear` toggle button
        clickRenaultSwitchPreference(rearToggle)
        // THEN rear sonar sensors group activation state toggles
        verify(exactly = 1) { sonarRepository.setSonarGroup(SONAR_GROUP_REAR, false) }
        assertRearSensorGroupInactive()
    }

    @Test
    fun should_hide_sonar_rear_upa_option_when_upa_settings_feature_not_present() { // ktlint-disable max-line-length
        // GIVEN UPA settings feature is not supported
        sonarRepository.rearUpaActivationSettingPresent = false
        sonarRepository.upaRearFeaturePresent = false
        sonarRepository.upaFrontFeaturePresent = false
        sonarRepository.fkpFeaturePresent = false
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        // THEN rear sonar options are hidden
        assertViewHasNoEffectiveVisibility(R.id.rear_arc)
        assertRenaultSwitchPreferenceDoesNotExist(rearToggle)
    }

    @Test
    fun should_hide_sonar_rear_upa_toggle_and_show_rear_arc_when_upa_rear_not_deactivable() { // ktlint-disable max-line-length
        // GIVEN UPA settings feature is not supported
        sonarRepository.rearUpaActivationSettingPresent = false
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = false
        sonarRepository.fkpFeaturePresent = false
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        // THEN rear sonar options are hidden
        assertViewHasEffectiveVisibility(R.id.rear_arc)
        assertRenaultSwitchPreferenceDoesNotExist(rearToggle)
    }

    @Test
    fun should_show_sonar_rear_upa_option_when_upa_settings_feature_is_present() { // ktlint-disable max-line-length
        // GIVEN UPA settings feature is supported
        sonarRepository.rearUpaActivationSettingPresent = true
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = false
        sonarRepository.fkpFeaturePresent = false
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        // THEN rear sonar options are visible
        assertViewHasEffectiveVisibility(R.id.rear_arc)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(rearToggle)
    }

    @Test
    fun should_activate_sonar_side_detection_when_user_clicks_side_toggle() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        //   AND FKP is supported
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = true
        sonarRepository.fkpFeaturePresent = true
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        //   AND side sonar sensors group is inactive
        sonarRepository.flankState.postValue(GroupState.DISABLED)
        assertSideSensorGroupInactive()
        // WHEN the user clicks the `side` toggle button
        clickRenaultSwitchPreference(sideToggle)
        // THEN side sonar sensors group activation state toggles
        verify(exactly = 1) { sonarRepository.setSonarGroup(SONAR_GROUP_FLANK, true) }
        assertSideSensorGroupActive()
    }

    @Test
    fun should_deactivate_sonar_side_detection_when_user_clicks_side_toggle() { // ktlint-disable max-line-length
        // GIVEN UPA is supported
        //   AND FKP is supported
        sonarRepository.upaRearFeaturePresent = true
        sonarRepository.upaFrontFeaturePresent = true
        sonarRepository.fkpFeaturePresent = true
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        //   AND side sonar sensors group is active
        sonarRepository.flankState.postValue(GroupState.ENABLED)
        assertSideSensorGroupActive()
        // WHEN the user clicks the `side` toggle button
        clickRenaultSwitchPreference(sideToggle)
        // THEN side sonar sensors group activation state toggles
        verify(exactly = 1) { sonarRepository.setSonarGroup(SONAR_GROUP_FLANK, false) }
        assertSideSensorGroupInactive()
    }

    @Test
    fun should_activate_rcta_flags_when_user_clicks_rcta_toggle() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        sonarRepository.collisionAlertEnabled.postValue(false)
        // WHEN the user clicks the `side` toggle button
        clickRenaultSwitchPreference(rctaToggle)
        // THEN RCTA settings button is toggled to disabled
        assertRenaultSwitchPreferenceIsChecked(rctaToggle)
    }

    @Test
    fun should_deactivate_rcta_flags_when_user_clicks_rcta_toggle() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        sonarRepository.collisionAlertEnabled.postValue(true)
        // WHEN the user clicks the `rtca` toggle button
        clickRenaultSwitchPreference(rctaToggle)
        // THEN RCTA settings button is toggled to enabled
        assertRenaultSwitchPreferenceIsNotChecked(rctaToggle)
    }

    @Test
    fun should_activate_raeb_when_user_clicks_raeb_toggle() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebFeaturePresent = true
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        sonarRepository.raebAlertEnabled.postValue(false)
        // WHEN the user clicks the `raeb` toggle button
        clickRenaultSwitchPreference(raebToggle)
        // THEN RAEB settings button is toggled to enabled
        assertRenaultSwitchPreferenceIsChecked(raebToggle)
    }

    @Test
    fun should_deactivate_raeb_when_user_clicks_raeb_toggle() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebFeaturePresent = true
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        sonarRepository.raebAlertEnabled.postValue(true)
        // WHEN the user clicks the `raeb` toggle button
        clickRenaultSwitchPreference(raebToggle)
        // THEN RAEB settings button is toggled to enabled
        assertRenaultSwitchPreferenceIsNotChecked(raebToggle)
    }

    @Test
    fun should_activate_ose_notification_when_user_clicks_ose_toggle() { // ktlint-disable max-line-length
        // GIVEN OSE is supported but disabled
        soundRepository.oseControlPresence = true
        soundRepository.oseEnabled.postValue(false)
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        // WHEN the user clicks the `ose` toggle button
        clickRenaultSwitchPreference(oseToggle)
        // THEN OSE settings button is toggled to enabled (neither audio nor visual feedback other than button state)
        assertRenaultSwitchPreferenceIsChecked(oseToggle)
    }

    @Test
    fun should_deactivate_ose_notification_when_user_clicks_ose_toggle() { // ktlint-disable max-line-length
        // GIVEN OSE is supported and enabled
        soundRepository.oseControlPresence = true
        soundRepository.oseEnabled.postValue(true)
        //   AND the main settings screen is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        // WHEN the user clicks the `ose` toggle button
        clickRenaultSwitchPreference(oseToggle)
        // THEN OSE settings button is toggled to disabled (neither audio nor visual feedback other than button state)
        assertRenaultSwitchPreferenceIsNotChecked(oseToggle)
    }

    @Test
    fun should_disable_detailed_sound_settings_when_toggle_inactive_and_sound_activation_control_present() { // ktlint-disable max-line-length
        // GIVEN UPA Audio config is supported with settings
        soundRepository.soundActivationControlPresence = true
        soundRepository.volumeControlPresence = true
        soundRepository.soundSelectionControlPresence = true
        soundRepository.soundEnabled.postValue(true)
        //   AND the sound settings menu is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        assertSoundSettingsIsEnabled()
        // WHEN the user clicks the `sound` toggle button
        clickRenaultSwitchPreference(soundToggle)
        // THEN the sound section is greyed out
        assertSoundSettingsIsDisabled()
    }

    @Test
    fun should_enable_detailed_sound_settings_when_toggle_active_and_sound_activation_control_present() { // ktlint-disable max-line-length
        // GIVEN UPA Audio config is supported with settings
        soundRepository.soundActivationControlPresence = true
        soundRepository.volumeControlPresence = true
        soundRepository.soundSelectionControlPresence = true
        soundRepository.soundEnabled.postValue(false)
        //   AND the sound settings menu is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        assertSoundSettingsIsDisabled()
        // WHEN the user clicks the `sound` toggle button
        clickRenaultSwitchPreference(soundToggle)
        // THEN the sound section is enabled
        assertSoundSettingsIsEnabled()
    }

    @Test
    fun should_disable_detailed_sound_settings_when_toggle_inactive_and_sound_activation_control_not_present() { // ktlint-disable max-line-length
        // GIVEN UPA Audio config is supported with settings
        soundRepository.soundActivationControlPresence = false
        soundRepository.volumeControlPresence = true
        soundRepository.soundSelectionControlPresence = true
        //   AND the sound settings menu is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        //   AND the sound setting is expanded
        clickOnView(soundExpandableCategory)
        assertSoundSettingsIsExpanded()
        // WHEN the user clicks the `sound` area
        clickOnView(soundExpandableCategory)
        // THEN the sound section is reduce
        assertSoundSettingsIsReduced()
    }

    @Test
    fun should_expand_detailed_sound_settings_when_toggle_inactive_and_sound_activation_control_not_present() { // ktlint-disable max-line-length
        // GIVEN UPA Audio config is supported with settings
        soundRepository.soundActivationControlPresence = false
        soundRepository.volumeControlPresence = true
        soundRepository.soundSelectionControlPresence = true
        //   AND the sound settings menu is displayed
        settingsActivityTestRule.launchActivity(Intent())
        assertMainSettingsIsDisplayed()
        assertSoundSettingsIsExpanded()
        // WHEN the user clicks the `sound` area
        clickOnView(soundExpandableCategory)
        // THEN the sound section is expand
        assertSoundSettingsIsExpanded()
    }

    @Test
    fun back_button_should_display_and_finish_activity_when_clicked() {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            settingsActivityTestRule.launchActivity(Intent())
            assertViewHasEffectiveVisibility(R.id.car_ui_toolbar_nav_icon)
            Espresso.onView(ViewMatchers.withId(R.id.car_ui_toolbar_nav_icon))
                .perform(ViewActions.click())
            assertTrue(settingsActivityTestRule.activity.isFinishing)
        }
    }

    private fun assertFrontSensorGroupActive() {
        assertRenaultSwitchPreferenceHasEffectiveVisibility(frontToggle)
        assertRenaultSwitchPreferenceIsChecked(frontToggle)
    }

    private fun assertFrontSensorGroupInactive() {
        assertRenaultSwitchPreferenceHasEffectiveVisibility(frontToggle)
        assertRenaultSwitchPreferenceIsNotChecked(frontToggle)
    }

    private fun assertFrontSensorGroupHidden() {
        assertViewHasNoEffectiveVisibility(R.id.front_arc)
        assertRenaultSwitchPreferenceDoesNotExist(frontToggle)
    }

    private fun assertRearSensorGroupActive() {
        assertRenaultSwitchPreferenceHasEffectiveVisibility(rearToggle)
        assertRenaultSwitchPreferenceIsChecked(rearToggle)
    }

    private fun assertRearSensorGroupInactive() {
        assertRenaultSwitchPreferenceHasEffectiveVisibility(rearToggle)
        assertRenaultSwitchPreferenceIsNotChecked(rearToggle)
    }

    private fun assertSideSensorGroupActive() {
        assertViewHasEffectiveVisibility(R.id.middle_arc)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(sideToggle)
        assertRenaultSwitchPreferenceIsChecked(sideToggle)
    }

    private fun assertSideSensorGroupInactive() {
        assertViewHasEffectiveVisibility(R.id.middle_arc)
        assertRenaultSwitchPreferenceHasEffectiveVisibility(sideToggle)
        assertRenaultSwitchPreferenceIsNotChecked(sideToggle)
    }

    private fun assertSideSensorGroupHidden() {
        assertViewHasNoEffectiveVisibility(R.id.middle_arc)
        assertRenaultSwitchPreferenceDoesNotExist(sideToggle)
    }

    private fun assertMainSettingsIsDisplayed() {
        assertViewHasEffectiveVisibility(R.id.main_settings_preference_fragment)
        assertViewHasEffectiveVisibility(R.id.main_settings_sonar_pref_fragment)
    }

    private fun assertSoundSettingsIsDisabled() {
        assertRenaultSwitchPreferenceHasEffectiveVisibility(soundToggle)
        assertViewHasEffectiveVisibility(soundTypeResume)
        assertRenaultRadioPreferenceDoesNotExist(R.string.rlb_parkassist_sound_type_1)
        assertRenaultRadioPreferenceDoesNotExist(R.string.rlb_parkassist_sound_type_2)
        assertRenaultRadioPreferenceDoesNotExist(R.string.rlb_parkassist_sound_type_3)
        assertViewHasEffectiveVisibility(soundVolume)
        assertRenaultPreferenceIsDisabled(soundVolume)
        assertRenaultPreferenceHasSummary(
            R.string.rlb_parkassist_sound_settings_volume,
            "0"
        )
        assertRenaultSeekBarPreferenceDoesNotExist()
    }

    private fun assertSoundSettingsIsEnabled() {
        assertRenaultSwitchPreferenceHasEffectiveVisibility(soundToggle)
        assertRenaultSwitchPreferenceIsChecked(soundToggle)
        // Default selected sound type is 'Type 2'
        // TODO: Re-enable when radio button preferences behavior is restored
        //  ref CCSEXT-71793
//        assertRenaultRadioPreferenceIsEnabled(R.string.rlb_parkassist_sound_type_1)
//        assertRenaultRadioPreferenceIsEnabled(R.string.rlb_parkassist_sound_type_2)
//        assertRenaultRadioPreferenceIsEnabled(R.string.rlb_parkassist_sound_type_3)
        assertRenaultPreferenceIsEnabled(soundVolume)
        assertRenaultSeekBarPreferenceIsEnabled()
        assertRenaultPreferenceHasSummary(
            R.string.rlb_parkassist_sound_settings_volume,
            "0"
        )
    }

    private fun assertSoundSettingsIsReduced() {
        assertViewHasEffectiveVisibility(soundExpandableCategory)
        assertRenaultRadioPreferenceDoesNotExist(R.string.rlb_parkassist_sound_type_1)
        assertRenaultRadioPreferenceDoesNotExist(R.string.rlb_parkassist_sound_type_2)
        assertRenaultRadioPreferenceDoesNotExist(R.string.rlb_parkassist_sound_type_3)
    }

    private fun assertSoundSettingsIsExpanded() {
        assertViewHasEffectiveVisibility(soundExpandableCategory)
        // TODO: Re-enable when radio button preferences behavior is restored
        //  ref CCSEXT-71793
//        assertRenaultRadioPreferenceHasEffectiveVisibility(R.string.rlb_parkassist_sound_type_1)
//        assertRenaultRadioPreferenceHasEffectiveVisibility(R.string.rlb_parkassist_sound_type_2)
//        assertRenaultRadioPreferenceHasEffectiveVisibility(R.string.rlb_parkassist_sound_type_3)
        assertRenaultSeekBarPreferenceHasEffectiveVisibility()
    }

    protected val orientation
        get() =
            InstrumentationRegistry.getInstrumentation().targetContext
                .resources.configuration.orientation
}