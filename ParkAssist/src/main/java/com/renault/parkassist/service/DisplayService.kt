package com.renault.parkassist.service

import alliancex.arch.core.logger.logD
import android.content.Intent
import android.os.Build.VERSION_CODES.Q
import android.os.IBinder
import com.renault.parkassist.overlay.ParkingDialogOverlayManager
import com.renault.parkassist.overlay.ScreenOverlayManager
import com.renault.parkassist.overlay.SurroundDialogOverlayManager
import com.renault.parkassist.routing.IDisplayServiceDelegate
import com.renault.parkassist.routing.IOverlayRequest
import com.renault.parkassist.routing.IRouteListener
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.pursuit.PursuitManager
import org.koin.core.KoinComponent
import org.koin.core.inject

class DisplayService : KoinComponent, ParkAssistService() {

    private val binder = LocalBinder()
    private val serviceConnectionDelegate: IDisplayServiceDelegate by inject()
    private val overlayRequests: IOverlayRequest by inject()
    private val pursuitManager: PursuitManager by inject()
    private val screenOverlayManager: ScreenOverlayManager by inject()
    private val surroundDialogOverlayManager: SurroundDialogOverlayManager by inject()
    private val parkingDialogOverlayManager: ParkingDialogOverlayManager by inject()

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        logD { "onBind" }
        pursuitManager.setRoute(overlayRequests.screenRequest)
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        logD { "onDestroy" }
        serviceConnectionDelegate.unregisterAllClients()
    }

    inner class LocalBinder : IDisplayService.Stub() {

        override fun registerProcessDeath(clientBinder: IBinder) {
            clientBinder.linkToDeath(object : IBinder.DeathRecipient {
                override fun binderDied() {
                    logD { "binderDied" }
                    serviceConnectionDelegate.unregisterRouteClient(clientBinder.hashCode())
                    clientBinder.unlinkToDeath(this, Q)
                }
            }, Q)
        }

        override fun registerRouteListener(listener: IRouteListener, client: IBinder) {
            serviceConnectionDelegate.registerRouteClient(listener, client.hashCode())
            // Those are user dependant
            // If previous process in previous user has been killed we need
            // to launch overlays in new user's process
            screenOverlayManager.setRoute(overlayRequests.screenRequest)
            surroundDialogOverlayManager.setRoute(overlayRequests.surroundDialogRequest)
            parkingDialogOverlayManager.setRoute(overlayRequests.parkingDialogRequest)
            logD { "route client added and overlays routes set" }
        }

        override fun isAlive(): Boolean {
            logD { "isAlive checked" }
            return true
        }

        override fun startPursuit(pursuit: Pursuit) {
            pursuitManager.startPursuit(pursuit)
        }

        override fun stopPursuit(pursuit: Pursuit) {
            pursuitManager.stopPursuit(pursuit)
        }

        override fun stopCurrentPursuit() {
            pursuitManager.stopCurrentPursuit()
        }
    }
}