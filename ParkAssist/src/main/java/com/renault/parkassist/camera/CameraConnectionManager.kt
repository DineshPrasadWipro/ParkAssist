package com.renault.parkassist.camera

import alliancex.arch.core.logger.logD
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import com.renault.parkassist.service.CameraConnectionService
import com.renault.parkassist.service.ICameraConnectionService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent

class CameraConnectionManager(
    private val context: Context
) :
    ICameraConnectionManager, KoinComponent, ServiceConnection {

    private lateinit var cameraConnectionService: ICameraConnectionService
    private val _isServiceConnected = BehaviorSubject.create<Boolean>()
    override val isServiceConnected: Observable<Boolean> = _isServiceConnected.hide()
    private var localBinder: IBinder? = Binder()

    override fun connect() {
        logD { "Connecting to CameraConnectionService..." }
        Intent(context, CameraConnectionService::class.java).also { intent ->
            context.bindService(
                intent,
                this,
                Context.BIND_ABOVE_CLIENT
            )
        }
    }

    override fun registerListener(listener: ICameraConnectionListener) {
        cameraConnectionService.registerCameraListener(listener, localBinder)
    }

    override fun unregisterListener() {
        cameraConnectionService.unregisterCameraListener(localBinder)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        logD { "CameraConnectionService connected." }
        cameraConnectionService = ICameraConnectionService.Stub.asInterface(service)
        _isServiceConnected.onNext(true)
        cameraConnectionService.registerProcessDeath(localBinder)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        logD { "CameraConnectionService disconnected." }
        _isServiceConnected.onNext(false)
    }
}