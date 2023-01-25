package com.renault.parkassist.routing.pursuit

import alliancex.arch.core.logger.logD
import com.renault.parkassist.repository.routing.PlatformPursuit
import com.renault.parkassist.repository.routing.PlatformRoutingBridge
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.policy.IPolicy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.SerialDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

class PursuitManager : IPursuit,
    KoinComponent {

    private var currentRoute = RouteIdentifier.NONE

    private val serialDisposable = SerialDisposable()

    private val platformRouting: PlatformRoutingBridge by inject()
    private val policy: IPolicy by inject()
    private val pursuitHelper: PursuitHelper by inject()

    fun setRoute(screenRequest: Observable<RouteIdentifier>) {
        serialDisposable.set(screenRequest.subscribe {
            currentRoute = it
        })
    }

    override fun startPursuit(pursuit: Pursuit) {
        val platformPursuit = policy.requestStartPursuit(
            pursuit, platformRouting.maneuverAvailability.value,
            platformRouting.trailerPresence.value
        )
        logD { "startPursuit $pursuit requested : starts $platformPursuit" }
        platformPursuit?.let { this.platformRouting.startPursuit(it) }
    }

    override fun stopPursuit(pursuit: Pursuit) {
        val pursuitsToClose: List<PlatformPursuit> =
            policy.requestStopPursuit(pursuit, currentRoute)
        logD { "stopPursuit $pursuit requested : stops $pursuitsToClose" }
        pursuitsToClose.forEach { platformRouting.closePursuit(it) }
    }

    override fun stopCurrentPursuit() {
        val route = currentRoute
        val pursuit = pursuitHelper.routeToPursuit(route)
        logD { "stopCurrentPursuit (route=$route => pursuit=$pursuit)" }
        pursuit?.let { stopPursuit(it) }
    }
}