package com.renault.parkassist.ui.settings

import alliancex.arch.core.logger.logD
import alliancex.renault.ui.*
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.renault.parkassist.R
import com.renault.parkassist.ui.camera.SEEKBAR_DEBOUNCE_DURATION
import com.renault.parkassist.viewmodel.settings.MainSettingsViewModelBase
import com.renault.parkassist.viewmodel.sonar.SonarSettingsViewModelBase
import com.renault.parkassist.viewmodel.sound.SoundSettingsViewModelBase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class MainSettingsPreferenceFragment : PreferenceFragmentCompat(), KoinComponent {

    private val NO_SUMMARY = ""

    private val sonarSettings by sharedViewModel<SonarSettingsViewModelBase>()
    private val mainSettings: MainSettingsViewModelBase by viewModel()

    private val sonarPreferenceId = R.xml.sonar_settings_preference
    private val sonarAdvancedPreferenceId = R.xml.sonar_advanced_settings_preference
    private val soundPreferenceId = R.xml.sound_settings_preference
    private val soundSwitchablePreferenceId = R.xml.sound_settings_switchable_preference

    private val soundSettingsViewModel: SoundSettingsViewModelBase by viewModel()

    private val isSoundSwitchable
        get() = soundSettingsViewModel.soundSwitchVisible

    private val soundTypeMap = mapOf(
        1 to R.string.rlb_parkassist_sound_type_1,
        2 to R.string.rlb_parkassist_sound_type_2,
        3 to R.string.rlb_parkassist_sound_type_3
    )

    // sonar_settings_pref
    private val frontToggle
        get() = getSwitchPreference(R.string.frontToggle)
    private val sideToggle
        get() = getSwitchPreference(R.string.sideToggle)
    private val rearToggle
        get() = getSwitchPreference(R.string.rearToggle)

    // sound_settings_preference
    private lateinit var soundTypeEntries: Array<CharSequence>
    private lateinit var soundTypeValues: Array<CharSequence>

    private val soundExpandableCategory
        get() = getPreferenceExpandableCategory(R.string.soundExpandableCategory)
    private val soundToggle
        get() = getSwitchPreference(R.string.soundToggle)
    private val soundTypeResume
        get() = getPreference(R.string.soundTypeResume)
    private val soundRadioPreference
        get() = getRadioPreference(R.string.soundRadioPreference)

    private val soundVolumeResume
        get() = getPreference(R.string.soundVolumeResume)
    private val soundVolume
        get() = getSeekBarPreference(R.string.soundVolume)

    // sonar_advanced_settings_pref
    private val rctaToggle
        get() = getSwitchPreference(R.string.rctaToggle)
    private val raebToggle
        get() = getSwitchPreference(R.string.raebToggle)
    private val oseToggle
        get() = getSwitchPreference(R.string.oseToggle)

    private fun getPreferenceExpandableCategory(key: Int): RenaultPreferenceExpandableCategory? {
        return preferenceScreen.findPreference(resources.getString(key))
    }

    private fun getPreference(key: Int): RenaultPreference? {
        return preferenceScreen.findPreference(resources.getString(key))
    }

    private fun getRadioPreference(key: Int): RenaultRadioPreference? {
        return preferenceScreen.findPreference(resources.getString(key))
    }

    private fun getSeekBarPreference(key: Int): RenaultSeekBarPreference? {
        return preferenceScreen.findPreference(resources.getString(key))
    }

    private fun getSwitchPreference(key: Int): RenaultSwitchPreference? {
        return preferenceScreen.findPreference(resources.getString(key))
    }

    private fun toggleSoundMenu(enabled: Boolean) {
        if (isSoundSwitchable) {
            soundToggle?.isChecked = enabled
            soundTypeResume?.setVisibility(
                if (soundSettingsViewModel.soundTypeVisible) !enabled else false
            )
            soundRadioPreference?.setVisibility(
                if (soundSettingsViewModel.soundTypeVisible) enabled else false
            )
            soundVolumeResume?.setVisibility(true)
            soundVolume?.setVisibility(if (soundSettingsViewModel.volumeVisible) enabled else false)
            soundVolumeResume?.isEnabled = enabled
            soundTypeResume?.isEnabled = enabled
        } else {
            soundExpandableCategory?.isEnabled = enabled
            soundRadioPreference?.setVisibility(
                if (soundSettingsViewModel.soundTypeVisible) !enabled else false
            )
            soundVolume?.setVisibility(
                if (soundSettingsViewModel.volumeVisible) !enabled else false
            )
        }
    }

    private fun attachObservers() {
        sonarSettings.frontEnabled.observe(viewLifecycleOwner, Observer { checked ->
            frontToggle?.isChecked = checked
        })
        sonarSettings.flankEnabled.observe(viewLifecycleOwner, Observer { enabled ->
            sideToggle?.isChecked = enabled
        })
        sonarSettings.rearEnabled.observe(viewLifecycleOwner, Observer { enabled ->
            rearToggle?.isChecked = enabled
        })
        sonarSettings.rctaEnabled.observe(viewLifecycleOwner, Observer { enabled ->
            rctaToggle?.isChecked = enabled
        })
        sonarSettings.raebEnabled.observe(viewLifecycleOwner, Observer { enabled ->
            raebToggle?.isChecked = enabled
        })
        sonarSettings.oseEnabled.observe(viewLifecycleOwner, Observer { enabled ->
            oseToggle?.isChecked = enabled
        })

        soundSettingsViewModel.soundEnabled.observe(viewLifecycleOwner, Observer { enabled ->
            toggleSoundMenu(enabled)
        })
        soundSettingsViewModel.soundId.observe(viewLifecycleOwner, Observer { id ->
            if (soundIdExist(id)) {
                soundRadioPreference?.value=id.toString()
            }
            soundTypeResume?.summary = getSoundName(id)
        })
        soundSettingsViewModel.volume.observe(viewLifecycleOwner, Observer { volume ->
            soundVolume?.value = volume
            soundVolumeResume?.summary = volume.toString()
        })
    }

    private fun registerSwitchPreference(switchPreference: RenaultSwitchPreference?) {
        switchPreference?.setOnPreferenceChangeListener { pref, newValue ->
            with(sonarSettings) {
                when (pref.key) {
                    resources.getString(R.string.frontToggle) -> enableFront(newValue as Boolean)
                    resources.getString(R.string.sideToggle) -> enableFlank(newValue as Boolean)
                    resources.getString(R.string.rearToggle) -> enableRear(newValue as Boolean)
                    resources.getString(R.string.rctaToggle) -> enableRcta(newValue as Boolean)
                    resources.getString(R.string.raebToggle) -> enableRaeb(newValue as Boolean)
                    resources.getString(R.string.oseToggle) -> enableOse(newValue as Boolean)
                }
            }
            true
        }
    }

    private fun soundIdExist(id: Int): Boolean {
        return (soundSettingsViewModel.sounds.indexOfFirst { it.id == id } != -1)
    }

    private fun getSoundName(id: Int) = soundTypeMap[id]?.let { getString(it) } ?: NO_SUMMARY

    private fun getCurrentSoundName(): String? {
        val id = soundSettingsViewModel.soundId.value
        return id?.let { getSoundName(it) }
    }

    private fun registerClickListeners() {
        registerSwitchPreference(frontToggle)
        registerSwitchPreference(sideToggle)
        registerSwitchPreference(rearToggle)
        registerSwitchPreference(rctaToggle)
        registerSwitchPreference(raebToggle)
        registerSwitchPreference(oseToggle)

        soundToggle?.setOnPreferenceChangeListener { _, newValue ->
            soundSettingsViewModel.enableSound(newValue as Boolean)
            true
        }

        soundExpandableCategory?.summary = getCurrentSoundName()
        soundRadioPreference?.setVisibility(false)
        soundVolume?.setVisibility(false)
        soundExpandableCategory?.setOnExpandStateChangedListener { expanded ->
            soundExpandableCategory?.summary = if (expanded) NO_SUMMARY else getCurrentSoundName()
            soundRadioPreference?.setVisibility(
                if (expanded) soundSettingsViewModel.soundTypeVisible else false
            )
            soundVolume?.setVisibility(
                if (expanded) soundSettingsViewModel.volumeVisible else false
            )
        }
        soundRadioPreference?.setOnPreferenceChangeListener { _, soundId ->
            val id = soundId.toString().toInt()
            if (soundIdExist(id)) {
                soundSettingsViewModel.setSound(id)
            }
            true
        }
        val obs = Observable.create<Int> {
            soundVolume?.setOnPreferenceChangeListener(object :
                Preference.OnPreferenceChangeListener {
                override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
                    it.onNext(newValue as Int)
                    return true
                }
            })
        }
        obs.debounce(
            SEEKBAR_DEBOUNCE_DURATION, TimeUnit.MILLISECONDS
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                soundVolumeResume?.summary = it.toString()
                soundSettingsViewModel.setVolume(it)
                logD { "$it" }
            }
    }

    private fun disableUnavailableOptions() {
        frontToggle?.setVisibility(sonarSettings.frontVisible)
        sideToggle?.setVisibility(sonarSettings.flankVisible)
        rearToggle?.setVisibility(sonarSettings.rearVisible && sonarSettings.rearToggleVisible)
        rctaToggle?.setVisibility(sonarSettings.rctaVisible)
        raebToggle?.setVisibility(sonarSettings.raebVisible)
        oseToggle?.setVisibility(sonarSettings.oseVisible)

        if (mainSettings.soundMenuVisible) {
            soundToggle?.setVisibility(isSoundSwitchable)
            if (isSoundSwitchable) {
                soundRadioPreference?.setVisibility(soundSettingsViewModel.soundTypeVisible)
                soundVolume?.setVisibility(soundSettingsViewModel.volumeVisible)
            }
            soundTypeResume?.setVisibility(soundSettingsViewModel.soundTypeVisible)
            if (soundSettingsViewModel.volumeVisible) {
                soundVolume?.min = soundSettingsViewModel.minVolume
                soundVolume?.max = soundSettingsViewModel.maxVolume
            }
        }
    }

    private fun fillSoundTypes() {
        if (soundRadioPreference == null)
            return

        // Gather available sound types
        val soundTypes: Map<CharSequence, CharSequence> = soundSettingsViewModel.sounds.map {
            soundTypeMap[it.id] to it.id
        }.filter { it.first != null }.map { getString(it.first!!) to it.second.toString() }.toMap()

        // Initialize sound type radio preference with current value
        soundRadioPreference?.setEntries(soundTypes)
        soundRadioPreference?.value=soundSettingsViewModel.soundId.value.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        disableUnavailableOptions()
        attachObservers()
        registerClickListeners()
        view.setTag(R.id.uxTag, "")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        addPreferencesFromResource(sonarPreferenceId)
        frontToggle?.setViewId(R.id.front_toggle)
        sideToggle?.setViewId(R.id.side_toggle)
        rearToggle?.setViewId(R.id.rear_toggle)

        if (mainSettings.soundMenuVisible) {
            if (isSoundSwitchable) {
                addPreferencesFromResource(soundSwitchablePreferenceId)
                soundToggle?.setViewId(R.id.sound_toggle)
                soundTypeResume?.setViewId(R.id.sound_type_resume)
                soundRadioPreference?.setViewId(R.id.sound_type)
                soundVolumeResume?.setViewId(R.id.sound_volume_resume)
                soundVolume?.setViewId(R.id.sound_volume)
            } else {
                addPreferencesFromResource(soundPreferenceId)
                soundExpandableCategory?.setViewId(R.id.sound_expandable_category)
                soundRadioPreference?.setViewId(R.id.sound_type)
                soundVolume?.setViewId(R.id.sound_volume)
            }
        }
        addPreferencesFromResource(sonarAdvancedPreferenceId)
        rctaToggle?.setViewId(R.id.rcta_toggle)
        raebToggle?.setViewId(R.id.raeb_toggle)
        oseToggle?.setViewId(R.id.ose_toggle)
        fillSoundTypes()
    }
}