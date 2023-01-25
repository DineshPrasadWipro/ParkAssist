package com.renault.parkassist.viewmodel.sonar

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renault.parkassist.repository.apa.IApaRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class SonarFullViewModel(application: Application) : SonarFullViewModelBase(application),
    KoinComponent {

    private val apaRepository: IApaRepository by inject()

    private val sonarAlertVisible = MutableLiveData<Boolean>(false)

    private val easyParkIndication: LiveData<Boolean> = apaRepository.automaticManeuver

    override fun setSonarAlertVisibility(visible: Boolean) {
        sonarAlertVisible.postValue(visible)
    }

    override fun getSonarAlertVisibility(): LiveData<Boolean> {
        return sonarAlertVisible
    }

    override fun getEasyParkIndication(): LiveData<Boolean> = easyParkIndication
}