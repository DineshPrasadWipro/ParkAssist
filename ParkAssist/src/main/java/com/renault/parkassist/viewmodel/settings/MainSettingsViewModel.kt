package com.renault.parkassist.viewmodel.settings

import android.app.Application
import com.renault.parkassist.repository.settings.ISoundRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainSettingsViewModel(application: Application) :
    MainSettingsViewModelBase(application), KoinComponent {

    private val soundRepository: ISoundRepository by inject()

    override fun getSoundMenuVisible(): Boolean =
        soundRepository.soundActivationControlPresence ||
            soundRepository.volumeControlPresence ||
            soundRepository.soundSelectionControlPresence
}