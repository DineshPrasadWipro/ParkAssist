package com.renault.parkassist.service

import alliancex.arch.core.logger.logD
import android.content.Context
import android.content.Intent
import com.renault.parkassist.routing.IDisplayManager
import com.renault.parkassist.ui.FullscreenShadowActivity
import org.koin.core.KoinComponent
import org.koin.core.inject

class ShadowLauncher(context: Context) : KoinComponent {
    private val displayManager: IDisplayManager by inject()

    private val startShadowIntent = Intent(
        context,
        FullscreenShadowActivity::class.java
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    init {
        logD { "Init" }
        displayManager.shadowRequested.subscribe { requested ->
            if (requested) {
                //context.startActivity(startShadowIntent)
            }
        }
    }
}