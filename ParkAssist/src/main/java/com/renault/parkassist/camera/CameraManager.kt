package com.renault.parkassist.camera

import alliance.car.surroundview.ErrorListener
import alliance.car.surroundview.GraphicLayer
import alliance.car.surroundview.GraphicLayer.RENDERING_RUNNING
import alliance.car.surroundview.GraphicLayerListener
import alliance.car.surroundview.PixelRectangle
import android.view.Surface
import androidx.lifecycle.Observer
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.routing.IDisplayManager
import com.renault.parkassist.utility.cameraInfoLog
import com.renault.parkassist.utility.cameraWarningLog
import com.renault.parkassist.utility.cameraWtfLog
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.withLatestFrom
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class CameraManager : KoinComponent {

    private val displayManager: IDisplayManager by inject()

    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val evsSurfaceTexture: EvsSurfaceTexture by inject()

    private val displayState: BehaviorSubject<SurroundState> = BehaviorSubject.create()

    private val viewRequest = displayState.filter {
        it.isRequest
    }
        .map { it.view }

    private val noDisplay = displayState.filter {
        !it.isRequest && it.view == View.NO_DISPLAY
    }
        .map { it.view }

    private val viewState = displayState.filter {
        !it.isRequest && it.view != View.NO_DISPLAY
    }
        .map { it.view }

    private val renderingState: BehaviorSubject<Int> = BehaviorSubject.create()

    private val rendering = renderingState
        .map { it == GraphicLayer.RENDERING_RUNNING }
        .startWithItem(false)

    private val shouldSetStatus = Observables.combineLatest(
        evsSurfaceTexture.surfaceAttached,
        displayManager.routeVisibility,
        rendering
    ).map { (surfaceAttached, routeVisible, rendering) ->
        surfaceAttached && routeVisible && rendering
    }.distinctUntilChanged()

    private val graphicLayerListener = object : GraphicLayerListener {

        override fun onCameraViewSizeChange(gl: GraphicLayer, rect: PixelRectangle) {
            // nothing to do (planned to be deprecated, then removed)
        }

        override fun onRenderingStateChange(
            gl: GraphicLayer,
            @GraphicLayer.RenderingState state: Int
        ) {
            cameraInfoLog(
                "rendering", "state = " +
                    "${if (state == RENDERING_RUNNING) "running" else "stopped"}"
            )
            renderingState.onNext(state)
        }
    }

    private val graphicLayerErrorListener =
        ErrorListener { errorCode, errorMessage ->
            cameraWarningLog(
                "graphic layer error. code: $errorCode message: $errorMessage"
            )
        }

    private val graphicLayer = surroundRepository.createGraphicLayer(Surface(evsSurfaceTexture))

    private val surroundObserver: Observer<SurroundState> = Observer { state ->
        displayState.onNext(state)
    }

    init {
        viewState.withLatestFrom(rendering).subscribe { (_, rendering) ->
            if (!rendering) startRendering()
        }
        noDisplay.withLatestFrom(rendering).subscribe { (view, rendering) ->
            if (rendering) stopRendering()
            setStatus(view)
        }
        viewRequest.withLatestFrom(rendering).subscribe { (_, rendering) ->
            if (!rendering) startRendering()
            setStatus(View.NO_DISPLAY)
        }

        Observables.combineLatest(shouldSetStatus, displayState).filter { (_, displayState) ->
            !displayState.isRequest && displayState.view != View.NO_DISPLAY
        }.subscribe { (status, displayState) ->
            if (status) setStatus(displayState.view)
        }
    }

    private fun setStatus(@View view: Int) {
        cameraInfoLog("setStatus", view.toString())
        surroundRepository.setStatus(view)
    }

    private fun startRendering() {
        graphicLayer.startRendering()
        cameraInfoLog("start", "rendering")
    }

    private fun stopRendering() {
        graphicLayer.stopRendering()
        cameraInfoLog("stop", "rendering")
    }

    private fun setSurroundStateListener() {
        surroundRepository.surroundState.observeForever(surroundObserver)
    }

    private fun unsetSurroundStateListener() {
        surroundRepository.surroundState.removeObserver(surroundObserver)
    }

    private fun setGraphicLayerListener() {
        try {
            graphicLayer.registerGraphicLayerListener(graphicLayerListener)
            graphicLayer.registerErrorListener(graphicLayerErrorListener)
        } catch (e: Exception) {
            cameraWtfLog("Cannot register graphic layer listeners")
        }
    }

    fun unsetGraphicLayerListener() {
        try {
            graphicLayer.unregisterErrorListener(graphicLayerErrorListener)
            graphicLayer.unregisterGraphicLayerListener(graphicLayerListener)
        } catch (e: Exception) {
            cameraWtfLog("Cannot unregister graphic layer listeners")
        }
    }

    fun initialize() {
        cameraInfoLog("initialize", "listeners")
        setGraphicLayerListener()
        setSurroundStateListener()
    }

    fun clear() {
        cameraInfoLog("clear", "listeners")
        unsetSurroundStateListener()
        unsetGraphicLayerListener()
        // Required by Surround View service to release the current client
        if (graphicLayer != null) stopRendering()
        setStatus(View.NO_DISPLAY)
    }
}