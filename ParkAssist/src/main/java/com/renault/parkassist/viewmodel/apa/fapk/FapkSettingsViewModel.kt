package com.renault.parkassist.viewmodel.apa.fapk

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import org.koin.core.KoinComponent
import org.koin.core.inject

class FapkSettingsViewModel() : ViewModel(), KoinComponent {
    private val surroundViewRepository: IExtSurroundViewRepository by inject()

    val shouldExitSettings: LiveData<Boolean> = surroundViewRepository.surroundState.map {
        it.view == View.APA_REAR_VIEW
    }
}