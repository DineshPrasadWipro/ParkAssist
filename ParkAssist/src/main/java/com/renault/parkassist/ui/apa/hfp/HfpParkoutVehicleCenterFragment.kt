package com.renault.parkassist.ui.apa.hfp

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.viewmodel.apa.hfp.HfpParkoutVehicleCenterViewModelBase
import com.renault.parkassist.viewmodel.debounce
import kotlinx.android.synthetic.main.fragment_parkout_vehicle_center.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HfpParkoutVehicleCenterFragment : FragmentBase() {

    private val hfpParkoutVehicleCenterViewModel:
        HfpParkoutVehicleCenterViewModelBase by viewModel()

    override val layout: Int = R.layout.fragment_parkout_vehicle_center

    override fun onBind() {

        hfpParkoutVehicleCenterViewModel.leftDoubleCurveVisible.debounce().observe {
            parkout_view.doubleCurveLeft = it
        }

        hfpParkoutVehicleCenterViewModel.rightDoubleCurveVisible.debounce().observe {
            parkout_view.doubleCurveRight = it
        }

        hfpParkoutVehicleCenterViewModel.stopVisible.debounce().observe {
            parkout_view.stop = it
        }

        hfpParkoutVehicleCenterViewModel.arrowRightSideVisible.debounce().observe {
            parkout_view.rightArrow = it
        }

        hfpParkoutVehicleCenterViewModel.arrowLeftSideVisible.debounce().observe {
            parkout_view.leftArrow = it
        }
    }
}