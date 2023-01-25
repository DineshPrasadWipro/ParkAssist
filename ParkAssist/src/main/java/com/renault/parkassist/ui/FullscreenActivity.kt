package com.renault.parkassist.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.TraceCompat.beginSection
import androidx.core.os.TraceCompat.endSection
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.renault.car.ui.components.RenaultBaseActivity
import com.renault.parkassist.R
import com.renault.parkassist.camera.EvsSurfaceTexture
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.utility.displayInfoLog
import com.renault.parkassist.utility.setVisible
import com.renault.parkassist.viewmodel.route.RouteViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

@SuppressLint("Registered")
class FullscreenActivity : RenaultBaseActivity(
    false,
    false
), KoinComponent {
    private val routeVm: RouteViewModel by viewModels()
    private val pursuitVm: PursuitViewModel by viewModels()

    private val evsSurfaceTexture: EvsSurfaceTexture by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        beginSection("FullscreenActivity onCreate")
        super.onCreate(savedInstanceState)

        evsSurfaceTexture.surfaceAttached.onNext(false)

        routeVm.routeVisibility.observe(this, Observer {
            beginSection("Visibility = ${if (it) "true" else "false"}")
            displayInfoLog("set visibility", "$it")
            window.setBackgroundDrawableResource(
                if (it) R.color.rc_surface
                else R.color.rc_transparent
            )
            findViewById<ConstraintLayout>(R.id.renault_root_view).setVisible(it)
            endSection()
        })

        routeVm.navigationRoute.observe(this, Observer { routeId: RouteIdentifier ->
            displayInfoLog("navigating to", "$routeId")
            when (routeId) {
                RouteIdentifier.SURROUND_AVM_MAIN ->
                    navigate(R.id.avmFragment)
                RouteIdentifier.SURROUND_AVM_TRAILER,
                RouteIdentifier.SURROUND_RVC_TRAILER ->
                    navigate(R.id.trailerFragment)
                RouteIdentifier.SURROUND_RVC_MAIN -> {
                    navigate(R.id.rvcFragment)
                }
                RouteIdentifier.SURROUND_RVC_SETTINGS ->
                    navigate(R.id.rvcSettings)
                RouteIdentifier.SURROUND_AVM_SETTINGS ->
                    navigate(R.id.avmSettings)
                RouteIdentifier.SURROUND_RVC_DEALER,
                RouteIdentifier.SURROUND_AVM_DEALER ->
                    navigate(R.id.dealerFragment)
                RouteIdentifier.PARKING_RVC_HFP_PARK_OUT,
                RouteIdentifier.PARKING_RVC_HFP_SCANNING ->
                    navigate(R.id.sonarHfpScanningFragment)
                RouteIdentifier.PARKING_AVM_HFP_PARK_OUT,
                RouteIdentifier.PARKING_AVM_HFP_SCANNING ->
                    navigate(R.id.avmHfpScanningFragment)
                RouteIdentifier.PARKING_RVC_HFP_GUIDANCE ->
                    navigate(R.id.RvcHfpGuidanceFragment)
                RouteIdentifier.PARKING_AVM_HFP_GUIDANCE ->
                    navigate(R.id.AvmHfpGuidanceFragment)
                RouteIdentifier.PARKING_FAPK_SCANNING,
                RouteIdentifier.PARKING_FAPK_GUIDANCE,
                RouteIdentifier.PARKING_FAPK_PARK_OUT ->
                    navigate(R.id.fapkFragment)
                RouteIdentifier.SURROUND_MVC_MAIN ->
                    navigate(R.id.mvcFragment)
                RouteIdentifier.SURROUND_MVC_SETTINGS ->
                    navigate(R.id.mvcSettings)
                RouteIdentifier.SURROUND_AVM_POPUP,
                RouteIdentifier.SONAR_POPUP,
                RouteIdentifier.SURROUND_WARNING,
                RouteIdentifier.PARKING_WARNING,
                RouteIdentifier.NONE -> {
                    finish()
                }
            }
        })
        endSection()
    }

    private fun navigate(@IdRes destinationId: Int) {
        beginSection("Fullscreen Navigate")
        val extras = getSharedCameraLoadingAnimationIfNeeded()
        val options = NavOptions.Builder()
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setEnterAnim(R.anim.nav_default_enter_anim)
            .build()
        renaultNavController.navigate(destinationId, null, options, extras)
        endSection()
    }

    private fun getSharedCameraLoadingAnimationIfNeeded(): FragmentNavigator.Extras? {
        return findViewById<FrameLayout>(R.id.loading_anim_frame)?.let {
            FragmentNavigatorExtras(
                it to getString(R.string.loading_transition)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        displayInfoLog("lifecycle", "onResume")
    }

    override fun onPause() {
        super.onPause()
        displayInfoLog("lifecycle", "onPause")
    }

    override fun onStart() {
        super.onStart()
        displayInfoLog("lifecycle", "onStart")
    }

    override fun onStop() {
        super.onStop()
        displayInfoLog("lifecycle", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        displayInfoLog("lifecycle", "onDestroy")
    }

    override val navigationGraphId: Int
        get() = R.navigation.fullscreen_navigation

    override fun onBackPressed() {
        // Do nothing
    }
}