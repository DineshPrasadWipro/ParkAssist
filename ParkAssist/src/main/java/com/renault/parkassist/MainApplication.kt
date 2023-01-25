package com.renault.parkassist

import android.app.Application
import android.os.Trace
import com.renault.parkassist.injection.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject

class MainApplication : Application(), KoinComponent {

    private val userProcess: UserProcess by inject()

    override fun onCreate() {
        super.onCreate()
        Trace.beginSection("onCreateMainApplication")
        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
        userProcess.init()
        Trace.endSection()
    }
}