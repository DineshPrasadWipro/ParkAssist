package com.renault.parkassist.ui.sonar

import androidx.fragment.app.viewModels
import com.renault.parkassist.R
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.click
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.viewmodel.sonar.SonarStateViewModelBase
import kotlinx.android.synthetic.main.sonar_pip.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SonarPipFragment : FragmentBase() {
    private val pursuitVm: PursuitViewModel by viewModels()
    private val sonarStateViewModel: SonarStateViewModelBase by viewModel()

    override val layout: Int = R.layout.sonar_pip

    override fun onBind() {
        sonarStateViewModel.closeVisible.observe { upa_pip_close.setPresent(it) }
        upa_pip_close.click.observe { pursuitVm.stop() }
    }
}