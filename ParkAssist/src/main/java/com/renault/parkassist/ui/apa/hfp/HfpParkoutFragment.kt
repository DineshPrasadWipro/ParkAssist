package com.renault.parkassist.ui.apa.hfp

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.setVisible
import com.renault.parkassist.viewmodel.apa.hfp.HfpParkoutViewModelBase
import kotlinx.android.synthetic.main.fragment_scanning_parkout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HfpParkoutFragment : FragmentBase() {

    private val hfpParkoutViewModel:
        HfpParkoutViewModelBase by viewModel()

    override val layout: Int = R.layout.fragment_scanning_parkout

    override fun onBind() {

        hfpParkoutViewModel.backgroundSidesVisible.observe {
            sides_background.setVisible(it)
            sides_vehicle_center.setVisible(it)
        }

        hfpParkoutViewModel.backgroundParallelLeftVisible.observe {
            parallel_left_background_parkout.setVisible(it)
        }

        hfpParkoutViewModel.parallelLeftVehicleCenterVisible.observe {
            parallel_left_vehicle_center.setVisible(it)
        }

        hfpParkoutViewModel.backgroundParallelRightVisible.observe {
            parallel_right_background_parkout.setVisible(it)
        }

        hfpParkoutViewModel.parallelRightVehicleCenterVisible.observe {
            parallel_right_vehicle_center.setVisible(it)
        }

        hfpParkoutViewModel.parallelLeftVehicleLeftVisible.observe {
            parallel_left_vehicle_left_parkout.setVisible(it)
        }

        hfpParkoutViewModel.parallelRightVehicleRightVisible.observe {
            parallel_right_vehicle_right_parkout.setVisible(it)
        }
    }
}