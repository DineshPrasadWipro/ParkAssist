package com.renault.parkassist.ui.apa

import alliancex.renault.ui.RenaultRadioPreference
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.preference.PreferenceFragmentCompat
import com.renault.parkassist.R
import com.renault.parkassist.viewmodel.apa.ApaSettingsViewModelBase
import com.renault.parkassist.viewmodel.apa.Maneuver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class ApaSettingsPreferenceFragment : PreferenceFragmentCompat(),
    KoinComponent {

    private val apaSettingsViewModel: ApaSettingsViewModelBase by sharedViewModel()

    private val apaSettingsPreferenceId = R.xml.apa_settings_preference

    private val isManeuverSelectorVisible
        get() = apaSettingsViewModel.maneuverSelectorVisible

    private val apaRadioPreference
        get() = getRadioPreference(R.string.apaManeuverTypeRadioPreference)

    private fun getRadioPreference(key: Int): RenaultRadioPreference? {
        return preferenceScreen.findPreference(resources.getString(key))
    }

    private fun attachObservers() {
        apaSettingsViewModel.defaultManeuver.observe(viewLifecycleOwner, Observer { maneuver ->
            apaRadioPreference?.value=maneuver.toString()
        })
    }

    private fun registerClickListeners() {
        apaRadioPreference?.setOnPreferenceChangeListener { _, maneuver ->
            val id = maneuver.toString().toInt()
            apaSettingsViewModel.setDefaultManeuver(id)
            true
        }
    }

    private fun disableUnavailableOptions() {
        // Filter available maneuvers
        val entries = mapOf<CharSequence, CharSequence>(
            getString(R.string.rlb_parkassist_apa_settings_parallel)
                to Maneuver.PARALLEL.toString(),
            getString(R.string.rlb_parkassist_apa_settings_perpendicular)
                to Maneuver.PERPENDICULAR.toString()
        ).filter {
            apaSettingsViewModel.maneuvers.contains(it.value.toString().toInt())
        }
        apaRadioPreference?.setEntries(entries)
        apaRadioPreference?.setVisibility(isManeuverSelectorVisible)
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
        setPreferencesFromResource(apaSettingsPreferenceId, rootKey)
    }
}