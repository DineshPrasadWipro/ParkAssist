package com.renault.parkassist.viewmodel.sonar

import alliance.car.sonar.AllianceCarSonarManager.*
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.filter
import org.koin.core.KoinComponent
import org.koin.core.inject

class SonarAlertsViewModel(application: Application) :
    SonarAlertsViewModelBase(application), KoinComponent {

    private val sonarRepository: ISonarRepository by inject()

    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val rearViews = listOf(
        View.REAR_VIEW,
        View.APA_REAR_VIEW,
        View.PANORAMIC_REAR_VIEW,
        View.SETTINGS_REAR_VIEW
    )

    private val rctaLeftVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            sonarRepository.collisionAlertSide,
            sonarRepository.collisionAlertEnabled,
            surroundRepository.surroundState.filter { !it.isRequest }
        )
    ) { (rctaLeftAlert, rctaEnabled, surroundState) ->
        ((rctaLeftAlert == SIDE_LEFT_ALERT) || (rctaLeftAlert == SIDE_LEFT_RIGHT_ALERT)) &&
            rctaEnabled && surroundState.view in rearViews
    }

    private val rctaRightVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            sonarRepository.collisionAlertSide,
            sonarRepository.collisionAlertEnabled,
            surroundRepository.surroundState.filter { !it.isRequest }
        )
    ) { (rctaRightAlert, rctaEnabled, surroundState) ->
        ((rctaRightAlert == SIDE_RIGHT_ALERT) || (rctaRightAlert == SIDE_LEFT_RIGHT_ALERT)) &&
            rctaEnabled && surroundState.view in rearViews
    }

    private val raebVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            sonarRepository.raebAlertState,
            sonarRepository.raebAlertEnabled,
            surroundRepository.surroundState.filter { !it.isRequest }
        )
    ) { (raebAlert, raebEnabled, surroundState) ->
        when {
            surroundState.view !in rearViews -> false
            (raebAlert == RAEB_ALERT_1) && raebEnabled -> true
            (raebAlert == RAEB_ALERT_2) && raebEnabled -> true
            else -> false
        }
    }

    override fun getRctaLeftVisible(): LiveData<Boolean> = rctaLeftVisible

    override fun getRctaRightVisible(): LiveData<Boolean> = rctaRightVisible

    override fun getRaebVisible(): LiveData<Boolean> = raebVisible
}