package com.renault.parkassist.repository.routing

import alliance.car.autopark.AutoPark
import alliance.car.surroundview.SurroundView
import com.renault.parkassist.repository.surroundview.Action.*
import com.renault.parkassist.utility.errorLog
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class PlatformRoutingBridge : KoinComponent {

    private val sonarRouting: ISonarRouting by inject()
    private val surroundRouting: ISurroundRouting by inject()
    private val apaRouting: IAutoParkRouting by inject()

    val trailerPresence: BehaviorSubject<Int> = surroundRouting.trailerPresence
    val maneuverAvailability: BehaviorSubject<Int> = surroundRouting.maneuverAvailability

    val sonarScreenRoutingRequest: Observable<PlatformRouteIdentifier> = sonarRouting.request
        .map { request ->
            when {
                !request.rear && !request.front && !request.flank ->
                    PlatformRouteIdentifier.SONAR_CLOSED

                !request.rear ->
                    PlatformRouteIdentifier.SONAR_NOT_REAR

                else ->
                    PlatformRouteIdentifier.SONAR_REAR
            }
        }

    val surroundRequest: Observable<Pair<PlatformRouteIdentifier, Boolean>> =
        Observables.zip(
            surroundRouting.screenRequest
                .map { viewState ->
                    mapSurroundRoutingLocation(viewState.state)
                },
            surroundRouting.screenRequest.map { request ->
                if (mapSurroundRoutingLocation(request.state)
                    == PlatformRouteIdentifier.SURROUND_FAPK
                ) true else request.closable
            })

    val surroundVisible: Observable<Boolean> = surroundRouting.screenRequest.map {
        it.displayMode == SurroundView.DISPLAY_MODE_VISIBLE
    }

    private fun mapSurroundRoutingLocation(@SurroundView.ViewState view: Int) = when (view) {
        SurroundView.REAR_VIEW,
        SurroundView.FRONT_VIEW,
        SurroundView.PANORAMIC_REAR_VIEW,
        SurroundView.PANORAMIC_FRONT_VIEW,
        SurroundView.SIDES_VIEW,
        SurroundView.THREE_DIMENSION_VIEW,
        SurroundView.AUTO_ZOOM_REAR_VIEW -> PlatformRouteIdentifier.SURROUND_MAIN
        SurroundView.SETTINGS_REAR_VIEW,
        SurroundView.SETTINGS_FRONT_VIEW -> PlatformRouteIdentifier.SURROUND_SETTINGS
        SurroundView.TRAILER_VIEW -> PlatformRouteIdentifier.SURROUND_TRAILER
        SurroundView.DEALER_VIEW -> PlatformRouteIdentifier.SURROUND_DEALER
        SurroundView.POP_UP_VIEW -> PlatformRouteIdentifier.SURROUND_POPUP
        SurroundView.NO_DISPLAY -> PlatformRouteIdentifier.SURROUND_CLOSED
        SurroundView.APA_FRONT_VIEW,
        SurroundView.APA_REAR_VIEW -> PlatformRouteIdentifier.SURROUND_FAPK
        else -> {
            errorLog(
                "routing", "Unsupported surround routing location",
                "fall back to NONE"
            )
            PlatformRouteIdentifier.SURROUND_CLOSED
        }
    }

    val apaScreenRoutingRequest: Observable<PlatformRouteIdentifier> = apaRouting.screenRequest
        .map { request ->
            when (request) {
                AutoPark.DISPLAY_SCANNING,
                AutoPark.DISPLAY_PARKOUT_CONFIRMATION -> PlatformRouteIdentifier.PARKING_SCANNING
                AutoPark.DISPLAY_GUIDANCE -> PlatformRouteIdentifier.PARKING_GUIDANCE
                else /* AutoPark.DISPLAY_NONE */ -> PlatformRouteIdentifier.PARKING_CLOSED
            }
        }

    val apaDialogRoutingRequest: Observable<PlatformRouteIdentifier> =
        apaRouting.dialogRequest.map { request ->
            if (request) PlatformRouteIdentifier.PARKING_WARNING
            else PlatformRouteIdentifier.PARKING_WARNING_CLOSED
        }

    val surroundDialogRoutingRequest: Observable<PlatformRouteIdentifier> =
        surroundRouting.dialogRequest.map { request ->
            if (request) PlatformRouteIdentifier.SURROUND_WARNING
            else PlatformRouteIdentifier.SURROUND_WARNING_CLOSED
        }

    val isApaTransitionFromScanningToGuidance: Observable<Boolean> =
        apaRouting.isTransitionFromScanningToGuidance.map {
            it
        }

    fun startPursuit(pursuit: PlatformPursuit) {
        when (pursuit) {
            PlatformPursuit.MANEUVER -> surroundRouting.requestAction(ACTIVATE_MANEUVER_VIEW)
            PlatformPursuit.WATCH_TRAILER -> surroundRouting.requestAction(ACTIVATE_TRAILER_VIEW)
            PlatformPursuit.PARK -> apaRouting.switchActivation(true)
            else -> errorLog(
                "routing",
                "Unsupported start of platform pursuit $pursuit",
                "do nothing"
            )
        }
    }

    fun closePursuit(pursuit: PlatformPursuit) = when (pursuit) {
        PlatformPursuit.MANEUVER -> surroundRouting.requestAction(CLOSE_VIEW)
        PlatformPursuit.MONITOR_OBSTACLES -> sonarRouting.close()
        PlatformPursuit.WATCH_TRAILER -> surroundRouting.requestAction(CLOSE_VIEW)
        PlatformPursuit.PARK -> apaRouting.switchActivation(false)
    }
}