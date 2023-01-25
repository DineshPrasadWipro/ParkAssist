package com.renault.parkassist.ui.sonar

import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.setVisible
import com.renault.parkassist.viewmodel.sonar.SonarAlertsViewModelBase
import kotlinx.android.synthetic.main.sonar_alerts.*
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseSonarAlertFragment : FragmentBase() {

    private val sonarAlertsViewModel: SonarAlertsViewModelBase by viewModel()

    override fun onBind() {
        sonarAlertsViewModel.rctaLeftVisible.observe { visible ->
            rcta_left.setVisible(visible)
        }

        sonarAlertsViewModel.rctaRightVisible.observe { visible ->
            rcta_right.setVisible(visible)
        }

        sonarAlertsViewModel.raebVisible.observe { visible ->
            raeb_right.setVisible(visible)
            raeb_left.setVisible(visible)
        }
    }
}