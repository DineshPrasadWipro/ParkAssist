package com.renault.parkassist.camera

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.KoinComponent

@SuppressLint("Recycle")
class EvsSurfaceTexture : SurfaceTexture(false), KoinComponent {
    val surfaceAttached: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    private var lastRequestCallback: SurfaceAttachCallback? = null

    /**
     * Request to attach the surface. A callback must be provided so the surface knows when it can
     * attach the surface and so it can be requested to be detached.
     *
     * @param callback A callback to let the caller know when to detach/attach the surface
     */
    fun requestAttach(callback: SurfaceAttachCallback) {
        lastRequestCallback?.onDetachSurface()
        lastRequestCallback = callback
        // Check when surfaceAttached is set to false to know when it's ready to be attached.
        surfaceAttached.filter { !it }.take(1).subscribe {
            lastRequestCallback?.onAttachSurface()
        }
    }

    /**
     * Unregister the surface attach callback. Use in onDestroyView to avoid calling a callback
     * in a dead fragment.
     */
    fun unregisterCallback(detachCallback: SurfaceAttachCallback) {
        if (lastRequestCallback == detachCallback) {
            detachCallback.onDetachSurface()
            lastRequestCallback = null
        }
    }

    interface SurfaceAttachCallback {
        fun onAttachSurface()
        fun onDetachSurface()
    }
}