package com.renault.parkassist.routing

import io.reactivex.rxjava3.core.Observable

interface IOverlayRequest {
    val screenRequest: Observable<RouteIdentifier>
    val surroundDialogRequest: Observable<RouteIdentifier>
    val parkingDialogRequest: Observable<RouteIdentifier>
}