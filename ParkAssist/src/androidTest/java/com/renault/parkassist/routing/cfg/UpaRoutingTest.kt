package com.renault.parkassist.routing.cfg

import com.renault.parkassist.routing.RoutingTest
import com.renault.parkassist.routing.policy.UpaPolicy

abstract class UpaRoutingTest : RoutingTest() {

    override val policy = UpaPolicy()
}