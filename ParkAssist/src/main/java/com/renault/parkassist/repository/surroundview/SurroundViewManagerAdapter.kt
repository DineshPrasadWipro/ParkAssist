package com.renault.parkassist.repository.surroundview

import Quadruple
import alliance.car.surroundview.*
import androidx.core.os.TraceCompat.*
import com.renault.parkassist.utility.*
import com.renault.parkassist.utility.surround.viewToString
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import java.lang.RuntimeException
import java.util.function.Predicate

class SurroundViewManagerAdapter(
    private val carSurroundViewManager: AllianceCarSurroundViewManager
) :
    KoinComponent {
    private val _surroundState: BehaviorSubject<Quadruple<Int, Int, List<Int>, Int>> =
        BehaviorSubject.create()
    private val _requestedSurroundState: BehaviorSubject<Quadruple<Int, Int, List<Int>, Int>> =
        BehaviorSubject.create()
    // FIXME: CCSEXT-54592
    // In order not to break delicate logic and avoid regressions in Repository,
    // a new RX is added to handle routing properly.
    // _surroundState and _requestedSurroundState should be removed for a clean solution.
    private val _routingState: BehaviorSubject<Quadruple<Int, Int, List<Int>, Int>> =
        BehaviorSubject.create()
    private val _trunkState: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _brightness: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _color: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _contrast: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _autoZoomRearViewActivation: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _dynamicGuidelinesActivation: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _staticGuidelinesActivation: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _trailerGuidelineActivation: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _trailerPresence: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _warningState: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _errorState: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _maneuverAvailability: BehaviorSubject<Int> = BehaviorSubject.create()

    val surroundState: Observable<Quadruple<Int, Int, List<Int>, Int>> = _surroundState.hide()
    val requestedSurroundState: Observable<Quadruple<Int, Int, List<Int>, Int>> =
        _requestedSurroundState.hide()
    val routingState: Observable<Quadruple<Int, Int, List<Int>, Int>> = _routingState.hide()
    val trunkState: Observable<Int> = _trunkState.hide()
    val brightness: Observable<Int> = _brightness.hide()
    val color: Observable<Int> = _color.hide()
    val contrast: Observable<Int> = _contrast.hide()
    val autoZoomRearViewActivation: Observable<Boolean> = _autoZoomRearViewActivation.hide()
    val dynamicGuidelinesActivation: Observable<Boolean> = _dynamicGuidelinesActivation.hide()
    val staticGuidelinesActivation: Observable<Boolean> = _staticGuidelinesActivation.hide()
    val trailerGuidelineActivation: Observable<Boolean> = _trailerGuidelineActivation.hide()
    val trailerPresence: Observable<Int> = _trailerPresence.hide()
    val warningState: Observable<Int> = _warningState.hide()
    val errorState: Observable<Int> = _errorState.hide()
    val maneuverAvailability: Observable<Int> = _maneuverAvailability.hide()

    // Allows to read vehicle configuration from android system properties for debug purpose
    private var useSysPropsVehicleConfig: Boolean = AndroidProperty.getBooleanSystemProperty(
        CFG_ADAS_USE_SYSTEM_PROPS_SURROUND_CONFIG, DEFAULT_CFG_ADAS_USE_SYSTEM_PROPS_SURROUND_CONFIG
    )
    var featureConfig: Int = SurroundViewCapabilities.FEATURE_NOT_SUPPORTED
        private set
    var isStaticGuidelinesSettingSupported: Boolean = false
        private set
    var isDynamicGuidelinesSettingSupported: Boolean = false
        private set
    var isTrailerGuidelineSettingSupported: Boolean = false
        private set
    var isAutoZoomRearViewSettingSupported: Boolean = false
        private set
    var isCameraOnTrunk: Boolean = false
        private set
    var is3DViewSupported: Boolean = false
        private set
    var isPanoramicViewSupported: Boolean = false
        private set
    var isRegulationApplicable: Boolean = false
        private set
    var isTrailerViewSupported: Boolean = false
        private set
    var brightnessConfig: Boolean = true
        private set
    var brightnessMinConfig: Int = 0
        private set
    var brightnessMaxConfig: Int = 20
        private set
    var contrastConfig: Boolean = true
        private set
    var contrastMinConfig: Int = 0
        private set
    var contrastMaxConfig: Int = 20
        private set
    var colorConfig: Boolean = true
        private set
    var colorMinConfig: Int = 0
        private set
    var colorMaxConfig: Int = 20
        private set

    fun createGraphicLayer(p0: Any): GraphicLayer {
        return carSurroundViewManager.createGraphicLayer(p0)
    }

    fun releaseGraphicLayer(graphicLayer: GraphicLayer) {
        carSurroundViewManager.releaseGraphicLayer(graphicLayer)
    }

    fun screenPressPixels(p0: Int, p1: Float, p2: Float) {
        carSurroundViewManager.screenPressPixels(p0, p1, p2)
    }

    fun request(p0: Int) {
        carSurroundViewManager.request(p0)
    }

    fun screenRelease(p0: Int) {
        carSurroundViewManager.screenRelease(p0)
    }

    fun setGraphicLayerPosition(p0: Int, p1: Int, p2: Int, p3: Int) {
        carSurroundViewManager.setGraphicLayerPosition(p0, p1, p2, p3)
    }

    val capabilities: SurroundViewCapabilities = carSurroundViewManager.capabilities

    fun setStatus(viewState: Int) {
        carSurroundViewManager.setStatus(viewState)
    }

    fun acknowledgeWarning(userAck: Int) {
        carSurroundViewManager.acknowledgeWarning(userAck)
    }

    fun screenPress(finger: Int, x: Float, y: Float) {
        carSurroundViewManager.screenPressPixels(finger, x, y)
    }

    fun setBrightness(value: Int) {
        carSurroundViewManager.setBrightness(value)
    }

    fun setContrast(value: Int) {
        carSurroundViewManager.setContrast(value)
    }

    fun setColor(value: Int) {
        carSurroundViewManager.setColor(value)
    }

    fun setTrailerGuidelineActivation(value: Boolean) {
        carSurroundViewManager.setTrailerGuidelineActivation(value)

    }

    fun setAutoZoomRearViewActivation(value: Boolean) {
        carSurroundViewManager.setAutoZoomRearViewActivation(value)
    }

    fun setStaticGuidelinesActivation(value: Boolean) {
        carSurroundViewManager.setStaticGuidelinesActivation(value)
    }

    fun setDynamicGuidelinesActivation(value: Boolean) {
        carSurroundViewManager.setDynamicGuidelinesActivation(value)
    }

    init {
        readVehicleConfiguration()
        if (SurroundViewCapabilities.FEATURE_NOT_SUPPORTED != featureConfig) {
            try {
                carSurroundViewManager.registerStateListener(object : StateListener {
                    override fun onViewStateChange(
                        @SurroundView.ViewState state: Int,
                        origin: Int,
                        authorizedActions: List<Int>,
                        @SurroundView.DisplayMode displayMode: Int
                    ) {
                        surroundReceiveInfoLog("onViewState", state.viewToString())
                        _surroundState.onNext(
                            Quadruple(
                                state, origin,
                                authorizedActions, displayMode
                            )
                        )
                        _routingState.onNext(
                            Quadruple(
                                state, origin,
                                authorizedActions, displayMode
                            )
                        )
                    }

                    override fun onViewChangeRequest(
                        requested: Int,
                        @SurroundView.RequestOrigin origin: Int,
                        authorizedActions: List<Int>,
                        @SurroundView.DisplayMode displayMode: Int
                    ) {
                        surroundReceiveInfoLog("onViewRequest", requested.viewToString())
                        _requestedSurroundState.onNext(
                            Quadruple(
                                requested,
                                origin,
                                authorizedActions,
                                displayMode
                            )
                        )
                        _routingState.onNext(
                            Quadruple(
                                requested, origin,
                                authorizedActions, displayMode
                            )
                        )
                    }

                    override fun onWarningStateChange(@SurroundView.WarningState state: Int) {
                        _warningState.onNext(state)
                    }

                    override fun onErrorStateChange(@ErrorState state: Int) {
                        _errorState.onNext(state)
                    }

                    override fun onTrunkStateChange(@SurroundView.TrunkState state: Int) {
                        _trunkState.onNext(state)
                    }
                })
            } catch (e: RuntimeException) {
                wtfLog(
                    "surround",
                    "couldn't register state listener : ${e.message}"
                )
            }
            try {
                carSurroundViewManager.registerSettingsListener(object : SettingsListener {
                    override fun onBrightnessChange(value: Int) {
                        _brightness.onNext(value)
                    }

                    override fun onColorChange(value: Int) {
                        _color.onNext(value)
                    }

                    override fun onContrastChange(value: Int) {
                        _contrast.onNext(value)
                    }

                    override fun onAutoZoomRearViewActivationChange(value: Boolean) {
                        _autoZoomRearViewActivation.onNext(value)
                    }

                    override fun onDynamicGuidelinesActivationChange(value: Boolean) {
                        _dynamicGuidelinesActivation.onNext(value)
                    }

                    override fun onStaticGuidelinesActivationChange(value: Boolean) {
                        _staticGuidelinesActivation.onNext(value)
                    }

                    override fun onTrailerGuidelineActivationChange(value: Boolean) {
                        _trailerGuidelineActivation.onNext(value)
                    }
                })
            } catch (e: RuntimeException) {
                wtfLog(
                    "surround",
                    "couldn't register settings listener : ${e.message}"
                )
            }

            try {
                carSurroundViewManager.registerFeatureAvailabilityListener(object :
                    FeatureAvailabilityListener {
                    override fun onTrailerPresenceChange(
                        @SurroundView.TrailerPresence presence: Int
                    ) {
                        _trailerPresence.onNext(presence)
                    }

                    override fun onManeuverAvailabilityChange(p0: Int) {
                        _maneuverAvailability.onNext(p0)
                    }
                })
            } catch (e: RuntimeException) {
                wtfLog(
                    "surround",
                    "couldn't register feature availability listener : ${e.message}"
                )
            }
        }
    }

    private fun readVehicleConfigurationFromSysProps() {
        featureConfig = when {
            1 == AndroidProperty.getIntSystemProperty(CFG_ADAS_RVC, DEFAULT_CFG_ADAS_RVC)
            -> SurroundViewCapabilities.FEATURE_REAR_VIEW_CAMERA
            1 == AndroidProperty.getIntSystemProperty(CFG_ADAS_AVM_D, DEFAULT_CFG_ADAS_AVM_D)
            -> SurroundViewCapabilities.FEATURE_AROUND_VIEW_MONITORING
            else -> SurroundViewCapabilities.FEATURE_NOT_SUPPORTED
        }
        when (featureConfig) {
            SurroundViewCapabilities.FEATURE_REAR_VIEW_CAMERA -> {
                isStaticGuidelinesSettingSupported = AndroidProperty.getIntSystemProperty(
                    CFG_ADAS_RVC_STATIC_GUIDELINES, DEFAULT_CFG_ADAS_RVC_STATIC_GUIDELINES
                ) != 0
                isDynamicGuidelinesSettingSupported = AndroidProperty.getIntSystemProperty(
                    CFG_ADAS_RVC_DYNAMIC_GUIDELINES, DEFAULT_CFG_ADAS_RVC_DYNAMIC_GUIDELINES
                ) != 0
                isTrailerGuidelineSettingSupported = AndroidProperty.getIntSystemProperty(
                    CFG_ADAS_RVC_TRAILER_GUIDELINES, DEFAULT_CFG_ADAS_RVC_TRAILER_GUIDELINES
                ) != 0
                isAutoZoomRearViewSettingSupported = AndroidProperty.getIntSystemProperty(
                    CFG_ADAS_RVC_AUTO_ZOOM, DEFAULT_CFG_ADAS_RVC_AUTO_ZOOM
                ) != 0
            }
            SurroundViewCapabilities.FEATURE_AROUND_VIEW_MONITORING -> {
                isStaticGuidelinesSettingSupported = true
                isDynamicGuidelinesSettingSupported = true
                isTrailerGuidelineSettingSupported = AndroidProperty.getIntSystemProperty(
                    CFG_ADAS_AVM_TRAILER_GUIDELINES, DEFAULT_CFG_ADAS_AVM_TRAILER_GUIDELINES
                ) != 0
                isAutoZoomRearViewSettingSupported = AndroidProperty.getIntSystemProperty(
                    CFG_ADAS_AVM_AUTO_ZOOM, DEFAULT_CFG_ADAS_AVM_AUTO_ZOOM
                ) != 0
            }
            else ->
                wtfLog(
                    "surround",
                    "Incorrect surround vehicle configuration, received $featureConfig"
                )
        }
    }

    private fun readVehicleConfigurationFromService() {
        with(carSurroundViewManager) {
            featureConfig = capabilities.featureConfiguration
            if (featureConfig == SurroundViewCapabilities.FEATURE_NOT_SUPPORTED) return

            isStaticGuidelinesSettingSupported = capabilities.isStaticGuidelinesSettingSupported
            isDynamicGuidelinesSettingSupported = capabilities.isDynamicGuidelinesSettingSupported
            isTrailerGuidelineSettingSupported = capabilities.isTrailerGuidelineSettingSupported
            isAutoZoomRearViewSettingSupported = capabilities.isAutoZoomRearViewSettingSupported
            brightnessConfig = brightnessConfiguration.isSupported
            if (brightnessConfig) {
                brightnessMinConfig = brightnessConfiguration.min
                brightnessMaxConfig = brightnessConfiguration.max
            }
            contrastConfig = contrastConfiguration.isSupported
            if (contrastConfig) {
                contrastMinConfig = contrastConfiguration.min
                contrastMaxConfig = contrastConfiguration.max
            }
            colorConfig = colorConfiguration.isSupported
            if (colorConfig) {
                colorMinConfig = colorConfiguration.min
                colorMaxConfig = colorConfiguration.max
            }
            isCameraOnTrunk = capabilities.isCameraOnTrunk
            isRegulationApplicable = capabilities.isRegulationApplicable
            is3DViewSupported = capabilities.is3DViewSupported
            isPanoramicViewSupported = capabilities.isPanoramicViewSupported
            isTrailerViewSupported = capabilities.isTrailerViewSupported
        }
    }

    private fun readVehicleConfiguration() {
        if (useSysPropsVehicleConfig) readVehicleConfigurationFromSysProps()
        else readVehicleConfigurationFromService()
    }
}