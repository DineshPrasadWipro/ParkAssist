package com.renault.parkassist.routing.mock

import alliance.car.surroundview.SurroundView
import com.renault.parkassist.repository.routing.ISurroundRouting
import com.renault.parkassist.repository.surroundview.ManeuverAvailability
import com.renault.parkassist.repository.surroundview.TrailerPresence
import io.reactivex.rxjava3.subjects.BehaviorSubject

class SurroundRoutingMock : ISurroundRouting {

    override val screenRequest: BehaviorSubject<ISurroundRouting.Request> =
        BehaviorSubject.createDefault(ISurroundRouting.Request(SurroundView.CLOSE_VIEW, true))

    override val dialogRequest: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    override val trailerPresence: BehaviorSubject<Int> =
        BehaviorSubject.createDefault(TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE)

    override val maneuverAvailability: BehaviorSubject<Int> =
        BehaviorSubject.createDefault(ManeuverAvailability.NOT_READY)

    override fun requestAction(action: Int) {
        requestedActions.add(action)
    }

    val requestedActions: MutableList<Int> = mutableListOf()

    val lastActionRequested: Int?
        get() = requestedActions.lastOrNull()

    fun reset() {
        requestedActions.clear()
        screenRequest.onNext(ISurroundRouting.Request(SurroundView.CLOSE_VIEW, true))
        dialogRequest.onNext(false)
        trailerPresence.onNext(TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE)
        maneuverAvailability.onNext(ManeuverAvailability.NOT_READY)
    }
}