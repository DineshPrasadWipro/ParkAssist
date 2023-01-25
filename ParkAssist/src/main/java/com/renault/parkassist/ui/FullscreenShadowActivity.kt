package com.renault.parkassist.ui

import alliancex.arch.core.logger.logD
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.viewmodel.shadow.ShadowFullscreenViewModel

class FullscreenShadowActivity : AppCompatActivity() {

    private val pursuitVm: PursuitViewModel by viewModels()
    private val shadowFullscreenViewModel: ShadowFullscreenViewModel by viewModels()
    private var shouldStopPursuit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD { "onCreate" }

        // TODO: moveTaskToBack(true) was used before, why???
        shadowFullscreenViewModel.shadowRequested.observe(this, Observer { requested ->
            if (!requested) {
                logD { "shadowRequested Observer: requested = $requested" }
                shouldStopPursuit = false
                finish()
            }
        })
    }

    // For an unknown reason, sometimes, when user click on NavBar, onStop callback is not called
    // In this case we want to be able to stop pursuit anyway
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        logD { "onUserLeaveHint" }
        stopPursuit()
    }

    override fun onResume() {
        super.onResume()
        logD { "onResume" }
        shouldStopPursuit = true
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
        stopPursuit()
    }

    override fun onBackPressed() {
        // Do nothing
    }

    private fun stopPursuit() {
        if (shouldStopPursuit) {
            logD { "stopping pursuit" }
            pursuitVm.stop()
        } else
            logD { "onStop: no need to stop pursuit" }
    }
}