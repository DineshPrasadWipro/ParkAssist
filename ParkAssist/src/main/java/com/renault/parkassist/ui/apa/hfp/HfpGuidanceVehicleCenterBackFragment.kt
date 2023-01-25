package com.renault.parkassist.ui.apa.hfp

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.viewmodel.apa.hfp.HfpGuidanceVehicleCenterBackViewModelBase
import com.renault.parkassist.viewmodel.debounce
import com.renault.ui.apaguidance.ApaStraightArrowState
import kotlinx.android.synthetic.main.fragment_guidance_vehicle_center_back.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HfpGuidanceVehicleCenterBackFragment : FragmentBase() {

    private val hfpGuidanceVehicleCenterBackViewModel:
        HfpGuidanceVehicleCenterBackViewModelBase by viewModel()

    override val layout: Int = R.layout.fragment_guidance_vehicle_center_back

    override fun onBind() {

        hfpGuidanceVehicleCenterBackViewModel.engageBackActiveVisible.observe {
            if (it)
                vehicle_center_back_view.back = ApaStraightArrowState.ON
        }

        hfpGuidanceVehicleCenterBackViewModel.engageBackNotActiveVisible.observe {
            if (it)
                vehicle_center_back_view.back = ApaStraightArrowState.OFF
        }

        hfpGuidanceVehicleCenterBackViewModel.engageBackVisible.debounce().observe {
            if (!it)
                vehicle_center_back_view.back = ApaStraightArrowState.INVISIBLE
        }

        hfpGuidanceVehicleCenterBackViewModel.stopBackVisible.debounce().observe {
            vehicle_center_back_view.stop = it
        }
    }
}