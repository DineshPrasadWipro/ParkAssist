package com.renault.parkassist.service

import alliance.car.autopark.AutoPark
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.overlay.ParkingDialogOverlayManager
import com.renault.parkassist.overlay.ScreenOverlayManager
import com.renault.parkassist.overlay.SurroundDialogOverlayManager
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.repository.routing.PlatformRoutingBridge
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.Router
import com.renault.parkassist.routing.policy.AvmPolicy
import com.renault.parkassist.routing.policy.IPolicy
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

// This class purpose is to test use case documented in
// F-M10-10_AVMDigital_intersystem_v32.3.pdf
// Plus few additional tests deemed useful
@SmallTest
class RouterAvmIntersystemTest : TestBase() {

    private lateinit var router: Router
    private val overlayManager = mockk<ScreenOverlayManager>(relaxed = true)
    private val overlaySurroundWarningManager = mockk<SurroundDialogOverlayManager>(relaxed = true)
    private val overlayParkingWarningManager = mockk<ParkingDialogOverlayManager>(relaxed = true)
    private val platformRouting = mockk<PlatformRoutingBridge>(relaxed = true)

    private lateinit var apaRequest: BehaviorSubject<PlatformRouteIdentifier>
    private lateinit var sonarRequest: BehaviorSubject<PlatformRouteIdentifier>
    private lateinit var surroundRequest: BehaviorSubject<Pair<PlatformRouteIdentifier, Boolean>>
    private lateinit var isApaTransitionFromScanningToGuidance: BehaviorSubject<Boolean>

    override val koinModule = module {
        factory<IPolicy> { AvmPolicy(AutoPark.FEATURE_FAPK) }
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
        apaRequest.onNext(
            PlatformRouteIdentifier.PARKING_CLOSED
        )
        sonarRequest.onNext(
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        surroundRequest.onNext(
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, true)
        )
        isApaTransitionFromScanningToGuidance.onNext(false)

        every { platformRouting.apaScreenRoutingRequest } returns apaRequest
        every { platformRouting.sonarScreenRoutingRequest } returns sonarRequest
        every { platformRouting.surroundRequest } returns surroundRequest
        every { platformRouting.isApaTransitionFromScanningToGuidance } returns
            isApaTransitionFromScanningToGuidance

        // GIVEN OverlayManager observes a route
        router = Router()
    }

    // Opening AVM Popup due to UPA/FKP obstacles
    @Test
    fun `should return surround avm popup when surround popup is requested`() {
        val expected = RouteIdentifier.SURROUND_AVM_POPUP
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // Closing of AVM Popup due to SONAR request (eg : no obstacles in normal mode)
    @Test
    fun `should return none when none is requested and current is popup`() {
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, false)

        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM opening due to user request, by pushing the AVM button
    @Test
    fun `should return surround avm main when surround main is requested with closable true`() {
        val expected = RouteIdentifier.SURROUND_AVM_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)

        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM activation when AVM Popup already displayed
    @Test
    fun `should return surround avm main when surround main is requested and current is popup`() {
        val expected = RouteIdentifier.SURROUND_AVM_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)

        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM opening due to AVM ECU request
    @Test
    fun `should return surround avm main when surround main is requested with closable false`() {
        val expected = RouteIdentifier.SURROUND_AVM_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM Closing
    @Test
    fun `should return none when surround close is requested and current is surround avm main`() {
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, false)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM closing but UPA/FKP obstacles still present (AVM Popup)
    @Test
    fun `should return avm popup when surround close is requested and obstacle detection still ongoing`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_POPUP
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, false)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM settings activation in settings menu without RearView displayed before
    @Test
    fun `should return avm settings when surround settings is requested`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_SETTINGS
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_SETTINGS, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM settings activation in settings menu, with AVM popup
    @Test
    fun `should return avm settings when surround settings is requested and current is avm popup`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_SETTINGS
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_SETTINGS, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM settings activation in AVM screen
    @Test
    fun `should return avm settings when surround settings is requested and current is avm main`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_SETTINGS
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

    // AVM settings deactivation, after settings activation in settings menu to Back button
    @Test
    fun `should return none when surround close is requested and current is settings`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_SETTINGS, true)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_CLOSED, false)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM settings FV deactivation, after settings activation in AVM screen due to Back button
    @Test
    fun `should return avm main when surround avm is requested and current is settings`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_MAIN
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

        request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // AVM settings FV deactivation, after settings activation in AVM screen due to Back button
    @Test
    fun `should return avm popup when surround popup is requested and current is settings`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_POPUP
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            Pair(PlatformRouteIdentifier.SURROUND_SETTINGS, true)
        surroundRequest.onNext(request)

        request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // FAPk activation without AVM/Popup before
    @Test
    fun `should return fapk scanning when apa scanning is requested`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.PARKING_FAPK_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val request =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // FAPk activation WITH AVM already displayed and launched by user
    @Test
    fun `should return fapk scanning when apa scanning is requested and current is closable avm main`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.PARKING_FAPK_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, true)
        surroundRequest.onNext(request)

        val parkingRequest =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // FAPk activation WITH AVM already displayed and launched by ECU
    @Test
    fun `should return surround avm main when apa scanning is requested and current is non closable avm main`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        val parkingRequest =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // FAPk activation when Popup layout already displayed
    @Test
    fun `should return fapk scanning when apa scanning is requested and current is avm popup`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.PARKING_FAPK_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        val parkingRequest =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // FAPk Transition Scan/Manoeuvre (NB: park out is handled internally by guidance views)
    @Test
    fun `should return fapk guidance when apa guidance is requested and current fapk scanning`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.PARKING_FAPK_GUIDANCE
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

    // FAPk deactivation without UPA/FKP obstacles & without RearGear Engaged
    @Test
    fun `should return none when apa requests none and current is fapk guidance`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var request =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(request)

        request =
            PlatformRouteIdentifier.PARKING_CLOSED
        apaRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // FAPk deactivation WITH RearGear Engaged
    @Test
    fun `should return surround avm main when apa requests none and current is fapk guidance and surround avm route is active`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var parkingRequest =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(parkingRequest)

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        parkingRequest =
            PlatformRouteIdentifier.PARKING_CLOSED
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // FAPk deactivation WITH UPA/FKP obstacles & without RearGear Engaged
    @Test
    fun `should return surround avm popup when apa requests none and current is fapk guidance and surround popup route is active`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_POPUP
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        var parkingRequest =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(parkingRequest)

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        parkingRequest =
            PlatformRouteIdentifier.PARKING_CLOSED
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // Rear gear engaged while in FAPK scanning but with instruction to engage rear gear
    @Test
    fun `should return avm fapk scanning when surround requests main and current is scanning with engage rear gear instruction`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.PARKING_FAPK_SCANNING
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

    // APA.SONAR activation without UPA/FKP obstacles
    @Test
    fun `should return avm hfp scanning when apa requests scanning`() { // ktlint-disable max-line-length
        setHfpConf()
        val expected = RouteIdentifier.PARKING_AVM_HFP_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        val request =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // APA.SONAR activation WITH UPA/FKP obstacles
    @Test
    fun `should return avm hfp scanning when apa requests scanning and current is avm popup`() { // ktlint-disable max-line-length
        setHfpConf()
        val expected = RouteIdentifier.PARKING_AVM_HFP_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }
        var request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        val parkingRequest =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // APA.SONAR activation WITH closable AVM already displayed
    @Test
    fun `should return avm hfp scanning when apa requests scanning and current is closable avm main `() { // ktlint-disable max-line-length
        setHfpConf()
        val expected = RouteIdentifier.PARKING_AVM_HFP_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }
        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, true)
        surroundRequest.onNext(request)

        val parkingRequest =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // APA.SONAR activation WITH non closable AVM already displayed
    @Test
    fun `should return surround avm main when apa requests scanning and current is non closable avm main `() { // ktlint-disable max-line-length
        setHfpConf()
        val expected = RouteIdentifier.SURROUND_AVM_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }
        var request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        val parkingRequest =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // APA.SONAR deactivation Without rear gear engaged, and without UPA/FKP obstacles
    @Test
    fun `should return none when apa requests none and current is hfp scanning`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.NONE
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }
        var request =
            PlatformRouteIdentifier.PARKING_SCANNING
        apaRequest.onNext(request)

        request =
            PlatformRouteIdentifier.PARKING_CLOSED
        apaRequest.onNext(request)

        Assert.assertEquals(expected, actual)
    }

    // APA.SONAR deactivation WITH AVM activated Without rear gear engaged, and WITH UPA/FKP obstacles
    // NB: APA.SONAR deactivation WITH AVM activated = deactivation while in guidance
    @Test
    fun `should return avm popup when apa requests none and current is hfp guidance and avm popup is active`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_POPUP
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }
        var parkingRequest =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(parkingRequest)

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        surroundRequest.onNext(request)

        parkingRequest =
            PlatformRouteIdentifier.PARKING_CLOSED
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // APA.SONAR deactivation WITH AVM activated WITH rear gear engaged
    @Test
    fun `should return surround avm main when apa requests none and current rear gear engaged`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SURROUND_AVM_MAIN
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }
        var parkingRequest =
            PlatformRouteIdentifier.PARKING_GUIDANCE
        apaRequest.onNext(parkingRequest)

        val request =
            Pair(PlatformRouteIdentifier.SURROUND_MAIN, false)
        surroundRequest.onNext(request)

        parkingRequest =
            PlatformRouteIdentifier.PARKING_CLOSED
        apaRequest.onNext(parkingRequest)

        Assert.assertEquals(expected, actual)
    }

    // Rear gear engaged while in APA scanning but with instruction to engage rear gear
    @Test
    fun `should return avm hfp scanning when surround requests main and current is scanning with engage rear gear instruction`() { // ktlint-disable max-line-length
        setHfpConf()
        val expected = RouteIdentifier.PARKING_AVM_HFP_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        apaRequest.onNext(PlatformRouteIdentifier.PARKING_SCANNING)

        isApaTransitionFromScanningToGuidance.onNext(true)

        surroundRequest.onNext(Pair(PlatformRouteIdentifier.SURROUND_MAIN, false))

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return fapk scanning when scanning request from surround avm popup`() { // ktlint-disable max-line-length
        setFapkConf()
        val expected = RouteIdentifier.PARKING_FAPK_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }
        surroundRequest.onNext(

            Pair(PlatformRouteIdentifier.SURROUND_POPUP, true)
        )
        apaRequest.onNext(

            PlatformRouteIdentifier.PARKING_SCANNING
        )

        Assert.assertEquals(expected, actual)
    }

    // Additional Tests

    @Test
    fun `should return fapk scanning route from closable surround main after router ignored a non closable surround apa view`() { // ktlint-disable max-line-length
        setFapkConf()
        val expected = RouteIdentifier.PARKING_FAPK_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        surroundRequest.onNext(Pair(PlatformRouteIdentifier.SURROUND_FAPK, false))
        surroundRequest.onNext(Pair(PlatformRouteIdentifier.SURROUND_MAIN, true))
        apaRequest.onNext(PlatformRouteIdentifier.PARKING_SCANNING)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return fapk scanning route from closable popup main after router ignored a non closable surround apa view`() { // ktlint-disable max-line-length
        setFapkConf()
        val expected = RouteIdentifier.PARKING_FAPK_SCANNING
        var actual = RouteIdentifier.NONE

        router.screenRequest.subscribe {
            actual = it
        }

        surroundRequest.onNext(Pair(PlatformRouteIdentifier.SURROUND_FAPK, false))
        surroundRequest.onNext(Pair(PlatformRouteIdentifier.SURROUND_POPUP, true))
        apaRequest.onNext(PlatformRouteIdentifier.PARKING_SCANNING)

        Assert.assertEquals(expected, actual)
    }

    private fun setHfpConf() {
        koin.loadModules(listOf(module {
            factory<IPolicy>(override = true) {
                AvmPolicy(AutoPark.FEATURE_HFP)
            }
        }))
        router = Router()
    }

    private fun setFapkConf() {
        koin.loadModules(listOf(module {
            factory<IPolicy>(override = true) {
                AvmPolicy(AutoPark.FEATURE_FAPK)
            }
        }))
        router = Router()
    }
}