package com.renault.parkassist.ui.apa.hfp

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.renault.parkassist.R
import com.renault.parkassist.utility.click
import com.renault.parkassist.utility.setVisible
import com.renault.parkassist.ui.sonar.SonarFullFragment
import com.renault.parkassist.viewmodel.apa.hfp.HfpGuidanceViewModelBase
import com.renault.parkassist.viewmodel.sonar.SonarFullViewModelBase
import kotlinx.android.synthetic.main.fragment_hfp_guidance.*
import kotlinx.android.synthetic.main.fragment_hfp_guidance.elt_raeb_off
import kotlinx.android.synthetic.main.sonar_full.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RvcHfpGuidanceFragment : HfpGuidanceFragment() {

    private val sonarFullViewModel: SonarFullViewModelBase by sharedViewModel()
    private val hfpGuidanceViewModel: HfpGuidanceViewModelBase by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
            .also { it?.setTag(R.id.uxTag, it.context.getString(R.string.hfp_guidance_rvc_ux)) }
    }

    @Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
    override fun onBind() {
        super.onBind()

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            elt_adas_camera_switch?.isSelected = true
            hfpGuidanceViewModel.requestCameraSwitch(true)
            sonarFullViewModel.setSonarAlertVisibility(false)
            hfp_guidance_picture.visibility = View.INVISIBLE
            elt_raeb_off.visibility = View.INVISIBLE
            sonar_alerts_fragment_container.visibility = View.INVISIBLE

            elt_adas_camera_switch?.click?.observe {
                it.isSelected = !it.isSelected
                hfpGuidanceViewModel.requestCameraSwitch(it.isSelected)
                hfp_guidance_picture.visibility =
                    if (!it.isSelected) View.VISIBLE else View.INVISIBLE
                sonarFullViewModel.setSonarAlertVisibility(!it.isSelected)
            }

            hfpGuidanceViewModel.raebSonarOffVisible.observe { visible: Boolean ->
                elt_sonar_raeb_off?.setVisible(visible)
            }
        }

        childFragmentManager.commit {
            add(R.id.sonar_full_fragment_container, SonarFullFragment())
        }
    }
}