package com.renault.parkassist.repository.apa

import alliance.car.autopark.AutoPark.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renault.parkassist.utility.*
import com.renault.parkassist.utility.apa.*
import com.renault.parkassist.viewmodel.apa.ApaWarningAcknowledgment
import org.koin.core.KoinComponent
import org.koin.core.inject

class ApaRepository : IApaRepository, KoinComponent {

    private val autoPark: AutoParkManagerAdapter by inject()
    private val apaMapper: ApaMapper by inject()

    private var _featureConfiguration = FeatureConfig.NONE
    private val _displayState = MutableLiveData<Int>(DISPLAY_NONE)
    private val _defaultManeuverType = MutableLiveData<Int>()
    private val _automaticManeuver = MutableLiveData<Boolean>()
    private val _maneuverMove = MutableLiveData<Int>()
    private var _maneuverSelection = MutableLiveData<Int>()
    private val _scanningSide = MutableLiveData<Int>()
    private val _warningMessage = MutableLiveData<Int>()
    private val _maneuverCompletion = MutableLiveData<Int>()
    private val _extendedInstruction = MutableLiveData<Int>()
    private val _leftSuitable = MutableLiveData<Boolean>()
    private val _rightSuitable = MutableLiveData<Boolean>()
    private val _leftSelected = MutableLiveData<Boolean>()
    private val _rightSelected = MutableLiveData<Boolean>()
    private val _maneuverParallelSelection = MutableLiveData<Int>()
    private val _maneuverPerpendicularSelection = MutableLiveData<Int>()
    private val _maneuverParkoutSelection = MutableLiveData<Int>()
    private val _maneuverStartSwitch = MutableLiveData<Int>()
    private val _viewMask = MutableLiveData<Int>()

    init {
        with(autoPark) {
            displayState.subscribe { state ->
                autoparkReceiveInfoLog("displayState", state.displayStateToString())
                _displayState.safeMapPost(
                    {
                        state.mapDisplayState()
                    },
                    {
                        autoparkWarningLog(
                            "Unexpected display state received:" +
                                " ${state.displayStateToString()}"
                        )
                    }
                )
            }
            leftSelected.subscribe { slot ->
                autoparkReceiveInfoLog(
                    "leftSlotSelected",
                    slot.parkingSlotLeftSelectedToString()
                )
                when (slot) {
                    SLOT_LEFT_SELECTED -> _leftSelected.postValue(true)
                    SLOT_LEFT_NOT_SELECTED -> _leftSelected.postValue(false)
                    else -> autoparkWarningLog("unknown left selected slot")
                }
            }
            rightSelected.subscribe { slot ->
                autoparkReceiveInfoLog(
                    "rightSlotSelected",
                    slot.parkingSlotRightSelectedToString()
                )
                when (slot) {
                    SLOT_RIGHT_SELECTED -> _rightSelected.postValue(true)
                    SLOT_RIGHT_NOT_SELECTED -> _rightSelected.postValue(false)
                    else -> autoparkWarningLog("unknown right selected slot")
                }
            }
            leftSuitable.subscribe { slot ->
                autoparkReceiveInfoLog(
                    "leftSlotSuitable",
                    slot.parkingSlotLeftSuitableToString()
                )
                when (slot) {
                    SLOT_LEFT_SUITABLE -> _leftSuitable.postValue(true)
                    SLOT_LEFT_NOT_SUITABLE -> _leftSuitable.postValue(false)
                    else -> autoparkWarningLog("unknown left suitable slot")
                }
            }
            rightSuitable.subscribe { slot ->
                autoparkReceiveInfoLog(
                    "rightSlotSuitable",
                    slot.parkingSlotRightSuitableToString()
                )
                when (slot) {
                    SLOT_RIGHT_SUITABLE -> _rightSuitable.postValue(true)
                    SLOT_RIGHT_NOT_SUITABLE -> _rightSuitable.postValue(false)
                    else -> autoparkWarningLog("unknown right suitable slot")
                }
            }
            statusDisplay.subscribe { status ->
                autoparkReceiveInfoLog("statusDisplay", status.statusDisplayToString())
                when (status) {
                    AUTOMATIC_MANEUVER_ON -> _automaticManeuver.postValue(true)
                    AUTOMATIC_MANEUVER_OFF -> _automaticManeuver.postValue(false)
                    else -> autoparkWarningLog("unknown status display")
                }
            }
            maneuverMove.subscribe { direction ->
                autoparkReceiveInfoLog("maneuverMove", direction.maneuverMoveToString())
                _maneuverMove.safeMapPost(
                    {
                        direction.mapManeuverMove()
                    }, {
                        autoparkWarningLog("unknown maneuver move")
                    }
                )
            }

            scanningSide.subscribe { side ->
                autoparkReceiveInfoLog("scanningSide", side.scanningSideToString())
                _scanningSide.safeMapPost(
                    {
                        side.mapScanningSide()
                    }, {
                        autoparkWarningLog("unknown scanning side")
                    }
                )
            }
            warningMessage.subscribe { message ->
                autoparkReceiveInfoLog("warningMessage", message.warningMessageToString())
                _warningMessage.safeMapPost(
                    {
                        message.mapWarningMessage()
                    }, {
                        autoparkWarningLog("unknown warning message")
                    }
                )
            }
            maneuverCompletion.subscribe { percent ->
                autoparkReceiveInfoLog("maneuverCompletion", percent)
                _maneuverCompletion.postValue(percent)
            }
            extendedInstruction.subscribe { instruction ->
                autoparkReceiveInfoLog(
                    "extendedInstruction",
                    instruction.extendedInstructionToString()
                )
                _extendedInstruction.safeMapPost(
                    {
                        instruction.mapInstruction()
                    }, {
                        autoparkWarningLog("unknown extended instruction")
                    }
                )
            }
            maneuverSelection.subscribe { (type, selection) ->
                autoparkReceiveInfoLog(
                    "maneuverSelection", listOf(
                        type.maneuverTypeToString(),
                        selection.maneuverSelectionToString()
                    )
                )

                when (type) {
                    MANEUVER_TYPE_PARKIN_PARALLEL ->
                        _maneuverParallelSelection.safeMapPost(
                            {
                                selection.mapManeuverSelection()
                            }, {
                                autoparkWarningLog("unknown selection type")
                            }
                        )
                    MANEUVER_TYPE_PARKIN_PERPENDICULAR ->
                        _maneuverPerpendicularSelection.safeMapPost(
                            {
                                selection.mapManeuverSelection()
                            }, {
                                autoparkWarningLog("unknown selection type")
                            }
                        )
                    MANEUVER_TYPE_PARKOUT ->
                        _maneuverParkoutSelection.safeMapPost(
                            {
                                selection.mapManeuverSelection()
                            }, {
                                autoparkWarningLog("unknown selection type")
                            }
                        )
                    else -> autoparkWarningLog("maneuver selection not supported")
                }
            }
            maneuverStartSwitch.subscribe { switch ->
                autoparkReceiveInfoLog(
                    "maneuverStartSwitch",
                    switch.maneuverStartSwitchToString()
                )
                _maneuverStartSwitch.safeMapPost(
                    {
                        switch.mapManeuverStartSwitch()
                    }, {
                        autoparkWarningLog("unknown maneuver start switch")
                    }
                )
            }

            defaultManeuverType.subscribe { type ->
                autoparkReceiveInfoLog("defaultManeuverType", type.maneuverTypeToString())
                _defaultManeuverType.safeMapPost(
                    {
                        type.mapManeuverType()
                    }, {
                        autoparkWarningLog("unknown default maneuver type")
                    }
                )
            }
            viewMask.subscribe { mask ->
                autoparkReceiveInfoLog("viewMask", mask.viewMaskToString())
                _viewMask.safeMapPost(
                    {
                        mask.mapViewMask()
                    }, {
                        autoparkWarningLog("unknown view mask")
                    }
                )
            }
        }

        autoparkInfoLog(
            "get",
            "featureConfig",
            _featureConfiguration.featureConfigurationToString()
        )
    }

    override fun getFeatureConfiguration(): Int =
        catchMapError(
            { autoPark.featureConfiguration.mapFeatureConfig() }, {
                autoparkErrorLog("unknown fature configuration", "fallback to feature NONE")
                FeatureConfig.NONE
            }
        )

    override fun getSupportedManeuvers(): List<Int> =
        autoPark.capabilities.supportedManeuverTypes.map {
            catchMapError({ it.mapManeuverType() }, {
                autoparkWarningLog("unknown maneuver type")
                null
            })
        }.filterNotNull()

    override fun getLeftSuitable(): LiveData<Boolean> = _leftSuitable
    override fun getLeftSelected(): LiveData<Boolean> = _leftSelected
    override fun getRightSuitable(): LiveData<Boolean> = _rightSuitable
    override fun getRightSelected(): LiveData<Boolean> = _rightSelected
    override fun getDisplayState(): LiveData<Int> = _displayState
    override fun getExtendedInstruction(): LiveData<Int> = _extendedInstruction
    override fun getAutomaticManeuver(): LiveData<Boolean> = _automaticManeuver
    override fun getDefaultManeuverType(): LiveData<Int> = _defaultManeuverType
    override fun getManeuverCompletion(): LiveData<Int> = _maneuverCompletion
    override fun getWarningMessage(): LiveData<Int> = _warningMessage
    override fun getManeuverMove(): LiveData<Int> = _maneuverMove
    override fun getScanningSide(): LiveData<Int> = _scanningSide
    override fun getManeuverSelection(): LiveData<Int> = _maneuverSelection
    override fun getManeuverSwitchSelection(): LiveData<Int> = _maneuverStartSwitch
    override fun getViewMask(): LiveData<Int> = _viewMask

    override fun getParallelManeuverSelection(): LiveData<Int> = _maneuverParallelSelection
    override fun getPerpendicularManeuverSelection(): LiveData<Int> =
        _maneuverPerpendicularSelection

    override fun getParkOutManeuverSelection(): LiveData<Int> = _maneuverParkoutSelection

    override fun requestManeuverType(@ManeuverType maneuverType: Int) = catchMapError(
        {
            maneuverType.unmapManeuverType().let {
                autoPark.requestManeuverType(it)
                autoparkSendInfoLog("maneuverType", it)
            }
        }, {
            autoparkErrorLog("internal error when requesting maneuver type")
        })

    override fun setDefaultManeuverType(@ManeuverType defaultManeuverType: Int) = catchMapError(
        {
            defaultManeuverType.unmapManeuverType().let {
                autoPark.setDefaultManeuverType(it)
                autoparkSendInfoLog("defaultManeuverType", it)
            }
        }, {
            autoparkErrorLog("internal error requesting default maneuver")
        }
    )

    override fun switchManeuverStartActivation() {
        autoPark.switchManeuverStart()
        autoparkSendInfoLog("switchManeuverStart")
    }

    override fun acknowledgeWarning(@ApaWarningAcknowledgment userAck: Int) = catchMapError(
        {
            userAck.unMapWarningAck().let {
                autoPark.acknowledgeWarning(it)
                autoparkSendInfoLog("acknowledgeWarning", it)
            }
        }, {
            autoparkErrorLog("internal error during warning acknowledgement")
        }
    )

    private fun Int.mapScanningSide() = apaMapper.mapScanningSide(this)
    private fun Int.mapInstruction() = apaMapper.mapInstruction(this)
    private fun Int.mapManeuverType() = apaMapper.mapManeuverType(this)
    private fun Int.mapManeuverMove() = apaMapper.mapManeuverMove(this)
    private fun Int.mapManeuverStartSwitch() = apaMapper.mapManeuverStartSwitch(this)
    private fun Int.mapFeatureConfig() = apaMapper.mapFeatureConfig(this)
    private fun Int.mapManeuverSelection() = apaMapper.mapManeuverSelection(this)
    private fun Int.unmapManeuverType() = apaMapper.unmapManeuverType(this)
    private fun Int.mapDisplayState() = apaMapper.mapDisplayState(this)
    private fun Int.mapWarningMessage() = apaMapper.mapApaViewModelWarningId(this)
    private fun Int.unMapWarningAck() = apaMapper.unMapApaViewModelAck(this)
    private fun Int.mapViewMask() = apaMapper.mapViewMask(this)
}