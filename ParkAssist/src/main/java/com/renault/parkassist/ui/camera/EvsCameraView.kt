package com.renault.parkassist.ui.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.TextureView
import com.renault.parkassist.camera.EvsSurfaceTexture
import com.renault.parkassist.utility.cameraInfoLog
import org.koin.core.KoinComponent
import org.koin.core.inject

class EvsCameraView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    TextureView(context, attrs), TextureView.SurfaceTextureListener, KoinComponent {

    private val evsSurfaceTexture: EvsSurfaceTexture by inject()

    init {
        surfaceTextureListener = this
        setSurfaceTexture(evsSurfaceTexture)
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        cameraInfoLog("attach", "surface", hashCode())
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        cameraInfoLog("detach", "surface", hashCode())
        return false
    }
}