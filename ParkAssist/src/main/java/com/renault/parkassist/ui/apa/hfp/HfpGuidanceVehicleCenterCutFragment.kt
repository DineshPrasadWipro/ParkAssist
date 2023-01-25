package com.renault.parkassist.ui.apa.hfp

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.viewmodel.apa.hfp.HfpGuidanceVehicleCenterCutViewModelBase
import com.renault.parkassist.viewmodel.debounce
import com.renault.ui.apaguidance.ApaArrowState
import kotlinx.android.synthetic.main.fragment_guidance_vehicle_center_cut.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HfpGuidanceVehicleCenterCutFragment : FragmentBase() {

    private val hfpGuidanceVehicleCenterCutViewModel:
        HfpGuidanceVehicleCenterCutViewModelBase by viewModel()

    override val layout: Int = R.layout.fragment_guidance_vehicle_center_cut

    override fun onBind() {

        hfpGuidanceVehicleCenterCutViewModel.arrowCurveDownVisible.debounce().observe {
            if (!it)
                vehicle_center_cut_view.side = ApaArrowState.INVISIBLE
        }

        hfpGuidanceVehicleCenterCutViewModel.engageLeftActiveVisible.observe {
            if (it)
                vehicle_center_cut_view.side = ApaArrowState.LEFT_ON
        }

        hfpGuidanceVehicleCenterCutViewModel.engageLeftNotActiveVisible.observe {
            if (it)
                vehicle_center_cut_view.side = ApaArrowState.LEFT_OFF
        }

        hfpGuidanceVehicleCenterCutViewModel.engageRightActiveVisible.observe {
            if (it)
                vehicle_center_cut_view.side = ApaArrowState.RIGHT_ON
        }

        hfpGuidanceVehicleCenterCutViewModel.engageRightNotActiveVisible.observe {
            if (it)
                vehicle_center_cut_view.side = ApaArrowState.RIGHT_OFF
        }
    }
}