package com.renault.parkassist.routing.mock

import com.renault.parkassist.repository.routing.ISonarRouting
import io.reactivex.rxjava3.subjects.BehaviorSubject

class SonarRoutingMock : ISonarRouting {
    override val closable: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(true)

    override val request: BehaviorSubject<ISonarRouting.Request> =
        BehaviorSubject.createDefault(
            ISonarRouting.Request(rear = false, front = false, flank = false)
        )

    override fun close() {
        isClosed = true
    }

    var isClosed = false

    fun reset() {
        request.onNext(ISonarRouting.Request(rear = false, front = false, flank = false))
        closable.onNext(true)
    }
}