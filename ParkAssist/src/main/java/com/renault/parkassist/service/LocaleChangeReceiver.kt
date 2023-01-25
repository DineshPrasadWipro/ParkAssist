package com.renault.parkassist.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.UserManager
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocaleChangeReceiver : BroadcastReceiver(), KoinComponent {

    private val trailerNotifier: TrailerNotifier by inject()

    private val userManager: UserManager by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (!userManager.isSystemUser && intent.action == Intent.ACTION_LOCALE_CHANGED) {
            trailerNotifier.refresh()
        }
    }
}