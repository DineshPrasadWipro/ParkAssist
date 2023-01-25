package com.renault.parkassist

import alliance.car.surroundview.SurroundViewCapabilities
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.os.UserHandle
import android.os.UserManager
import com.renault.parkassist.camera.CameraManager
import com.renault.parkassist.camera.ICameraConnectionListener
import com.renault.parkassist.camera.ICameraConnectionManager
import com.renault.parkassist.repository.surroundview.SurroundViewManagerAdapter
import com.renault.parkassist.routing.IDisplayManager
import com.renault.parkassist.utility.cameraInfoLog
import com.renault.parkassist.utility.systemErrorLog
import com.renault.parkassist.utility.systemInfoLog
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserProcess(private val context: Context) : KoinComponent {

    companion object {
        private val NO_USER = 0xDEAD
    }

    private val carSurroundViewManagerAdapter: SurroundViewManagerAdapter by inject()
    private val displayManager: IDisplayManager by inject()
    private val userManager: UserManager by inject()
    private val cameraConnectionManager: ICameraConnectionManager by inject()
    private val cameraManager: CameraManager by inject()
    private var userId: Int = NO_USER

    private val cameraConnectionListener = lazy {
        object : ICameraConnectionListener.Stub() {
            override fun onCameraConnectionAccepted() {
                cameraInfoLog("register", "camera")
                Handler(Looper.getMainLooper()).post {
                    systemInfoLog("camera registration", "user $userId")
                    cameraManager.initialize()
                }
            }
        }
    }

    private val actionUserReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_USER_BACKGROUND -> {
                    systemInfoLog("to background", "user $userId")
                    cameraManager.clear()
                    cameraConnectionManager.unregisterListener()
                }
                Intent.ACTION_USER_FOREGROUND -> {
                    systemInfoLog("to foreground", "user $userId")
                    // Reconnect to force a setRouteListener to get current router state, otherwise
                    // Activities may no be restarted (use case = user switch while camera view is
                    // ongoing)
                    displayManager.forceReconnect()
                    requestConnectionToCameraManager()
                }
                else -> systemErrorLog("unexpected user action")
            }
        }
    }

    fun init() {
        if (!userManager.isSystemUser) {

            // Hashcode returns only the user ID, which is more convenient than using toString()
            userId = UserHandle.getUserHandleForUid(context.applicationContext.applicationInfo.uid)
                .hashCode()

            if (carSurroundViewManagerAdapter.featureConfig !=
                SurroundViewCapabilities.FEATURE_NOT_SUPPORTED
            ) {
                cameraConnectionManager.connect()
                requestConnectionToCameraManager()
                context.registerReceiver(actionUserReceiver, IntentFilter().apply {
                    addAction(Intent.ACTION_USER_BACKGROUND)
                    addAction(Intent.ACTION_USER_FOREGROUND)
                })
            }

            displayManager.connect()
        }
    }

    private fun requestConnectionToCameraManager() {
        cameraConnectionManager.isServiceConnected
            .filter { it == true }
            .take(1)
            .subscribe {
                systemInfoLog("camera registration request", "user $userId")
                cameraConnectionManager.registerListener(cameraConnectionListener.value)
            }
    }
}