package com.renault.parkassist

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic

object TestUtils {
    fun mockLifecycleOwner(): LifecycleOwner {
        val owner = mockk<LifecycleOwner>()
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        every { owner.lifecycle } returns lifecycle
        return owner
    }
}

fun mockLogs() {
    mockkStatic(Log::class)
    mockkStatic(SystemClock::class)
    every { SystemClock.elapsedRealtime() } returns 1
    every { Log.e(any(), any()) } answers {
        println(firstArg<String>() + ": " + secondArg<String>())
        0
    }
    every { Log.w(any(), any<String>()) } answers {
        println(firstArg<String>() + ": " + secondArg<String>())
        0
    }
    every { Log.i(any(), any()) } answers {
        println(firstArg<String>() + ": " + secondArg<String>())
        0
    }
    every { Log.d(any(), any()) } answers {
        println(firstArg<String>() + ": " + secondArg<String>())
        0
    }
    every { Log.isLoggable(any(), any()) } answers { true }
    every { SystemClock.elapsedRealtime() } answers {
        20
    }
}