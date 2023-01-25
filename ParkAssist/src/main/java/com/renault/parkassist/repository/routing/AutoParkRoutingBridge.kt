package com.renault.parkassist.repository.routing

import alliance.car.autopark.AutoPark
import alliance.car.autopark.AutoPark.MESSAGE_NONE
import com.renault.parkassist.repository.apa.AutoParkManagerAdapter
import com.renault.parkassist.utility.apa.displayStateToString
import com.renault.parkassist.utility.apa.extendedInstructionToString
import com.renault.parkassist.utility.apa.warningMessageToString
import com.renault.parkassist.utility.routingReceiveInfoLog
import com.renault.parkassist.utility.routingSendInfoLog
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class AutoParkRoutingBridge : IAutoParkRouting,
    KoinComponent {

    private val autoParkManager: AutoParkManagerAdapter by inject()
    override val screenRequest: BehaviorSubject<Int> =
        BehaviorSubject.createDefault(AutoPark.DISPLAY_NONE)
    override val dialogRequest: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)
    override val isTransitionFromScanningToGuidance: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    init {
        with(autoParkManager) {
            displayState.subscribe { state ->
                routingReceiveInfoLog(
                    "displayState",
                    state.displayStateToString()
                )
                screenRequest.onNext(state)
            }
            warningMessage.subscribe { message ->
                routingReceiveInfoLog(
                    "warningMessage",
                    message.warningMessageToString()
                )
                dialogRequest.onNext(message != MESSAGE_NONE)
            }
            extendedInstruction.subscribe { instruction ->
                routingReceiveInfoLog(
                    "extendedInstruction",
                    instruction.extendedInstructionToString()
                )
                isTransitionFromScanningToGuidance.onNext(
                    instruction == AutoPark.EXT_INSTRUCTION_ENGAGE_REAR_GEAR ||
                        instruction == AutoPark.EXT_INSTRUCTION_ENGAGE_REAR_GEAR_OR_PRESS_START
                )
            }
        }
    }

    override fun switchActivation(activate: Boolean) {
        autoParkManager.switchActivation(activate)
        routingSendInfoLog(
            "switchActivation",
            activate
        )
    }
}