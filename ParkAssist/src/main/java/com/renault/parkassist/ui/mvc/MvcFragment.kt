package com.renault.parkassist.ui.mvc

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.android.car.ui.toolbar.MenuItem
import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultToolbar
import com.renault.car.ui.components.widget.RenaultToggleIconButton
import com.renault.parkassist.R
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.ui.Selector
import com.renault.parkassist.ui.camera.CameraSharedTransitionFragment
import com.renault.parkassist.viewmodel.mvc.MvcModeRequest
import com.renault.parkassist.viewmodel.mvc.MvcModeSelected
import com.renault.parkassist.viewmodel.mvc.MvcStateViewModelBase
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class MvcFragment : CameraSharedTransitionFragment(), KoinComponent {
    override val layout: Int = R.layout.mvc
    private val pursuitVm: PursuitViewModel by viewModels()

    private var rearButton: RenaultToggleIconButton? = null
    private var frontButton: RenaultToggleIconButton? = null
    private var leftButton: RenaultToggleIconButton? = null
    private var rightButton: RenaultToggleIconButton? = null

    private val mvcStateViewModel: MvcStateViewModelBase by viewModel()
    private lateinit var modeSelector: Selector<Int>
    private lateinit var menuListWithSettings: List<MenuItem>
    private lateinit var menuListWithoutSettings: List<MenuItem>
    private val backListener = {
        pursuitVm.stop()
        true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuItemSettings = MenuItem.builder(context)
            .setTitle(resources.getString(R.string.toolbar_menu_settings))
            .setIcon(R.drawable.ric_gen_settings)
            .setId(R.id.toolbar_icon_settings)
            .setOnClickListener {
                mvcStateViewModel.requestViewMode(MvcModeRequest.SETTINGS)
            }
            .build()

        val menuItemEasyPark = MenuItem.builder(context)
            .setTitle(resources.getString(R.string.toolbar_menu_easypark))
            .setIcon(R.drawable.ric_adas_park_assist_easypark)
            .setId(R.id.toolbar_icon_easypark)
            .setOnClickListener {
                pursuitVm.startEasyPark()
            }
            .build()

        val menuItemAppsMonitor = MenuItem.builder(context)
            .setTitle(resources.getString(R.string.toolbar_menu_apps_monitor))
            .setIcon(R.drawable.ric_apps_monitor)
            .setId(R.id.toolbar_icon_apps_monitor)
            .build()

        if (mvcStateViewModel.easyparkShortcutVisible) {
            menuListWithSettings = listOf(menuItemAppsMonitor, menuItemEasyPark, menuItemSettings)
            menuListWithoutSettings = listOf(menuItemAppsMonitor, menuItemEasyPark)
        } else {
            menuListWithSettings = listOf(menuItemAppsMonitor, menuItemSettings)
            menuListWithoutSettings = listOf(menuItemAppsMonitor)
        }
        setMenuVisibility(true)
        with(renaultToolbar) {
            title = resources.getString(R.string.rlb_parkassist_disclaimer)
            setNavButtonMode(NavButtonMode.BACK)
            registerBackListener(backListener)
        }

        initButtons(view)
    }

    override fun onBind() {
        setUpButtons()
        modeSelector.click.observe {
            when (it) {
                rearButton -> {
                    mvcStateViewModel.requestViewMode(MvcModeRequest.REAR)
                }
                frontButton -> {
                    mvcStateViewModel.requestViewMode(MvcModeRequest.FRONT)
                }
                leftButton -> {
                    mvcStateViewModel.requestViewMode(MvcModeRequest.LEFT)
                }
                rightButton -> {
                    mvcStateViewModel.requestViewMode(MvcModeRequest.RIGHT)
                }
            }
        }
        mvcStateViewModel.settingsVisible.observe { visible ->
            if (visible) {
                renaultToolbar.setMenuItems(menuListWithSettings)
            } else {
                renaultToolbar.setMenuItems(menuListWithoutSettings)
            }
        }

    }

    private fun setUpButtons() {
        val sel = HashMap<Int, CompoundButton>()
        rearButton?.let { sel[MvcModeSelected.REAR] = it }
        frontButton?.let { sel[MvcModeSelected.FRONT] = it }
        leftButton?.let { sel[MvcModeSelected.LEFT] = it }
        rightButton?.let { sel[MvcModeSelected.RIGHT] = it }

        modeSelector = Selector(sel)
    }

    private fun initButtons(view: View) {
        rearButton = view.findViewById(R.id.mvc_front)
        frontButton = view.findViewById(R.id.mvc_rear)
        leftButton = view.findViewById(R.id.mvc_left)
        rightButton = view.findViewById(R.id.mvc_right)
    }
}