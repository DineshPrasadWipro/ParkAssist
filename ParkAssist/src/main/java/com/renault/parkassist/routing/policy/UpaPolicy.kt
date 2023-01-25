package com.renault.parkassist.routing.policy

import com.renault.parkassist.repository.routing.PlatformPursuit
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier

class UpaPolicy : BasePolicy() {

    override fun requestSonarRoute(sonar: PlatformRouteIdentifier) =
        when (sonar) {
            PlatformRouteIdentifier.SONAR_REAR,
            PlatformRouteIdentifier.SONAR_NOT_REAR -> RouteIdentifier.SONAR_POPUP
            else -> RouteIdentifier.NONE
        }

    override fun requestStartPursuit(
        pursuit: Pursuit,
        maneuverAvailability: Int,
        trailerPresence: Int
    ): PlatformPursuit? = null

    override fun requestScreen(
        parkingRoute: PlatformRouteIdentifier,
        sonarRoute: PlatformRouteIdentifier,
        surroundRoute: PlatformRouteIdentifier,
        surroundClosable: Boolean,
        apaTransition: Boolean
    ): RouteIdentifier =
    requestSonarRoute(sonarRoute)

    override fun requestStopPursuit(
        pursuit: Pursuit,
        route: RouteIdentifier
    ) = when (pursuit) {
        Pursuit.MANEUVER -> listOf(PlatformPursuit.MONITOR_OBSTACLES)
        Pursuit.PARK -> listOf(PlatformPursuit.PARK)
    }
}