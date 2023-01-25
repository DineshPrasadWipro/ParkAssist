package com.renault.parkassist.ui

import alliancex.arch.core.logger.logD
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.renault.parkassist.routing.pursuit.PursuitViewModel

// TODO : replace with an intent service or by the DisplayService when Intents updated
class EasyParkLauncherActivity : AppCompatActivity() {

    private val pursuitVm: PursuitViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        logD{"onResume"}
        pursuitVm.startEasyPark()
        finish()
    }

    override fun onPause() {
        super.onPause()
        logD{"onPause"}
    }
}