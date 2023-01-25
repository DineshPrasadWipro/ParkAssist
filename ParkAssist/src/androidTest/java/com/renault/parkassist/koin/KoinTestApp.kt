package com.renault.parkassist.koin

import alliancex.arch.core.logger.Loggable
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.renault.parkassist.camera.CameraManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin

class KoinTestApp : Application(), KoinComponent, Loggable {

    override val TAG = "HMI.ParkAssist.Test"

    companion object {
        val runningActivities: MutableList<Activity> = mutableListOf()
        val isRunningActivitiesEmpty
            get() = runningActivities.isEmpty()
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(appModule)
        }
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
                runningActivities.add(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
                runningActivities.remove(activity)
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityResumed(activity: Activity) {
            }
        })
        getKoin().get<CameraManager>()
    }
}