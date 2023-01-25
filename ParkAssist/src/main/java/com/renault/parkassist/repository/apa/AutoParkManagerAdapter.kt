package com.renault.parkassist.repository.apa

import alliance.car.autopark.*
import com.renault.parkassist.utility.wtfLog
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent

class AutoParkManagerAdapter(private val autoPark: AllianceCarAutoParkManager) :
    KoinComponent {

    private val _displayState: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _defaultManeuverType: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _statusDisplay: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _maneuverMove: BehaviorSubject<Int> = BehaviorSubject.create()
    private var _maneuverSelection: BehaviorSubject<Pair<Int, Int>> = BehaviorSubject.create()
    private val _scanningSide: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _warningMessage: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _maneuverCompletion: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _extendedInstruction: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _leftSuitable: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _rightSuitable: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _leftSelected: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _rightSelected: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _maneuverStartSwitch: BehaviorSubject<Int> = BehaviorSubject.create()
    private val _viewMask: BehaviorSubject<Int> = BehaviorSubject.create()

    val displayState: Observable<Int> = _displayState.hide()
    val defaultManeuverType: Observable<Int> = _defaultManeuverType.hide()
    val statusDisplay: Observable<Int> = _statusDisplay.hide()
    val maneuverMove: Observable<Int> = _maneuverMove.hide()
    val maneuverSelection: Observable<Pair<Int, Int>> =
        _maneuverSelection.hide().replay(3).autoConnect(0)
    val scanningSide: Observable<Int> = _scanningSide.hide()
    val warningMessage: Observable<Int> = _warningMessage.hide()
    val maneuverCompletion: Observable<Int> = _maneuverCompletion.hide()
    val extendedInstruction: Observable<Int> = _extendedInstruction.hide()
    val leftSuitable: Observable<Int> = _leftSuitable.hide()
    val rightSuitable: Observable<Int> = _rightSuitable.hide()
    val leftSelected: Observable<Int> = _leftSelected.hide()
    val rightSelected: Observable<Int> = _rightSelected.hide()
    val maneuverStartSwitch: Observable<Int> = _maneuverStartSwitch.hide()
    val viewMask: Observable<Int> = _viewMask.hide()

    val capabilities: AutoParkCapabilities = autoPark.capabilities

    var featureConfiguration = AutoPark.FEATURE_NONE
        private set

    fun requestManeuverType(maneuverType: Int) {
        autoPark.requestManeuverType(maneuverType)
    }

    fun setDefaultManeuverType(defaultManeuverType: Int) {
        autoPark.setDefaultManeuverType(defaultManeuverType)
    }

    fun switchManeuverStart() {
        autoPark.switchManeuverStart()
    }

    fun acknowledgeWarning(userAck: Int) {
        autoPark.acknowledgeWarning(userAck)
    }

    fun switchActivation(activate: Boolean) {
        autoPark.switchActivation(activate)
    }

    init {
        featureConfiguration = autoPark.capabilities.featureConfiguration
        if (featureConfiguration != AutoPark.FEATURE_NONE) {
            try {
                autoPark.registerStateListener(object : StateListener {
                    override fun onScanInfoChange(p0: Int) {
                        // nothing to do, use only for easypark assist gadget and launcher
                    }

                    override fun onDisplayStateChange(p0: Int) {
                        _displayState.onNext(p0)
                    }

                    override fun onStatusDisplayChange(p0: Int) {
                        _statusDisplay.onNext(p0)
                    }

                    override fun onManeuverMoveChange(p0: Int) {
                        _maneuverMove.onNext(p0)
                    }

                    override fun onParkingSlotRightSuitableChange(p0: Int) {
                        _rightSuitable.onNext(p0)
                    }

                    override fun onParkingSlotRightSelectedChange(p0: Int) {
                        _rightSelected.onNext(p0)
                    }

                    override fun onScanningSideChange(p0: Int) {
                        _scanningSide.onNext(p0)
                    }

                    override fun onParkingSlotLeftSelectedChange(p0: Int) {
                        _leftSelected.onNext(p0)
                    }

                    override fun onWarningMessageChange(p0: Int) {
                        _warningMessage.onNext(p0)
                    }

                    override fun onManeuverCompletionChange(p0: Int) {
                        _maneuverCompletion.onNext(p0)
                    }

                    override fun onExtendedInstructionChange(p0: Int) {
                        _extendedInstruction.onNext(p0)
                    }

                    override fun onManeuverSelectionChange(p0: Int, p1: Int) {
                        _maneuverSelection.onNext(Pair(p0, p1))
                    }

                    override fun onParkingSlotLeftSuitableChange(p0: Int) {
                        _leftSuitable.onNext(p0)
                    }

                    override fun onManeuverStartSwitchChange(p0: Int) {
                        _maneuverStartSwitch.onNext(p0)
                    }

                    override fun onViewMaskChange(p0: Int) {
                        _viewMask.onNext(p0)
                    }
                })
            } catch (e: Exception) {
                wtfLog("autopark",
                    "couldn't register state Listener : ${e.message}"
                )
            }
            try {
                autoPark.registerSettingsListener(object : SettingsListener {
                    override fun onBubbleActivationChange(p0: Boolean) {
                        // nothing to do, managed by Launcher and EasyPark gadget only
                    }

                    override fun onDefaultManeuverTypeChange(p0: Int) {
                        _defaultManeuverType.onNext(p0)
                    }
                })
            } catch (e: Exception) {
                wtfLog("autopark",
                    "couldn't register settings Listener : ${e.message}"
                )
            }
        }
    }
}