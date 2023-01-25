package com.renault.parkassist.repository.sonar

import alliance.car.sonar.AllianceCarSonarManager
import alliance.car.sonar.AllianceCarSonarManager.*
import alliance.car.sonar.Sonar
import com.renault.parkassist.utility.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent

class SonarCarManagerAdapter(private val sonarCarManager: AllianceCarSonarManager) :
    KoinComponent {

    private val _collisionAlertEvent: BehaviorSubject<Pair<Int, Int>> = BehaviorSubject.create()
    private val _collisionAlertStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _raebEvent: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _raebStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _frontSetting: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _rearSetting: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _flankSetting: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _sonarGroupError: BehaviorSubject<Pair<Int, Int>> = BehaviorSubject.create()
    private val _closeAllowed: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _displayRequest: BehaviorSubject<Triple<Boolean, Boolean, Boolean>> =
        BehaviorSubject.create()
    private val _sonarError: BehaviorSubject<Pair<Int, Int>> = BehaviorSubject.create()

    private val _sonarEvents: Map<Int, BehaviorSubject<Sonar>> = mapOf(
        SONAR_FRONT_LEFT to BehaviorSubject.create(),
        SONAR_FRONT_CENTER to BehaviorSubject.create(),
        SONAR_FRONT_RIGHT to BehaviorSubject.create(),
        SONAR_RIGHT_FRONT to BehaviorSubject.create(),
        SONAR_RIGHT_FRONT_CENTER to BehaviorSubject.create(),
        SONAR_RIGHT_REAR_CENTER to BehaviorSubject.create(),
        SONAR_RIGHT_REAR to BehaviorSubject.create(),
        SONAR_REAR_LEFT to BehaviorSubject.create(),
        SONAR_REAR_CENTER to BehaviorSubject.create(),
        SONAR_REAR_RIGHT to BehaviorSubject.create(),
        SONAR_LEFT_FRONT to BehaviorSubject.create(),
        SONAR_LEFT_FRONT_CENTER to BehaviorSubject.create(),
        SONAR_LEFT_REAR_CENTER to BehaviorSubject.create(),
        SONAR_LEFT_REAR to BehaviorSubject.create()
    )

    val collisionAlertEvent: Observable<Pair<Int, Int>> = _collisionAlertEvent.hide()
    val collisionAlertStatus: Observable<Boolean> = _collisionAlertStatus.hide()
    val raebEvent: Observable<Int> = _raebEvent.hide()
    val raebStatus: Observable<Boolean> = _raebStatus.hide()
    val frontSetting: Observable<Boolean> = _frontSetting.hide()
    val rearSetting: Observable<Boolean> = _rearSetting.hide()
    val flankSetting: Observable<Boolean> = _flankSetting.hide()
    val sonarGroupError: Observable<Pair<Int, Int>> = _sonarGroupError.hide()
    val closeAllowed: Observable<Boolean> = _closeAllowed.hide()
    val displayRequest: Observable<Triple<Boolean, Boolean, Boolean>> =
        _displayRequest.hide()
    val sonarError: Observable<Pair<Int, Int>> = _sonarError.hide()
    val sonarEvents: List<Observable<Sonar>> = _sonarEvents.values.map {
        it.hide()
    }

    // Allows to read vehicle configuration from android system properties for debug purpose
    private val useSysPropsVehicleConfig: Boolean = AndroidProperty.getBooleanSystemProperty(
        CFG_ADAS_USE_SYSTEM_PROPS_SONAR_CONFIG, DEFAULT_CFG_ADAS_USE_SYSTEM_PROPS_SONAR_CONFIG
    )
    var upaRearConfig: Boolean = false
        private set
    var upaFrontConfig: Boolean = false
        private set
    var fkpConfig: Boolean = false
        private set
    var visualFeedbackConfig: Boolean = false
        private set
    var rctaConfig: Boolean = false
        private set
    var raebConfig: Boolean = false
        private set
    var upaSettingConfig: Boolean = false

    private fun getGroupLabel(@AllianceCarSonarManager.SonarGroupId groupId: Int) = when (groupId) {
        SONAR_GROUP_FRONT -> "FRONT"
        SONAR_GROUP_REAR -> "REAR"
        SONAR_GROUP_FLANK -> "FLANK"
        else -> "UNKNOWN ($groupId)"
    }

    init {
        readVehicleConfiguration()
        try {
            sonarCarManager.registerSonarGroupListener(object :
                SonarGroupListener {
                override fun onSonarGroupError(p0: Int, p1: Int) {
                    _sonarGroupError.onNext(p0 to p1)
                }

                override fun onSonarGroupEvent(groupId: Int, enabled: Boolean) {
                    sonarReceiveInfoLog(
                        "groupEvent", listOf(
                            getGroupLabel(groupId),
                            enabled
                        )
                    )
                    when (groupId) {
                        SONAR_GROUP_FRONT -> _frontSetting.onNext(enabled)
                        SONAR_GROUP_REAR -> _rearSetting.onNext(enabled)
                        SONAR_GROUP_FLANK -> _flankSetting.onNext(enabled)
                        else -> sonarWarningLog("unknown sonar group event received")
                    }
                }
            })
        } catch (e: RuntimeException) {
            wtfLog(
                "sonar",
                "couldn't register Sonar Group Listener : ${e.message}"
            )
        }
        try {
            sonarCarManager.registerSonarDisplayContextListener(object :
                SonarDisplayContextListener {
                override fun onClosingAuthorization(p0: Boolean) {
                    _closeAllowed.onNext(p0)
                }

                override fun onViewRequested(p0: Boolean, p1: Boolean, p2: Boolean) {
                    _displayRequest.onNext(Triple(p0, p1, p2))
                }
            })
        } catch (e: RuntimeException) {
            wtfLog(
                "sonar",
                "couldn't register Sonar Display Context Listener : ${e.message}"
            )
        }
        if (visualFeedbackConfig) {
            try {
                sonarCarManager.registerSonarVisualFeedbackListener(object :
                    SonarVisualFeedbackListener {
                    override fun onSonarError(p0: Int, p1: Int) {
                        _sonarError.onNext(p0 to p1)
                    }

                    override fun onSonarEvent(p0: Sonar) {
                        _sonarEvents[p0.sonarId]?.onNext(p0)
                    }
                })
            } catch (e: RuntimeException) {
                wtfLog(
                    "sonar",
                    "couldn't register Sonar Visual Feedback Listener : ${e.message}"
                )
            }
        }
        if (raebConfig) {
            try {
                sonarCarManager.registerRearAutoEmergencyBreakListener(object :
                    RearAutoEmergencyBreakListener {
                    override fun onRaebEvent(p0: Int) {
                        _raebEvent.onNext(p0)
                    }

                    override fun onRaebStatus(p0: Boolean) {
                        _raebStatus.onNext(p0)
                    }
                })
            } catch (e: RuntimeException) {
                wtfLog(
                    "sonar",
                    "couldn't register Sonar Auto Emergency Listener : ${e.message}"
                )
            }
        }
        if (rctaConfig) {
            try {
                sonarCarManager.registerCollisionAlertListener(object :
                    CollisionAlertListener {
                    override fun onEvent(p0: Int, p1: Int) {
                        _collisionAlertEvent.onNext(p0 to p1)
                    }

                    override fun onStatus(p0: Boolean) {
                        _collisionAlertStatus.onNext(p0)
                    }
                })
            } catch (e: RuntimeException) {
                wtfLog(
                    "sonar",
                    "couldn't register Collision Alert Listener : ${e.message}"
                )
            }
        }
    }

    fun enableCollisionAlert(enable: Boolean) {
        sonarCarManager.enableCollisionAlert(enable)
    }

    fun enableRearAutoEmergencyBreak(enable: Boolean) {
        sonarCarManager.enableRearAutoEmergencyBreak(enable)
    }

    fun setSonarGroup(sonarGroupId: Int, enable: Boolean) {
        sonarCarManager.enableSonarGroup(sonarGroupId, enable)
    }

    fun close() {
        sonarCarManager.close()
    }

    private fun readVehicleConfiguration() {
        if (useSysPropsVehicleConfig) {
            upaRearConfig = AndroidProperty.getBooleanSystemProperty(
                CFG_ADAS_UPA_Visual_Rear, DEFAULT_CFG_ADAS_UPA_Visual_Rear
            )
            upaFrontConfig = AndroidProperty.getBooleanSystemProperty(
                CFG_ADAS_UPA_Visual_Front, DEFAULT_CFG_ADAS_UPA_Visual_Front
            )
            fkpConfig =
                AndroidProperty.getBooleanSystemProperty(CFG_ADAS_FKP, DEFAULT_CFG_ADAS_FKP)
            visualFeedbackConfig = true
            rctaConfig =
                AndroidProperty.getBooleanSystemProperty(CFG_ADAS_RCTA, DEFAULT_CFG_ADAS_RCTA)
            raebConfig =
                AndroidProperty.getBooleanSystemProperty(CFG_ADAS_RAEB, DEFAULT_CFG_ADAS_RAEB)
        } else {
            val sonarCapabilities = sonarCarManager.sonarCapabilities
            upaRearConfig =
                isFeatureSupported(
                    FLAG_UPA,
                    sonarCapabilities.supportedCapabilities
                ) &&
                    sonarCapabilities.supportedGroupIds.contains(
                        SONAR_GROUP_REAR
                    )
            upaFrontConfig =
                isFeatureSupported(
                    FLAG_UPA,
                    sonarCapabilities.supportedCapabilities
                ) &&
                    sonarCapabilities.supportedGroupIds.contains(
                        SONAR_GROUP_FRONT
                    )
            fkpConfig =
                isFeatureSupported(
                    FLAG_UPA,
                    sonarCapabilities.supportedCapabilities
                ) &&
                    sonarCapabilities.supportedGroupIds.contains(
                        SONAR_GROUP_FLANK
                    )
            visualFeedbackConfig =
                isFeatureSupported(
                    FLAG_UPA_VISUAL_FEEDBACK,
                    sonarCapabilities.supportedCapabilities
                )
            rctaConfig =
                isFeatureSupported(
                    FLAG_RCTA,
                    sonarCapabilities.supportedCapabilities
                )
            raebConfig =
                isFeatureSupported(
                    FLAG_RAEB,
                    sonarCapabilities.supportedCapabilities
                )
            upaSettingConfig =
                isFeatureSupported(
                    FLAG_UPA_VISUAL_SETTING,
                    sonarCapabilities.supportedCapabilities
                )
        }
    }

    private fun isFeatureSupported(feature: Int, capabilities: Int): Boolean {
        return capabilities.and(feature) == feature
    }
}