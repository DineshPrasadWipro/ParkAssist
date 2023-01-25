package com.renault.parkassist.repository.routing

import alliance.car.surroundview.SurroundView
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.repository.routing.ISurroundRouting.Request
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier.*
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.*
import org.koin.dsl.module

@SmallTest
class PlatformRoutingBridgeSurroundTest : TestBase() {

    private val sonarNavigation = mockk<ISonarRouting>(relaxed = true)
    private val surroundNavigation = mockk<ISurroundRouting>(relaxed = true)
    private val apaNavigation = mockk<IAutoParkRouting>(relaxed = true)

    override val koinModule = module {
        factory<ISonarRouting> { sonarNavigation }
        factory<ISurroundRouting> { surroundNavigation }
        factory<IAutoParkRouting> { apaNavigation }
    }

    private val request: BehaviorSubject<Request> = BehaviorSubject.create()

    @Before
    override fun setup() {
        super.setup()
        every { surroundNavigation.screenRequest } returns request
    }

    @Test
    fun `request SURROUND_CLOSED and closble true when surround requests no display`() { // ktlint-disable max-line-length
        val expected = SURROUND_CLOSED
        var actual = SURROUND_CLOSED
        val cup = PlatformRoutingBridge()
        cup.surroundRequest.subscribe { (route, closable) ->
            actual = route
        }

        request.onNext(Request(SurroundView.NO_DISPLAY, false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request surround main upon surround requests`() { // ktlint-disable max-line-length
        val expected = SURROUND_MAIN
        var actual = SURROUND_CLOSED
        val cup = PlatformRoutingBridge()
        cup.surroundRequest.subscribe { (route, closable) ->
            actual = route
        }

        request.onNext(Request(SurroundView.REAR_VIEW, false))
        Assert.assertEquals(expected, actual)

        request.onNext(Request(SurroundView.FRONT_VIEW, false))
        Assert.assertEquals(expected, actual)

        request.onNext(Request(SurroundView.PANORAMIC_REAR_VIEW, false))
        Assert.assertEquals(expected, actual)

        request.onNext(Request(SurroundView.PANORAMIC_FRONT_VIEW, false))
        Assert.assertEquals(expected, actual)

        request.onNext(Request(SurroundView.SIDES_VIEW, false))
        Assert.assertEquals(expected, actual)

        request.onNext(Request(SurroundView.THREE_DIMENSION_VIEW, false))
        Assert.assertEquals(expected, actual)

        request.onNext(Request(SurroundView.AUTO_ZOOM_REAR_VIEW, false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request surround settings upon surround requests`() { // ktlint-disable max-line-length
        val expected = SURROUND_SETTINGS
        var actual = SURROUND_CLOSED
        val cup = PlatformRoutingBridge()
        cup.surroundRequest.subscribe { (route, closable) ->
            actual = route
        }

        request.onNext(Request(SurroundView.SETTINGS_REAR_VIEW, false))
        Assert.assertEquals(expected, actual)

        request.onNext(Request(SurroundView.SETTINGS_FRONT_VIEW, false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request surround trailer upon surround requests`() { // ktlint-disable max-line-length
        val expected = SURROUND_TRAILER
        var actual = SURROUND_CLOSED
        val cup = PlatformRoutingBridge()
        cup.surroundRequest.subscribe { (route, closable) ->
            actual = route
        }

        request.onNext(Request(SurroundView.TRAILER_VIEW, false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request surround dealer upon surround requests`() { // ktlint-disable max-line-length
        val expected = SURROUND_DEALER
        var actual = SURROUND_CLOSED
        val cup = PlatformRoutingBridge()
        cup.surroundRequest.subscribe { (route, closable) ->
            actual = route
        }

        request.onNext(Request(SurroundView.DEALER_VIEW, false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request surround popup upon surround requests`() { // ktlint-disable max-line-length
        val expected = SURROUND_POPUP
        var actual = SURROUND_CLOSED
        val cup = PlatformRoutingBridge()
        cup.surroundRequest.subscribe { (route, closable) ->
            actual = route
        }

        request.onNext(Request(SurroundView.POP_UP_VIEW, false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request fapk when apa front requested on top of surround main`() {
        var expected = SURROUND_MAIN
        var actual = SURROUND_CLOSED
        val cup = PlatformRoutingBridge()
        cup.surroundRequest.subscribe { (route, closable) ->
            actual = route
        }

        request.onNext(Request(SurroundView.REAR_VIEW, false))
        Assert.assertEquals(expected, actual)

        expected = SURROUND_FAPK
        request.onNext(Request(SurroundView.APA_FRONT_VIEW, false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `request fapk when apa rear requested on top of surround main`() {
        var expected = SURROUND_MAIN
        var actual = SURROUND_CLOSED
        val cup = PlatformRoutingBridge()
        cup.surroundRequest.subscribe { (route, closable) ->
            actual = route
        }

        request.onNext(Request(SurroundView.REAR_VIEW, false))
        Assert.assertEquals(expected, actual)

        expected = SURROUND_FAPK
        request.onNext(Request(SurroundView.APA_REAR_VIEW, false))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `closable should be false when NO_DISPLAY requested`() {
        val expected = SURROUND_CLOSED
        var actual = SURROUND_MAIN
        val cup = PlatformRoutingBridge()
        cup.surroundRequest.subscribe { (route, closable) ->
            actual = route
        }

        request.onNext(Request(SurroundView.NO_DISPLAY, false))
        Assert.assertEquals(expected, actual)

        request.onNext(Request(SurroundView.NO_DISPLAY, true))
        Assert.assertEquals(expected, actual)
    }
}