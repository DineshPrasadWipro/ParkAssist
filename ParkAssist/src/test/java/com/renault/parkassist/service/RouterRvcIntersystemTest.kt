package com.renault.parkassist.service

import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.overlay.ParkingDialogOverlayManager
import com.renault.parkassist.overlay.ScreenOverlayManager
import com.renault.parkassist.overlay.SurroundDialogOverlayManager
import com.renault.parkassist.repository.routing.*
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.Router
import com.renault.parkassist.routing.policy.IPolicy
import com.renault.parkassist.routing.policy.RvcPolicy
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.*
import org.koin.core.module.Module
import org.koin.dsl.module

// This class purpose is to test use case documented in
// RVC_intersystem_V2.pptx
// Additional use cases added since documentation is not extensive
@SmallTest
class RouterRvcIntersystemTest : TestBase() {

    override val koinModule: Module
        get() = module {
            factory<IPolicy> { RvcPolicy() }
            factory<ScreenOverlayManager> { overlayManager }
            factory<ParkingDialogOverlayManager> { overlayParkingWarningManager }
            factory<SurroundDialogOverlayManager> { overlaySurroundWarningManager }
            factory<PlatformRoutingBridge> { platformRouting }
        }

    private lateinit var router: Router
    private val overlayManager = mockk<ScreenOverlayManager>(relaxed = true)
    private val overlaySurroundWarningManager = mockk<SurroundDialogOverlayManager>(relaxed = true)
    private val overlayParkingWarningManager = mockk<ParkingDialogOverlayManager>(relaxed = true)
    private val platformRouting = mockk<PlatformRoutingBridge>(relaxed = true)

    private lateinit var apaRequest: BehaviorSubject<PlatformRouteIdentifier>
    private lateinit var sonarRequest: BehaviorSubject<PlatformRouteIdentifier>
    private lateinit var surroundRequest: BehaviorSubject<Pair<PlatformRouteIdentifier, Boolean>>
    private lateinit var isApaTransitionFromScanningToGuidance: BehaviorSubject<Boolean>
    private lateinit var surroundClosable: BehaviorSubject<Boolean>

    override fun setup() {
        super.setup()

        apaRequest = BehaviorSubject.create()
        sonarRequest = BehaviorSubject.create()
        surroundRequest = BehaviorSubject.create()
        surroundClosable = BehaviorSubject.create()
        isApaTransitionFromScanningToGuidance = BehaviorSubject.create()
        apaRequest.onNext(

                PlatformRouteIdentifier.PARKING_CLOSED
        )
        sonarRequest.onNext(

                PlatformRouteIdentifier.SONAR_CLOSED
        )
        surroundRequest.onNext(

                Pair(PlatformRouteIdentifier.SURROUND_CLOSED, false)
        )
        isApaTransitionFromScanningToGuidance.onNext(false)
        surroundClosable.onNext(true)

        every { platformRouting.apaScreenRoutingRequest } returns apaRequest
        every { platformRouting.sonarScreenRoutingRequest } returns sonarRequest
        every { platformRouting.surroundRequest } returns surroundRequest
        every { platformRouting.isApaTransitionFromScanningToGuidance } returns
            isApaTransitionFromScanningToGuidance

        // GIVEN OverlayManager observes a route
        router = Router()
    }

    // RVC opening due to Rear Gear Engaged
    @Test
    fun `should return full screen when surround rvc main is requested`() {
        val expected = RouteIdentifier.SURROUND_RVC_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // RVC closing due to RVC conditions NOK
    @Test
    fun `should return none when surround closed is requested and current route is surround rvc main`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        sonarRequest.onNext(

                PlatformRouteIdentifier.SONAR_CLOSED
        )
        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, true)
        surroundRequest.onNext(request)
        Assert.assertEquals(expected, actual)
    }

    // RVC settings access, with RVC already displayed
    @Test
    fun `should return settings when settings is requested and current route is surround rvc main`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_RVC_SETTINGS
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_SETTINGS, true)

        surroundRequest.onNext(request)
        Assert.assertEquals(expected, actual)
    }

    // Following use cases are not covered by intersystem doc

    // Front Sonar Detection
    @Test
    fun `should return sonar popup when sonar request popup route`() {
        val expected = RouteIdentifier.SONAR_POPUP
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.SONAR_NOT_REAR
        sonarRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Popup close when close request received
    @Test
    fun `should return none when sonar requests none route and current route is sonar popup`() {
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.SONAR_NOT_REAR
        sonarRequest.onNext(request)

        request =
            PlatformRouteIdentifier.SONAR_CLOSED
        sonarRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Rear Sonar Detection
    @Test
    fun `should return surround rvc main when sonar request fullscreen route`() {
        val expected = RouteIdentifier.SURROUND_RVC_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.SONAR_REAR
        sonarRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Popup close when close request received
    @Test
    fun `should return none when sonar requests none route and current route is sonar fullscreen`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.SONAR_REAR
        sonarRequest.onNext(request)

        request =
            PlatformRouteIdentifier.SONAR_CLOSED
        sonarRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Sonar front detection request while rvc main being displayed
    @Test
    fun `should return surround rvc main when sonar requests popup route and current route is surround rvc main`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_RVC_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        val detectionRequest =
            PlatformRouteIdentifier.SONAR_NOT_REAR
        sonarRequest.onNext(detectionRequest)

        Assert.assertEquals(expected, actual)
    }

    // Sonar front detection request while rvc main being displayed
    @Test
    fun `should return surround sonar pop up when sonar requests popup route and current route is surround rvc main and rvc main is closed`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SONAR_POPUP
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        val detectionRequest =
            PlatformRouteIdentifier.SONAR_NOT_REAR
        sonarRequest.onNext(detectionRequest)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Sonar rear detection request while rvc main being displayed
    @Test
    fun `should return surround rvc main when sonar requests fullscreen route and current route is surround rvc main`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_RVC_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        val detectionRequest =
            PlatformRouteIdentifier.SONAR_REAR
        sonarRequest.onNext(detectionRequest)

        Assert.assertEquals(expected, actual)
    }

    // Sonar close request while rvc main being displayed
    @Test
    fun `should return surround rvc main when sonar requests close and current route is surround rvc main`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_RVC_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        val detectionRequest =
            PlatformRouteIdentifier.SONAR_CLOSED
        sonarRequest.onNext(detectionRequest)

        Assert.assertEquals(expected, actual)
    }

    // Surround close request when sonar front detection on going
    @Test
    fun `should return surround sonar popup when surround requests close and front sonar detection on going`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SONAR_POPUP
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        val detectionRequest =
            PlatformRouteIdentifier.SONAR_NOT_REAR
        sonarRequest.onNext(detectionRequest)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Surround close request when sonar rear detection on going
    @Test
    fun `should return surround rvc main when surround requests close and rear sonar detection on going`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_RVC_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        val detectionRequest =
            PlatformRouteIdentifier.SONAR_REAR
        sonarRequest.onNext(detectionRequest)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // // HFP

    // APA Scanning request
    @Test
    fun `should return rvc hfp scanning main when parking requests parking scanning`() {
        val expected = RouteIdentifier.PARKING_RVC_HFP_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // APA Switch from scanning to guidance screen (assuming cannot get guidance request from no display
    // even if it works)
    @Test
    fun `should return rvc hfp guidance main when parking requests parking guidance and current is scanning`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.PARKING_RVC_HFP_GUIDANCE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(request)

        request =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // APA close request from scanning
    @Test
    fun `should return none when parking requests none while current is scanning`() {
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        sonarRequest.onNext(PlatformRouteIdentifier.SONAR_CLOSED)
        var request =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(request)

        request =
            PlatformRouteIdentifier.PARKING_CLOSED
        apaRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // APA close request from guidance
    @Test
    fun `should return none when parking requests none while current is guidance`() {
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        sonarRequest.onNext(PlatformRouteIdentifier.SONAR_CLOSED)
        var request =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(request)

        request =
            PlatformRouteIdentifier.PARKING_CLOSED
        apaRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Surround view request while in HFP Scanning
    @Test
    fun `should return surround rvc main when surround requests main while current is scanning`() {
        val expected = RouteIdentifier.SURROUND_RVC_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var parkingRequest =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(parkingRequest)

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        parkingRequest =
            PlatformRouteIdentifier.PARKING_CLOSED
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // Surround view request while in HFP Scanning but user requested to engage rear gear
    @Test
    fun `should return rvc hfp scanning when surround requests main while current is scanning and instruction is engage rear gear`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.PARKING_RVC_HFP_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val parkingRequest =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(parkingRequest)

        isApaTransitionFromScanningToGuidance.onNext(true)

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Surround close request while in HFP Scanning
    @Test
    fun `should return rvc hfp scanning when surround requests close while current is scanning`() {
        val expected = RouteIdentifier.PARKING_RVC_HFP_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val parkingRequest =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(parkingRequest)

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Surround view request while in HFP Guidance
    @Test
    fun `should return surround parking guidance when surround requests main while current is guidance`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.PARKING_RVC_HFP_GUIDANCE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val parkingRequest =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(parkingRequest)

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Surround close request while in HFP Guidance
    @Test
    fun `should return rvc hfp guidance when surround requests close while current is guidance`() {
        val expected = RouteIdentifier.PARKING_RVC_HFP_GUIDANCE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var parkingRequest =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(parkingRequest)

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Sonar view request while in HFP scanning
    @Test
    fun `should return rvc hfp scanning when sonar requests popup while current is scanning`() {
        val expected = RouteIdentifier.PARKING_RVC_HFP_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(request)

        request =
            PlatformRouteIdentifier.SONAR_NOT_REAR
        sonarRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Sonar view request while in HFP guidance
    @Test
    fun `should return rvc hfp guidance when sonar requests fullscreen while current is guidance`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.PARKING_RVC_HFP_GUIDANCE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(request)

        request =
            PlatformRouteIdentifier.SONAR_REAR
        sonarRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Sonar close request while in HFP scanning
    @Test
    fun `should return rvc hfp scanning when sonar requests close while current is scanning`() {
        val expected = RouteIdentifier.PARKING_RVC_HFP_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(request)

        request =
            PlatformRouteIdentifier.SONAR_CLOSED
        sonarRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Sonar close request while in HFP guidance
    @Test
    fun `should return rvc hfp guidance when sonar requests close while current is guidance`() {
        val expected = RouteIdentifier.PARKING_RVC_HFP_GUIDANCE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(request)

        request =
            PlatformRouteIdentifier.SONAR_CLOSED
        sonarRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }
}