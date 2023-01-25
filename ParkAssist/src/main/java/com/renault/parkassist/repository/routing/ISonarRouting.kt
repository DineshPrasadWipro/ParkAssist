package com.renault.parkassist.repository.routing

import io.reactivex.rxjava3.subjects.BehaviorSubject

interface ISonarRouting {
    data class Request(val rear: Boolean, val front: Boolean, val flank: Boolean)

    val closable: BehaviorSubject<Boolean>
    val request: BehaviorSubject<Request>

    fun close()
}