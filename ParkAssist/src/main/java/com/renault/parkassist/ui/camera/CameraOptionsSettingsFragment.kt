package com.renault.parkassist.ui.camera

import android.view.View
import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultButtonBar
import com.renault.car.ui.components.renaultToolbar
import com.renault.car.ui.components.widget.RenaultBottomButtonBar
import com.renault.car.ui.components.widget.RenaultToggleIconButton
import com.renault.parkassist.R
import com.renault.parkassist.viewmodel.camera.CameraSettingsViewModelBase
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class CameraOptionsSettingsFragment : CameraSharedTransitionFragment() {

    companion object {
        const val dynamicLinesButtonTag = "DynamicLinesButton"
        const val staticLinesButtonTag = "StaticLinesButton"
        const val trailerButtonTag = "TrailerButton"
        const val zoomAutoButtonTag = "ZoomAutoButton"
    }

    private val cameraSettingsViewModel: CameraSettingsViewModelBase by viewModel()

    private val mBackListener = {
        cameraSettingsViewModel.navigateBack()
        true
    }
    private var currentBackListener = mBackListener

    override fun onBind() {
        createButtonList()
        attachObservers()

        renaultToolbar.title = resources.getString(R.string.rlb_parkassist_camera_settings)
        renaultToolbar.setNavButtonMode(NavButtonMode.BACK)
        renaultToolbar.registerBackListener(currentBackListener)
        renaultButtonBar.visibility = View.VISIBLE
    }

    private fun createButtonList() {
        if (cameraSettingsViewModel.isDynamicGuidelinesAvailable)
            renaultButtonBar.addButton(RenaultBottomButtonBar.Button.ToggleIconButton(
                R.drawable.ric_adas_camera_dynamic_lines,
                R.style.RenaultToggleIconButton_Mex_Width2,
                tag = dynamicLinesButtonTag
            ) {
                cameraSettingsViewModel.setDynamicGuidelines(it)
            })

        if (cameraSettingsViewModel.isStaticGuidelinesAvailable)
            renaultButtonBar.addButton(RenaultBottomButtonBar.Button.ToggleIconButton(
                R.drawable.ric_adas_camera_fixed_lines,
                R.style.RenaultToggleIconButton_Mex_Width2,
                tag = staticLinesButtonTag
            ) {
                    cameraSettingsViewModel.setStaticGuidelines(it)
            })

        if (cameraSettingsViewModel.isTrailerGuidelinesAvailable)
            renaultButtonBar.addButton(RenaultBottomButtonBar.Button.ToggleIconButton(
                R.drawable.ric_adas_trailer,
                R.style.RenaultToggleIconButton_Mex_Width2,
                tag = trailerButtonTag
            ) {
                cameraSettingsViewModel.setTrailerGuidelines(it)
            })

        if (cameraSettingsViewModel.isAutoZoomAvailable)
            renaultButtonBar.addButton(RenaultBottomButtonBar.Button.ToggleIconButton(
                R.drawable.ric_settings_zoom_auto,
                R.style.RenaultToggleIconButton_Mex_Width2,
                tag = zoomAutoButtonTag
            ) {
                cameraSettingsViewModel.setAutoZoom(it)
            })
    }

    private fun attachObservers() {
        cameraSettingsViewModel.isDynamicGuidelinesActive.observe {
            renaultButtonBar
                .findButtonWithTag<RenaultToggleIconButton>(dynamicLinesButtonTag)?.isChecked =
                it
        }

        cameraSettingsViewModel.isStaticGuidelinesActive.observe {
            renaultButtonBar
                .findButtonWithTag<RenaultToggleIconButton>(staticLinesButtonTag)?.isChecked =
                it
        }

        cameraSettingsViewModel.isTrailerGuidelinesActive.observe {
            renaultButtonBar
                .findButtonWithTag<RenaultToggleIconButton>(trailerButtonTag)?.isChecked =
                it
        }

        cameraSettingsViewModel.isAutoZoomActive.observe {
            renaultButtonBar
                .findButtonWithTag<RenaultToggleIconButton>(zoomAutoButtonTag)?.isChecked =
                it
        }

        cameraSettingsViewModel.toolbarEnabled.observe { enabled ->
            renaultToolbar.unregisterBackListener(currentBackListener)
            currentBackListener = if (enabled) mBackListener else { { true } }
            renaultToolbar.registerBackListener(currentBackListener)
        }

        cameraSettingsViewModel.backButtonVisibility.observe {
            renaultToolbar.setNavButtonMode(if (it) NavButtonMode.BACK else NavButtonMode.DISABLED)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        renaultToolbar.unregisterBackListener(mBackListener)
    }
}
