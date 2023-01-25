package com.renault.parkassist.routing.policy

import com.renault.parkassist.repository.routing.PlatformPursuit
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.repository.surroundview.ManeuverAvailability
import com.renault.parkassist.repository.surroundview.TrailerPresence
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier

abstract class RvcMvcCommonPolicy : BasePolicy() {

    override fun requestStartPursuit(
        pursuit: Pursuit,
        @ManeuverAvailability maneuverAvailability: Int,
        @TrailerPresence trailerPresence: Int
    ): PlatformPursuit? {
        return when (pursuit) {
            Pursuit.PARK -> PlatformPursuit.PARK
            Pursuit.MANEUVER -> {
                if (maneuverAvailability == ManeuverAvailability.READY &&
                    trailerPresence == TrailerPresence.TRAILER_PRESENCE_DETECTED
                ) PlatformPursuit.WATCH_TRAILER
                else null
            }
        }
    }

    override fun requestStopPursuit(
        pursuit: Pursuit,
        route: RouteIdentifier
    ) = when (pursuit) {
        Pursuit.MANEUVER -> when (route) {
            RouteIdentifier.SONAR_POPUP -> listOf(PlatformPursuit.MONITOR_OBSTACLES)
            else -> listOf(PlatformPursuit.MANEUVER, PlatformPursuit.MONITOR_OBSTACLES)
        }
        Pursuit.PARK -> listOf(PlatformPursuit.PARK)
    }

    override fun shouldShowShadow(
        surroundClosable: Boolean,
        parkingRoute: PlatformRouteIdentifier,
        surroundRoute: PlatformRouteIdentifier,
        sonarRoute: PlatformRouteIdentifier
    ): Boolean =
        // Mind the order, surround route has priority over parking / sonar
        when {
            surroundRoute != PlatformRouteIdentifier.SURROUND_CLOSED -> surroundClosable
            parkingRoute == PlatformRouteIdentifier.PARKING_SCANNING -> true
            sonarRoute == PlatformRouteIdentifier.SONAR_REAR -> true
            else -> false
        }

    override fun requestScreen(
        parkingRoute: PlatformRouteIdentifier,
        sonarRoute: PlatformRouteIdentifier,
        surroundRoute: PlatformRouteIdentifier,
        surroundClosable: Boolean,
        apaTransition: Boolean
    ): RouteIdentifier {
        // A configurable hardware button ("favorite switch") is available to driver who can
        // use it to launch Easy Park Assist anytime (scanning), including while rear gear
        // is engaged and a regulatory view is displaying. In this case, regulatory view
        // should remain.
        return when {
            isGuidanceRequest(parkingRoute) -> requestAutoParkRoute(parkingRoute)
            isScanningRequest(parkingRoute) && apaTransition -> requestAutoParkRoute(parkingRoute)
            isSurroundRequest(surroundRoute) && !surroundClosable
            -> requestSurroundRoute(surroundRoute)
            isScanningRequest(parkingRoute) -> requestAutoParkRoute(parkingRoute)
            isSurroundRequest(surroundRoute) -> requestSurroundRoute(surroundRoute)
            else -> requestSonarRoute(sonarRoute)
        }
    }
}