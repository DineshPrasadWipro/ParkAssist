package com.renault.parkassist.routing.mock

import alliance.car.autopark.AutoPark
import com.renault.parkassist.repository.routing.IAutoParkRouting
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ApaRoutingMock : IAutoParkRouting {
    override val screenRequest: BehaviorSubject<Int> =
        BehaviorSubject.createDefault(AutoPark.DISPLAY_NONE)

    override val dialogRequest: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    override val isTransitionFromScanningToGuidance: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    override fun switchActivation(activate: Boolean) {
        switchActivation = activate
    }

    var switchActivation: Boolean? = null

    fun reset() {
        screenRequest.onNext(AutoPark.DISPLAY_NONE)
        dialogRequest.onNext(false)
        switchActivation = null
        isTransitionFromScanningToGuidance.onNext(false)
    }
}