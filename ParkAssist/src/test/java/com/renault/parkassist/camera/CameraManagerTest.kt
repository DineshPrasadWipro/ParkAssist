package com.renault.parkassist.camera

import alliance.car.surroundview.GraphicLayer
import alliance.car.surroundview.GraphicLayer.RENDERING_RUNNING
import alliance.car.surroundview.GraphicLayer.RENDERING_STOPPED
import alliance.car.surroundview.GraphicLayerListener
import androidx.lifecycle.MutableLiveData
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.repository.surroundview.*
import com.renault.parkassist.routing.IDisplayManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class CameraManagerTest : TestBase() {

    private lateinit var cameraManager: CameraManager

    private val mockedSurroundState = MutableLiveData<SurroundState>()
    private val mockedGraphicLayer = mockk<GraphicLayer>(relaxed = true)
    private val evsSurfaceTexture: EvsSurfaceTexture = mockk()
    private val mockedSurfaceAttached: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val displayManager: IDisplayManager = mockk(relaxed = true)
    private val mockedRouteVisibility: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private lateinit var surroundViewRepository: SurroundViewRepository

    private var capturedGraphicLayerListener: GraphicLayerListener? = null
    override val koinModule = module {
        single<IExtSurroundViewRepository> {
            surroundViewRepository
        }
        single { evsSurfaceTexture }
        single { displayManager }
    }

    @Before
    override fun setup() {
        super.setup()
        surroundViewRepository = mockk(relaxed = true) {
            every { surroundState } returns mockedSurroundState
            every { createGraphicLayer(any()) } returns mockedGraphicLayer
        }

        every { evsSurfaceTexture.surfaceAttached } returns mockedSurfaceAttached
        every { displayManager.routeVisibility } returns mockedRouteVisibility

        capturedGraphicLayerListener = null
        every { mockedGraphicLayer.registerGraphicLayerListener(any()) } answers {
            capturedGraphicLayerListener = arg(0) as GraphicLayerListener
        }

        cameraManager = CameraManager()
        cameraManager.initialize()
    }

    @Test
    fun `should register graphicLayer listeners`() {
        verify { mockedGraphicLayer.registerGraphicLayerListener(any()) }
        verify { mockedGraphicLayer.registerErrorListener(any()) }
    }

    @Test
    fun should_start_camera_manager_on_view_request_if_stopped() {
        renderingStop()
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, true))
        verify { mockedGraphicLayer.startRendering() }
        verify { surroundViewRepository.setStatus(View.NO_DISPLAY) }
    }

    @Test
    fun should_not_start_camera_manager_on_view_request_if_started() {
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, true))
        verify { surroundViewRepository.setStatus(View.NO_DISPLAY) }
        verify(exactly = 0) { mockedGraphicLayer.startRendering() }
    }

    @Test
    fun should_start_camera_manager_on_view_state_if_started() {
        renderingStop()
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, false))
        verify { mockedGraphicLayer.startRendering() }
    }

    @Test
    fun should_not_start_camera_manager_on_view_state_if_running() {
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, false))
        verify(exactly = 0) { mockedGraphicLayer.startRendering() }
    }

    @Test
    fun should_respond_with_same_view_state() {
        mockedSurfaceAttached.onNext(true)
        mockedRouteVisibility.onNext(true)
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, false))
        verify { surroundViewRepository.setStatus(View.REAR_VIEW) }
    }

    @Test
    fun should_not_start_when_already_started() {
        mockedSurfaceAttached.onNext(true)
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, true))
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.FRONT_VIEW, true))
        verify(exactly = 2) { surroundViewRepository.setStatus(View.NO_DISPLAY) }
        verify(exactly = 1) { mockedGraphicLayer.startRendering() }
    }

    @Test
    fun should_respond_no_display_to_no_display() {
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.NO_DISPLAY, false))
        verify { surroundViewRepository.setStatus(View.NO_DISPLAY) }
    }

    @Test
    fun should_stop_when_receiving_no_display() {
        renderingStop()
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, false))
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.NO_DISPLAY, false))
        verify(exactly = 1) { mockedGraphicLayer.startRendering() }
        verify(exactly = 1) { mockedGraphicLayer.stopRendering() }
    }

    @Test
    fun should_not_stop_when_already_stopped() {
        renderingStop()
        mockedSurroundState.postValue(SurroundState(View.NO_DISPLAY, false))
        verify(exactly = 0) { mockedGraphicLayer.stopRendering() }
    }

    @Test
    fun should_wait_surface_attached_to_send_view_state() {
        mockedSurfaceAttached.onNext(false)
        mockedRouteVisibility.onNext(true)
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, false))
        verify(exactly = 0) { surroundViewRepository.setStatus(View.REAR_VIEW) }
        mockedSurfaceAttached.onNext(true)
        verify(exactly = 1) { surroundViewRepository.setStatus(View.REAR_VIEW) }
    }

    @Test
    fun should_send_view_state_if_surface_attached_and_rendering_running_and_route_visible() {
        mockedSurfaceAttached.onNext(true)
        mockedRouteVisibility.onNext(true)
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, false))
        verify(exactly = 1) { surroundViewRepository.setStatus(View.REAR_VIEW) }
    }

    @Test
    fun should_wait_rendering_running_attached_to_send_view_state() {
        mockedSurfaceAttached.onNext(true)
        mockedRouteVisibility.onNext(true)
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, false))
        verify(exactly = 0) { surroundViewRepository.setStatus(View.REAR_VIEW) }
        renderingRunning()
        verify(exactly = 1) { surroundViewRepository.setStatus(View.REAR_VIEW) }
    }

    @Test
    fun should_wait_route_visible_to_send_view_state() {
        mockedSurfaceAttached.onNext(true)
        mockedRouteVisibility.onNext(false)
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, false))
        verify(exactly = 0) { surroundViewRepository.setStatus(View.REAR_VIEW) }
        mockedRouteVisibility.onNext(true)
        verify(exactly = 1) { surroundViewRepository.setStatus(View.REAR_VIEW) }
    }

    @Test
    fun should_return_status_only_if_state_has_changed() {
        // Display pop-up view
        mockedSurfaceAttached.onNext(true)
        mockedRouteVisibility.onNext(true)
        renderingRunning()
        mockedSurroundState.postValue(SurroundState(View.POP_UP_VIEW, false))
        verify(exactly = 1) { surroundViewRepository.setStatus(View.POP_UP_VIEW) }

        // FAPK requested
        mockedSurroundState.postValue(SurroundState(View.APA_FRONT_VIEW, true))
        verify(exactly = 0) { surroundViewRepository.setStatus(View.APA_FRONT_VIEW) }
        verify(exactly = 1) { surroundViewRepository.setStatus(View.POP_UP_VIEW) }
        mockedSurfaceAttached.onNext(false)

        // Conditions to setStatus OK before the state has changed
        mockedSurfaceAttached.onNext(true)
        verify(exactly = 0) { surroundViewRepository.setStatus(View.APA_FRONT_VIEW) }
        verify(exactly = 1) { surroundViewRepository.setStatus(View.POP_UP_VIEW) }

        // State has changed, status can be sent
        mockedSurroundState.postValue(SurroundState(View.APA_FRONT_VIEW, false))
        verify(exactly = 1) { surroundViewRepository.setStatus(View.APA_FRONT_VIEW) }
    }

    private fun renderingRunning() {
        capturedGraphicLayerListener?.onRenderingStateChange(mockedGraphicLayer, RENDERING_RUNNING)
    }

    private fun renderingStop() {
        capturedGraphicLayerListener?.onRenderingStateChange(mockedGraphicLayer, RENDERING_STOPPED)
    }
}