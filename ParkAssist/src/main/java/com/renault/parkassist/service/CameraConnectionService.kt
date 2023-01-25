package com.renault.parkassist.service

import alliancex.arch.core.logger.logD
import android.content.Intent
import android.os.IBinder
import com.renault.parkassist.camera.ICameraConnectionListener
import org.koin.core.KoinComponent
import org.koin.core.inject

class CameraConnectionService : ParkAssistService(), KoinComponent {

    private val cameraConnectionPolicy: ICameraConnectionPolicy by inject()
    private val binder = LocalBinder()

    inner class LocalBinder : ICameraConnectionService.Stub() {
        override fun registerProcessDeath(clientBinder: IBinder) {
            clientBinder.linkToDeath(object : IBinder.DeathRecipient {
                override fun binderDied() {
                    logD { "binderDied" }
                    unregisterCameraListener(clientBinder)
                    clientBinder.unlinkToDeath(this, 0)
                }
            }, 0)
        }

        override fun registerCameraListener(listener: ICameraConnectionListener, binder: IBinder) =
            cameraConnectionPolicy.addClient(listener, binder)

        override fun unregisterCameraListener(binder: IBinder) =
            cameraConnectionPolicy.removeClient(binder)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        logD { "onBind" }
        return binder
    }
}