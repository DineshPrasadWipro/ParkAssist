package com.renault.parkassist.ui.apa.hfp

import alliancex.renault.ui.RenaultTextView
import alliancex.renault.ui.RenaultToggleIconButton
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.car.ui.toolbar.MenuItem
import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultToolbar
import com.renault.parkassist.R
import com.renault.parkassist.repository.apa.ManeuverType
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.click
import com.renault.parkassist.utility.isActive
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.viewmodel.apa.hfp.HfpScanningViewModelBase
import com.renault.ui.apascanning.ApaArrowState
import com.renault.uilibrary.utils.extensions.setImageResourceAsTag
import kotlinx.android.synthetic.main.fragment_hfp_scanning.*
import kotlinx.android.synthetic.main.scanning_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class HfpScanningFragment : FragmentBase() {

    private val hfpScanningViewModel: HfpScanningViewModelBase by viewModel()
    private val pursuitVm: PursuitViewModel by viewModels()
    protected abstract val settingsAction: Int
    private var scanningInstructionTextView: RenaultTextView? = null
    private lateinit var maneuverParallelButton: RenaultToggleIconButton
    private lateinit var maneuverPerpendicularButton: RenaultToggleIconButton
    private lateinit var maneuverParkoutButton: RenaultToggleIconButton

    private val backListener = {
        pursuitVm.stop()
        true
    }
    override val layout = R.layout.fragment_hfp_scanning

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanningInstructionTextView = view.findViewById(R.id.elt_apa_scanning_instruction)
        maneuverParallelButton = view.findViewById(R.id.maneuver_parallel)
        maneuverPerpendicularButton = view.findViewById(R.id.maneuver_perpendicular)
        maneuverParkoutButton = view.findViewById(R.id.maneuver_parkout)
    }

    override fun onBind() {
        with(renaultToolbar) {
            title = resources.getString(R.string.rlb_parkassist_easy_park_assist)
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setNavButtonMode(NavButtonMode.BACK)
                registerBackListener(backListener)
            }
        }

        hfpScanningViewModel.maneuverParallelButtonEnabled.observe {
            maneuverParallelButton.isEnabled = it
        }

        hfpScanningViewModel.maneuverPerpendicularButtonEnabled.observe {
            maneuverPerpendicularButton.isEnabled = it
        }

        hfpScanningViewModel.maneuverParkoutButtonEnabled.observe {
            maneuverParkoutButton.isEnabled = it
        }

        hfpScanningViewModel.maneuverParallelButtonSelected.observe {
            maneuverParallelButton.isActive = it
        }

        hfpScanningViewModel.maneuverPerpendicularButtonSelected.observe {
            maneuverPerpendicularButton.isActive = it
        }

        hfpScanningViewModel.maneuverParkoutButtonSelected.observe {
            maneuverParkoutButton.isActive = it
        }

        maneuverParallelButton.click.observe {
            hfpScanningViewModel.setManeuver(ManeuverType.PARALLEL)
        }

        maneuverPerpendicularButton.click.observe {
            hfpScanningViewModel.setManeuver(ManeuverType.PERPENDICULAR)
        }

        maneuverParkoutButton.click.observe {
            hfpScanningViewModel.setManeuver(ManeuverType.PARKOUT)
        }

        hfpScanningViewModel.instruction.observe {
            val instruction = it?.let { getString(it) }
            scanningInstructionTextView?.apply { text = instruction } ?: kotlin.run {
                renaultToolbar.title = instruction
            }
        }

        val navController = findNavController()
        val menuItem = MenuItem.builder(context)
            .setTitle(resources.getString(R.string.rlb_parkassist_auto_park_assist))
            .setIcon(R.drawable.ric_gen_settings)
            .setId(R.id.toolbar_icon_settings)
            .setOnClickListener {
                navController.navigate(settingsAction)
            }
            .build()

        val items = listOf(menuItem)
        renaultToolbar.setMenuItems(items)

        hfpScanningViewModel.displayParkout.observe {
            hfp_parkout_picture.setPresent(it)
            elt_hfp_scanning_view.setPresent(!it)
        }

        hfpScanningViewModel.backgroundResource.observe {
            map_area.setImageResourceAsTag(it ?: 0)
        }

        hfpScanningViewModel.rearLeftShortArrowVisible.observe {
            if (it) {
                hfp_scanning_car_top_view.bottomCenterArrow = ApaArrowState.LEFT_SHORT_ON
            }
        }

        hfpScanningViewModel.rearRightShortArrowVisible.observe {
            if (it) {
                hfp_scanning_car_top_view.bottomCenterArrow = ApaArrowState.RIGHT_SHORT_ON
            }
        }

        hfpScanningViewModel.rearLeftLongArrowVisible.observe {
            if (it) {
                hfp_scanning_car_top_view.bottomCenterArrow = ApaArrowState.LEFT_LONG_ON
            }
        }

        hfpScanningViewModel.rearRightLongArrowVisible.observe {
            if (it) {
                hfp_scanning_car_top_view.bottomCenterArrow = ApaArrowState.RIGHT_LONG_ON
            }
        }

        hfpScanningViewModel.leftSlotResource.observe {
            left_parking_slot_parallel.setImageResourceAsTag(it ?: 0)
            left_parking_slot_perpendicular.setImageResourceAsTag(it ?: 0)
        }

        hfpScanningViewModel.rightSlotResource.observe {
            right_parking_slot_parallel.setImageResourceAsTag(it ?: 0)
            right_parking_slot_perpendicular.setImageResourceAsTag(it ?: 0)
        }

        hfpScanningViewModel.leftParkingSlotParallelVisible.observe {
            left_parking_slot_parallel.setPresent(it)
        }

        hfpScanningViewModel.rightParkingSlotParallelVisible.observe {
            right_parking_slot_parallel.setPresent(it)
        }

        hfpScanningViewModel.leftParkingSlotPerpendicularVisible.observe {
            left_parking_slot_perpendicular.setPresent(it)
        }

        hfpScanningViewModel.rightParkingSlotPerpendicularVisible.observe {
            right_parking_slot_perpendicular.setPresent(it)
        }

        hfpScanningViewModel.rearArrowResourceVisible.observe {
            if (!it) {
                hfp_scanning_car_top_view.bottomCenterArrow = ApaArrowState.INVISIBLE
            }
        }

        hfpScanningViewModel.carFrontStopResourceVisible.observe {
            hfp_scanning_car_top_view.topStop = it
        }

        hfpScanningViewModel.carFrontArrowResourceVisible.observe {
            hfp_scanning_car_top_view.topArrow = it
        }

        hfpScanningViewModel.leftIndicatorSelected.observe {
            hfp_scanning_car_top_view.bottomLeftArrow = it
        }

        hfpScanningViewModel.rightIndicatorSelected.observe {
            hfp_scanning_car_top_view.bottomRightArrow = it
        }

        hfpScanningViewModel.upaDisabledApaScanning.observe {
            sonar_disabled_view.setPresent(it)
        }
    }
}