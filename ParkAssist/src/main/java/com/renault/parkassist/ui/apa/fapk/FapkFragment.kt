package com.renault.parkassist.ui.apa.fapk

import alliancex.renault.ui.RenaultToggleButton
import alliancex.renault.ui.RenaultToggleIconButton
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.car.ui.toolbar.MenuItem
import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultNavController
import com.renault.car.ui.components.renaultToolbar
import com.renault.parkassist.R
import com.renault.parkassist.isLhd
import com.renault.parkassist.repository.apa.ManeuverType
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.click
import com.renault.parkassist.utility.isActive
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.viewmodel.apa.fapk.FapkViewModelBase
import kotlinx.android.synthetic.main.fragment_fapk.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FapkFragment : FragmentBase() {

    private val fapkViewModel: FapkViewModelBase by viewModel()
    private lateinit var mToolbarSetting: MenuItem
    private val pursuitVm: PursuitViewModel by viewModels()
    override val layout: Int = R.layout.fragment_fapk
    private lateinit var maneuverPerpendicularButton: RenaultToggleIconButton
    private lateinit var maneuverParallelButton: RenaultToggleIconButton
    private lateinit var maneuverParkoutButton: RenaultToggleIconButton
    private lateinit var maneuverStartSwitchButton: RenaultToggleButton
    private lateinit var maneuverStopSwitchButton: RenaultToggleButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        maneuverPerpendicularButton = view.findViewById(R.id.elt_fapk_maneuver_perpendicular)
        maneuverParallelButton = view.findViewById(R.id.elt_fapk_maneuver_parallel)
        maneuverParkoutButton = view.findViewById(R.id.elt_fapk_maneuver_park_out)

        if (context?.isLhd == true) {
            maneuverPerpendicularButton.setCompoundDrawables(null, resources.getDrawable
                (R.drawable.ric_adas_maneuver_right_perpendicular), null, null)
            maneuverParallelButton.setCompoundDrawables(null, resources.getDrawable
                (R.drawable.ric_adas_maneuver_right_parallel), null, null)
            maneuverParkoutButton.setCompoundDrawables(null, resources.getDrawable
                (R.drawable.ric_adas_park_out_right), null, null)
        } else {
            maneuverPerpendicularButton.setCompoundDrawables(null, resources.getDrawable
                (R.drawable.ric_adas_maneuver_left_perpendicular), null, null)
            maneuverParallelButton.setCompoundDrawables(null, resources.getDrawable
                (R.drawable.ric_adas_maneuver_left_parallel), null, null)
            maneuverParkoutButton.setCompoundDrawables(null, resources.getDrawable
                (R.drawable.ric_adas_park_out_left), null, null)
        }
        maneuverStartSwitchButton = view.findViewById(R.id.elt_fapk_maneuver_switch_start)
        maneuverStopSwitchButton = view.findViewById(R.id.elt_fapk_maneuver_switch_stop)
    }

    override fun onBind() {

        with(renaultToolbar) {
            title = resources.getString(R.string.rlb_parkassist_auto_park_assist)
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setNavButtonMode(NavButtonMode.BACK)
                registerBackListener {
                    pursuitVm.stop()
                    true
                }
            }
        }

        mToolbarSetting = MenuItem.builder(context)
            .setTitle(resources.getString(R.string.rlb_parkassist_auto_park_assist))
            .setIcon(R.drawable.ric_gen_settings)
            .setId(R.id.toolbar_icon_settings)
            .setOnClickListener {
                renaultNavController.navigate(R.id.action_to_fapkSettingsFragment)
            }
            .build()

        val items = listOf(mToolbarSetting)
        renaultToolbar.setMenuItems(items)

        fapkViewModel.instruction.observe {
            val instruction = if (it != null) getString(it) else null
            fapk_guidance_instruction?.apply { text = instruction } ?: kotlin.run {
                renaultToolbar.title = instruction
            }
        }

        elt_fapk_maneuver_parallel.setPresent(fapkViewModel.maneuverParallelVisible)

        elt_fapk_maneuver_perpendicular.setPresent(fapkViewModel.maneuverPerpendicularVisible)

        elt_fapk_maneuver_park_out.setPresent(fapkViewModel.maneuverParkoutVisible)

        fapkViewModel.maneuverParallelButtonEnabled.observe {
            maneuverParallelButton.isEnabled = it
        }

        fapkViewModel.maneuverParallelButtonSelected.observe {
            maneuverParallelButton.isActive = it
        }

        fapkViewModel.maneuverPerpendicularButtonEnabled.observe {
            maneuverPerpendicularButton.isEnabled = it
        }

        fapkViewModel.maneuverPerpendicularButtonSelected.observe {
            maneuverPerpendicularButton.isActive = it
        }

        fapkViewModel.maneuverParkoutButtonEnabled.observe {
            maneuverParkoutButton.isEnabled = it
        }

        fapkViewModel.maneuverParkoutButtonSelected.observe {
            maneuverParkoutButton.isActive = it
        }

        fapkViewModel.maneuverStartSwitchButtonVisible.observe { visible: Boolean ->
            maneuverStartSwitchButton.setPresent(visible)
            // To reinitialize when it will become visible again:
            // default value as it is in fragment_fapk.xml with android:checked = 'true'
            if (!visible)
                maneuverStartSwitchButton.isChecked = true
        }

        fapkViewModel.maneuverStopSwitchButtonVisible.observe { visible: Boolean ->
            maneuverStopSwitchButton.setPresent(visible)
            if (!visible)
                maneuverStopSwitchButton.isChecked = true
        }

        fapkViewModel.maneuverStartSwitchButtonEnabled.observe { enabled: Boolean ->
            maneuverStartSwitchButton.isEnabled = enabled
            if (!enabled)
                maneuverStartSwitchButton.isChecked = true
        }

        fapkViewModel.maneuverStartSwitchButtonSelected.observe {
            maneuverStartSwitchButton.isSelected = it
        }

        fapkViewModel.maneuverStopSwitchButtonSelected.observe {
            maneuverStopSwitchButton.isSelected = it
        }

        fapkViewModel.settingsVisible.observe {
            mToolbarSetting.isVisible = it
        }

        maneuverParallelButton.click.observe {
            fapkViewModel.setManeuver(ManeuverType.PARALLEL)
        }
        maneuverPerpendicularButton.click.observe {
            fapkViewModel.setManeuver(ManeuverType.PERPENDICULAR)
        }

        maneuverParkoutButton.click.observe {
            fapkViewModel.setManeuver(ManeuverType.PARKOUT)
        }

        maneuverStartSwitchButton.click.observe {
            fapkViewModel.maneuverStart()
        }

        maneuverStopSwitchButton.click.observe {
            fapkViewModel.maneuverStop()
        }
    }
}