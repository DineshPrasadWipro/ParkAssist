package com.renault.parkassist.ui.apa.hfp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.renault.parkassist.R
import com.renault.parkassist.ui.camera.CameraFragment
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.viewmodel.apa.hfp.HfpScanningViewModelBase
import kotlinx.android.synthetic.main.fragment_hfp_scanning.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AvmHfpScanningFragment : HfpScanningFragment() {

    override val settingsAction = R.id.action_to_avmHfpSettingsFragment
    private val hfpScanningViewModel: HfpScanningViewModelBase by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
            .also { it?.setTag(R.id.uxTag, it.context.getString(R.string.hfp_scanning_avm_ux)) }
    }

    override fun onBind() {
        super.onBind()

        childFragmentManager.commit {
            add(R.id.camera_fragment_container, CameraFragment())
        }

        hfpScanningViewModel.sonarAvmVisible.observe {
            sonar_view_no_camera.setPresent(!it)
        }
    }
}