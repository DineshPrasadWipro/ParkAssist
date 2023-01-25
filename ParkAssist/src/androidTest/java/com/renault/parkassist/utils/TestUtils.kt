package com.renault.parkassist.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun waitForCondition(timeOutMs: Long = 100L, condition: () -> Boolean) {
    var timeOut = false
    GlobalScope.launch {
        delay(timeOutMs)
        timeOut = true
    }
    runBlocking {
        while (!condition() && !timeOut) {
            delay(10)
        }
    }
}

fun launchIntentForComponent(context: Context, pkg: String, appComponent: String) {
    context.startActivity(
        Intent().apply {
            component = ComponentName(
                pkg,
                "$pkg.$appComponent"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

fun broadcastIntentForComponent(
    context: Context,
    pkg: String,
    appComponent: String,
    intentAction: String
) {
    context.sendBroadcast(
        Intent().apply {
            component = ComponentName(
                pkg,
                "$pkg.$appComponent"
            )
            action = intentAction
        }
    )
}