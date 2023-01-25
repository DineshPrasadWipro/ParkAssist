package com.renault.parkassist.routing.policy

import com.renault.parkassist.repository.routing.PlatformPursuit
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier

class NoAdasPolicy : BasePolicy() {
    override fun requestStartPursuit(
        pursuit: Pursuit,
        maneuverAvailability: Int,
        trailerPresence: Int
    ): PlatformPursuit? = null

    override fun requestStopPursuit(
        pursuit: Pursuit,
        route: RouteIdentifier
    ): List<PlatformPursuit> = listOf(PlatformPursuit.MANEUVER)

    override fun requestScreen(
        parkingRoute: PlatformRouteIdentifier,
        sonarRoute: PlatformRouteIdentifier,
        surroundRoute: PlatformRouteIdentifier,
        surroundClosable: Boolean,
        apaTransition: Boolean
    ): RouteIdentifier = RouteIdentifier.NONE
}