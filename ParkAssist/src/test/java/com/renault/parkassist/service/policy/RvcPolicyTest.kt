package com.renault.parkassist.service.policy

import androidx.test.filters.SmallTest
import com.renault.parkassist.mockLogs
import com.renault.parkassist.repository.routing.PlatformPursuit
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier.*
import com.renault.parkassist.repository.surroundview.ManeuverAvailability
import com.renault.parkassist.repository.surroundview.TrailerPresence
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.policy.RvcPolicy
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@SmallTest
class RvcPolicyTest {

    private lateinit var rvcPolicy: RvcPolicy

    @Before
    fun setup() {
        mockLogs()

        rvcPolicy = RvcPolicy()
    }

    @Test
    fun `Start PARK pursuit request should always return PARK pursuit`() { // ktlint-disable max-line-length
        val expected = PlatformPursuit.PARK
        val actual = RvcPolicy().requestStartPursuit(
            Pursuit.PARK,
            ManeuverAvailability.NOT_READY,
            TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Start MANEUVER pursuit request should return null when Maneuver is NOT ready`() { // ktlint-disable max-line-length
        val expected = null
        var actual = RvcPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.NOT_READY,
            TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE
        )
        Assert.assertEquals(expected, actual)

        actual = RvcPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.NOT_READY,
            TrailerPresence.TRAILER_PRESENCE_DETECTED
        )
        Assert.assertEquals(expected, actual)

        actual = RvcPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.NOT_READY,
            TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED
        )
        Assert.assertEquals(expected, actual)

        actual = RvcPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.RESTRICTED,
            TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE
        )
        Assert.assertEquals(expected, actual)

        actual = RvcPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.RESTRICTED,
            TrailerPresence.TRAILER_PRESENCE_DETECTED
        )
        Assert.assertEquals(expected, actual)

        actual = RvcPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.RESTRICTED,
            TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Start MANEUVER pursuit request should return MANEUVER when Maneuver is ready and trailer is present`() { // ktlint-disable max-line-length
        val expected = PlatformPursuit.WATCH_TRAILER
        val actual = RvcPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.READY,
            TrailerPresence.TRAILER_PRESENCE_DETECTED
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Start MANEUVER pursuit request should return null when Maneuver is ready and trailer is NOT present`() { // ktlint-disable max-line-length
        val expected = null
        var actual = RvcPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.READY,
            TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED
        )
        Assert.assertEquals(expected, actual)

        actual = RvcPolicy().requestStartPursuit(
            Pursuit.MANEUVER,
            ManeuverAvailability.READY,
            TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Stop MANEUVER pursuit request should return WATCH_OBSTACLES platform pursuit when route is SONAR_POPUP `() { // ktlint-disable max-line-length
        listOf(RouteIdentifier.SONAR_POPUP).forEach { route ->
            val actual = RvcPolicy().requestStopPursuit(
                Pursuit.MANEUVER, route
            )
            Assert.assertEquals(
                listOf(PlatformPursuit.MONITOR_OBSTACLES),
                actual
            )
        }
    }

    @Test
    fun `Stop MANEUVER pursuit request should return MANEUVER, WATCH_OBSTACLES platform pursuit  when route is  ot SONAR_POPUP `() { // ktlint-disable max-line-length
        val actual = RvcPolicy().requestStopPursuit(
            Pursuit.MANEUVER, RouteIdentifier.SURROUND_RVC_MAIN
        )
        Assert.assertEquals(
            listOf(PlatformPursuit.MANEUVER, PlatformPursuit.MONITOR_OBSTACLES),
            actual
        )
    }

    @Test
    fun `Stop PARK pursuit request should return PARK platform pursuit `() { // ktlint-disable max-line-length
        val actual = RvcPolicy().requestStopPursuit(
            Pursuit.PARK, mockk(relaxed = true)
        )
        Assert.assertEquals(
            listOf(PlatformPursuit.PARK),
            actual
        )
    }

    // ////////////////////////////////
    // requestFullscreenRoute tests  //
    // ////////////////////////////////

    @Test
    fun `should return rvc main when full screen requested with surround main id `() {
        val expected = RouteIdentifier.SURROUND_RVC_MAIN
        val surroundRequest =
            SURROUND_MAIN

        val actual = rvcPolicy.requestSurroundRoute(surroundRequest)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return rvc trailer when full screen requested with surround trailer id `() {
        val expected = RouteIdentifier.SURROUND_RVC_TRAILER
        val surroundRequest = SURROUND_TRAILER
        val actual = rvcPolicy.requestSurroundRoute(surroundRequest)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return rvc settings when full screen requested with surround settings id `() {
        val expected = RouteIdentifier.SURROUND_RVC_SETTINGS
        val surroundRequest =
                SURROUND_SETTINGS

        val actual = rvcPolicy.requestSurroundRoute(surroundRequest)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return rvc dealer when full screen requested with surround dealer id `() {
        val expected = RouteIdentifier.SURROUND_RVC_DEALER
        val surroundRequest = SURROUND_DEALER

        val actual = rvcPolicy.requestSurroundRoute(surroundRequest)
        Assert.assertEquals(expected, actual)
    }

    // Non Trivial cases

    @Test
    fun `should return rvc main when full screen requested with closed request id but sonar is full screen`() { // ktlint-disable max-line-length
        val expectedSonar = RouteIdentifier.SURROUND_RVC_MAIN
        val expected = RouteIdentifier.NONE
        var surroundRequest =
            SURROUND_CLOSED
        val sonarRequest =
                SONAR_REAR

        val actualFromSurround = rvcPolicy.requestSurroundRoute(surroundRequest)
        val actualFromSonar = rvcPolicy.requestSonarRoute(sonarRequest)
        Assert.assertEquals(expected, actualFromSurround)
        Assert.assertEquals(expectedSonar, actualFromSonar)
    }

    // /////////////////////////
    // requestHfpRoute tests  //
    // /////////////////////////

    @Test
    fun `should return rvc hfp scanning when hfp requested with parking scanning id `() {
        val expected = RouteIdentifier.PARKING_RVC_HFP_SCANNING
        val actual = rvcPolicy.requestAutoParkRoute(
                PARKING_SCANNING
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return avm hfp guidance when hfp requested with parking guidance id `() {
        val expected = RouteIdentifier.PARKING_RVC_HFP_GUIDANCE
        val actual = rvcPolicy.requestAutoParkRoute(
                PARKING_GUIDANCE
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return avm hfp parkout when hfp requested with parking parkout id `() {
        val expected = RouteIdentifier.PARKING_RVC_HFP_PARK_OUT
        val actual = rvcPolicy.requestAutoParkRoute(
                PARKING_PARK_OUT
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return none when hfp requested with not relevant id `() {
        val expected = RouteIdentifier.NONE
        var actual: RouteIdentifier

        val platformIds = values()

        platformIds.filter { it != PARKING_SCANNING }
            .filter { it != PARKING_GUIDANCE }
            .filter { it != PARKING_PARK_OUT }.forEach {
                actual =
                    rvcPolicy.requestAutoParkRoute(it)
                Assert.assertEquals(expected, actual)
            }
    }

    @Test
    fun `should request shadow when surround route and closable true`() {
        val shadowRequest = RvcPolicy().shouldShowShadow(
            true,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_MAIN,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertTrue(shadowRequest)
    }

    @Test
    fun `should not request shadow when no surround route`() {
        val shadowRequest = RvcPolicy().shouldShowShadow(
            true,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_CLOSED,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertFalse(shadowRequest)
    }

    @Test
    fun `should not request shadow when surround route and closable false`() {
        val shadowRequest = RvcPolicy().shouldShowShadow(
            false,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_MAIN,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertFalse(shadowRequest)
    }

    @Test
    fun `should request shadow when no surround, sonar and a parking route is present`() {
        val shadowRequest = RvcPolicy().shouldShowShadow(
            false,
            PlatformRouteIdentifier.PARKING_SCANNING,
            PlatformRouteIdentifier.SURROUND_CLOSED,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertTrue(shadowRequest)
    }

    @Test
    fun `should request shadow when no surround, parking  and a sonar route is present`() {
        val shadowRequest = RvcPolicy().shouldShowShadow(
            false,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_CLOSED,
            PlatformRouteIdentifier.SONAR_REAR
        )
        assertTrue(shadowRequest)
    }

    @Test
    fun `should no request shadow when no surround, parking, sonar  route is present`() {
        val shadowRequest = RvcPolicy().shouldShowShadow(
            false,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_CLOSED,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertFalse(shadowRequest)
    }
}