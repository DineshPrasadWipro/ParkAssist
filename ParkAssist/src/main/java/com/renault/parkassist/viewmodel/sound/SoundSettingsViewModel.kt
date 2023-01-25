package com.renault.parkassist.viewmodel.sound

import android.app.Application
import androidx.lifecycle.LiveData
import com.renault.parkassist.repository.settings.ISoundRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class SoundSettingsViewModel(application: Application) : SoundSettingsViewModelBase(application),
    KoinComponent {
    private val repository: ISoundRepository by inject()
    private val _sounds = repository.soundTypes.map {
        SoundDescriptor(it.name, it.id)
    }

    override fun getSounds(): List<SoundDescriptor> = _sounds
    override fun getMinVolume(): Int = repository.minVolume
    override fun getMaxVolume(): Int = repository.maxVolume
    override fun getSoundSwitchVisible(): Boolean = repository.soundActivationControlPresence
    override fun getSoundTypeVisible(): Boolean =
        repository.soundSelectionControlPresence && (_sounds.size > 1)
    override fun getVolumeVisible(): Boolean =
        repository.volumeControlPresence && (maxVolume > minVolume)

    override fun getSoundEnabled(): LiveData<Boolean> = repository.soundEnabled
    override fun getSoundId(): LiveData<Int> = repository.soundType
    override fun getVolume(): LiveData<Int> = repository.volume

    override fun enableSound(enable: Boolean) = repository.enableSound(enable)
    override fun setSound(id: Int) {
        if (id != soundId.value) {
            repository.setSoundType(id)
        }
    }
    override fun setVolume(volume: Int) = repository.setVolume(volume)
}