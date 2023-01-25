package com.renault.parkassist.overlay

import alliance.car.windowoverlay.AllianceCarWindowOverlay
import android.content.Context
import android.content.Intent
import org.koin.core.KoinComponent
import org.koin.core.inject

class AllianceCarWindowManagerAdapter : IAllianceCarWindowOverlayManager, KoinComponent {
    private val context: Context by inject()
    override fun createOverlay(intent: Intent, layer: Int): AllianceCarWindowOverlay? {
        // TODO: this needs to be replaced with final window overlay solution:
        // https://jira.dt.renault.com/browse/CCSEXT-61565
        // TODO: also revert all patches that implement AWOS fallback solution, see:
        // https://jira.dt.renault.com/browse/CCSEXT-74732
        context.startActivity(
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        return null
    }
}