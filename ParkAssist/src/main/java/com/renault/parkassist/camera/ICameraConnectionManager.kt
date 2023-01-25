package com.renault.parkassist.camera

import io.reactivex.rxjava3.core.Observable

interface ICameraConnectionManager {
    fun connect()
    fun registerListener(listener: ICameraConnectionListener)
    fun unregisterListener()
    val isServiceConnected: Observable<Boolean>
}