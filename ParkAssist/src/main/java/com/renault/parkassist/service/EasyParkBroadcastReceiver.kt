package com.renault.parkassist.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.renault.parkassist.R
import com.renault.parkassist.routing.IDisplayManager
import com.renault.parkassist.routing.Pursuit
import com.renault.parkassist.utility.warningLog
import org.koin.core.KoinComponent
import org.koin.core.inject

// This receiver is needed in order to send start activity intent
// in another process than the sender's one.
// Here, the start intent is sent by overlay activity and shadow start activity is run
// in its process, overlay activity goes in 'paused' state, which is not wanted.
class EasyParkBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val manager: IDisplayManager by inject()

    override fun onReceive(context: Context, intent: Intent) {
        val actionStart = context.getString(R.string.action_start_easy_park)

        when (intent.action) {
            actionStart -> {
                manager.startPursuit(Pursuit.PARK)
            }
            else -> {
                warningLog("internal", "unexpected shadow intent received")
            }
        }
    }
}