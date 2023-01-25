package com.renault.parkassist.ui.apa.hfp

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultToolbar
import com.renault.car.ui.components.widget.RenaultTextView
import com.renault.parkassist.R
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.ui.apa.ManeuverProgressBar
import com.renault.parkassist.utility.setVisible
import com.renault.parkassist.utility.setVisibleWithFadeInFadeOut
import com.renault.parkassist.viewmodel.apa.hfp.HfpGuidanceViewModelBase
import com.renault.parkassist.viewmodel.debounce
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.guidance_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class HfpGuidanceFragment : FragmentBase() {

    private val apaGuidanceViewModel: HfpGuidanceViewModelBase by viewModel()
    private val pursuitVm: PursuitViewModel by viewModels()

    private lateinit var hfpGuidanceInstruction: RenaultTextView
    private lateinit var hfpGuidanceGauge: ManeuverProgressBar

    private val mBackListener = {
        pursuitVm.stop()
        true
    }

    override val layout: Int = R.layout.fragment_hfp_guidance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hfpGuidanceInstruction = view.findViewById(R.id.elt_hfp_guidance_instruction)
        hfpGuidanceGauge = view.findViewById(R.id.elt_adas_gauge)
    }

    override fun onBind() {
        with(renaultToolbar) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                title = resources.getString(R.string.rlb_parkassist_easy_park_assist)
                setNavButtonMode(NavButtonMode.BACK)
                registerBackListener(mBackListener)
            }
        }

        apaGuidanceViewModel.backgroundParallelLeftVisible.debounce().observe {
            parallel_left_background.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.backgroundParallelRightVisible.debounce().observe {
            parallel_right_background.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelLeftVehicleCenterCutVisible.debounce().observe {
            parallel_left_parking_large.setVisibleWithFadeInFadeOut(it)
            parallel_left_vehicle_center_cut.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelRightVehicleCenterCutVisible.debounce().observe {
            parallel_right_parking_large.setVisibleWithFadeInFadeOut(it)
            parallel_right_vehicle_center_cut.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelLeftVehicleCenterFrontVisible.debounce().observe {
            parallel_left_vehicle_center__front.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelRightVehicleCenterFrontVisible.debounce().observe {
            parallel_right_vehicle_center__front.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelLeftVehicleCenterBackVisible.debounce().observe {
            parallel_left_vehicle_center__back.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelRightVehicleCenterBackVisible.debounce().observe {
            parallel_right_vehicle_center__back.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelLeftVehicleCenterVisible.debounce().observe {
            parallel_left_vehicle_center__center.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelRightVehicleCenterVisible.debounce().observe {
            parallel_right_vehicle_center__center.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelLeftParkVisible.debounce().observe {
            parallel_left_parking_finished.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parallelRightParkVisible.debounce().observe {
            parallel_right_parking_finished.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parkoutLeftVehicleLeftVisible.debounce().observe {
            parallel_left_vehicle_left.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.parkoutRightVehicleRightVisible.debounce().observe {
            parallel_right_vehicle_right.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.backgroundPerpendicularCenterVisible.debounce().observe {
            perpendicular_center_background.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.perpendicularVehicleCenterParkVisible.debounce().observe {
            perpendicular_vehicle_center_park.setVisibleWithFadeInFadeOut(it)
            perpendicular_center_parking_finished.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.perpendicularVehicleCenterFrontVisible.debounce().observe {
            perpendicular_vehicle_center_front.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.perpendicularVehicleCenterBackVisible.debounce().observe {
            perpendicular_vehicle_center_back.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.perpendicularVehicleCenterBackStopBackVisible.debounce().observe {
            perpendicular_vehicle_center_back__stop.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.perpendicularVehicleCenterFrontStopFrontVisible.debounce().observe {
            perpendicular_vehicle_center_front__stop.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.perpendicularLeftVehicleCenterCutVisible.debounce().observe {
            perpendicular_left_background.setVisibleWithFadeInFadeOut(it)
            perpendicular_left_parking_large.setVisibleWithFadeInFadeOut(it)
            perpendicular_left_vehicle_center_cut.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.perpendicularRightVehicleCenterCutVisible.debounce().observe {
            perpendicular_right_background.setVisibleWithFadeInFadeOut(it)
            perpendicular_right_parking_large.setVisibleWithFadeInFadeOut(it)
            perpendicular_right_vehicle_center_cut.setVisibleWithFadeInFadeOut(it)
        }

        apaGuidanceViewModel.extendedInstruction.observe {
            if (it != null)
                hfpGuidanceInstruction.setText(it)
            else hfpGuidanceInstruction.text = null
        }

        apaGuidanceViewModel.maneuverCompletion.observe {
            hfpGuidanceGauge.progress = it.toFloat() / 100
        }

        apaGuidanceViewModel.isForwardGauge.observe {
            hfpGuidanceGauge.forward = it
        }

        apaGuidanceViewModel.gaugeVisible.observe {
            hfpGuidanceGauge.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        apaGuidanceViewModel.gaugeColor.observe {
            hfpGuidanceGauge.color = requireContext().getColor(it)
        }

        apaGuidanceViewModel.isCameraVisible.observe {
            camera_container.setVisible(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        renaultToolbar.unregisterBackListener(mBackListener)
    }
}