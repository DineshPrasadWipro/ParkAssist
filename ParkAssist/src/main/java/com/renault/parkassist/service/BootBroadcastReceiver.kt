package com.renault.parkassist.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.UserManager
import com.renault.parkassist.routing.policy.IPolicy
import com.renault.parkassist.routing.policy.NoAdasPolicy
import com.renault.parkassist.utility.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class BootBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val userManager: UserManager by inject()
    private val policy: IPolicy by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (policy !is NoAdasPolicy) {
            if (Intent.ACTION_LOCKED_BOOT_COMPLETED != intent?.action) {
                warningLog("boot", "Received unexpected intent $intent")
                return
            }
            if (context == null) {
                warningLog("boot", "Received intent with null context")
                return
            }

            if (userManager.isSystemUser) {
                infoLog(
                    "boot",
                    "received",
                    "System user received ${intent.action} intent," +
                        "starting system scoped services"
                )
                context.startForegroundService(Intent(context, CameraConnectionService::class.java))
            } else {
                infoLog(
                    "boot",
                    "received",
                    "Non system user received ${intent.action} intent, " +
                        "starting user scoped service"
                )
                context.startForegroundService(Intent(context, DisplayService::class.java))
                context.startForegroundService(Intent(context, UserBootService::class.java))
            }
        }
    }
}