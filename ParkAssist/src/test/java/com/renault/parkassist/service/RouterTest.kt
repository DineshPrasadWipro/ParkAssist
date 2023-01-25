package com.renault.parkassist.service

import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.overlay.ParkingDialogOverlayManager
import com.renault.parkassist.overlay.ScreenOverlayManager
import com.renault.parkassist.overlay.SurroundDialogOverlayManager
import com.renault.parkassist.repository.routing.*
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier.*
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.Router
import com.renault.parkassist.routing.policy.IPolicy
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.*
import org.koin.dsl.module

@SmallTest
class RouterTest : TestBase() {

    companion object {
        private const val DELIGHTFUL_TIME_MS = 50L
    }

    private lateinit var router: Router
    private val overlayManager = mockk<ScreenOverlayManager>(relaxed = true)
    private val overlaySurroundWarningManager = mockk<SurroundDialogOverlayManager>(relaxed = true)
    private val overlayParkingWarningManager = mockk<ParkingDialogOverlayManager>(relaxed = true)
    private val platformRouting = mockk<PlatformRoutingBridge>(relaxed = true)
    private val policy = mockk<IPolicy>()

    private lateinit var apaRequest: BehaviorSubject<PlatformRouteIdentifier>
    private lateinit var sonarRequest: BehaviorSubject<PlatformRouteIdentifier>
    private lateinit var surroundRequest: BehaviorSubject<Pair<PlatformRouteIdentifier, Boolean>>
    private lateinit var isApaTransitionFromScanningToGuidance: BehaviorSubject<Boolean>
    override val koinModule = module {
        factory<IPolicy> { policy }
        factory<ScreenOverlayManager> { overlayManager }
        factory<ParkingDialogOverlayManager> { overlayParkingWarningManager }
        factory<SurroundDialogOverlayManager> { overlaySurroundWarningManager }
        factory<PlatformRoutingBridge> { platformRouting }
    }

    @Before
    override fun setup() {
        super.setup()
        apaRequest = BehaviorSubject.create()
        sonarRequest = BehaviorSubject.create()
        surroundRequest = BehaviorSubject.create()
        isApaTransitionFromScanningToGuidance = BehaviorSubject.create()

        every { policy.shouldDebounce(any(), any(), any()) } returns false
        every { platformRouting.apaScreenRoutingRequest } returns apaRequest
        every { platformRouting.sonarScreenRoutingRequest } returns sonarRequest
        every { platformRouting.surroundRequest } returns surroundRequest
        every { platformRouting.isApaTransitionFromScanningToGuidance } returns
            isApaTransitionFromScanningToGuidance

        every {
            policy.requestScreen(any(), any(), any(), any(), any())
        } returns
            RouteIdentifier.NONE

        every {
            policy.shouldShowShadow(any(), any(), any(), any())
        } returns
            false

        apaRequest.onNext(PARKING_CLOSED)
        sonarRequest.onNext(SONAR_CLOSED)
        surroundRequest.onNext(Pair(SURROUND_CLOSED, true))
        isApaTransitionFromScanningToGuidance.onNext(false)

        // GIVEN OverlayManager observes a route
        router = Router()
    }

    @Test
    fun `should request screen `() {
        val testObserver = router.screenRequest.test()

        every {
            policy.requestScreen(any(), any(), any(), any(), any())
        } returns
            RouteIdentifier.SURROUND_RVC_MAIN

        surroundRequest.onNext(Pair(SURROUND_MAIN, false))

        testObserver.assertValueAt(
            testObserver.values().lastIndex,
            RouteIdentifier.SURROUND_RVC_MAIN
        )
    }

    @Test
    fun `should request shadow when accordingly to policy true`() {
        val testObserver = router.shadowRequested.test()

        every {
            policy.shouldShowShadow(any(), any(), any(), any())
        } returns
            true

        apaRequest.onNext(PARKING_CLOSED)
        sonarRequest.onNext(SONAR_CLOSED)
        surroundRequest.onNext(Pair(SURROUND_CLOSED, true))

        testObserver.assertValueAt(
            testObserver.values().lastIndex,
            true
        )

        every {
            policy.shouldShowShadow(any(), any(), any(), any())
        } returns
            false

        surroundRequest.onNext(Pair(SURROUND_CLOSED, false))

        testObserver.assertValueAt(
            testObserver.values().lastIndex,
            false
        )
    }
    @Test
    fun `should debounce when closing apa and closable avm surround ongoing`() {

        val testObserver = router.screenRequest.test()

        every {
            policy.requestScreen(any(), any(), any(), any(), any())
        } returns
            RouteIdentifier.PARKING_AVM_HFP_SCANNING

        apaRequest.onNext(PARKING_SCANNING)
        surroundRequest.onNext(Pair(SURROUND_MAIN, false))

        every { policy.shouldDebounce(any(), any(), any()) } returns true

        every {
            policy.requestScreen(any(), any(), any(), any(), any())
        } returns
            RouteIdentifier.SURROUND_AVM_MAIN

        apaRequest.onNext(PARKING_CLOSED)

        testObserver.assertValueAt(
            testObserver.values().lastIndex,
            RouteIdentifier.PARKING_AVM_HFP_SCANNING
        )

        Thread.sleep(Router.DEBOUNCE_TIME_MS + DELIGHTFUL_TIME_MS)

        testObserver.assertValueAt(
            testObserver.values().lastIndex,
            RouteIdentifier.SURROUND_AVM_MAIN
        )
    }

    @Test
    fun `Should always return a screen request when observed`() {

        every {
            policy.requestScreen(any(), any(), any(), any(), any())
        } returns
            RouteIdentifier.PARKING_AVM_HFP_SCANNING

        val testObserver = router.screenRequest.test()

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(
            0,
            RouteIdentifier.PARKING_AVM_HFP_SCANNING
        )
    }
}