package com.renault.parkassist.routing.policy

import com.renault.parkassist.repository.routing.PlatformPursuit
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.repository.surroundview.ManeuverAvailability
import com.renault.parkassist.repository.surroundview.TrailerPresence
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier

interface IPolicy {
    fun requestStartPursuit(
        pursuit: Pursuit,
        @ManeuverAvailability maneuverAvailability: Int,
        @TrailerPresence trailerPresence: Int
    ): PlatformPursuit?

    fun requestStopPursuit(
        pursuit: Pursuit,
        route: RouteIdentifier
    ): List<PlatformPursuit>

    fun shouldDebounce(
        previous: RouteIdentifier,
        current: RouteIdentifier,
        surroundClosable: Boolean
    ): Boolean

    fun shouldShowShadow(
        surroundClosable: Boolean,
        parkingRoute: PlatformRouteIdentifier,
        surroundRoute: PlatformRouteIdentifier,
        sonarRoute: PlatformRouteIdentifier
    ): Boolean

    fun requestScreen(
        parkingRoute: PlatformRouteIdentifier,
        sonarRoute: PlatformRouteIdentifier,
        surroundRoute: PlatformRouteIdentifier,
        surroundClosable: Boolean,
        apaTransition: Boolean
    ): RouteIdentifier
}

abstract class BasePolicy : IPolicy {
    open fun requestAutoParkRoute(parking: PlatformRouteIdentifier): RouteIdentifier =
        RouteIdentifier.NONE

    open fun requestSurroundRoute(surround: PlatformRouteIdentifier): RouteIdentifier =
        RouteIdentifier.NONE

    open fun requestSonarRoute(sonar: PlatformRouteIdentifier): RouteIdentifier =
        RouteIdentifier.NONE

    override fun shouldShowShadow(
        surroundClosable: Boolean,
        parkingRoute: PlatformRouteIdentifier,
        surroundRoute: PlatformRouteIdentifier,
        sonarRoute: PlatformRouteIdentifier
    ): Boolean = false

    protected fun isGuidanceRequest(parkingRoute: PlatformRouteIdentifier) =
        parkingRoute == PlatformRouteIdentifier.PARKING_GUIDANCE

    protected fun isScanningRequest(parkingRoute: PlatformRouteIdentifier) =
        parkingRoute == PlatformRouteIdentifier.PARKING_SCANNING

    open fun isSurroundRequest(surroundRoute: PlatformRouteIdentifier) =
        surroundRoute != PlatformRouteIdentifier.SURROUND_CLOSED

    override fun shouldDebounce(
        previous: RouteIdentifier,
        current: RouteIdentifier,
        surroundClosable: Boolean
    ): Boolean = false
}