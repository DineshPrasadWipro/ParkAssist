package com.renault.parkassist.repository.sonar

import alliance.car.sonar.AllianceCarSonarManager.*
import alliance.car.sonar.Sonar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renault.parkassist.utility.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class SonarRepository : KoinComponent, ISonarRepository {

    private val sonarManager: SonarCarManagerAdapter by inject()

    private fun getSensorLabel(@SonarId sensorId: Int) = when (sensorId) {
        SONAR_FRONT_LEFT -> "SONAR_FRONT_LEFT"
        SONAR_FRONT_RIGHT -> "SONAR_FRONT_RIGHT"
        SONAR_RIGHT_FRONT -> "SONAR_RIGHT_FRONT"
        SONAR_RIGHT_FRONT_CENTER -> "SONAR_RIGHT_FRONT_CENTER"
        SONAR_RIGHT_REAR_CENTER -> "SONAR_RIGHT_REAR_CENTER"
        SONAR_RIGHT_REAR -> "SONAR_RIGHT_REAR"
        SONAR_REAR_LEFT -> "SONAR_REAR_LEFT"
        SONAR_REAR_CENTER -> "SONAR_REAR_CENTER"
        SONAR_REAR_RIGHT -> "SONAR_REAR_RIGHT"
        SONAR_LEFT_FRONT -> "SONAR_LEFT_FRONT"
        SONAR_LEFT_FRONT_CENTER -> "SONAR_LEFT_FRONT_CENTER"
        SONAR_LEFT_REAR_CENTER -> "SONAR_LEFT_REAR_CENTER"
        SONAR_LEFT_REAR -> "SONAR_LEFT_REAR"
        else -> "UNKNOWN  ($sensorId)"
    }

    private fun sonarToSensor(sonar: Sonar): SensorState {
        return sonar.run { SensorState(isHwSupported, isHatched, isScanned, level) }
    }

    private fun getGroupLabel(@SonarGroupId groupId: Int) = when (groupId) {
        SONAR_GROUP_FRONT -> "FRONT"
        SONAR_GROUP_REAR -> "REAR"
        SONAR_GROUP_FLANK -> "FLANK"
        else -> "UNKNOWN ($groupId)"
    }

    private val _displayRequest =
        MutableLiveData<@DisplayType Int>().apply { value = DisplayType.NONE }
    private val _upaDisplayRequest =
        MutableLiveData<@UpaDisplayRequestType Int>().apply {
            value = UpaDisplayRequestType.NO_DISPLAY
        }
    private val _fkpDisplayRequest =
        MutableLiveData<@FkpDisplayRequestType Int>().apply {
            value = FkpDisplayRequestType.NO_DISPLAY
        }
    private val _frontLeft = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _frontCenter = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _frontRight = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _rearLeft = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _rearCenter = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _rearRight = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _leftFront = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _leftFrontCenter = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _leftRearCenter = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _leftRear = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _rightFront = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _rightFrontCenter = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _rightRearCenter = MutableLiveData<SensorState>().apply { value = SensorState() }
    private val _rightRear = MutableLiveData<SensorState>().apply { value = SensorState() }

    private val _sonarEvents = mapOf(
        SONAR_FRONT_LEFT to _frontLeft,
        SONAR_FRONT_CENTER to _frontCenter,
        SONAR_FRONT_RIGHT to _frontRight,
        SONAR_RIGHT_FRONT to _rightFront,
        SONAR_RIGHT_FRONT_CENTER to _rightFrontCenter,
        SONAR_RIGHT_REAR_CENTER to _rightRearCenter,
        SONAR_RIGHT_REAR to _rightRear,
        SONAR_REAR_LEFT to _rearLeft,
        SONAR_REAR_CENTER to _rearCenter,
        SONAR_REAR_RIGHT to _rearRight,
        SONAR_LEFT_FRONT to _leftFront,
        SONAR_LEFT_FRONT_CENTER to _leftFrontCenter,
        SONAR_LEFT_REAR_CENTER to _leftRearCenter,
        SONAR_LEFT_REAR to _leftRear
    )

    private val _obstacle = MutableLiveData<Boolean>().apply { value = false }
    private val _frontState =
        MutableLiveData<@GroupState Int>().apply { value = GroupState.DISABLED }
    private val _rearState =
        MutableLiveData<@GroupState Int>().apply { value = GroupState.DISABLED }
    private val _flankState =
        MutableLiveData<@GroupState Int>().apply { value = GroupState.DISABLED }
    private val _collisionAlertEnabled = MutableLiveData<Boolean>().apply { value = false }
    private val _collisionAlertSide = MutableLiveData<@Side Int>().apply { value = NO_ALERT }
    private val _collisionAlertLevel =
        MutableLiveData<@Level Int>().apply { value = LEVEL_LOW_COLLISION_RISK }
    private val _raebAlertEnabled = MutableLiveData<Boolean>().apply { value = false }
    private val _raebAlertState = MutableLiveData<@RaebAlert Int>().apply { value = RAEB_NO_ALERT }
    private val _closeAllowed = MutableLiveData<Boolean>().apply { value = false }

    override fun getDisplayRequest(): LiveData<Int> = _displayRequest
    override fun getUpaDisplayRequest(): LiveData<Int> = _upaDisplayRequest
    override fun getFkpDisplayRequest(): LiveData<Int> = _fkpDisplayRequest
    override fun getFrontLeft(): LiveData<SensorState> = _frontLeft
    override fun getFrontCenter(): LiveData<SensorState> = _frontCenter
    override fun getFrontRight(): LiveData<SensorState> = _frontRight
    override fun getRearLeft(): LiveData<SensorState> = _rearLeft
    override fun getRearCenter(): LiveData<SensorState> = _rearCenter
    override fun getRearRight(): LiveData<SensorState> = _rearRight
    override fun getLeftFront(): LiveData<SensorState> = _leftFront
    override fun getLeftFrontCenter(): LiveData<SensorState> = _leftFrontCenter
    override fun getLeftRearCenter(): LiveData<SensorState> = _leftRearCenter
    override fun getLeftRear(): LiveData<SensorState> = _leftRear
    override fun getRightFront(): LiveData<SensorState> = _rightFront
    override fun getRightFrontCenter(): LiveData<SensorState> = _rightFrontCenter
    override fun getRightRearCenter(): LiveData<SensorState> = _rightRearCenter
    override fun getRightRear(): LiveData<SensorState> = _rightRear
    override fun getObstacle(): LiveData<Boolean> = _obstacle
    override fun getFrontState(): LiveData<Int> = _frontState
    override fun getFlankState(): LiveData<Int> = _flankState
    override fun getRearState(): LiveData<Int> = _rearState
    override fun getCollisionAlertEnabled(): LiveData<Boolean> = _collisionAlertEnabled
    override fun getCollisionAlertSide(): LiveData<Int> = _collisionAlertSide
    override fun getCollisionAlertLevel(): LiveData<Int> = _collisionAlertLevel
    override fun getRaebAlertEnabled(): LiveData<Boolean> = _raebAlertEnabled
    override fun getRaebAlertState(): LiveData<Int> = _raebAlertState
    override fun getCloseAllowed(): LiveData<Boolean> = _closeAllowed

    init {
        with(sonarManager) {
            frontSetting.subscribe {
                val state = if (it) GroupState.ENABLED else GroupState.DISABLED
                _frontState.postValue(state)
            }
            rearSetting.subscribe {
                val state = if (it) GroupState.ENABLED else GroupState.DISABLED
                _rearState.postValue(state)
            }
            flankSetting.subscribe {
                val state = if (it) GroupState.ENABLED else GroupState.DISABLED
                _flankState.postValue(state)
            }

            sonarGroupError.subscribe { (groupId, errorCode) ->
                sonarWarningLog(
                    "An error occured on group '${getGroupLabel(groupId)}' " +
                        "with code $errorCode"
                )
            }

            displayRequest.subscribe { (isRear, isFront, isFlank) ->
                sonarReceiveInfoLog("displayRequest", listOf(isRear, isFront, isFlank))
                if (isRear)
                    _displayRequest.postValue(DisplayType.FULLSCREEN)
                else if (isFront || isFlank)
                    _displayRequest.postValue(DisplayType.WIDGET)
                else
                    _displayRequest.postValue(DisplayType.NONE)

                val upaDisplayRequestType =
                    when {
                        isRear && isFront -> UpaDisplayRequestType.REAR_FRONT
                        isRear -> UpaDisplayRequestType.REAR
                        isFront -> UpaDisplayRequestType.FRONT
                        else -> UpaDisplayRequestType.NO_DISPLAY
                    }

                if (upaDisplayRequestType != _upaDisplayRequest.value)
                    _upaDisplayRequest.postValue(upaDisplayRequestType)

                val fkpDisplayRequestType =
                    if (isFlank) FkpDisplayRequestType.FLANK else FkpDisplayRequestType.NO_DISPLAY

                if (fkpDisplayRequestType != _fkpDisplayRequest.value)
                    _fkpDisplayRequest.postValue(fkpDisplayRequestType)
            }
            closeAllowed.subscribe {
                sonarReceiveInfoLog("closeAllowed", it)
                _closeAllowed.postValue(it)
            }

            sonarEvents.forEach { sonarObs ->
                sonarObs.subscribe { sonar ->
                    if (sonar == null) {
                        sonarErrorLog("onSonarEvent called with null argument", "discard")
                        return@subscribe
                    }
                    sonarReceiveInfoLog("sonarEvent", sonar)
                    _sonarEvents[sonar.sonarId]?.postValue(sonarToSensor(sonar))
                }
            }

            sonarError.subscribe { (sensorId, errorCode) ->
                sonarWarningLog(
                    "An error occured on sensor '${getSensorLabel(sensorId)}'" +
                        " with code $errorCode"
                )
            }
            raebEvent.subscribe {
                sonarReceiveInfoLog("raebEvent", it)
                _raebAlertState.postValue(it)
            }
            raebStatus.subscribe {
                sonarReceiveInfoLog("raebStatus", it)
                _raebAlertEnabled.postValue(it)
            }
            collisionAlertEvent.subscribe { (side, level) ->
                sonarReceiveInfoLog("collisionAlertEvent", listOf(side, level))
                _collisionAlertSide.postValue(side)
                _collisionAlertLevel.postValue(level)
            }
            collisionAlertStatus.subscribe {
                sonarReceiveInfoLog("collisionAlertStatus", it)
                _collisionAlertEnabled.postValue(it)
            }
        }
        sonarInfoLog(
            "get",
            "upaRearFeaturePresent",
            sonarManager.upaRearConfig
        )
        sonarInfoLog(
            "get",
            "upaFrontFeaturePresent",
            sonarManager.upaFrontConfig
        )
        sonarInfoLog(
            "get", "upaFkpFeaturePresent",
            sonarManager.fkpConfig
        )
        sonarInfoLog(
            "get",
            "upaFkpVisualFeedbackFeature",
            sonarManager.visualFeedbackConfig
        )
        sonarInfoLog(
            "get",
            "rctaFeaturePresent",
            sonarManager.rctaConfig
        )
        sonarInfoLog(
            "get",
            "raebFeaturePresent",
            sonarManager.raebConfig
        )
        sonarInfoLog(
            "get",
            "upaActivationSettingsPresent",
            sonarManager.upaSettingConfig
        )
    }

    override fun enableCollisionAlert(enable: Boolean) {
        sonarManager.enableCollisionAlert(enable)
        sonarSendInfoLog("enableCollisionAlert", enable)
    }

    override fun enableRearAutoEmergencyBreak(enable: Boolean) {
        sonarManager.enableRearAutoEmergencyBreak(enable)
        sonarSendInfoLog("enableRearAutoEmergencyBreak", enable)
    }

    override fun setSonarGroup(sonarGroupId: Int, enable: Boolean) {
        sonarManager.setSonarGroup(sonarGroupId, enable)
        sonarSendInfoLog(
            "sonarGroup", listOf(
                getGroupLabel(sonarGroupId),
                enable
            )
        )
    }

    override fun getUpaRearFeaturePresent(): Boolean = sonarManager.upaRearConfig
    override fun getUpaFrontFeaturePresent(): Boolean = sonarManager.upaFrontConfig
    override fun getFkpFeaturePresent(): Boolean = sonarManager.fkpConfig
    override fun getUpaFkpVisualFeedbackFeaturePresent(): Boolean = sonarManager
        .visualFeedbackConfig

    override fun getRctaFeaturePresent(): Boolean = sonarManager.rctaConfig
    override fun getRaebFeaturePresent(): Boolean = sonarManager.raebConfig
    override fun getRearUpaActivationSettingPresent() = sonarManager.upaSettingConfig
}