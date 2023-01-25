package com.renault.parkassist.viewmodel.route

import alliancex.arch.core.logger.logD
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.renault.parkassist.routing.IDisplayManager
import com.renault.parkassist.routing.RouteIdentifier
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

class RouteViewModel : ViewModel(), KoinComponent {
    private val manager: IDisplayManager by inject()
    private val disposables = CompositeDisposable()
    val navigationRoute = MutableLiveData<RouteIdentifier>()
    val routeVisibility = MutableLiveData<Boolean>()

    init {
        logD { "RouteViewModel init" }
        manager.checkAlive()

        disposables.add(manager.navigationRouteId.subscribe {
            navigationRoute.postValue(it)
        })

        disposables.add(manager.routeVisibility.subscribe {
            routeVisibility.postValue(it)
        })
    }

    override fun onCleared() {
        super.onCleared()
        logD { "RouteViewModel onCleared" }
        disposables.dispose()
    }
}