package com.renault.parkassist.routing

import alliancex.arch.core.logger.logD
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.os.RemoteException
import com.renault.parkassist.service.*
import com.renault.parkassist.utility.displayError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class DisplayManager(private val context: Context) : IDisplayManager, KoinComponent {

    private var displayService: IDisplayService? = null

    override val navigationRouteId: BehaviorSubject<RouteIdentifier> =
        BehaviorSubject.createDefault(RouteIdentifier.NONE)

    override val routeVisibility: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(true)

    private val connection: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    private val _shadowRequested: BehaviorSubject<Boolean> = BehaviorSubject.create()
    override val shadowRequested: Observable<Boolean> = _shadowRequested.hide()

    private val connected
        get() = connection.filter { it }.map { }.take(1)

    private val routeListenerFactory: RouteListenerFactory by inject()

    private val listener = routeListenerFactory.getRouteListener(
        { route ->
            logD { "navigationRouteId: routeId = $route" }
            this@DisplayManager.navigationRouteId.onNext(route)
        },
        { shadowRequested ->
            logD { "onShadowRequestedChange: requested = $shadowRequested" }
            this@DisplayManager._shadowRequested.onNext(shadowRequested)
        },
        { routeVisibility ->
            logD { "routeVisibility: visibility = $routeVisibility" }
            this@DisplayManager.routeVisibility.onNext(routeVisibility)
        }
    )

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            logD { "display service connected" }
            val binder = Binder()
            displayService = IDisplayService.Stub.asInterface(service)
            displayService?.registerProcessDeath(binder)
            displayService?.registerRouteListener(listener, binder)
            connection.onNext(true)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            displayError("disconnected")
            retryConnectService()
        }
    }

    private fun retryConnectService() {
        logD { "retryConnectService" }
        connection.onNext(false)
        // Retry connection
        connect()
    }

    override fun connect() {
        logD { "connect service" }
        Intent(context, DisplayService::class.java).also { intent ->
            context.bindService(
                intent,
                serviceConnection,
                Context.BIND_ABOVE_CLIENT
            )
        }
    }

    override fun forceReconnect() {
        logD { "force service reconnection" }
        context.unbindService(serviceConnection)
        connect()
    }

    override fun checkAlive() {
        logD { "check alive" }
        try {
            displayService?.isAlive
        } catch (e: RemoteException) {
            retryConnectService()
        }
    }

    override fun startPursuit(pursuit: Pursuit) {
        logD { "startPursuit" }
        // Delay till service connected
        connected.subscribe {
            try {
                displayService?.startPursuit(pursuit)
            } catch (e: RemoteException) {
                retryConnectService()
            }
        }
    }

    override fun stopPursuit(pursuit: Pursuit) {
        logD { "stopPursuit" }
        // Delay till service connected
        connected.subscribe {
            try {
                displayService?.stopPursuit(pursuit)
            } catch (e: RemoteException) {
                retryConnectService()
            }
        }
    }

    override fun stopCurrentPursuit() {
        logD { "stopCurrentPursuit" }
        // Delay till service connected
        connected.subscribe {
            try {
                displayService?.stopCurrentPursuit()
            } catch (e: RemoteException) {
                retryConnectService()
            }
        }
    }
}