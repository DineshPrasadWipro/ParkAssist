package com.renault.parkassist.viewmodel.mvc

import android.app.Application
import androidx.lifecycle.LiveData
import com.renault.parkassist.repository.apa.FeatureConfig
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.surroundview.Action
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.map
import com.renault.parkassist.viewmodel.mvc.MvcModeRequest.CLOSE
import org.koin.core.KoinComponent
import org.koin.core.inject

class MvcStateViewModel (application: Application) : MvcStateViewModelBase(application),KoinComponent {

    private val surroundRepository: IExtSurroundViewRepository by inject()
    private val apaRepository: IApaRepository by inject()

    private val mvcMapper: MvcMapper by inject()

    private val authsAndView = LiveDataUtils
        .combineNonNull(surroundRepository.authorizedActions,
            surroundRepository.surroundState.map { it.view })

    override fun requestView() {
        //requestViewMode(MANEUVER)
    }

    override fun closeView() {
        requestViewMode(CLOSE)
    }

    private val closeVisible = surroundRepository.authorizedActions.map {
        it.contains(Action.CLOSE_VIEW)
    }

    private val modeSelected: LiveData<Int> = surroundRepository.surroundState.map {
        mvcMapper.mapAvmButtonSelected(it.view)
    }

    private val maneuverVisible = surroundRepository.authorizedActions.map {
        it.contains(Action.ACTIVATE_MANEUVER_VIEW)
    }

    private val settingsVisible = surroundRepository.authorizedActions.map {
        it.contains(Action.ACTIVATE_SETTINGS_VIEW)
    }

    private val trailerVisible = surroundRepository.authorizedActions.map {
        it.contains(Action.ACTIVATE_TRAILER_VIEW)
    }

    override fun getEasyparkShortcutVisible(): Boolean {
        return apaRepository.featureConfiguration != FeatureConfig.NONE
    }

    private val selectFrontViewVisible = authsAndView.map { (auths, view) ->
        (auths.contains(Action.SELECT_FRONT_CAMERA) || view == View.FRONT_VIEW)
    }

    private val rearViewInfoVisible = authsAndView.map { (auths, view) ->
        (auths.contains(Action.SELECT_REAR_CAMERA) || view == View.REAR_VIEW)
    }

    private val selectLeftSideViewVisible = authsAndView.map { (auths, view) ->
        (auths.contains(Action.SELECT_LEFT_CAMERA) || view == View.LEFT_VIEW)
    }

    private val selectRightSideViewVisible = authsAndView.map { (auths, view) ->
        (auths.contains(Action.SELECT_RIGHT_CAMERA) || view == View.RIGHT_VIEW)
    }

    override fun getModeSelected(): LiveData<Int> = modeSelected
    override fun getCloseVisible(): LiveData<Boolean> = closeVisible
    override fun getManeuverVisible(): LiveData<Boolean> = maneuverVisible
    override fun getSettingsVisible(): LiveData<Boolean> = settingsVisible
    override fun getTrailerVisible(): LiveData<Boolean> = trailerVisible
    override fun getSelectFrontViewVisible(): LiveData<Boolean> = selectFrontViewVisible
    override fun getRearViewInfoVisible(): LiveData<Boolean> = rearViewInfoVisible
    override fun getSelectLeftSideViewVisible(): LiveData<Boolean> = selectLeftSideViewVisible
    override fun getSelectRightSideViewVisible(): LiveData<Boolean> = selectRightSideViewVisible

    override fun requestViewMode(@MvcModeRequest request: Int) {
        surroundRepository.request(request.unMapRequestMode())
    }

    private fun Int.unMapRequestMode() = mvcMapper.unMapModeRequest(this)
}