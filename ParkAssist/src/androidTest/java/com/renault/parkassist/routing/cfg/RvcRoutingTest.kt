package com.renault.parkassist.routing.cfg

import com.renault.parkassist.routing.RoutingTest
import com.renault.parkassist.routing.policy.RvcPolicy

abstract class RvcRoutingTest : RoutingTest() {

    override val policy = RvcPolicy()
}