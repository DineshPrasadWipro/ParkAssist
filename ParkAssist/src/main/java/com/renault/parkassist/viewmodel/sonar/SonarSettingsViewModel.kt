package com.renault.parkassist.viewmodel.sonar

import alliance.car.sonar.AllianceCarSonarManager
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.settings.ISoundRepository
import com.renault.parkassist.repository.sonar.GroupState
import com.renault.parkassist.repository.sonar.ISonarRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class SonarSettingsViewModel(application: Application) : SonarSettingsViewModelBase(application),
    KoinComponent {
    private val sonarRepository: ISonarRepository by inject()
    private val soundRepository: ISoundRepository by inject()

    private val rearEnabled = Transformations.map(sonarRepository.rearState) { state ->
        groupStateToActivation(state)
    }
    private val frontEnabled = Transformations.map(sonarRepository.frontState) { state ->
        groupStateToActivation(state)
    }
    private val flankEnabled = Transformations.map(sonarRepository.flankState) { state ->
        groupStateToActivation(state)
    }

    override fun getFrontEnabled(): LiveData<Boolean> = frontEnabled

    override fun getFrontVisible(): Boolean = sonarRepository.upaFrontFeaturePresent

    override fun enableFront(enable: Boolean) {
        sonarRepository.setSonarGroup(AllianceCarSonarManager.SONAR_GROUP_FRONT, enable)
    }

    override fun getRearEnabled(): LiveData<Boolean> = rearEnabled

    override fun getRearVisible(): Boolean = sonarRepository.upaRearFeaturePresent

    override fun getRearToggleVisible(): Boolean =
        sonarRepository.upaRearFeaturePresent && sonarRepository.rearUpaActivationSettingPresent

    override fun enableRear(enable: Boolean) {
        sonarRepository.setSonarGroup(AllianceCarSonarManager.SONAR_GROUP_REAR, enable)
    }

    override fun getFlankEnabled(): LiveData<Boolean> = flankEnabled

    override fun getFlankVisible(): Boolean = sonarRepository.fkpFeaturePresent

    override fun enableFlank(enable: Boolean) {
        sonarRepository.setSonarGroup(AllianceCarSonarManager.SONAR_GROUP_FLANK, enable)
    }

    override fun getRctaEnabled(): LiveData<Boolean> = sonarRepository.collisionAlertEnabled

    override fun getRctaVisible(): Boolean = sonarRepository.rctaFeaturePresent

    override fun enableRcta(enable: Boolean) = sonarRepository.enableCollisionAlert(enable)

    override fun getRaebEnabled(): LiveData<Boolean> = sonarRepository.raebAlertEnabled

    override fun getRaebVisible(): Boolean = sonarRepository.raebFeaturePresent

    override fun enableRaeb(enable: Boolean) = sonarRepository.enableRearAutoEmergencyBreak(enable)

    override fun getOseEnabled(): LiveData<Boolean> = soundRepository.oseEnabled

    override fun getOseVisible(): Boolean = soundRepository.oseControlPresence

    override fun enableOse(enable: Boolean) = soundRepository.enableOse(enable)

    companion object {
        private fun groupStateToActivation(@GroupState state: Int): Boolean {
            return when (state) {
                GroupState.ENABLED -> true
                else /*GroupState.DISABLED*/ -> false
            }
        }
    }
}