package com.renault.parkassist.service.policy

import androidx.test.filters.SmallTest
import com.renault.parkassist.repository.routing.PlatformPursuit
import com.renault.parkassist.repository.routing.PlatformRouteIdentifier
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.policy.AvmPolicy
import com.renault.parkassist.routing.policy.UpaPolicy
import io.mockk.mockk
import kotlin.test.assertFalse
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@SmallTest
class UpaPolicyTest {

    private lateinit var upaPolicy: UpaPolicy

    @Before
    fun setup() {
        upaPolicy = UpaPolicy()
    }

    @Test
    fun `should return popup request when full screen route requested with sonar fullscreen id`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.SONAR_POPUP
        val actual = upaPolicy.requestSonarRoute(
                PlatformRouteIdentifier.SONAR_REAR
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return none request when full screen route requested with no sonar fullscreen id nor sonar popup`() { // ktlint-disable max-line-length
        val expected = RouteIdentifier.NONE
        var actual: RouteIdentifier

        val platformIds = PlatformRouteIdentifier.values()

        platformIds.filter {
            it != PlatformRouteIdentifier.SONAR_REAR &&
                it != PlatformRouteIdentifier.SONAR_NOT_REAR
        }
            .forEach() {
                actual =
                    upaPolicy.requestSonarRoute(it)
                Assert.assertEquals(expected, actual)
            }
    }

    @Test
    fun `should return popup request when popup route requested with popup id`() {
        val expected = RouteIdentifier.SONAR_POPUP
        val actual = upaPolicy.requestSonarRoute(
                PlatformRouteIdentifier.SONAR_NOT_REAR
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return none request when popup route requested with no sonar id`() {
        val expected = RouteIdentifier.NONE
        var actual: RouteIdentifier

        val platformIds = PlatformRouteIdentifier.values()

        platformIds.filter {
            it != PlatformRouteIdentifier.SONAR_NOT_REAR &&
                it != PlatformRouteIdentifier.SONAR_REAR
        }
            .forEach() {
                actual =
                    upaPolicy.requestSonarRoute(it)
                Assert.assertEquals(expected, actual)
            }
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
    fun `Stop MANEUVER pursuit request should return MONITOR_OBSTACLES platform pursuit `() { // ktlint-disable max-line-length
        val actual = UpaPolicy().requestStopPursuit(
            Pursuit.MANEUVER, mockk()
        )
        Assert.assertEquals(
            listOf(PlatformPursuit.MONITOR_OBSTACLES),
            actual
        )
    }

    @Test
    fun `should not request shadow`() {
        var shadowRequest = UpaPolicy().shouldShowShadow(
            true,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_MAIN,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertFalse(shadowRequest)

        shadowRequest = UpaPolicy().shouldShowShadow(
            true,
            PlatformRouteIdentifier.PARKING_SCANNING,
            PlatformRouteIdentifier.SURROUND_CLOSED,
            PlatformRouteIdentifier.SONAR_CLOSED
        )
        assertFalse(shadowRequest)

        shadowRequest = UpaPolicy().shouldShowShadow(
            true,
            PlatformRouteIdentifier.PARKING_CLOSED,
            PlatformRouteIdentifier.SURROUND_CLOSED,
            PlatformRouteIdentifier.SONAR_REAR
        )
        assertFalse(shadowRequest)
    }
}