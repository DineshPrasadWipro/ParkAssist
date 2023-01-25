package com.renault.parkassist.ui.sonar

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.onCheck
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.viewmodel.sonar.SonarMuteStateViewModelBase
import kotlinx.android.synthetic.main.sonar_mute.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SonarMuteFragment : FragmentBase() {

    private val sonarMuteViewModel: SonarMuteStateViewModelBase by viewModel()

    override val layout: Int = R.layout.sonar_mute

    override fun onBind() {
        sonar_mute_button.onCheck.observe { mute ->
            sonarMuteViewModel.mute(mute)
        }

        sonarMuteViewModel.muted.observe { muted ->
            sonar_mute_button.isChecked = muted
        }

        sonarMuteViewModel.visible.observe {
            sonar_mute_button.setPresent(it)
        }
    }
}