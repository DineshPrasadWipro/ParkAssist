package com.renault.parkassist.repository.routing

import alliance.car.surroundview.SurroundView
import io.reactivex.rxjava3.subjects.BehaviorSubject

interface ISurroundRouting {
    data class Request(
        @SurroundView.ViewState val state: Int,
        val closable: Boolean,
        @SurroundView.DisplayMode val displayMode: Int = SurroundView.DISPLAY_MODE_VISIBLE
    )

    val screenRequest: BehaviorSubject<Request>
    val dialogRequest: BehaviorSubject<Boolean>
    val trailerPresence: BehaviorSubject<Int>
    val maneuverAvailability: BehaviorSubject<Int>
    fun requestAction(@SurroundView.Action action: Int)
}