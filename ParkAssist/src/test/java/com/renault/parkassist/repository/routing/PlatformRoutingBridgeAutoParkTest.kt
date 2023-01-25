package com.renault.parkassist.repository.routing

import alliance.car.autopark.AutoPark
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier.*
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.*
import org.koin.dsl.module

@SmallTest
class PlatformRoutingBridgeAutoParkTest : TestBase() {

    private val sonarNavigation = mockk<ISonarRouting>(relaxed = true)
    private val surroundNavigation = mockk<ISurroundRouting>(relaxed = true)
    private val apaNavigation = mockk<IAutoParkRouting>(relaxed = true)

    override val koinModule = module {
        factory<ISonarRouting> { sonarNavigation }
        factory<ISurroundRouting> { surroundNavigation }
        factory<IAutoParkRouting> { apaNavigation }
    }

    private val viewRequest: BehaviorSubject<Int> = BehaviorSubject.create()

    @Before
    override fun setup() {
        super.setup()
        every { apaNavigation.screenRequest } returns viewRequest
    }

    @Test
    fun `request none when auto park requests no display`() { // ktlint-disable max-line-length
        val expected = PARKING_CLOSED
        var actual = PARKING_CLOSED
        val cup = PlatformRoutingBridge()
        cup.apaScreenRoutingRequest.subscribe {
            actual = it
        }

        viewRequest.onNext(AutoPark.DISPLAY_NONE)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request scanning upon auto park request`() { // ktlint-disable max-line-length
        val expected = PARKING_SCANNING
        var actual = PARKING_CLOSED
        val cup = PlatformRoutingBridge()
        cup.apaScreenRoutingRequest.subscribe {
            actual = it
        }

        viewRequest.onNext(AutoPark.DISPLAY_SCANNING)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request guidance upon auto park request`() { // ktlint-disable max-line-length
        val expected = PARKING_GUIDANCE
        var actual = PARKING_CLOSED
        val cup = PlatformRoutingBridge()
        cup.apaScreenRoutingRequest.subscribe {
            actual = it
        }

        viewRequest.onNext(AutoPark.DISPLAY_GUIDANCE)
        Assert.assertEquals(expected, actual)
    }
}