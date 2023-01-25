package com.renault.parkassist.ui.apa.hfp

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.viewmodel.apa.hfp.HfpGuidanceVehicleCenterViewModelBase
import com.renault.parkassist.viewmodel.debounce
import com.renault.ui.apaguidance.ApaArrowState
import com.renault.ui.apaguidance.ApaStraightArrowState
import kotlinx.android.synthetic.main.fragment_guidance_vehicle_center.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HfpGuidanceVehicleCenterFragment : FragmentBase() {

    private val hfpGuidanceVehicleCenterViewModel:
        HfpGuidanceVehicleCenterViewModelBase by viewModel()

    override val layout: Int = R.layout.fragment_guidance_vehicle_center

    override fun onBind() {

        hfpGuidanceVehicleCenterViewModel.engageFrontNotActiveVisible.observe {
            if (it)
                vehicle_center_view.front = ApaStraightArrowState.OFF
        }

        hfpGuidanceVehicleCenterViewModel.engageFrontActiveVisible.observe {
            if (it)
                vehicle_center_view.front = ApaStraightArrowState.ON
        }

        hfpGuidanceVehicleCenterViewModel.rightCurveFrontActiveVisible.observe {
            if (it)
                vehicle_center_view.curveFront = ApaArrowState.RIGHT_ON
        }

        hfpGuidanceVehicleCenterViewModel.rightCurveFrontNotActiveVisible.observe {
            if (it)
                vehicle_center_view.curveFront = ApaArrowState.RIGHT_OFF
        }

        hfpGuidanceVehicleCenterViewModel.leftCurveFrontActiveVisible.observe {
            if (it)
                vehicle_center_view.curveFront = ApaArrowState.LEFT_ON
        }

        hfpGuidanceVehicleCenterViewModel.leftCurveFrontNotActiveVisible.observe {
            if (it)
                vehicle_center_view.curveFront = ApaArrowState.LEFT_OFF
        }

        hfpGuidanceVehicleCenterViewModel.arrowStraightUpVisible.debounce().observe {
            if (!it)
                vehicle_center_view.front = ApaStraightArrowState.INVISIBLE
        }

        hfpGuidanceVehicleCenterViewModel.arrowCurveUpVisible.debounce().observe {
            if (!it)
                vehicle_center_view.curveFront = ApaArrowState.INVISIBLE
        }

        hfpGuidanceVehicleCenterViewModel.engageBackActiveVisible.observe {
            if (it)
                vehicle_center_view.back = ApaStraightArrowState.ON
        }

        hfpGuidanceVehicleCenterViewModel.engageBackNotActiveVisible.observe {
            if (it)
                vehicle_center_view.back = ApaStraightArrowState.OFF
        }

        hfpGuidanceVehicleCenterViewModel.rightCurveBackActiveVisible.observe {
            if (it)
                vehicle_center_view.curveBack = ApaArrowState.RIGHT_ON
        }

        hfpGuidanceVehicleCenterViewModel.rightCurveBackNotActiveVisible.observe {
            if (it)
                vehicle_center_view.curveBack = ApaArrowState.RIGHT_OFF
        }

        hfpGuidanceVehicleCenterViewModel.leftCurveBackActiveVisible.observe {
            if (it)
                vehicle_center_view.curveBack = ApaArrowState.LEFT_ON
        }

        hfpGuidanceVehicleCenterViewModel.leftCurveBackNotActiveVisible.observe {
            if (it)
                vehicle_center_view.curveBack = ApaArrowState.LEFT_OFF
        }

        hfpGuidanceVehicleCenterViewModel.arrowStraightDownVisible.debounce().observe {
            if (!it)
                vehicle_center_view.back = ApaStraightArrowState.INVISIBLE
        }

        hfpGuidanceVehicleCenterViewModel.arrowCurveDownVisible.debounce().observe {
            if (!it)
                vehicle_center_view.curveBack = ApaArrowState.INVISIBLE
        }

        hfpGuidanceVehicleCenterViewModel.leftDoubleCurveVisible.debounce().observe {
            if (it)
                vehicle_center_view.leftDoubleCurve = ApaStraightArrowState.ON
            else
                vehicle_center_view.leftDoubleCurve = ApaStraightArrowState.INVISIBLE
        }

        hfpGuidanceVehicleCenterViewModel.rightDoubleCurveVisible.debounce().observe {
            if (it)
                vehicle_center_view.rightDoubleCurve = ApaStraightArrowState.ON
            else
                vehicle_center_view.rightDoubleCurve = ApaStraightArrowState.INVISIBLE
        }

        hfpGuidanceVehicleCenterViewModel.stopVisible.debounce().observe {
            vehicle_center_view.stop = it
        }
    }
}