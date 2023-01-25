package com.renault.parkassist.viewmodel.sonar

import android.app.Application
import androidx.lifecycle.*
import com.renault.parkassist.repository.settings.ISoundRepository
import com.renault.parkassist.repository.sonar.*
import com.renault.parkassist.repository.surroundview.FeatureConfig
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.map
import com.renault.parkassist.viewmodel.mergeNotNullWith
import org.koin.core.KoinComponent
import org.koin.core.inject

class SonarMuteStateViewModel(application: Application) :
    SonarMuteStateViewModelBase(application), KoinComponent {
    private val soundRepository: ISoundRepository by inject()
    private val sonarRepository: ISonarRepository by inject()
    private val surroundViewRepository: IExtSurroundViewRepository by inject()

    private val upaEnabledInSettings = Transformations.map(
        LiveDataUtils.combineLatest(
            sonarRepository.flankState,
            sonarRepository.frontState,
            sonarRepository.rearState
        )
    ) { (flankState, frontState, rearState) ->
        when {
            flankState == GroupState.DISABLED &&
                frontState == GroupState.DISABLED &&
                rearState == GroupState.DISABLED -> false
            else -> true
        }
    }

    private val upaAndSoundEnabledInSettings = Transformations.map(
        LiveDataUtils.combineNonNull(
            upaEnabledInSettings,
            soundRepository.soundEnabled
        )
    ) { (upaEnabledInSettings, soundEnabledInSettings) ->
        upaEnabledInSettings && soundEnabledInSettings
    }

    // Make a list of all activated sonar sensors
    private val activatedSensors = with(sonarRepository) {
        Transformations.map(
            LiveDataUtils.combineNonNull(
                LiveDataUtils.combineNonNull(upaDisplayRequest, frontState, rearState),
                LiveDataUtils.combineNonNull(fkpDisplayRequest, flankState)
            )
        ) { (upaState, fkpState) ->
            val (upaDisplayRequested, frontState, rearState) = upaState
            val (fkpDisplayRequested, flankState) = fkpState

            val flankSensors =
                if (fkpDisplayRequested != FkpDisplayRequestType.NO_DISPLAY &&
                    flankState != GroupState.DISABLED
                )
                    listOf(
                        rightFront,
                        rightFrontCenter,
                        rightRearCenter,
                        rightRear,
                        leftRear,
                        leftRearCenter,
                        leftFrontCenter,
                        leftFront
                    )
                else listOf()

            val frontSensors =
                if ((upaDisplayRequested == UpaDisplayRequestType.FRONT ||
                        upaDisplayRequested == UpaDisplayRequestType.REAR_FRONT) &&
                    frontState != GroupState.DISABLED
                )
                    listOf(
                        frontLeft,
                        frontCenter,
                        frontRight
                    )
                else listOf()

            val rearSensors =
                if ((upaDisplayRequested == UpaDisplayRequestType.REAR ||
                        upaDisplayRequested == UpaDisplayRequestType.REAR_FRONT) &&
                    rearState != GroupState.DISABLED
                )
                    listOf(
                        rearLeft,
                        rearCenter,
                        rearRight
                    )
                else listOf()

            flankSensors + frontSensors + rearSensors
        }
    }

    private val sonarObstacleDetected = activatedSensors.switchMap { sensors ->
        // Merge all sensor states into a single boolean that's true when an obstacle is detected
        if (sensors.isNotEmpty())
            sensors.map { sensor ->
                sensor.map { it.isHwSupported && it.level > 0 }
            }.reduce { result, sensorActivatedLiveData ->
                result.mergeNotNullWith(sensorActivatedLiveData) { sensor1, sensor2 ->
                    sensor1 || sensor2
                }
            }
        // If all sensors are deactivated, just return false
        else
            MutableLiveData(false)
    }.distinctUntilChanged()

    private val sonarSoundEnabledAndObstacleDetected = Transformations.map(
        LiveDataUtils.combineNonNull(soundRepository.soundEnabled, sonarObstacleDetected)
    ) { (soundEnabled, obstacleDetected) ->
        soundEnabled && soundRepository.temporaryMuteControlPresence && obstacleDetected
    }

    private val sonarDisplayRequested =
        Transformations.map(sonarRepository.displayRequest) { displayRequest ->
            soundRepository.temporaryMuteControlPresence && displayRequest != DisplayType.NONE
        }.mergeNotNullWith(upaAndSoundEnabledInSettings) { displayRequest, upaAndSoundEnabled ->
            upaAndSoundEnabled && displayRequest
        }

    private val visible = if (surroundViewRepository.featureConfig != FeatureConfig.NONE) {
        Transformations.map(
            LiveDataUtils.combineNonNull(
                surroundViewRepository.surroundState,
                sonarSoundEnabledAndObstacleDetected,
                sonarDisplayRequested
            )
        ) { (surroundState, sonarSoundEnabledAndObstacleDetected, sonarDisplayRequested) ->
            when (surroundState.view) {
                View.PANORAMIC_FRONT_VIEW,
                View.PANORAMIC_REAR_VIEW,
                View.THREE_DIMENSION_VIEW ->
                    sonarSoundEnabledAndObstacleDetected
                else -> sonarDisplayRequested
            }
        }
    } else {
        sonarDisplayRequested
    }

    override fun getMuted(): LiveData<Boolean> = soundRepository.muted
    override fun getVisible(): LiveData<Boolean> = visible
    override fun mute(muted: Boolean) = soundRepository.mute(muted)
}