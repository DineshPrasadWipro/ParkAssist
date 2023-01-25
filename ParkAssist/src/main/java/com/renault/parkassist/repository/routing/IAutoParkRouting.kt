package com.renault.parkassist.repository.routing

import io.reactivex.rxjava3.subjects.BehaviorSubject

interface IAutoParkRouting {
    val screenRequest: BehaviorSubject</*@AutoPark.DisplayState*/ Int>
    val dialogRequest: BehaviorSubject<Boolean>
    val isTransitionFromScanningToGuidance: BehaviorSubject<Boolean>
    fun switchActivation(activate: Boolean)
}