package com.renault.parkassist.repository.routing

import com.renault.parkassist.repository.sonar.SonarCarManagerAdapter
import com.renault.parkassist.utility.routingReceiveInfoLog
import com.renault.parkassist.utility.routingSendInfoLog
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class SonarRoutingBridge : ISonarRouting,
    KoinComponent {

    private val manager: SonarCarManagerAdapter by inject()

    override val closable: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(true)
    override val request: BehaviorSubject<ISonarRouting.Request> =
        BehaviorSubject.createDefault(
            ISonarRouting.Request(rear = false, front = false, flank = false)
        )

    init {
        with(manager) {
            closeAllowed.subscribe {
                routingReceiveInfoLog(
                    "closeAuthorization",
                    it
                )
                closable.onNext(it)
            }
            displayRequest.subscribe { (isRear, isFront, isFlank) ->
                routingReceiveInfoLog(
                    "sonarDisplayRequest",
                    listOf(isRear, isFront, isFlank)
                )
                request.onNext(ISonarRouting.Request(isRear, isFront, isFlank))
            }
        }
    }

    override fun close() {
        manager.close()
        routingSendInfoLog(
            "sonarClose"
        )
    }
}