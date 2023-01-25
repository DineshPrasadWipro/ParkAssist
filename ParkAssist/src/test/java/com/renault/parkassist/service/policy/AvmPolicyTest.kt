package com.renault.parkassist.service.policy

import alliance.car.autopark.AutoPark
import androidx.test.filters.SmallTest
import com.renault.parkassist.repository.routing.PlatformPursuit
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.repository.surroundview.ManeuverAvailability
import com.renault.parkassist.repository.surroundview.TrailerPresence
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.policy.AvmPolicy
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@SmallTest
class AvmPolicyTest {

    private lateinit var avmPolicy: AvmPolicy

    @Before
    fun setup() {
        avmPolicy = AvmPolicy()
    }

    @Test
    fun `Start PARK pursuit request should always return PARK pursuit`() { // ktlint-disable max-line-length
        val expected = PlatformPursuit.PARK
        val actual = AvmPolicy().requestStartPursuit(
            Pursuit.PARK,
            ManeuverAvailability.NOT_READY,
            TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Start MANEUVER pursuit request should return null when Maneuver is NOT ready`() { // ktlint-disable max-line-length
        val expected = null
        var actual = AvmPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.NOT_READY,
            TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE
        )
        Assert.assertEquals(expected, actual)

        actual = AvmPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.NOT_READY,
            TrailerPresence.TRAILER_PRESENCE_DETECTED
        )
        Assert.assertEquals(expected, actual)

        actual = AvmPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.NOT_READY,
            TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Start MANEUVER pursuit request should return MANEUVER when Maneuver is ready`() { // ktlint-disable max-line-length
        val expected = PlatformPursuit.MANEUVER
        var actual = AvmPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.READY,
            TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE
        )
        Assert.assertEquals(expected, actual)

        actual = AvmPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.READY,
            TrailerPresence.TRAILER_PRESENCE_DETECTED
        )
        Assert.assertEquals(expected, actual)

        actual = AvmPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.READY,
            TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Start MANEUVER pursuit request should return TRAILER when Maneuver is restricted and trailer is present`() { // ktlint-disable max-line-length
        val expected = PlatformPursuit.WATCH_TRAILER
        val actual = AvmPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.RESTRICTED,
            TrailerPresence.TRAILER_PRESENCE_DETECTED
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Start MANEUVER pursuit request should return MANEUVER when Maneuver is restricted and trailer not present`() { // ktlint-disable max-line-length
        val expected = PlatformPursuit.MANEUVER
        var actual = AvmPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.RESTRICTED,
            TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED
        )
        Assert.assertEquals(expected, actual)

        actual = AvmPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.RESTRICTED,
            TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE
        )
        Assert.assertEquals(expected, actual)
    }

    // ////////////////////////////////
    // requestFullscreenRoute tests  //
    // ////////////////////////////////

    @Test
    fun `should return avm main when full screen requested with surround main id `() {
        val expected = RouteIdentifier.SURROUND_AVM_MAIN
        val actual = avmPolicy.requestSurroundRoute(
            PlatformRouteIdentifier.SURROUND_MAIN
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return avm trailer when full screen requested with surround trailer id `() {
        val expected = RouteIdentifier.SURROUND_AVM_TRAILER
        val actual = avmPolicy.requestSurroundRoute(
            PlatformRouteIdentifier.SURROUND_TRAILER
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return avm settings when full screen requested with surround settings id `() {
        val expected = RouteIdentifier.SURROUND_AVM_SETTINGS
        val actual = avmPolicy.requestSurroundRoute(
            PlatformRouteIdentifier.SURROUND_SETTINGS
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return avm dealer when full screen requested with surround dealer id `() {
        val expected = RouteIdentifier.SURROUND_AVM_DEALER
        val actual = avmPolicy.requestSurroundRoute(
            PlatformRouteIdentifier.SURROUND_DEALER
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return none when full screen requested with not relevant id `() {
        val expected = RouteIdentifier.NONE
        var actual: RouteIdentifier

        val platformIds = PlatformRouteIdentifier.values()

        platformIds.filter { it != PlatformRouteIdentifier.SURROUND_MAIN }
            .filter { it != PlatformRouteIdentifier.SURROUND_TRAILER }
            .filter { it != PlatformRouteIdentifier.SURROUND_SETTINGS }
            .filter { it != PlatformRouteIdentifier.SURROUND_POPUP }
            .filter { it != PlatformRouteIdentifier.SURROUND_DEALER }.forEach {
                actual =
                    avmPolicy.requestSurroundRoute(it)
                Assert.assertEquals(expected, actual)
            }
    }

    // ///////////////////////////
    // requestPopupRoute tests  //
    // ///////////////////////////

    @Test
    fun `should return avm popup when popup requested with surround popup id `() {
        val expected = RouteIdentifier.SURROUND_AVM_POPUP
        val actual = avmPolicy.requestSurroundRoute(
            PlatformRouteIdentifier.SURROUND_POPUP
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return none when popup requested with no popup id `() {
        val expected = RouteIdentifier.NONE
        var actual: RouteIdentifier

        val platformIds = PlatformRouteIdentifier.values()

        platformIds.filter { it != PlatformRouteIdentifier.SURROUND_POPUP }.forEach {
            actual =
                avmPolicy.requestSonarRoute(it)
            Assert.assertEquals(expected, actual)
        }
    }

    // /////////////////////////
    // requestHfpRoute tests  //
    // /////////////////////////

    @Test
    fun `should return avm hfp scanning when hfp requested with parking scanning id `() {
        val expected = RouteIdentifier.PARKING_AVM_HFP_SCANNING
        avmPolicy = AvmPolicy(AutoPark.FEATURE_HFP)
        val actual = avmPolicy.requestAutoParkRoute(
            PlatformRouteIdentifier.PARKING_SCANNING
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return avm hfp guidance when hfp requested with parking guidance id `() {
        val expected = RouteIdentifier.PARKING_AVM_HFP_GUIDANCE
        avmPolicy = AvmPolicy(AutoPark.FEATURE_HFP)
        val actual = avmPolicy.requestAutoParkRoute(
            PlatformRouteIdentifier.PARKING_GUIDANCE
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return avm hfp parkout when hfp requested with parking parkout id `() {
        val expected = RouteIdentifier.PARKING_AVM_HFP_PARK_OUT
        avmPolicy = AvmPolicy(AutoPark.FEATURE_HFP)
        val actual = avmPolicy.requestAutoParkRoute(
            PlatformRouteIdentifier.PARKING_PARK_OUT
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return none when hfp requested with not relevant id `() {
        val expected = RouteIdentifier.NONE
        var actual: RouteIdentifier

        val platformIds = PlatformRouteIdentifier.values()

        platformIds.filter { it != PlatformRouteIdentifier.PARKING_SCANNING }
            .filter { it != PlatformRouteIdentifier.PARKING_GUIDANCE }
            .filter { it != PlatformRouteIdentifier.PARKING_PARK_OUT }.forEach {
                avmPolicy = AvmPolicy(AutoPark.FEATURE_HFP)
                actual =
                    avmPolicy.requestAutoParkRoute(it)
                Assert.assertEquals(expected, actual)
            }
    }

    // //////////////////////////
    // requestFapkRoute tests  //
    // //////////////////////////

    @Test
    fun `should return fapk scanning when fapk requested with parking scanning id `() {
        val expected = RouteIdentifier.PARKING_FAPK_SCANNING
        avmPolicy = AvmPolicy(AutoPark.FEATURE_FAPK)
        val actual = avmPolicy.requestAutoParkRoute(
            PlatformRouteIdentifier.PARKING_SCANNING
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return fapk guidance when fapk requested with parking guidance id `() {
        val expected = RouteIdentifier.PARKING_FAPK_GUIDANCE
        avmPolicy = AvmPolicy(AutoPark.FEATURE_FAPK)
        val actual = avmPolicy.requestAutoParkRoute(
            PlatformRouteIdentifier.PARKING_GUIDANCE
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return fapk parkout when fapk requested with parking parkout id `() {
        val expected = RouteIdentifier.PARKING_FAPK_PARK_OUT
        avmPolicy = AvmPolicy(AutoPark.FEATURE_FAPK)
        val actual = avmPolicy.requestAutoParkRoute(
            PlatformRouteIdentifier.PARKING_PARK_OUT
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return none when fapk requested with not relevant id `() {
        val expected = RouteIdentifier.NONE
        var actual: RouteIdentifier

        val platformIds = PlatformRouteIdentifier.values()

        platformIds.filter { it != PlatformRouteIdentifier.PARKING_SCANNING }
            .filter { it != PlatformRouteIdentifier.PARKING_GUIDANCE }
            .filter { it != PlatformRouteIdentifier.PARKING_PARK_OUT }.forEach {
                avmPolicy = AvmPolicy(AutoPark.FEATURE_FAPK)
                actual =
                    avmPolicy.requestAutoParkRoute(it)
                Assert.assertEquals(expected, actual)
            }
    }

    @Test
    fun `isSurroundRequest should return false if id is surround camera fapk view`() {
        val expected = false
        var actual = true

        avmPolicy = AvmPolicy(AutoPark.FEATURE_FAPK)

        actual = avmPolicy.isSurroundRequest(PlatformRouteIdentifier.SURROUND_FAPK)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `isSurroundRequest should return false if id is surround closed`() {
        val expected = false
        var actual: Boolean

        avmPolicy = AvmPolicy(AutoPark.FEATURE_FAPK)

        actual = avmPolicy.isSurroundRequest(PlatformRouteIdentifier.SURROUND_CLOSED)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `isSurroundRequest should return true if id not surround closed or fapk views`() {
        val expected = true
        var actual: Boolean

        avmPolicy = AvmPolicy(AutoPark.FEATURE_FAPK)

        listOf(
            PlatformRouteIdentifier.SURROUND_MAIN,
            PlatformRouteIdentifier.SURROUND_POPUP,
            PlatformRouteIdentifier.SURROUND_DEALER,
            PlatformRouteIdentifier.SURROUND_SETTINGS,
            PlatformRouteIdentifier.SURROUND_TRAILER
        ).forEach {
            actual = avmPolicy.isSurroundRequest(it)
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `Stop MANEUVER pursuit request should return MANEUVER platform pursuit `() { // ktlint-disable max-line-length
        val actual = AvmPolicy().requestStopPursuit(
            Pursuit.MANEUVER, mockk()
        )
        Assert.assertEquals(
            listOf(PlatformPursuit.MANEUVER),
            actual
        )
    }

    @Test
    fun `Stop PARK pursuit request should return PARK platform pursuit `() { // ktlint-disable max-line-length
        val actual = AvmPolicy().requestStopPursuit(
            Pursuit.PARK, mockk()
        )
        Assert.assertEquals(
            listOf(PlatformPursuit.PARK),
            actual
        )
    }

    @Test
    fun `should debounce if previous route is APA, current is closable SURROUND `() {
        val debounce = AvmPolicy().shouldDebounce(
            RouteIdentifier.PARKING_AVM_HFP_SCANNING,
            RouteIdentifier.SURROUND_AVM_MAIN,
            true
        )
        Assert.assertEquals(true, debounce)
    }

    @Test
    fun `should not debounce if previous route is APA, current is non closable SURROUND `() {
        val debounce = AvmPolicy().shouldDebounce(
            RouteIdentifier.PARKING_FAPK_PARK_OUT,
            RouteIdentifier.SURROUND_AVM_MAIN,
            false
        )
        Assert.assertEquals(false, debounce)
    }

    @Test
    fun `should not debounce if previous route is POPUP, current is closable SURROUND `() {
        val debounce = AvmPolicy().shouldDebounce(
            RouteIdentifier.SONAR_POPUP,
            RouteIdentifier.SURROUND_AVM_MAIN,
            true
        )
        Assert.assertEquals(false, debounce)
    }

    @Test
    fun `should request shadow when surround route and closable true`() {
        val shadowRequest = AvmPolicy().shouldShowShadow(
            true,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_MAIN,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertTrue(shadowRequest)
    }

    @Test
    fun `should not request shadow when no surround route`() {
        val shadowRequest = AvmPolicy().shouldShowShadow(
            true,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_CLOSED,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertFalse(shadowRequest)
    }

    @Test
    fun `should not request shadow when surround route and closable false`() {
        val shadowRequest = AvmPolicy().shouldShowShadow(
            false,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_MAIN,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertFalse(shadowRequest)
    }
}