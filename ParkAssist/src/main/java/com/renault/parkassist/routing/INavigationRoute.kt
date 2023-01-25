package com.renault.parkassist.routing

import io.reactivex.rxjava3.core.Observable

interface INavigationRoute {
    val navigationRouteId: Observable<RouteIdentifier>
    val routeVisibility: Observable<Boolean>
    val shadowRequested: Observable<Boolean>
}