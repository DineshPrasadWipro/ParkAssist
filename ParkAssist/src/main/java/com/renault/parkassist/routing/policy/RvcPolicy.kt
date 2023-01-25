package com.renault.parkassist.routing.policy

import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.routing.RouteIdentifier

class RvcPolicy : RvcMvcCommonPolicy() {

    override fun requestAutoParkRoute(parking: PlatformRouteIdentifier) = when (parking) {
        PlatformRouteIdentifier.PARKING_SCANNING -> RouteIdentifier.PARKING_RVC_HFP_SCANNING
        PlatformRouteIdentifier.PARKING_GUIDANCE -> RouteIdentifier.PARKING_RVC_HFP_GUIDANCE
        PlatformRouteIdentifier.PARKING_PARK_OUT -> RouteIdentifier.PARKING_RVC_HFP_PARK_OUT
        else -> RouteIdentifier.NONE
    }

    override fun requestSurroundRoute(surround: PlatformRouteIdentifier) = when (surround) {
        PlatformRouteIdentifier.SURROUND_MAIN -> RouteIdentifier.SURROUND_RVC_MAIN
        PlatformRouteIdentifier.SURROUND_TRAILER -> RouteIdentifier.SURROUND_RVC_TRAILER
        PlatformRouteIdentifier.SURROUND_SETTINGS -> RouteIdentifier.SURROUND_RVC_SETTINGS
        PlatformRouteIdentifier.SURROUND_DEALER -> RouteIdentifier.SURROUND_RVC_DEALER
        else -> RouteIdentifier.NONE
    }

    override fun requestSonarRoute(sonar: PlatformRouteIdentifier) =
        when (sonar) {
            PlatformRouteIdentifier.SONAR_REAR -> RouteIdentifier.SURROUND_RVC_MAIN
            PlatformRouteIdentifier.SONAR_NOT_REAR -> RouteIdentifier.SONAR_POPUP
            else -> RouteIdentifier.NONE
        }
}