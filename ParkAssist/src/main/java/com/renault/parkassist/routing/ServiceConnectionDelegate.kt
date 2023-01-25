package com.renault.parkassist.routing

import alliancex.arch.core.logger.logD
import org.koin.core.KoinComponent
import org.koin.core.inject

class ServiceConnectionDelegate : IDisplayServiceDelegate,
    KoinComponent {

    private val router: INavigationRoute by inject()

    private val client: MutableMap<Int, RouterClient> = mutableMapOf()

    override fun registerRouteClient(listener: IRouteListener, id: Int) {
        logD { "registerRouteClient (id = $id)" }
        client.putIfAbsent(
            id,
            RouterClient(
                listener,
                router.navigationRouteId,
                router.routeVisibility,
                router.shadowRequested
            )
        )
        logD { "clients : ${client.entries.toList().map { it.key }}" }
    }

    override fun unregisterRouteClient(id: Int) {
        logD { "unregisterRouteClient (id = $id)" }
        client[id]?.disconnect()
        client.remove(id)
        logD { "clients : ${client.entries.toList().map { it.key }}" }
    }

    override fun unregisterAllClients() {
        logD { "unregisterAllClients" }
        client.entries.toList().forEach { it.value.disconnect() }
        client.clear()
        logD { "clients : ${client.entries.toList().map { it.key }}" }
    }
}