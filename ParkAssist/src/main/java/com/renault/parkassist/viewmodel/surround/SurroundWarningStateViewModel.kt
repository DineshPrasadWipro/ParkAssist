package com.renault.parkassist.viewmodel.surround

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.UserAcknowledgement
import org.koin.core.KoinComponent
import org.koin.core.inject

class SurroundWarningStateViewModel(app: Application) : SurroundWarningStateViewModelBase(app),
    KoinComponent {

    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val surroundWarningMapper: SurroundWarningMapper by inject()

    private val warning = Transformations.map(surroundRepository.warningState) {
        surroundWarningMapper.mapSurroundWarningType(it)
    }

    override fun getWarning(): LiveData<Int> = warning

    override fun acknowledge() {
        surroundRepository.acknowledgeWarning(UserAcknowledgement.ACK_OK)
    }
}