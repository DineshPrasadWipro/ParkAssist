package com.renault.parkassist.ui.avm

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.android.car.ui.toolbar.MenuItem
import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultButtonBar
import com.renault.car.ui.components.renaultToolbar
import com.renault.car.ui.components.widget.RenaultBottomButtonBar
import com.renault.car.ui.components.widget.RenaultToggleIconButton
import com.renault.parkassist.R
import com.renault.parkassist.isLhd
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.ui.Selector
import com.renault.parkassist.ui.camera.CameraSharedTransitionFragment
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.viewmodel.avm.AvmModeRequest
import com.renault.parkassist.viewmodel.avm.AvmModeSelected
import com.renault.parkassist.viewmodel.avm.AvmStateViewModelBase
import kotlinx.android.synthetic.main.avm.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AvmFragment : CameraSharedTransitionFragment(), KoinComponent {

    private val pursuitVm: PursuitViewModel by viewModels()
    private val avmStateViewModel: AvmStateViewModelBase by viewModel()

    /**
     * modeSelector represents the RadioButton-like control composed of the 5 buttons
     * that allow to select the active view.
     */
    private lateinit var modeSelector: Selector<Int>
    override val layout: Int = R.layout.avm

    private var standardButton: RenaultToggleIconButton? = null
    private var panoramicButton: RenaultToggleIconButton? = null
    private var sideButton: RenaultToggleIconButton? = null
    private var threeDButton: RenaultToggleIconButton? = null

    private lateinit var menuItemSettings: MenuItem
    private lateinit var menuItemEasyPark: MenuItem
    private lateinit var menuListWithSettings: List<MenuItem>
    private lateinit var menuListWithoutSettings: List<MenuItem>

    companion object {
        val standardViewButtonTag = "standardViewButtonTag"
        val panoramicViewButtonTag = "panoramicViewButtonTag"
        val sideViewButtonTag = "sideViewButtonTag"
        val threeDButtonTag = "threedButtonTag"
    }

    private val backListener = {
        pursuitVm.stop()
        true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuItemSettings = MenuItem.builder(context)
            .setTitle(resources.getString(R.string.toolbar_menu_settings))
            .setIcon(R.drawable.ric_gen_settings)
            .setId(R.id.toolbar_icon_settings)
            .setOnClickListener {
                avmStateViewModel.requestViewMode(AvmModeRequest.SETTINGS)
            }
            .build()

        menuItemEasyPark = MenuItem.builder(context)
            .setTitle(resources.getString(R.string.toolbar_menu_easypark))
            .setIcon(R.drawable.ric_adas_park_assist_easypark)
            .setId(R.id.toolbar_icon_easypark)
            .setOnClickListener {
                pursuitVm.startEasyPark()
            }
            .build()

        if (avmStateViewModel.easyparkShortcutVisible) {
            menuListWithSettings = listOf(menuItemEasyPark, menuItemSettings)
            menuListWithoutSettings = listOf(menuItemEasyPark)
        } else {
            menuListWithSettings = listOf(menuItemSettings)
            menuListWithoutSettings = listOf()
        }

        createButtonList()
    }

    override fun onBind() {
        setUpButtons()

        avmStateViewModel.birdSideCameraMargin.observe {
            setLandscapeCameraContainerMargin(it)
        }

        avmStateViewModel.modeSelected.observe { button ->
            setMode(button)
        }

        avmStateViewModel.selectStandardViewVisible.observe {
            standardButton?.setPresent(it)
        }

        avmStateViewModel.selectPanoramicViewVisible.observe {
            panoramicButton?.setPresent(it)
        }

        avmStateViewModel.settingsVisible.observe { visible ->
            if (visible) renaultToolbar.setMenuItems(menuListWithSettings) else
                renaultToolbar.setMenuItems(menuListWithoutSettings)
        }

        avmStateViewModel.backButtonVisible.observe { visible ->
            with(renaultToolbar) {
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    title = resources.getString(R.string.rlb_parkassist_avm)
                    renaultToolbar.setNavButtonMode(
                        if (visible)
                            NavButtonMode.BACK else NavButtonMode.DISABLED
                    )
                } else {
                    title = resources.getString(R.string.rlb_parkassist_avm_disclaimer)
                }
                registerBackListener(backListener)
            }
        }

        avmStateViewModel.selectSidesViewVisible.observe {
            sideButton?.setPresent(it)
        }

        avmStateViewModel.selectThreeDimensionViewVisible.observe {
            threeDButton?.setPresent(it)
        }
        avmStateViewModel.buttonsEnabled.observe { enabled ->
            modeSelector.setClickable(enabled)
            if (enabled) {
                renaultToolbar.registerBackListener(backListener)
            } else {
                renaultToolbar.unregisterBackListener(backListener)
            }
        }

        avmStateViewModel.threeDimensionInfoVisible.observe {
            elt_avm_3d_info?.setPresent(it)
        }

        modeSelector.click.observe {
            when (it) {
                standardButton -> {
                    avmStateViewModel.requestViewMode(AvmModeRequest.STANDARD)
                }
                panoramicButton -> {
                    avmStateViewModel.requestViewMode(AvmModeRequest.PANORAMIC)
                }
                sideButton -> {
                    avmStateViewModel.requestViewMode(AvmModeRequest.SIDES)
                }
                threeDButton -> {
                    avmStateViewModel.requestViewMode(AvmModeRequest.VIEW_3_D)
                }
            }
        }
    }

    private fun setLandscapeCameraContainerMargin(setMargin: Boolean) {
        camera_container_landscape?.let { container ->
            val params =
                (container.layoutParams as LinearLayout.LayoutParams)

            val margin = if (setMargin)
                resources.getDimension(R.dimen.avm_bird_sides_camera_margin).toInt()
            else 0

            val (leftMargin, rightMargin) =
                if (requireContext().isLhd) (margin to 0) else (0 to margin)

            params.setMargins(leftMargin, 0, rightMargin, 0)
            container.layoutParams = params
        }
    }

    private fun setMode(button: Int) {
        modeSelector.select(button)
    }

    private fun setUpButtons() {
        val sel = HashMap<Int, CompoundButton>()
        standardButton?.let { sel[AvmModeSelected.STANDARD] = it }
        panoramicButton?.let { sel[AvmModeSelected.PANORAMIC] = it }
        sideButton?.let { sel[AvmModeSelected.SIDES] = it }
        threeDButton?.let { sel[AvmModeSelected.THREE_D] = it }

        modeSelector = Selector(sel)
    }

    private fun createButtonList() {
        renaultButtonBar.visibility = View.VISIBLE
        renaultButtonBar.addButton(
            RenaultBottomButtonBar.Button.ToggleIconButton(
                R.drawable.ric_adas_avm_panoramic_upa,
                R.style.RenaultToggleIconButton_Mex_Width2,
                tag = standardViewButtonTag
            )
        )
        renaultButtonBar.addButton(
            RenaultBottomButtonBar.Button.ToggleIconButton(
                R.drawable.ric_adas_camera_switch,
                R.style.RenaultToggleIconButton_Mex_Width2,
                tag = panoramicViewButtonTag
            )
        )

        renaultButtonBar.addButton(
            RenaultBottomButtonBar.Button.ToggleIconButton(
                R.drawable.ric_adas_park_assist_sides,
                R.style.RenaultToggleIconButton_Mex_Width2,
                tag = sideViewButtonTag
            )
        )

        renaultButtonBar.addButton(
            RenaultBottomButtonBar.Button.ToggleIconButton(
                R.drawable.ric_adas_camera_360,
                R.style.RenaultToggleIconButton_Mex_Width2,
                tag = threeDButtonTag
            )
        )
        setButtons()
    }

    private fun setButtons() {
        standardButton = renaultButtonBar
            .findButtonWithTag<RenaultToggleIconButton>(standardViewButtonTag)
        panoramicButton = renaultButtonBar
            .findButtonWithTag<RenaultToggleIconButton>(panoramicViewButtonTag)
        sideButton = renaultButtonBar
            .findButtonWithTag<RenaultToggleIconButton>(sideViewButtonTag)
        threeDButton = renaultButtonBar
            .findButtonWithTag<RenaultToggleIconButton>(threeDButtonTag)
    }
}