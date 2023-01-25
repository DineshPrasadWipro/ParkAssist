package com.renault.parkassist.service

import android.app.Application
import android.content.ServiceConnection
import com.renault.parkassist.TestBase
import com.renault.parkassist.routing.*
import com.renault.parkassist.routing.RouteIdentifier.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module

class DisplayManagerTest : TestBase() {

    private var context = mockk<Application>(relaxed = true)

    private lateinit var displayManager: IDisplayManager

    private val mockDisplayService: IDisplayService = mockk(relaxed = true)

    private val mockedRouteListenerFactory: RouteListenerFactory = mockk()

    private lateinit var routeListener: IRouteListener

    override val koinModule: Module
        get() = module {
            factory { mockedRouteListenerFactory }
        }

    @Before
    fun init() {
        mockkStatic(IDisplayService.Stub::class)
        every { IDisplayService.Stub.asInterface(any()) } returns mockDisplayService
        every { context.bindService(any(), any(), any()) } answers {
            val serviceConnection = arg(1) as ServiceConnection
            serviceConnection.onServiceConnected(null, mockk())
            true
        }
        every { mockDisplayService.registerRouteListener(any(), any()) } answers {
            routeListener = arg(0) as IRouteListener
        }
        every { mockedRouteListenerFactory.getRouteListener(any(), any(), any()) } answers {
            object : IRouteListener.Default() {
                override fun onRouteChange(routeId: RouteIdentifier?) {
                    (arg(0) as (route: RouteIdentifier?) -> Unit).invoke(routeId)
                }

                override fun onShadowRequestedChange(shadowRequested: Boolean) {
                    (arg(1) as (visibility: Boolean) -> Unit).invoke(shadowRequested)
                }

                override fun onVisibilityChange(visibility: Boolean) {
                    (arg(2) as (visibility: Boolean) -> Unit).invoke(visibility)
                }
            }
        }
        displayManager = DisplayManager(context)
    }

    @Test
    fun manager_should_connect_and_receive_route_id_updates() {
        val testRouteId = displayManager.navigationRouteId.test()
        displayManager.connect()
        routeListener.onRouteChange(SURROUND_AVM_MAIN)
        testRouteId.assertValueSequence(listOf(NONE, SURROUND_AVM_MAIN))
    }

    @Test
    fun manager_should_connect_and_receive_route_visibility_updates() {
        val testVisibility = displayManager.routeVisibility.test()
        displayManager.connect()
        routeListener.onVisibilityChange(false)
        testVisibility.assertValueSequence(listOf(true, false))
    }

    @Test
    fun manager_should_connect_and_receive_shadow_requests_updates() {
        val testVisibility = displayManager.shadowRequested.test()
        displayManager.connect()
        routeListener.onShadowRequestedChange(true)
        testVisibility.assertValueSequence(listOf(true))
    }

    @Test
    fun manager_should_send_start_pursuit_when_connected() {
        displayManager.connect()
        displayManager.startPursuit(Pursuit.PARK)
        verify(exactly = 1) { mockDisplayService.startPursuit(Pursuit.PARK) }
    }

    @Test
    fun manager_should_send_stop_pursuit_when_connected() {
        displayManager.connect()
        displayManager.stopPursuit(Pursuit.PARK)
        verify(exactly = 1) { mockDisplayService.stopPursuit(Pursuit.PARK) }
    }

    @Test
    fun manager_should_send_start_pursuit_after_connected() {
        displayManager.startPursuit(Pursuit.PARK)
        verify(exactly = 0) { mockDisplayService.startPursuit(Pursuit.PARK) }
        displayManager.connect()
        verify(exactly = 1) { mockDisplayService.startPursuit(Pursuit.PARK) }
    }

    @Test
    fun manager_should_send_stop_current_pursuit_when_connected() {
        displayManager.connect()
        displayManager.stopCurrentPursuit()
        verify(exactly = 1) { mockDisplayService.stopCurrentPursuit() }
    }

    @Test
    fun manager_should_send_stop_current_pursuit_after_connected() {
        displayManager.stopCurrentPursuit()
        verify(exactly = 0) { mockDisplayService.stopCurrentPursuit() }
        displayManager.connect()
        verify(exactly = 1) { mockDisplayService.stopCurrentPursuit() }
    }
}