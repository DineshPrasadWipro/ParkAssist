package com.renault.parkassist.repository.surroundview

import alliance.car.surroundview.GraphicLayer
import alliance.car.surroundview.SurroundView.Action
import alliancex.arch.core.logger.logD
import android.view.Surface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renault.parkassist.utility.*
import com.renault.parkassist.utility.surround.*
import com.renault.parkassist.viewmodel.avm.Finger
import org.koin.core.KoinComponent
import org.koin.core.inject

class SurroundViewRepository : IExtSurroundViewRepository, KoinComponent {

    private val carSurroundViewManagerAdapter: SurroundViewManagerAdapter by inject()

    private val surroundMapper: SurroundMapper by inject()

    private val _surroundState = MutableLiveData<SurroundState>()
    private val _authorizedActions = MutableLiveData<List<Int>>()
    private val _trunkState = MutableLiveData<Int>()
    private val _brightness = MutableLiveData<Int>()
    private val _color = MutableLiveData<Int>()
    private val _contrast = MutableLiveData<Int>()
    private val _autoZoomRearViewActivation = MutableLiveData<Boolean>()
    private val _dynamicGuidelinesActivation = MutableLiveData<Boolean>()
    private val _staticGuidelinesActivation = MutableLiveData<Boolean>()
    private val _trailerGuidelineActivation = MutableLiveData<Boolean>()
    private val _trailerPresence = MutableLiveData<Int>()
    private val _warningState = MutableLiveData<Int>()
    private val _errorState = MutableLiveData<Int>()

    init {
        logD { "Register service listeners" }
        registerStateListener()
        registerSettingsListener()
        logVehicleConfiguration()
        // FIXME Test purpose only
        _trailerPresence.value = AndroidProperty.getIntSystemProperty(
            CFG_ADAS_TRAILER_PRESENCE,
            DEFAULT_CFG_ADAS_TRAILER_PRESENCE
        )
    }

    private fun registerSettingsListener() {
        with(carSurroundViewManagerAdapter) {
            brightness.subscribe {
                surroundReceiveInfoLog("brightness", it)
                _brightness.postValue(it)
            }
            color.subscribe {
                surroundReceiveInfoLog("color", it)
                _color.postValue(it)
            }
            contrast.subscribe {
                surroundReceiveInfoLog("contrast", it)
                _contrast.postValue(it)
            }
            autoZoomRearViewActivation.subscribe {
                surroundReceiveInfoLog("autoZoomRearViewActivation", it)
                _autoZoomRearViewActivation.postValue(it)
            }
            dynamicGuidelinesActivation.subscribe {
                surroundReceiveInfoLog("dynamicGuidelinesActivation", it)
                _dynamicGuidelinesActivation.postValue(it)
            }
            staticGuidelinesActivation.subscribe {
                surroundReceiveInfoLog("staticGuidelinesActivation", it)
                _staticGuidelinesActivation.postValue(it)
            }
            trailerGuidelineActivation.subscribe {
                surroundReceiveInfoLog("trailerGuidelineActivation", it)
                _trailerGuidelineActivation.postValue(it)
            }
        }
    }

    private fun registerStateListener() {
        with(carSurroundViewManagerAdapter) {
            requestedSurroundState
                .filter { (view, _, _, _) ->
                    !(_surroundState.value?.view?.unMapViewState() == view &&
                        _surroundState.value?.isRequest == false)
                }.map { (view, _, _, _) ->
                    surroundReceiveInfoLog("viewRequest", view.viewToString())
                    mapSurroundState(view, true)
                }
                .mergeWith(surroundState
                    .map { (view, _, _, _) ->
                        surroundReceiveInfoLog("viewState", view.viewToString())
                        mapSurroundState(view, false)
                    })
                .filter { it != null }
                .subscribe { surroundState ->
                    _surroundState.value = surroundState
                }

            surroundState
                .mergeWith(requestedSurroundState)
                .subscribe { (_, _, authorizedActions, _) ->
                    surroundReceiveInfoLog(
                        "authorizedActions",
                        authorizedActions.map { it.actionToString() }
                    )
                    _authorizedActions.postValue(authorizedActions.mapNotNull { action ->
                        catchMapError(
                            {
                                action.mapAction()
                            }, {
                                surroundWarningLog(
                                    "Unsupported action: ${action.actionToString()}"
                                )
                                null
                            }
                        )
                    })
                }

            warningState.subscribe {
                surroundReceiveInfoLog("warningState", it.warningStateToString())
                _warningState.safeMapPost(
                    {
                        it.mapWarningState()
                    }, {
                        surroundWarningLog(
                            "Unsupported warning state ${it.warningStateToString()}"
                        )
                    }
                )
            }
            errorState.subscribe {
                surroundReceiveInfoLog("errorState", it.errorStateToString())
                _errorState.safeMapPost(
                    {
                        it.mapErrorState()
                    }, {
                        surroundWarningLog(
                            "Unsupported error state ${it.errorStateToString()}"
                        )
                    }
                )
            }

            trailerPresence.subscribe { presence ->
                surroundReceiveInfoLog(
                    "trailerPresence",
                    presence.trailerPresenceToString()
                )
                _trailerPresence.safeMapPost(
                    {
                        presence.mapTrailerPresence()
                    },
                    {
                        surroundWarningLog(
                            "Unexpected trailer presence ${presence.trailerPresenceToString()}"
                        )
                    }
                )
            }

            trunkState.subscribe { state ->
                surroundReceiveInfoLog(
                    "trunkState",
                    state.trunkStateToString()
                )
                _trunkState.safeMapPost(
                    {
                        state.mapTrunkState()
                    },
                    {
                        surroundWarningLog(
                            "Unexpected trunk state ${state.trunkStateToString()}"
                        )
                    }
                )
            }
        }
    }

    private fun mapSurroundState(
        state: Int,
        isRequest: Boolean
    ): SurroundState? = catchMapError(
        {
            surroundMapper.mapSurroundState(state, isRequest)
        }, {
            surroundWarningLog(
                "Unexpected avm state received: state ${state.viewToString()}"
            )
            null
        }
    )

    override fun getSurroundState(): LiveData<SurroundState> = _surroundState

    override fun getBrightness(): LiveData<Int> = _brightness

    override fun getAuthorizedActions(): LiveData<List<Int>> = _authorizedActions

    override fun getColor(): LiveData<Int> = _color

    override fun getContrast(): LiveData<Int> = _contrast

    override fun getStaticGuidelinesActivation(): LiveData<Boolean> = _staticGuidelinesActivation

    override fun getDynamicGuidelinesActivation(): LiveData<Boolean> = _dynamicGuidelinesActivation

    override fun getTrailerGuidelinesActivation(): LiveData<Boolean> = _trailerGuidelineActivation

    override fun getAutoZoomRearViewActivation(): LiveData<Boolean> = _autoZoomRearViewActivation

    override fun getTrunkState(): LiveData<Int> = _trunkState

    override fun getWarningState(): LiveData<Int> = _warningState

    override fun getErrorState(): LiveData<Int> = _errorState

    override fun getTrailerPresence(): LiveData<Int> = _trailerPresence

    override fun createGraphicLayer(surface: Surface): GraphicLayer {
        surroundSendInfoLog("createGraphicLayer")
        return carSurroundViewManagerAdapter.createGraphicLayer(surface)
    }

    override fun releaseGraphicLayer(graphicLayer: GraphicLayer) {
        surroundSendInfoLog("releaseGraphicLayer")
        carSurroundViewManagerAdapter.releaseGraphicLayer(graphicLayer)
    }

    override fun request(@Action action: Int) = catchMapError(
        {
            action.unMapAction().let {
                carSurroundViewManagerAdapter.request(it)
                surroundSendInfoLog("action", it.actionToString())
            }
        },
        {
            surroundErrorLog("internal error sending request action")
        }
    )

    override fun setStatus(viewState: Int) = catchMapError(
        {
            viewState.unMapViewState().let {
                carSurroundViewManagerAdapter.setStatus(it)
                surroundSendInfoLog("viewState", it.viewToString())
            }
        }, {
            surroundErrorLog("internal error sending view status")
        }
    )

    override fun acknowledgeWarning(userAck: Int) = catchMapError(
        {
            userAck.unmapAck().let {
                carSurroundViewManagerAdapter.acknowledgeWarning(it)
                surroundSendInfoLog(
                    "userAcknowledgement",
                    it.userAcknowledgementToString()
                )
            }
        }, {
            surroundErrorLog("internal error sending acknowledge warning")
        }
    )

    override fun setCameraPosition(x0: Int, y0: Int, x1: Int, y1: Int) {
        carSurroundViewManagerAdapter.setGraphicLayerPosition(x0, y0, x1, y1)
    }

    override fun screenPress(@Finger finger: Int, x: Float, y: Float) = catchMapError(
        {
            finger.unMapFinger().let {
                carSurroundViewManagerAdapter.screenPressPixels(it, x, y)
                surroundSendInfoLog(
                    "screenPressPixels",
                    listOf(it.fingerTouchToString(), x, y)
                )
            }
        },
        {
            surroundErrorLog("internal error sending screen press pixels")
        }
    )

    override fun screenRelease(@Finger finger: Int) = catchMapError(
        {
            finger.unMapFinger().let {
                carSurroundViewManagerAdapter.screenRelease(it)
                surroundSendInfoLog("screenRelease", it.fingerTouchToString())
            }
        },
        {
            surroundErrorLog("internal error sending screen release")
        }
    )

    override fun setBrightness(value: Int) {
        carSurroundViewManagerAdapter.setBrightness(value)
        surroundSendInfoLog("brightness", value)
    }

    override fun setContrast(value: Int) {
        carSurroundViewManagerAdapter.setContrast(value)
        surroundSendInfoLog("contrast", value)
    }

    override fun setColor(value: Int) {
        carSurroundViewManagerAdapter.setColor(value)
        surroundSendInfoLog("color", value)
    }

    override fun setTrailerGuidelinesActivation(value: Boolean) {
        carSurroundViewManagerAdapter.setTrailerGuidelineActivation(value)
        surroundSendInfoLog("trailerGuidelinesActivation", value)
    }

    override fun setAutoZoomRearViewActivation(value: Boolean) {
        carSurroundViewManagerAdapter.setAutoZoomRearViewActivation(value)
        surroundSendInfoLog("autoZoomRear", value)
    }

    override fun setStaticGuidelinesActivation(value: Boolean) {
        carSurroundViewManagerAdapter.setStaticGuidelinesActivation(value)
        surroundSendInfoLog("staticGuideLinesActivation", value)
    }

    override fun setDynamicGuidelinesActivation(value: Boolean) {
        carSurroundViewManagerAdapter.setDynamicGuidelinesActivation(value)
        surroundSendInfoLog("dynamicGuidelinesActivation", value)
    }

    override fun getFeatureConfig(): Int = catchMapError(
        {
            carSurroundViewManagerAdapter.featureConfig.mapConfig()
        },
        {
            surroundErrorLog(
                "unknown feature config ${carSurroundViewManagerAdapter.featureConfig}",
                "fallback to config NONE"
            )
            FeatureConfig.NONE
        }
    )

    override fun getIsBrightnessSupported(): Boolean =
        carSurroundViewManagerAdapter.brightnessConfig

    override fun getBrightnessMin(): Int = carSurroundViewManagerAdapter.brightnessMinConfig
    override fun getBrightnessMax(): Int = carSurroundViewManagerAdapter.brightnessMaxConfig
    override fun getIsContrastSupported(): Boolean = carSurroundViewManagerAdapter.contrastConfig
    override fun getContrastMin(): Int = carSurroundViewManagerAdapter.contrastMinConfig
    override fun getContrastMax(): Int = carSurroundViewManagerAdapter.contrastMaxConfig
    override fun getIsColorSupported(): Boolean = carSurroundViewManagerAdapter.colorConfig
    override fun getColorMin(): Int = carSurroundViewManagerAdapter.colorMinConfig
    override fun getColorMax(): Int = carSurroundViewManagerAdapter.colorMaxConfig
    override fun getIsStaticGuidelinesSupported(): Boolean =
        carSurroundViewManagerAdapter.isStaticGuidelinesSettingSupported

    override fun getIsDynamicGuidelinesSupported(): Boolean =
        carSurroundViewManagerAdapter.isDynamicGuidelinesSettingSupported

    override fun getIsTrailerGuidelinesSupported(): Boolean =
        carSurroundViewManagerAdapter.isTrailerGuidelineSettingSupported

    override fun getIsAutoZoomSupported(): Boolean =
        carSurroundViewManagerAdapter.isAutoZoomRearViewSettingSupported

    override fun getIs3DViewSupported(): Boolean = carSurroundViewManagerAdapter.is3DViewSupported
    override fun getIsPanoramicViewSupported(): Boolean =
        carSurroundViewManagerAdapter.isPanoramicViewSupported

    override fun getIsTrailerViewSupported(): Boolean =
        carSurroundViewManagerAdapter.isTrailerViewSupported

    override fun getIsCameraOnTrunk(): Boolean = carSurroundViewManagerAdapter.isCameraOnTrunk
    override fun getIsRegulationApplicable(): Boolean =
        carSurroundViewManagerAdapter.isRegulationApplicable

    private fun logVehicleConfiguration() {
        surroundInfoLog(
            "get", "feature",
            "${carSurroundViewManagerAdapter.featureConfig}"
        )
        surroundInfoLog(
            "get",
            "staticGuidelinePresent",
            "${carSurroundViewManagerAdapter.isStaticGuidelinesSettingSupported}"
        )
        surroundInfoLog(
            "get",
            "dynamicGuidelinePresent",
            "${carSurroundViewManagerAdapter.isDynamicGuidelinesSettingSupported}"
        )
        surroundInfoLog(
            "get",
            "trailerGuidelinePresent",
            "${carSurroundViewManagerAdapter.isTrailerGuidelineSettingSupported}"
        )
        surroundInfoLog(
            "get",
            "autoZoomViewPresent",
            "${carSurroundViewManagerAdapter.isAutoZoomRearViewSettingSupported}"
        )
        surroundInfoLog(
            "get",
            "3DViewPresent",
            "${carSurroundViewManagerAdapter.is3DViewSupported}"
        )
        surroundInfoLog(
            "get",
            "tanoramicViewPresent",
            "${carSurroundViewManagerAdapter.is3DViewSupported}"
        )
        surroundInfoLog(
            "get",
            "trailerViewPresent",
            "${carSurroundViewManagerAdapter.is3DViewSupported}"
        )
        surroundInfoLog(
            "get",
            "regulationApplicable",
            "${carSurroundViewManagerAdapter.isRegulationApplicable}"
        )
        surroundInfoLog(
            "get",
            "cameraOnTrunk",
            "${carSurroundViewManagerAdapter.isCameraOnTrunk}"
        )
        surroundInfoLog(
            "get",
            "brightnessFeature",
            "${carSurroundViewManagerAdapter.brightnessConfig}"
        )
        surroundInfoLog(
            "get",
            "contrastFeature",
            "${carSurroundViewManagerAdapter.contrastConfig}"
        )
        surroundInfoLog(
            "get",
            "colorFeature",
            "${carSurroundViewManagerAdapter.colorConfig}"
        )
    }

    private fun Int.mapTrailerPresence() = surroundMapper.mapTrailerPresence(this)
    private fun Int.mapTrunkState() = surroundMapper.mapTrunkState(this)
    private fun Int.mapWarningState() = surroundMapper.mapWarningState(this)
    private fun Int.unmapAck() = surroundMapper.unmapAck(this)
    private fun Int.unMapViewState() = surroundMapper.unmapView(this)
    private fun Int.mapAction() = surroundMapper.mapAction(this)
    private fun Int.unMapAction() = surroundMapper.unmapAction(this)
    private fun Int.unMapFinger() = surroundMapper.unmapFinger(this)
    private fun Int.mapConfig() = surroundMapper.mapConfig(this)
    private fun Int.mapErrorState() = surroundMapper.mapErrorState(this)
}