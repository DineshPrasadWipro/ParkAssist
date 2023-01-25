package com.renault.parkassist.service

import android.os.IBinder
import com.renault.parkassist.camera.ICameraConnectionListener
import com.renault.parkassist.utility.cameraInfoLog

class CameraConnectionPolicy : ICameraConnectionPolicy {

    private data class Client(
        val id: Int,
        val listener: ICameraConnectionListener,
        var isRegistered: Boolean
    )

    private val clientProcessList = mutableListOf<Client>()

    override fun addClient(listener: ICameraConnectionListener, binder: IBinder) {
        val client = Client(binder.hashCode(), listener, false)

        when {
            clientProcessList.isEmpty() -> {
                cameraInfoLog("registration permission", "client ${client.id}")
                listener.onCameraConnectionAccepted()
                client.isRegistered = true
                clientProcessList.add(client)
            }
            clientProcessList.none { it.id == binder.hashCode() } -> {
                cameraInfoLog("add to camera client list", "client ${client.id}")
                clientProcessList.add(client)
            }
            else -> cameraInfoLog("already in client list", "client ${client.id}")
        }

        cameraInfoLog("client list after add", "$clientProcessList")
    }

    override fun removeClient(binder: IBinder) {
        if (clientProcessList.removeIf { it.id == binder.hashCode() } &&
            clientProcessList.isNotEmpty() &&
            !clientProcessList.first().isRegistered) {
            cameraInfoLog("remove from client list", "client = ${binder.hashCode()}")
            with(clientProcessList.first()) {
                listener.onCameraConnectionAccepted()
                isRegistered = true
            }
        }
        cameraInfoLog("client list after removal", "$clientProcessList")
    }
}