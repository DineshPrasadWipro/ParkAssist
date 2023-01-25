package com.renault.parkassist.routing

interface IDisplayServiceDelegate {
    fun registerRouteClient(listener: IRouteListener, id: Int)
    fun unregisterRouteClient(id: Int)
    fun unregisterAllClients()
}