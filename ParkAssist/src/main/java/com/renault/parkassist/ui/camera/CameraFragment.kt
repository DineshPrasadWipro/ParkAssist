package com.renault.parkassist.ui.camera

import alliancex.arch.core.logger.logD
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.fragment.app.commit
import com.renault.parkassist.R
import com.renault.parkassist.camera.EvsSurfaceTexture
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.ui.camera.CameraOverlayFragment.LayoutOverlay
import com.renault.parkassist.utility.extractLocationOnScreen
import com.renault.parkassist.utility.cameraInfoLog
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.viewmodel.camera.AvmCameraSize
import com.renault.parkassist.viewmodel.camera.CameraOverlay
import com.renault.parkassist.viewmodel.camera.ExtCameraViewModelBase
import kotlinx.android.synthetic.main.fragment_camera.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.inject

class CameraFragment : FragmentBase() {

    override val layout: Int = R.layout.fragment_camera

    private val cameraViewModel: ExtCameraViewModelBase by viewModel()

    private val evsSurfaceTexture: EvsSurfaceTexture by inject()

    private val onSurfaceDetachRequest = object : EvsSurfaceTexture.SurfaceAttachCallback {
        override fun onAttachSurface() {
            cameraInfoLog("add EvsCameraView", "fragment", this@CameraFragment.hashCode())
            elt_camera_view.addView(EvsCameraView(requireContext()))
            evsSurfaceTexture.surfaceAttached.onNext(true)
        }

        override fun onDetachSurface() {
            cameraInfoLog("detach EvsCameraView", "fragment", this@CameraFragment.hashCode())
            elt_camera_view.removeAllViews()
            evsSurfaceTexture.surfaceAttached.onNext(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraInfoLog("viewCreated", "fragment", this@CameraFragment.hashCode())
        evsSurfaceTexture.requestAttach(onSurfaceDetachRequest)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBind() {
        elt_camera_view.setOnTouchListener(
            TouchInterceptor(
                object :
                    TouchInterceptor.TouchListener {
                    override fun screenPress(finger: Int, x: Float, y: Float) {
                        logD { "screenPress location  x:$x, y:$y" }
                        cameraViewModel.screenPress(finger, x, y)
                    }

                    override fun screenRelease(finger: Int) {
                        cameraViewModel.screenRelease(finger)
                    }
                })
        )

        cameraViewModel.cameraError.observe {
            showError(it)
        }

        cameraViewModel.cameraSize.observe {
            setSize(it)
        }

        cameraViewModel.cameraGravity.observe {
            setGravity(it)
        }

        cameraViewModel.cameraOverlay.observe {
            setOverlay(it)
        }

        cameraViewModel.cameraVisible.observe { visible ->
            if (visible) {
                // Use alpha instead of visibility to keep surface sizing available for EVS service
                elt_camera_view.alpha = 1f
                elt_camera_overlay_container.visibility = View.VISIBLE
                loading_anim_frame.visibility = View.GONE
                loading_anim.cancelAnimation()
            } else {
                loading_anim.playAnimation()
                loading_anim_frame.visibility = View.VISIBLE
                elt_camera_overlay_container.visibility = View.INVISIBLE
                // Use alpha instead of visibility to keep surface sizing available for EVS service
                elt_camera_view.alpha = 0f
            }
        }

        cameraViewModel.updateCameraViewPosition.observe {
            if (it) updateCameraViewPosition()
        }
    }

    override fun onStart() {
        super.onStart()
        cameraInfoLog("onStart", "fragment", this@CameraFragment.hashCode())
    }

    override fun onPause() {
        super.onPause()
        cameraInfoLog("onPause", "fragment", this@CameraFragment.hashCode())
    }

    override fun onStop() {
        super.onStop()
        cameraInfoLog("onStop", "fragment", this@CameraFragment.hashCode())
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraInfoLog("onDestroy", "fragment", this@CameraFragment.hashCode())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        evsSurfaceTexture.unregisterCallback(onSurfaceDetachRequest)
    }

    private fun updateCameraViewPosition() {
        elt_camera_view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val (x0, y0) = elt_camera_view.extractLocationOnScreen()
                val x1 = x0 + elt_camera_view.width - 1
                val y1 = y0 + elt_camera_view.height - 1
                logD {
                    "updateCameraViewPosition Android coord: x0=$x0 y0=$y0 " +
                        "x1=$x1 y1=$y1 "
                }
                logD {
                    "updateCameraViewPosition Surround coord: x0=$x0 y1=$y1" +
                        "x1=$x1 y0=$y0 "
                }
                cameraViewModel.setCameraViewPosition(x0, y1, x1, y0)
                elt_camera_view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
        )
    }

    private fun showError(show: Boolean) {
        camera_error_overlay.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setSize(size: AvmCameraSize) {
        with(camera_container) {
            layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                val orientedSize =
                    if (resources.configuration.orientation ==
                        Configuration.ORIENTATION_LANDSCAPE
                    ) size.land else size.port
                if (orientedSize.width != width)
                    width = orientedSize.width
                if (orientedSize.height != height)
                    height = orientedSize.height
                logD { "Fixed cameraSize w:$width, h:$height" }
            }
        }
    }

    private fun setGravity(gravityValue: Int) {
        with(camera_container) {
            layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                if (gravityValue != gravity)
                    gravity = gravityValue
            }
        }
    }

    private fun setOverlay(@CameraOverlay cameraOverlay: Int) {
        logD { "set overlay : $cameraOverlay" }
        val overlay = when (cameraOverlay) {
            CameraOverlay.RVC_STANDARD -> LayoutOverlay.RVC_STANDARD
            CameraOverlay.AVM_STANDARD -> LayoutOverlay.AVM_STANDARD
            CameraOverlay.AVM_PANORAMIC -> LayoutOverlay.AVM_PANORAMIC
            CameraOverlay.AVM_SIDES -> LayoutOverlay.AVM_SIDES
            CameraOverlay.AVM_POPUP -> LayoutOverlay.AVM_POPUP
            CameraOverlay.AVM_3_D -> LayoutOverlay.AVM_3_D
            CameraOverlay.NONE -> LayoutOverlay.NONE
            CameraOverlay.TRAILER -> LayoutOverlay.TRAILER
            CameraOverlay.RVC_SETTINGS -> LayoutOverlay.RVC_SETTINGS
            // AVM settings use same indicator overlays as AVM standard view
            // TODO Check if any specific needs for avm_settings-land layout with designers
            CameraOverlay.AVM_SETTINGS -> LayoutOverlay.AVM_STANDARD
            CameraOverlay.AVM_APA -> LayoutOverlay.AVM_APA
            CameraOverlay.RVC_APA -> LayoutOverlay.RVC_APA
            else -> {
                errorLog(
                    "camera", "unsupported overlay $cameraOverlay",
                    "not changing overlay"
                )
                return
            }
        }
        childFragmentManager.commit {
            replace(
                R.id.elt_camera_overlay_container,
                CameraOverlayFragment.newInstance(overlay.layoutResId)
            )
        }
    }
}