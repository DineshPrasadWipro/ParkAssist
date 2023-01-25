package com.renault.parkassist.service

import android.os.IBinder
import com.renault.parkassist.camera.ICameraConnectionListener

interface ICameraConnectionPolicy {
    fun addClient(listener: ICameraConnectionListener, binder: IBinder)
    fun removeClient(binder: IBinder)
}