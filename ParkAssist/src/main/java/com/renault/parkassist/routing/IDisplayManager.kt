package com.renault.parkassist.routing

import com.renault.parkassist.routing.pursuit.IPursuit

interface IDisplayManager : INavigationRoute, IPursuit {
    fun connect()
    fun forceReconnect()
    fun checkAlive()
}