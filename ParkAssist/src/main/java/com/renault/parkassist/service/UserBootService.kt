package com.renault.parkassist.service

import alliancex.arch.core.logger.logD
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserBootService : ParkAssistService(), KoinComponent {

    private val shadowLauncher: ShadowLauncher by inject()

    private val trailerNotifier: TrailerNotifier by inject()

    override fun onCreate() {
        super.onCreate()
        logD { "onCreate" }

        startForeground(1, notification)
        shadowLauncher
        trailerNotifier.startListening()
    }

    // We need this local binder for test purposes
    // Espresso needs to bind to service
    inner class LocalBinder : Binder()

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }
}