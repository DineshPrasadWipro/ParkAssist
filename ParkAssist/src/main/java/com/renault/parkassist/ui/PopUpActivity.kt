package com.renault.parkassist.ui

import alliancex.arch.core.logger.logD
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.renault.car.ui.components.RenaultBaseActivity
import com.renault.parkassist.R
import com.renault.parkassist.camera.EvsSurfaceTexture
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.viewmodel.route.RouteViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

@SuppressLint("Registered")
class PopUpActivity : RenaultBaseActivity(false, false), KoinComponent {

    override val navigationGraphId: Int
        get() = R.navigation.popup_navigation

    private val routeVm: RouteViewModel by viewModels()

    private val evsSurfaceTexture: EvsSurfaceTexture by inject()

    private val pursuitVm: PursuitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        evsSurfaceTexture.surfaceAttached.onNext(false)

        routeVm.navigationRoute.observe(this, Observer { routeId: RouteIdentifier ->
            logD { "route Changed to $routeId" }
            when (routeId) {
                RouteIdentifier.SURROUND_AVM_POPUP -> renaultNavController.navigate(R.id.avmPipFragment)
                RouteIdentifier.SONAR_POPUP -> renaultNavController.navigate(R.id.sonarPipFragment)
                else -> {
                    finish()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        logD { "onResume" }
    }

    override fun onPause() {
        super.onPause()
        logD { "onPause" }
    }

    override fun onDestroy() {
        super.onDestroy()
        logD { "onDestroy" }
    }

    override fun onStart() {
        super.onStart()
        logD { "onStart" }
    }

    override fun onStop() {
        super.onStop()
        logD { "onStop" }
    }

    override fun onBackPressed() {
        // Do nothing
    }
}