package com.renault.parkassist.service

import alliance.car.windowoverlay.AllianceCarWindowOverlay
import alliance.car.windowoverlay.AllianceCarWindowOverlay.LayoutParams
import alliance.car.windowoverlay.AllianceCarWindowOverlay.LayoutParams.FLAG_MODELESS
import android.app.Application
import android.content.res.Resources
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.overlay.IAllianceCarWindowOverlayManager
import com.renault.parkassist.overlay.ScreenOverlayManager
import com.renault.parkassist.routing.RouteIdentifier
import io.mockk.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class ScreenOverlayManagerTest : TestBase() {

    private var context = mockk<Application>(relaxed = true)
    private var resources = mockk<Resources>(relaxed = true)
    private lateinit var screenOverlayManager: ScreenOverlayManager
    private val awosManager = mockk<IAllianceCarWindowOverlayManager>()
    private val overlay = mockk<AllianceCarWindowOverlay>(relaxed = true)

    private lateinit var routeEmitter: ObservableEmitter<RouteIdentifier>
    private lateinit var route: Observable<RouteIdentifier>

    private val layoutParams = mockk<LayoutParams>(relaxed = true)

    override val koinModule = module {
        factory<IAllianceCarWindowOverlayManager> { awosManager }
        factory<LayoutParams> { layoutParams }
    }

    @Before
    override fun setup() {
        super.setup()
        every { awosManager.createOverlay(any(), any()) } returns overlay
        every { context.resources } returns resources
        every { resources.getDimension(any()) } returns 100F

        route = Observable.create {
            routeEmitter = it
            it.onNext(
                RouteIdentifier.NONE
            )
        }

        // GIVEN OverlayManager observes a route
        screenOverlayManager =
            ScreenOverlayManager(context)
        screenOverlayManager.setRoute(route)
    }

    @Test
    fun `should do nothing when route and closable do not change`() {
        // WHEN route is not NONE
        routeEmitter.onNext(RouteIdentifier.SURROUND_RVC_MAIN)

        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 1) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 1) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }

        // AND route does not change
        routeEmitter.onNext(RouteIdentifier.SURROUND_RVC_MAIN)

        // THEN no awos call should occur
        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 1) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 1) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }
    }

    // Activity opening & closure
    @Test
    fun `should create new overlay when route changes from NONE to *`() {
        // WHEN route change to not NONE
        routeEmitter.onNext(RouteIdentifier.SURROUND_RVC_MAIN)

        // THEN a new overlay should be created
        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 1) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 1) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }
    }

    @Test
    fun `should dispose & remove overlay when route changes from * to NONE`() {
        // WHEN route is not NONE
        routeEmitter.onNext(RouteIdentifier.SURROUND_RVC_MAIN)
        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 1) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 1) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }

        // AND route change to NONE
        routeEmitter.onNext(RouteIdentifier.NONE)
        verify(exactly = 1) { overlay.remove() }
        verify(exactly = 1) { overlay.dispose() }
        verify(exactly = 1) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 1) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }
    }

    @Test
    fun `should not update popup overlay when route closable change`() {
        // WHEN route is POPUP and not closable
        routeEmitter.onNext(RouteIdentifier.SONAR_POPUP)
        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 1) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 1) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }

        // AND closable change
        routeEmitter.onNext(RouteIdentifier.SONAR_POPUP)

        // THEN update shall not be called
        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 1) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 1) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }

        // AND WHEN closable change back
        routeEmitter.onNext(RouteIdentifier.SONAR_POPUP)

        // THEN update shall be called
        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 1) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 1) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }
    }

    @Test
    fun `should not update overlay when route is none and closable change`() {
        // WHEN closable change
        routeEmitter.onNext(RouteIdentifier.NONE)

        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 0) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 0) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }

        // AND WHEN closable change back
        routeEmitter.onNext(RouteIdentifier.NONE)

        // THEN update shall be called
        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 0) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 0) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }
    }

    // Activity switch tests
    @Test
    fun `should close current overlay and open a new one when route change`() {
        // WHEN route is FULLSCREEN and not closable
        routeEmitter.onNext(RouteIdentifier.SURROUND_RVC_MAIN)
        verify(exactly = 0) { overlay.remove() }
        verify(exactly = 0) { overlay.dispose() }
        verify(exactly = 1) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 1) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }

        // AND route change to a new non NONE type
        routeEmitter.onNext(RouteIdentifier.SONAR_POPUP)

        // THEN should close previous overlay and create a new one
        verify(exactly = 1) { overlay.remove() }
        verify(exactly = 1) { overlay.dispose() }
        verify(exactly = 2) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 2) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }

        // WHEN back to FULLSCREEEN with closable change to true
        routeEmitter.onNext(RouteIdentifier.SURROUND_RVC_MAIN)

        // THEN update shall be called
        verify(exactly = 2) { overlay.remove() }
        verify(exactly = 2) { overlay.dispose() }
        verify(exactly = 3) { awosManager.createOverlay(any(), any()) }
        verify(exactly = 3) { overlay.add(any()) }
        verify(exactly = 0) { overlay.update(any()) }
        assertEquals(FLAG_MODELESS, layoutParams.flags and FLAG_MODELESS)
    }
}