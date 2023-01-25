package com.renault.parkassist.ui.avm

import androidx.fragment.app.viewModels
import com.renault.parkassist.R
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.click
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.viewmodel.avm.AvmStateViewModelBase
import kotlinx.android.synthetic.main.avm_pip.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AvmPipFragment : FragmentBase() {
    private val pursuitVm: PursuitViewModel by viewModels()
    private val avmStateViewModel: AvmStateViewModelBase by viewModel()

    override val layout: Int = R.layout.avm_pip

    override fun onBind() {
        avmStateViewModel.closeVisible.observe { avm_pip_close.setPresent(it) }
        avm_pip_close.click.observe { pursuitVm.stop() }
        avm_pip_camera.click.observe { pursuitVm.startManeuver() }
    }
}