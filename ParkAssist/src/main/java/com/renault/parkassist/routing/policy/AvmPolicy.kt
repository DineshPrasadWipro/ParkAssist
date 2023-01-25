package com.renault.parkassist.routing.policy

import alliance.car.autopark.AutoPark
import com.renault.parkassist.repository.routing.PlatformPursuit
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.repository.surroundview.ManeuverAvailability
import com.renault.parkassist.repository.surroundview.TrailerPresence
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier

class AvmPolicy(private val autoParkConfig: Int = AutoPark.FEATURE_NONE) : BasePolicy() {

    override fun shouldDebounce(
        previous: RouteIdentifier,
        current: RouteIdentifier,
        surroundClosable: Boolean
    ): Boolean =
        (listOf(
            RouteIdentifier.PARKING_AVM_HFP_SCANNING,
            RouteIdentifier.PARKING_AVM_HFP_PARK_OUT,
            RouteIdentifier.PARKING_AVM_HFP_GUIDANCE,
            RouteIdentifier.PARKING_FAPK_SCANNING,
            RouteIdentifier.PARKING_FAPK_PARK_OUT,
            RouteIdentifier.PARKING_FAPK_GUIDANCE
        ).contains(previous) &&
            (current == RouteIdentifier.SURROUND_AVM_MAIN && surroundClosable))

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
            else -> requestSurroundRoute(surroundRoute)
        }
    }

    private fun requestHfpRoute(parking: PlatformRouteIdentifier): RouteIdentifier =
        when (parking) {
            PlatformRouteIdentifier.PARKING_SCANNING -> RouteIdentifier.PARKING_AVM_HFP_SCANNING
            PlatformRouteIdentifier.PARKING_GUIDANCE -> RouteIdentifier.PARKING_AVM_HFP_GUIDANCE
            PlatformRouteIdentifier.PARKING_PARK_OUT -> RouteIdentifier.PARKING_AVM_HFP_PARK_OUT
            else -> RouteIdentifier.NONE
        }

    private fun requestFapkRoute(parking: PlatformRouteIdentifier) = when (parking) {
        PlatformRouteIdentifier.PARKING_SCANNING -> RouteIdentifier.PARKING_FAPK_SCANNING
        PlatformRouteIdentifier.PARKING_GUIDANCE -> RouteIdentifier.PARKING_FAPK_GUIDANCE
        PlatformRouteIdentifier.PARKING_PARK_OUT -> RouteIdentifier.PARKING_FAPK_PARK_OUT
        else -> RouteIdentifier.NONE
    }

    override fun requestAutoParkRoute(parking: PlatformRouteIdentifier): RouteIdentifier =
        when (autoParkConfig) {
            AutoPark.FEATURE_FAPK -> requestFapkRoute(parking)
            AutoPark.FEATURE_HFP -> requestHfpRoute(parking)
            else -> RouteIdentifier.NONE
        }

    override fun requestSurroundRoute(surround: PlatformRouteIdentifier): RouteIdentifier =
        when (surround) {
            PlatformRouteIdentifier.SURROUND_MAIN -> RouteIdentifier.SURROUND_AVM_MAIN
            PlatformRouteIdentifier.SURROUND_TRAILER -> RouteIdentifier.SURROUND_AVM_TRAILER
            PlatformRouteIdentifier.SURROUND_SETTINGS -> RouteIdentifier.SURROUND_AVM_SETTINGS
            PlatformRouteIdentifier.SURROUND_DEALER -> RouteIdentifier.SURROUND_AVM_DEALER
            PlatformRouteIdentifier.SURROUND_POPUP -> RouteIdentifier.SURROUND_AVM_POPUP
            else -> RouteIdentifier.NONE
        }

    override fun requestStartPursuit(
        pursuit: Pursuit,
        @ManeuverAvailability maneuverAvailability: Int,
        @TrailerPresence trailerPresence: Int
    ): PlatformPursuit? {
        return when (pursuit) {
            Pursuit.PARK -> PlatformPursuit.PARK
            Pursuit.MANEUVER -> when (maneuverAvailability) {
                ManeuverAvailability.READY -> PlatformPursuit.MANEUVER
                ManeuverAvailability.RESTRICTED -> when (trailerPresence) {
                    TrailerPresence.TRAILER_PRESENCE_DETECTED -> PlatformPursuit.WATCH_TRAILER
                    else -> PlatformPursuit.MANEUVER
                }
                else -> null
            }
        }
    }

    override fun requestStopPursuit(
        pursuit: Pursuit,
        route: RouteIdentifier
    ) = when (pursuit) {
        Pursuit.MANEUVER -> listOf(PlatformPursuit.MANEUVER)
        Pursuit.PARK -> listOf(PlatformPursuit.PARK)
    }

    override fun shouldShowShadow(
        surroundClosable: Boolean,
        parkingRoute: PlatformRouteIdentifier,
        surroundRoute: PlatformRouteIdentifier,
        sonarRoute: PlatformRouteIdentifier
    ): Boolean =
        surroundClosable && surroundRoute != PlatformRouteIdentifier.SURROUND_CLOSED &&
            surroundRoute != PlatformRouteIdentifier.SURROUND_POPUP

    override fun isSurroundRequest(surroundRoute: PlatformRouteIdentifier) =
        surroundRoute !in listOf(
            PlatformRouteIdentifier.SURROUND_CLOSED,
            PlatformRouteIdentifier.SURROUND_FAPK
        )
}