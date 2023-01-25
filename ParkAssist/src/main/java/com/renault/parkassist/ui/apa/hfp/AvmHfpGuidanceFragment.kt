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
import com.renault.parkassist.ui.sonar.SonarAlertMiniFragment
import com.renault.parkassist.viewmodel.apa.hfp.HfpGuidanceViewModelBase
import kotlinx.android.synthetic.main.fragment_hfp_guidance.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AvmHfpGuidanceFragment : HfpGuidanceFragment() {

    private val hfpGuidanceViewModel: HfpGuidanceViewModelBase by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
            .also { it?.setTag(R.id.uxTag, it.context.getString(R.string.hfp_guidance_avm_ux)) }
    }

    @Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
    override fun onBind() {
        super.onBind()

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            childFragmentManager.commit {
                add(R.id.sonar_alerts_fragment_container, SonarAlertMiniFragment())
            }
            elt_adas_camera_switch?.isSelected = true
            hfpGuidanceViewModel.requestCameraSwitch(true)

            hfp_guidance_picture.visibility = View.INVISIBLE
            elt_raeb_off.visibility = View.INVISIBLE
            sonar_alerts_fragment_container.visibility = View.INVISIBLE

            elt_adas_camera_switch?.click?.observe {
                it.isSelected = !it.isSelected
                hfpGuidanceViewModel.requestCameraSwitch(it.isSelected)
                hfp_guidance_picture.visibility =
                    if (!it.isSelected) View.VISIBLE else View.INVISIBLE
                sonar_alerts_fragment_container.visibility =
                    if (!it.isSelected) View.VISIBLE else View.INVISIBLE
            }

            hfpGuidanceViewModel.raebSonarOffVisible.observe { visible: Boolean ->
                elt_raeb_off.setVisible(visible)
            }
        }

        sonar_full_fragment_container.visibility = View.GONE
    }

    // TODO Manage RCTA flags position in AVM STANDARD camera overlay in landscape
}