package com.renault.parkassist.routing.cfg

import alliance.car.autopark.AutoPark
import com.renault.parkassist.routing.RoutingTest
import com.renault.parkassist.routing.policy.AvmPolicy

abstract class AvmFapkRoutingTest : RoutingTest() {

    override val policy = AvmPolicy(AutoPark.FEATURE_FAPK)
}