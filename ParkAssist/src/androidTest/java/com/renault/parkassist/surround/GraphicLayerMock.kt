package com.renault.parkassist.surround

import alliance.car.surroundview.ErrorListener
import alliance.car.surroundview.GraphicLayer
import alliance.car.surroundview.GraphicLayerListener
import android.os.Handler
import android.os.Looper

class GraphicLayerMock : GraphicLayer {

    private var graphicLayerListener: GraphicLayerListener? = null

    override fun registerGraphicLayerListener(listener: GraphicLayerListener) {
        graphicLayerListener = listener
        listener.onRenderingStateChange(this, GraphicLayer.RENDERING_STOPPED)
    }

    override fun unregisterErrorListener(p0: ErrorListener?) {
    }

    override fun registerErrorListener(p0: ErrorListener?) {
    }

    override fun stopRendering() {
        graphicLayerListener?.onRenderingStateChange(this, GraphicLayer.RENDERING_STOPPED)
    }

    override fun unregisterGraphicLayerListener(p0: GraphicLayerListener?) {
        graphicLayerListener = null
    }

    override fun startRendering() {
        Handler(Looper.getMainLooper()).post {
            graphicLayerListener?.onRenderingStateChange(this, GraphicLayer.RENDERING_RUNNING)
        }
    }
}