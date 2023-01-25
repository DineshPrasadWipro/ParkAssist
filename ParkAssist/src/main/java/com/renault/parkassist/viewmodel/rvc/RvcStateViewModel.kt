package com.renault.parkassist.viewmodel.rvc

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renault.parkassist.repository.surroundview.Action.ACTIVATE_SETTINGS_VIEW
import com.renault.parkassist.repository.surroundview.Action.CLOSE_VIEW
import com.renault.parkassist.repository.surroundview.FeatureConfig
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.viewmodel.map
import org.koin.core.KoinComponent
import org.koin.core.inject

class RvcStateViewModel(application: Application) : RvcStateViewModelBase(application),
    KoinComponent {

    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val _toolbarEnabled = surroundRepository.surroundState.map {
        !it.isRequest
    }

    private val settingsVisible = surroundRepository.authorizedActions.map {
        it.contains(ACTIVATE_SETTINGS_VIEW)
    }

    override fun getSettingsVisible(): LiveData<Boolean> {
        return when (surroundRepository.featureConfig) {
            FeatureConfig.RVC -> settingsVisible
            else -> MutableLiveData(false)
        }
    }

    override fun navigateToSettings() {
        surroundRepository.request(ACTIVATE_SETTINGS_VIEW)
    }

    override fun close() {
        surroundRepository.request(CLOSE_VIEW)
    }

    override fun getToolbarEnabled(): LiveData<Boolean> = _toolbarEnabled
}