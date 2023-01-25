package com.renault.parkassist.routing.policy

import alliance.car.surroundview.SurroundViewCapabilities
import com.renault.parkassist.repository.apa.AutoParkManagerAdapter
import com.renault.parkassist.repository.sonar.SonarCarManagerAdapter
import com.renault.parkassist.repository.surroundview.SurroundViewManagerAdapter
import com.renault.parkassist.utility.infoLog

class PolicyLoader(
    private val surroundManager: SurroundViewManagerAdapter,
    private val sonarManager: SonarCarManagerAdapter,
    private val autoParkManager: AutoParkManagerAdapter
) {

    fun loadPolicy(): IPolicy = when {
        surroundManager.capabilities.featureConfiguration
            == SurroundViewCapabilities.FEATURE_AROUND_VIEW_MONITORING -> {
            infoLog("boot", "load", "policy", "AVM")
            AvmPolicy(autoParkManager.capabilities.featureConfiguration)
        }
        //TODO CCSEXT-84113 add MVC here when service side implementation available
        // infoLog("boot", "load", "policy", "MVC")
        // MvcPolicy(autoParkManager.capabilities.featureConfiguration)
        surroundManager.capabilities.featureConfiguration
            == SurroundViewCapabilities.FEATURE_REAR_VIEW_CAMERA &&
            sonarManager.visualFeedbackConfig -> {
            infoLog("boot", "load", "policy", "RVC")
            RvcPolicy()
        }
        sonarManager.visualFeedbackConfig -> {
            infoLog("boot", "load", "policy", "UPA")
            UpaPolicy()
        }
        else -> {
            infoLog("boot", "load", "policy", "NO_ADAS")
            NoAdasPolicy()
        }
    }
}