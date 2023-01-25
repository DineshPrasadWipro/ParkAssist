package com.renault.parkassist.repository.routing

import alliance.car.surroundview.SurroundView
import alliance.car.surroundview.SurroundView.WARNING_STATE_NONE
import com.renault.parkassist.repository.surroundview.*
import com.renault.parkassist.utility.*
import com.renault.parkassist.utility.surround.*
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class SurroundRoutingBridge : KoinComponent, ISurroundRouting {

    private val manager: SurroundViewManagerAdapter by inject()
    override val screenRequest: BehaviorSubject<ISurroundRouting.Request> =
        BehaviorSubject.createDefault(
            ISurroundRouting.Request(
                SurroundView.CLOSE_VIEW,
                true,
                SurroundView.DISPLAY_MODE_HIDDEN
            )
        )

    override val dialogRequest: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)
    override val trailerPresence: BehaviorSubject<Int> =
        BehaviorSubject.createDefault(TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE)
    override val maneuverAvailability: BehaviorSubject<Int> =
        BehaviorSubject.createDefault(ManeuverAvailability.NOT_READY)

    private val surroundMapper: SurroundMapper by inject()
    private fun Int.mapTrailerPresence() = surroundMapper.mapTrailerPresence(this)
    private fun Int.mapManeuverAvailability() = surroundMapper.mapManeuverAvailability(this)
    private fun Int.unmapAction() = surroundMapper.unmapAction(this)

    override fun requestAction(@Action action: Int) = catchMapError(
        {
            action.unmapAction().let {
                manager.request(it)
                routingSendInfoLog(
                    "action",
                    it.actionToString()
                )
            }
        }, {
            routingErrorLog(
                "internal error sending action"
            )
        }
    )

    private fun registerStateListener() {
        with(manager) {
            routingState.subscribe { (state, _, authorizedActions, displayMode) ->
                val closable = authorizedActions.contains(SurroundView.CLOSE_VIEW)

                routingReceiveInfoLog(
                    "state",
                    listOf(
                        state.viewToString(),
                        authorizedActions.map { it.actionToString() },
                        displayMode.displayModeToString()
                    )
                )
                screenRequest.onNext(ISurroundRouting.Request(state, closable, displayMode))
            }

            warningState.subscribe { state ->
                routingInfoLog(
                    "requestedState",
                    state.warningStateToString()
                )
                dialogRequest.onNext(state != WARNING_STATE_NONE)
            }
        }
    }

    private fun registerFeatureAvailabilityListener() {
        with(manager) {
            trailerPresence.subscribe {
                routingReceiveInfoLog(
                    "trailerPresence",
                    it.trailerPresenceToString()
                )
                this@SurroundRoutingBridge.trailerPresence.safeMapOnNext(
                    {
                        it.mapTrailerPresence()
                    }, {
                        routingWarningLog(
                            "unknown trailer presence"
                        )
                    }
                )
            }
            maneuverAvailability.subscribe {
                routingReceiveInfoLog(
                    "maneuverAvailability",
                    it.availabilityStateToString()
                )
                this@SurroundRoutingBridge.maneuverAvailability.safeMapOnNext(
                    {
                        it.mapManeuverAvailability()
                    }, {
                        routingWarningLog(
                            "unknown trailer presence"
                        )
                    }
                )
            }
        }
    }

    init {
        registerStateListener()
        registerFeatureAvailabilityListener()
    }
}