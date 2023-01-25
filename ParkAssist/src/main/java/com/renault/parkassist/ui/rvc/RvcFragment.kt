package com.renault.parkassist.ui.rvc

import android.os.Bundle
import android.os.Trace.beginSection
import android.os.Trace.endSection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.car.ui.toolbar.MenuItem
import com.renault.car.ui.components.renaultToolbar
import com.renault.parkassist.R
import com.renault.parkassist.ui.camera.CameraSharedTransitionFragment
import com.renault.parkassist.viewmodel.rvc.RvcStateViewModelBase
import org.koin.androidx.viewmodel.ext.android.viewModel

class RvcFragment : CameraSharedTransitionFragment() {
    private val rvcStateViewModel: RvcStateViewModelBase by viewModel()
    override val layout: Int = R.layout.rvc

    private val menuItemClickListener = { item: MenuItem ->
        if (item.id == R.id.toolbar_icon_settings)
            rvcStateViewModel.navigateToSettings()
    }

    private var backListener = { true }

    private val settingsMenuItem: MenuItem?
        get() = renaultToolbar.findMenuItemById(R.id.toolbar_icon_settings)

    override fun onBind() {
        rvcStateViewModel.settingsVisible.observe(this::settingsVisible)

        rvcStateViewModel.toolbarEnabled.observe { enable ->
            renaultToolbar.unregisterBackListener(backListener)
            backListener = { !enable }
            renaultToolbar.registerBackListener(backListener)

            if (enable)
                settingsMenuItem?.setOnClickListener(menuItemClickListener)
            else
                settingsMenuItem?.setOnClickListener { }
        }

        renaultToolbar.title = resources.getString(R.string.rlb_parkassist_rvc)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        beginSection("RvcFragment Create")
        val view = super.onCreateView(inflater, container, savedInstanceState)
        endSection()
        return view
    }

    private fun settingsVisible(visible: Boolean) {
        if (visible)
            renaultToolbar.setMenuItems(R.xml.menu_settings_item)
        else
            renaultToolbar.setMenuItems(null)
    }
}