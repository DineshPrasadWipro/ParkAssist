package com.renault.parkassist.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.renault.parkassist.R
import com.renault.parkassist.routing.IDisplayManager
import com.renault.parkassist.routing.Pursuit
import org.koin.core.KoinComponent
import org.koin.core.inject

// This receiver is needed in order to answer AVM broadcast sent by favorite
class AvmBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val manager: IDisplayManager by inject()

    override fun onReceive(context: Context, intent: Intent) {
        val actionStart = context.getString(R.string.action_start_avm)

        when (intent.action) {
            actionStart -> {
                manager.startPursuit(Pursuit.MANEUVER)
            }
        }
    }
}