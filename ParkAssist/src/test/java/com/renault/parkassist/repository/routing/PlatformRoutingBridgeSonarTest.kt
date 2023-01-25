package com.renault.parkassist.repository.routing

import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.repository.routing.ISonarRouting.Request
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier.*
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class PlatformRoutingBridgeSonarTest : TestBase() {

    private val sonarNavigation = mockk<ISonarRouting>(relaxed = true)
    private val surroundNavigation = mockk<ISurroundRouting>(relaxed = true)
    private val apaNavigation = mockk<IAutoParkRouting>(relaxed = true)

    override val koinModule = module {
        factory<ISonarRouting> { sonarNavigation }
        factory<ISurroundRouting> { surroundNavigation }
        factory<IAutoParkRouting> { apaNavigation }
    }

    private val sonarClosable: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val sonarRequest: BehaviorSubject<Request> = BehaviorSubject.create()

    @Before
    override fun setup() {
        super.setup()
        every { sonarNavigation.closable } returns sonarClosable
        every { sonarNavigation.request } returns sonarRequest
    }

    @Test
    fun `request none when sonar contains no detection`() { // ktlint-disable max-line-length
        val expected = SONAR_CLOSED
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = false, front = false, flank = false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request fullscreen when sonar contains rear detection 1`() { // ktlint-disable max-line-length
        val expected = SONAR_REAR
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = true, front = false, flank = false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request fullscreen when sonar contains rear detection 2`() { // ktlint-disable max-line-length
        val expected = SONAR_REAR
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = true, front = true, flank = false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request fullscreen when sonar contains rear detection 3`() { // ktlint-disable max-line-length
        val expected = SONAR_REAR
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = true, front = false, flank = true))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request fullscreen when sonar contains rear detection 4`() { // ktlint-disable max-line-length
        val expected = SONAR_REAR
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = true, front = true, flank = true))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request popup when sonar contains a detection not including rear 1`() { // ktlint-disable max-line-length
        val expected = SONAR_NOT_REAR
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = false, front = true, flank = true))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request popup when sonar contains a detection not including rear 2`() { // ktlint-disable max-line-length
        val expected = SONAR_NOT_REAR
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = false, front = false, flank = true))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request popup when sonar contains a detection not including rear 3`() { // ktlint-disable max-line-length
        val expected = SONAR_NOT_REAR
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = false, front = true, flank = false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `do not switch from fullscreen to popup request when sonar contains a detection including rear`() { // ktlint-disable max-line-length
        val expected = SONAR_REAR
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = true, front = true, flank = false))
        Assert.assertEquals(expected, actual)

        sonarRequest.onNext(Request(rear = true, front = false, flank = false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `switch from fullscreen to popup request when sonar contains a detection not including rear`() { // ktlint-disable max-line-length
        var expected = SONAR_REAR
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = true, front = true, flank = false))
        Assert.assertEquals(expected, actual)

        expected = SONAR_NOT_REAR
        sonarRequest.onNext(Request(rear = false, front = true, flank = true))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request should be updated when sonar update its navigation properties`() { // ktlint-disable max-line-length
        var actual = SONAR_CLOSED
        val cup = PlatformRoutingBridge()
        cup.sonarScreenRoutingRequest.subscribe {
            actual = it
        }

        sonarClosable.onNext(true)
        sonarRequest.onNext(Request(rear = true, front = true, flank = false))
        var expected = SONAR_REAR
        Assert.assertEquals(expected, actual)

        sonarClosable.onNext(false)
        expected = SONAR_REAR
        Assert.assertEquals(expected, actual)

        sonarRequest.onNext(Request(rear = false, front = false, flank = false))
        expected = SONAR_CLOSED
        Assert.assertEquals(expected, actual)

        sonarClosable.onNext(true)
        expected = SONAR_CLOSED
        Assert.assertEquals(expected, actual)

        sonarRequest.onNext(Request(rear = false, front = false, flank = true))
        expected = SONAR_NOT_REAR
        Assert.assertEquals(expected, actual)

        sonarRequest.onNext(Request(rear = true, front = false, flank = true))
        expected = SONAR_REAR
        Assert.assertEquals(expected, actual)
    }
}