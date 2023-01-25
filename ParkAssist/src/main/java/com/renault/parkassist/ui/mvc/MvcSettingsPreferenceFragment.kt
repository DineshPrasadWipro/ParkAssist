package com.renault.parkassist.ui.mvc

import android.os.Bundle
import com.renault.car.ui.components.preference.RenaultPreferenceFragment
import com.renault.parkassist.R

class MvcSettingsPreferenceFragment : RenaultPreferenceFragment() {
    private val mvcSettingsPreferenceId = R.xml.mvc_settings_preference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(mvcSettingsPreferenceId, rootKey)
    }
}