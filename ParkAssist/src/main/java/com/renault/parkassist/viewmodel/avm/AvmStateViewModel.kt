package com.renault.parkassist.viewmodel.avm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.renault.parkassist.repository.apa.FeatureConfig
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.surroundview.Action.*
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.repository.surroundview.View.*
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.avm.AvmModeRequest.CLOSE
import com.renault.parkassist.viewmodel.avm.AvmModeRequest.MANEUVER
import com.renault.parkassist.viewmodel.map
import org.koin.core.KoinComponent
import org.koin.core.inject

class AvmStateViewModel(application: Application) : AvmStateViewModelBase(application),
    KoinComponent {

    private val surroundRepository: IExtSurroundViewRepository by inject()
    private val apaRepository: IApaRepository by inject()

    private val avmMapper: AvmMapper by inject()

    private val _buttonsEnabled = surroundRepository.surroundState.map {
        !it.isRequest
    }

    override fun requestView() {
        requestViewMode(MANEUVER)
    }

    override fun closeView() {
        requestViewMode(CLOSE)
    }

    private val birdSideCameraMargin = surroundRepository.surroundState.map {
        it.view == View.SIDES_VIEW && !it.isRequest
    }.distinctUntilChanged()

    private val authsAndView = LiveDataUtils
        .combineNonNull(surroundRepository.authorizedActions,
            surroundRepository.surroundState.map { it.view })

    private val closeVisible = surroundRepository.authorizedActions.map {
        it.contains(CLOSE_VIEW)
    }

    private val maneuverVisible = surroundRepository.authorizedActions.map {
        it.contains(ACTIVATE_MANEUVER_VIEW)
    }
    private val settingsVisible = surroundRepository.authorizedActions.map {
        it.contains(ACTIVATE_SETTINGS_VIEW)
    }

    private val trailerVisible = surroundRepository.authorizedActions.map {
        it.contains(ACTIVATE_TRAILER_VIEW)
    }

    private val backButtonVisible = surroundRepository.authorizedActions.map { authorizedActions ->
        authorizedActions.contains(CLOSE_VIEW)
    }

    private val selectPanoramaViewVisible = authsAndView.map { (auths, view) ->
        (auths.contains(SELECT_PANORAMIC_VIEW) ||
            listOf(
                PANORAMIC_FRONT_VIEW,
                PANORAMIC_REAR_VIEW
            ).contains(view))
    }

    private val selectStandardViewVisible = authsAndView.map { (auths, view) ->
        (auths.contains(SELECT_STANDARD_VIEW) ||
            listOf(FRONT_VIEW, REAR_VIEW).contains(view))
    }

    private val selectSidesViewVisible = authsAndView.map { (auths, view) ->
        (auths.contains(SELECT_SIDES_VIEW) || view == SIDES_VIEW)
    }

    private val selectThreeDimensionViewVisible = authsAndView.map { (auths, view) ->
        (auths.contains(SELECT_THREE_DIMENSION_VIEW) || view == THREE_DIMENSION_VIEW)
    }

    private val modeSelected: LiveData<Int> = surroundRepository.surroundState.map {
        avmMapper.mapAvmButtonSelected(it.view)
    }

    private val threeDimensionInfoVisible = surroundRepository.surroundState.map {
        it.view == THREE_DIMENSION_VIEW && !it.isRequest
    }.distinctUntilChanged()

    override fun getEasyparkShortcutVisible(): Boolean {
        return apaRepository.featureConfiguration != FeatureConfig.NONE
    }

    override fun getCloseVisible(): LiveData<Boolean> = closeVisible
    override fun getBackButtonVisible(): LiveData<Boolean> = backButtonVisible

    override fun getManeuverVisible(): LiveData<Boolean> = maneuverVisible

    override fun getSettingsVisible(): LiveData<Boolean> = settingsVisible
    override fun getButtonsEnabled(): LiveData<Boolean> = _buttonsEnabled
    override fun getThreeDimensionInfoVisible(): LiveData<Boolean> = threeDimensionInfoVisible

    override fun getTrailerVisible(): LiveData<Boolean> = trailerVisible
    override fun getBirdSideCameraMargin(): LiveData<Boolean> = birdSideCameraMargin

    override fun getModeSelected(): LiveData<Int> = modeSelected

    override fun getSelectPanoramicViewVisible(): LiveData<Boolean> = selectPanoramaViewVisible

    override fun getSelectStandardViewVisible(): LiveData<Boolean> = selectStandardViewVisible

    override fun getSelectSidesViewVisible(): LiveData<Boolean> = selectSidesViewVisible

    override fun getSelectThreeDimensionViewVisible(): LiveData<Boolean> =
        selectThreeDimensionViewVisible

    override fun requestViewMode(@AvmModeRequest request: Int) {
        surroundRepository.request(request.unMapRequestMode())
    }

    private fun Int.unMapRequestMode() = avmMapper.unMapModeRequest(this)
}