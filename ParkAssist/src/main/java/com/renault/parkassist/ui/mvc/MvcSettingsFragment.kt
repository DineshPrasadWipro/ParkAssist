package com.renault.parkassist.ui.mvc

import android.os.Bundle
import android.view.View
import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultToolbar
import com.renault.car.ui.components.widget.RenaultToggleIconButton
import com.renault.parkassist.R
import com.renault.parkassist.ui.camera.CameraSharedTransitionFragment
import com.renault.parkassist.ui.camera.SEEKBAR_DEBOUNCE_DURATION
import com.renault.parkassist.utility.Conversion
import com.renault.parkassist.utility.onCheck
import com.renault.parkassist.utility.onSeekBarChange
import com.renault.parkassist.viewmodel.camera.CameraSettingsViewModelBase
import kotlinx.android.synthetic.main.fragment_colorimetry_camera.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MvcSettingsFragment : CameraSharedTransitionFragment() {

    override val layout: Int = R.layout.settings_layout_mvc
    private val cameraSettingsViewModel: CameraSettingsViewModelBase by viewModel()

    private val backListener = {
        cameraSettingsViewModel.navigateBack()
        true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(renaultToolbar) {
            title = resources.getString(R.string.rlb_parkassist_camera_settings)
            setNavButtonMode(NavButtonMode.BACK)
            registerBackListener(backListener)
        }
        setupButtons(view)
    }

    private fun setupButtons(view: View) {
        val dynamicGuideline =
            view.findViewById<RenaultToggleIconButton>(R.id.mvcDynamicGuideline)

        val fixedGuideline =
            view.findViewById<RenaultToggleIconButton>(R.id.mvcStaticGuideline)

        val trailerGuideline =
            view.findViewById<RenaultToggleIconButton>(R.id.mvcTrailerGuideline)

        val autoZoom = view.findViewById<RenaultToggleIconButton>(R.id.mvcAutoZoomGuideline)
        attachObserver(dynamicGuideline, fixedGuideline, trailerGuideline, autoZoom)
    }

    private fun attachObserver(
        dynamicGuideline: RenaultToggleIconButton,
        fixedGuideline: RenaultToggleIconButton,
        trailerGuideline: RenaultToggleIconButton,
        autoZoom: RenaultToggleIconButton
    ) {

        cameraSettingsViewModel.isDynamicGuidelinesActive.observe {
            dynamicGuideline.isChecked = it
        }
        cameraSettingsViewModel.isStaticGuidelinesActive.observe {
            fixedGuideline.isChecked = it
        }
        cameraSettingsViewModel.isTrailerGuidelinesActive.observe {
            trailerGuideline.isChecked = it
        }
        cameraSettingsViewModel.isAutoZoomActive.observe {
            autoZoom.isChecked = it
        }

        dynamicGuideline.onCheck.observe {
            cameraSettingsViewModel.setDynamicGuidelines(it)
        }
        fixedGuideline.onCheck.observe {
            cameraSettingsViewModel.setStaticGuidelines(it)
        }
        trailerGuideline.onCheck.observe {
            cameraSettingsViewModel.setTrailerGuidelines(it)
        }
        autoZoom.onCheck.observe {
            cameraSettingsViewModel.setAutoZoom(it)
        }
    }

    override fun onBind() {
        cameraSettingsViewModel.brightness.observe {
            luminosity_seek_bar.progress = it
            luminosity_seek_bar_value.text = getString(R.string.progress_percentage_text, it)
        }
        cameraSettingsViewModel.contrast.observe {
            contrast_seek_bar_value.text = getString(R.string.progress_percentage_text, it)
            contrast_seek_bar.progress = it
        }
        cameraSettingsViewModel.color.observe {
            hue_seek_bar_value.text = getString(R.string.progress_percentage_text, it)
            hue_seek_bar.progress = it
        }

        luminosity_seek_bar.onSeekBarChange.debounce(
            SEEKBAR_DEBOUNCE_DURATION, TimeUnit.MILLISECONDS
        )
            .observe {
                val value = Conversion.percentageToRangeToPercentage(
                    it,
                    cameraSettingsViewModel.brightnessMin,
                    cameraSettingsViewModel.brightnessMax
                )
                luminosity_seek_bar.progress = value
                luminosity_seek_bar_value.text = getString(R.string.progress_percentage_text, value)
                cameraSettingsViewModel.setBrightness(it)
            }

        contrast_seek_bar.onSeekBarChange.debounce(
            SEEKBAR_DEBOUNCE_DURATION, TimeUnit.MILLISECONDS
        )
            .observe {
                val value = Conversion.percentageToRangeToPercentage(
                    it,
                    cameraSettingsViewModel.contrastMin,
                    cameraSettingsViewModel.contrastMax
                )
                contrast_seek_bar.progress = value
                contrast_seek_bar_value.text = getString(R.string.progress_percentage_text, value)
                cameraSettingsViewModel.setContrast(it)
            }

        hue_seek_bar.onSeekBarChange.debounce(
            SEEKBAR_DEBOUNCE_DURATION, TimeUnit.MILLISECONDS
        )
            .observe {
                val value = Conversion.percentageToRangeToPercentage(
                    it,
                    cameraSettingsViewModel.colorMin,
                    cameraSettingsViewModel.colorMax
                )
                hue_seek_bar.progress = value
                hue_seek_bar_value.text = getString(R.string.progress_percentage_text, value)

                cameraSettingsViewModel.setColor(it)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        renaultToolbar.unregisterBackListener(backListener)
    }
}