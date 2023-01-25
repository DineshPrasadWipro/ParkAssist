package com.renault.parkassist.routing

import alliancex.arch.core.logger.logE
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.repository.routing.PlatformRoutingBridge
import com.renault.parkassist.routing.policy.IPolicy
import com.renault.parkassist.utility.routingInfoLog
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables
import org.koin.core.KoinComponent
import java.util.concurrent.TimeUnit
import org.koin.core.inject

class Router : INavigationRoute, IOverlayRequest, KoinComponent {

    companion object {
        const val DEBOUNCE_TIME_MS = 300L
    }

    private val platformRouting: PlatformRoutingBridge by inject()

    private val policy: IPolicy by inject()

    override val screenRequest: Observable<RouteIdentifier> = Observables.combineLatest(
        platformRouting.apaScreenRoutingRequest,
        platformRouting.surroundRequest,
        platformRouting.sonarScreenRoutingRequest,
        platformRouting.isApaTransitionFromScanningToGuidance
    ) { fromParkingRoute, (fromSurroundRoute, surroundClosable), fromSonarRoute, isApaTransition ->
        policy.requestScreen(
            fromParkingRoute,
            fromSonarRoute,
            fromSurroundRoute,
            surroundClosable,
            isApaTransition
        ) to surroundClosable
    }
        .startWithItem(RouteIdentifier.NONE to true)
        .buffer(2, 1)
        .map { Triple(it[0].first, it[1].first, it[1].second) }
        .debounce { (previousRoute, currentRoute, currentClosable) ->
            if (policy.shouldDebounce(previousRoute, currentRoute, currentClosable))
                Observable.timer(
                    DEBOUNCE_TIME_MS,
                    TimeUnit.MILLISECONDS
                )
            else
                Observable.empty()
        }
        .map { (_, currentRoute, _) ->
            currentRoute
        }
        .distinctUntilChanged()
        .doOnNext {
            routingInfoLog("request", "screen", it)
        }
        .replay(1)
        .refCount()

    override val shadowRequested: Observable<Boolean> = Observables.combineLatest(
        platformRouting.surroundRequest,
        platformRouting.apaScreenRoutingRequest,
        platformRouting.sonarScreenRoutingRequest
    ) { (surroundRoute, surroundClosable), parkingRoute, sonarRoute ->
        routingInfoLog(
            "request", "shadow", policy.shouldShowShadow(
                surroundClosable,
                parkingRoute,
                surroundRoute,
                sonarRoute
            )
        )
        policy.shouldShowShadow(surroundClosable, parkingRoute, surroundRoute, sonarRoute)
    }

    override val surroundDialogRequest: Observable<RouteIdentifier> = platformRouting
        .surroundDialogRoutingRequest.map { platformRouteIdentifier ->
            when (platformRouteIdentifier) {
                PlatformRouteIdentifier.SURROUND_WARNING -> RouteIdentifier.SURROUND_WARNING
                else -> RouteIdentifier.NONE
            }
        }.distinctUntilChanged()
        .doOnNext {
            routingInfoLog("request", "surround dialog", it)
        }
        .doOnError { logE { "InternalRoute error $it" } }

    override val parkingDialogRequest: Observable<RouteIdentifier> = platformRouting
        .apaDialogRoutingRequest.map { platformRouteIdentifier ->
            when (platformRouteIdentifier) {
                PlatformRouteIdentifier.PARKING_WARNING -> RouteIdentifier.PARKING_WARNING
                else -> RouteIdentifier.NONE
            }
        }.distinctUntilChanged()
        .doOnNext {
            routingInfoLog("request", "parking dialog", it)
        }

    override val navigationRouteId: Observable<RouteIdentifier> = screenRequest

    override val routeVisibility: Observable<Boolean> =
        this.platformRouting.surroundVisible.distinctUntilChanged().doOnNext {
            routingInfoLog("request", "visible", it)
        }
}