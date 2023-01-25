package com.renault.parkassist.ui.apa.hfp

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.viewmodel.apa.hfp.HfpGuidanceVehicleCenterFrontViewModelBase
import com.renault.parkassist.viewmodel.debounce
import com.renault.ui.apaguidance.ApaStraightArrowState
import kotlinx.android.synthetic.main.fragment_guidance_vehicle_center_front.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HfpGuidanceVehicleCenterFrontFragment : FragmentBase() {

    private val hfpGuidanceVehicleCenterFrontViewModel:
        HfpGuidanceVehicleCenterFrontViewModelBase by viewModel()

    override val layout: Int = R.layout.fragment_guidance_vehicle_center_front

    override fun onBind() {

        hfpGuidanceVehicleCenterFrontViewModel.arrowStraightUpVisible.debounce().observe {
            if (!it)
                vehicle_center_front_view.front = ApaStraightArrowState.INVISIBLE
        }

        hfpGuidanceVehicleCenterFrontViewModel.engageFrontActiveVisible.observe {
            if (it)
                vehicle_center_front_view.front = ApaStraightArrowState.ON
        }

        hfpGuidanceVehicleCenterFrontViewModel.engageFrontNotActiveVisible.observe {
            if (it)
                vehicle_center_front_view.front = ApaStraightArrowState.OFF
        }

        hfpGuidanceVehicleCenterFrontViewModel.stopFrontVisible.debounce().observe {
            vehicle_center_front_view.stop = it
        }
    }
}