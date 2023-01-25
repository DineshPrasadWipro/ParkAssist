package com.renault.parkassist.routing.pursuit

import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier

class PursuitHelper {
    fun routeToPursuit(route: RouteIdentifier): Pursuit? {
        return when (route) {
            RouteIdentifier.SURROUND_AVM_MAIN,
            RouteIdentifier.SURROUND_AVM_SETTINGS,
            RouteIdentifier.SURROUND_AVM_DEALER,
            RouteIdentifier.SURROUND_RVC_MAIN,
            RouteIdentifier.SURROUND_RVC_SETTINGS,
            RouteIdentifier.SURROUND_RVC_DEALER,
            RouteIdentifier.SURROUND_AVM_POPUP,
            RouteIdentifier.SURROUND_AVM_TRAILER,
            RouteIdentifier.SONAR_POPUP,
            RouteIdentifier.SURROUND_RVC_TRAILER -> Pursuit.MANEUVER
            RouteIdentifier.PARKING_AVM_HFP_SCANNING,
            RouteIdentifier.PARKING_AVM_HFP_GUIDANCE,
            RouteIdentifier.PARKING_AVM_HFP_PARK_OUT,
            RouteIdentifier.PARKING_RVC_HFP_SCANNING,
            RouteIdentifier.PARKING_RVC_HFP_GUIDANCE,
            RouteIdentifier.PARKING_RVC_HFP_PARK_OUT,
            RouteIdentifier.PARKING_FAPK_SCANNING,
            RouteIdentifier.PARKING_FAPK_GUIDANCE,
            RouteIdentifier.PARKING_FAPK_PARK_OUT -> Pursuit.PARK
            else -> null
        }
    }
}