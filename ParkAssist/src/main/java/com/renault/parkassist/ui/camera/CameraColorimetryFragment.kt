package com.renault.parkassist.ui.camera

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.onSeekBarChange
import com.renault.parkassist.utility.Conversion
import com.renault.parkassist.viewmodel.camera.CameraSettingsViewModelBase
import java.util.concurrent.TimeUnit
import kotlinx.android.synthetic.main.fragment_colorimetry_camera.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val SEEKBAR_DEBOUNCE_DURATION = 100L

class CameraColorimetryFragment : FragmentBase() {

    private val cameraSettingsViewModel: CameraSettingsViewModelBase by viewModel()

    override val layout = R.layout.fragment_colorimetry_camera

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
}