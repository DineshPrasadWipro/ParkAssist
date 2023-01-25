package com.renault.parkassist.routing

import alliancex.arch.core.logger.logD
import alliancex.arch.core.logger.logE
import android.os.DeadObjectException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

class RouterClient(
    val remoteListener: IRouteListener,
    route: Observable<RouteIdentifier>,
    visibility: Observable<Boolean>,
    shadowRequested: Observable<Boolean>
) {
    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(route.subscribe { routeId ->
            try {
                logD { "onRouteChange $routeId" }
                remoteListener.onRouteChange(routeId)
            } catch (e: DeadObjectException) {
                logE { "Dead object Exception" }
            }
        })
        compositeDisposable.add(visibility.subscribe { visibilityValue ->
            try {
                logD { "onVisibilityChange $visibility" }
                remoteListener.onVisibilityChange(visibilityValue)
            } catch (e: DeadObjectException) {
                logE { "Dead object Exception" }
            }
        })
        compositeDisposable.add(shadowRequested.subscribe { shadowRequestedValue ->
            try {
                logD { "onShadowRequestedChange $shadowRequestedValue" }
                remoteListener.onShadowRequestedChange(shadowRequestedValue)
            } catch (e: DeadObjectException) {
                logE { "Dead object Exception" }
            }
        })
    }

    fun disconnect() {
        compositeDisposable.dispose()
    }
}