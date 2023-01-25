package com.renault.parkassist.viewmodel.camera

import android.app.Application
import androidx.lifecycle.*
import com.renault.parkassist.repository.surroundview.Action
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.utility.Conversion.percentageToRange
import com.renault.parkassist.utility.Conversion.rangeToPercentage
import org.koin.core.KoinComponent
import org.koin.core.inject

class CameraSettingsViewModel(application: Application) : CameraSettingsViewModelBase(application),
    KoinComponent {
    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val _toolBarEnabled = surroundRepository.surroundState.map {
        !it.isRequest
    }

    private val _brightness: LiveData<Int> = Transformations.map(surroundRepository.brightness) {
        rangeToPercentage(it, brightnessMin, brightnessMax)
    }
    private val _color: LiveData<Int> = Transformations.map(surroundRepository.color) {
        rangeToPercentage(it, colorMin, colorMax)
    }
    private val _contrast: LiveData<Int> = Transformations.map(surroundRepository.contrast) {
        rangeToPercentage(it, contrastMin, contrastMax)
    }
    private val _backButtonVisibility: LiveData<Boolean> =
        Transformations.map(surroundRepository.authorizedActions) {
            it.contains(Action.BACK_FROM_SETTINGS_VIEW)
        }

    override fun getBrightness(): LiveData<Int> = _brightness
    override fun getBrightnessMin(): Int = surroundRepository.brightnessMin
    override fun getToolbarEnabled(): LiveData<Boolean> = _toolBarEnabled
    override fun getBackButtonVisibility(): LiveData<Boolean> = _backButtonVisibility

    override fun getBrightnessMax(): Int = surroundRepository.brightnessMax

    override fun getColor(): LiveData<Int> = _color
    override fun getColorMin(): Int = surroundRepository.colorMin
    override fun getColorMax(): Int = surroundRepository.colorMax

    override fun getContrast(): LiveData<Int> = _contrast
    override fun getContrastMin(): Int = surroundRepository.contrastMin
    override fun getContrastMax(): Int = surroundRepository.contrastMax

    override fun getIsStaticGuidelinesAvailable(): Boolean =
        surroundRepository.isStaticGuidelinesSupported

    override fun getIsStaticGuidelinesActive(): LiveData<Boolean> =
        surroundRepository.staticGuidelinesActivation

    override fun getIsTrailerGuidelinesAvailable(): Boolean =
        surroundRepository.isTrailerGuidelinesSupported

    override fun getIsTrailerGuidelinesActive(): LiveData<Boolean> =
        surroundRepository.trailerGuidelinesActivation

    override fun getIsAutoZoomAvailable(): Boolean = surroundRepository.isAutoZoomSupported
    override fun getIsAutoZoomActive(): LiveData<Boolean> =
        surroundRepository.autoZoomRearViewActivation

    override fun getIsDynamicGuidelinesAvailable(): Boolean =
        surroundRepository.isDynamicGuidelinesSupported

    override fun getIsDynamicGuidelinesActive(): LiveData<Boolean> =
        surroundRepository.dynamicGuidelinesActivation

    override fun setBrightness(brightness: Int) = surroundRepository.setBrightness(
        percentageToRange(brightness, brightnessMin, brightnessMax)
    )

    override fun setContrast(contrast: Int) = surroundRepository.setContrast(
        percentageToRange(contrast, contrastMin, contrastMax)
    )

    override fun setColor(color: Int) = surroundRepository.setColor(
        percentageToRange(color, colorMin, colorMax)
    )

    override fun setStaticGuidelines(active: Boolean) =
        surroundRepository.setStaticGuidelinesActivation(active)

    override fun setDynamicGuidelines(active: Boolean) =
        surroundRepository.setDynamicGuidelinesActivation(active)

    override fun setTrailerGuidelines(active: Boolean) =
        surroundRepository.setTrailerGuidelinesActivation(active)

    override fun setAutoZoom(active: Boolean) =
        surroundRepository.setAutoZoomRearViewActivation(active)

    override fun navigateBack() {
        surroundRepository.request(Action.BACK_FROM_SETTINGS_VIEW)
    }
}