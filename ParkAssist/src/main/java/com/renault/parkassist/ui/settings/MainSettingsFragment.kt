package com.renault.parkassist.ui.settings

import android.view.View
import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.setVisible
import com.renault.parkassist.viewmodel.sonar.SonarSettingsViewModelBase
import kotlinx.android.synthetic.main.fragment_main_settings.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainSettingsFragment : FragmentBase() {
    override val layout: Int = R.layout.fragment_main_settings

    private val sonarSettingsViewModel by sharedViewModel<SonarSettingsViewModelBase>()

    override fun onBind() {
        if (sonarSettingsViewModel.frontVisible)
            sonarSettingsViewModel.frontEnabled.observe {
                main_settings_sonar_view.front = it
            }
        else
            main_settings_sonar_view.findViewById<View>(R.id.front_arc).setVisible(false)

        if (sonarSettingsViewModel.flankVisible)
            sonarSettingsViewModel.flankEnabled.observe {
                main_settings_sonar_view.side = it
            }
        else
            main_settings_sonar_view.findViewById<View>(R.id.middle_arc).setVisible(false)

        if (sonarSettingsViewModel.rearVisible)
            sonarSettingsViewModel.rearEnabled.observe {
                main_settings_sonar_view.rear = it
            }
        else
            main_settings_sonar_view.findViewById<View>(R.id.rear_arc).setVisible(false)

        if (sonarSettingsViewModel.rctaVisible)
            sonarSettingsViewModel.rctaEnabled.observe {
                main_settings_sonar_view.rcta = it
            }
        else
            main_settings_sonar_view.findViewById<View>(R.id.rcta_flag).setVisible(false)

        if (sonarSettingsViewModel.raebVisible)
            sonarSettingsViewModel.raebEnabled.observe {
                main_settings_sonar_view.reb = it
            }
        else
            main_settings_sonar_view.findViewById<View>(R.id.raeb_flag).setVisible(false)

        if (sonarSettingsViewModel.oseVisible)
            sonarSettingsViewModel.oseEnabled.observe {
                main_settings_sonar_view.ose = it
            }
        else
            main_settings_sonar_view.findViewById<View>(R.id.ose_flag).setVisible(false)
    }
}