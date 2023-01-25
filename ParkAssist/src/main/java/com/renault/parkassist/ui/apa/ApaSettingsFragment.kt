package com.renault.parkassist.ui.apa

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.viewmodel.apa.ApaSettingsViewModelBase
import com.renault.parkassist.viewmodel.apa.Maneuver
import com.renault.ui.apasettings.RenaultApaSettingsView.ApaSettingsState
import kotlinx.android.synthetic.main.fragment_apa_settings.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class ApaSettingsFragment : FragmentBase() {

    override val layout: Int = R.layout.fragment_apa_settings

    private val viewModel: ApaSettingsViewModelBase by sharedViewModel()

    override fun onBind() {
        viewModel.defaultManeuver.observe {
            maneuver_view.apaState = when (it) {
                Maneuver.PARALLEL -> ApaSettingsState.PARALLEL
                Maneuver.PERPENDICULAR -> ApaSettingsState.PERPENDICULAR
                else -> return@observe
            }
        }
    }
}