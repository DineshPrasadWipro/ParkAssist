package com.renault.parkassist.service

import alliancex.arch.core.logger.logD
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.UserManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import org.koin.core.KoinComponent
import org.koin.core.inject

open class ParkAssistService : KoinComponent, LifecycleService() {

    private val userManager: UserManager by inject()

    protected val notification: Notification
        get() {
            val channelId = "Park Assist"
            val channel = NotificationChannel(
                channelId,
                "System Listener",
                NotificationManager.IMPORTANCE_MIN
            )

            val mgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mgr.createNotificationChannel(channel)

            return NotificationCompat.Builder(this, channelId)
                .setContentTitle("")
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
        }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, notification)

        logD {
            "onCreate called by " +
                "${if (userManager.isSystemUser) "" else "non"} system user"
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return intent?.let {
            val flag = super.onStartCommand(intent, flags, startId)
            logD {
                "onStartCommand called by " +
                    "${if (userManager.isSystemUser) "" else "non"} system user " +
                    "with flag $flag"
            }
            flag
        } ?: run {
            logD { "received null intent, certainly due to service restart" }
            START_STICKY
        }
    }
}