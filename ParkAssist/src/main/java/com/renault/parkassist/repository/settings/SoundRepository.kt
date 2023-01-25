package com.renault.parkassist.repository.settings

import alliance.car.media.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.utility.infoLog
import org.koin.core.KoinComponent
import org.koin.core.inject

class SoundRepository : KoinComponent, ISoundRepository {

    private val manager: AllianceCarAudioManager by inject()
    private val _soundEnabled = MutableLiveData<Boolean>().apply { value = false }
    private val _soundType = MutableLiveData<Int?>().apply { value = 0 }
    private val _volume = MutableLiveData<Int>().apply { value = 0 }
    private val _muted = MutableLiveData<Boolean>().apply { value = false }
    private var _oseEnabled = MutableLiveData<Boolean>().apply { value = false }
    private var _soundTypes: List<SoundType> = listOf()
    private var volumeCtrlPresence = false
    private var minVolume = 0
    private var maxVolume = 0
    private var temporaryMuteCtrlPresence = false
    private var audioActivationCtrlPresence = false
    private var soundSelectionCtrlPresence = false
    private var oseCtrlPresence = false

    private var soundActivationControl: ActivationControlOp? = null
    private var oseActivationControl: ActivationControlOp? = null
    private var soundSelectionControl: SoundSelectionControlOp? = null
    private var soundVolumeControl: VolumeControlOp? = null
    private var temporaryMuteControl: MuteControlOp? = null

    override fun getSoundActivationControlPresence(): Boolean = audioActivationCtrlPresence
    override fun getSoundSelectionControlPresence(): Boolean = soundSelectionCtrlPresence
    override fun getVolumeControlPresence(): Boolean = volumeCtrlPresence
    override fun getMinVolume(): Int = minVolume
    override fun getMaxVolume(): Int = maxVolume
    override fun getTemporaryMuteControlPresence(): Boolean = temporaryMuteCtrlPresence
    override fun getOseControlPresence(): Boolean = oseCtrlPresence

    override fun getSoundTypes(): List<SoundType> = _soundTypes
    override fun getMuted(): LiveData<Boolean> = _muted
    override fun getSoundEnabled(): LiveData<Boolean> = _soundEnabled
    override fun getSoundType(): LiveData<Int?> = _soundType
    override fun getVolume(): LiveData<Int> = _volume
    override fun getOseEnabled(): LiveData<Boolean> = _oseEnabled

    private val soundActivationChangeListener =
        ActivationControlOp.OnActivationChangeListener { _, enabled ->
            infoLog("sound", "receive", " enable", enabled)
            _soundEnabled.postValue(enabled)
        }

    private val soundSelectionChangeListener =
        SoundSelectionControlOp.OnSoundSelectionChangeListener { _, soundId ->
            infoLog("sound", "receive", " type", soundId)
            _soundType.postValue(soundId)
        }

    private val volumeChangeListener =
        VolumeControlOp.OnVolumeChangeListener { _, volume ->
            infoLog("sound", "receive", " volume", volume)
            _volume.postValue(volume)
        }

    private val muteChangeListener =
        MuteControlOp.OnMuteChangeListener { _, muted ->
            infoLog("sound", "receive", " mute", muted)
            _muted.postValue(muted)
        }

    private val oseActivationChangeListener =
        ActivationControlOp.OnActivationChangeListener { _, enabled ->
            infoLog("sound", "receive", " oseEnable", enabled)
            _oseEnabled.postValue(enabled)
        }

    init {
        configureAccordinglyToVehicleConfiguration()
    }

    private fun configureAccordinglyToVehicleConfiguration() {
        val sonarFeature = manager.features.find { it.name == AudioFeature.UPA_FKP }
        sonarFeature?.let { feature ->
            soundActivationControl = manager.getActivationControlOp(feature)
            soundActivationControl?.apply {
                registerActivationChangeListener(soundActivationChangeListener)
                audioActivationCtrlPresence = true
            } ?: run {
                // If sound activation control is not present, we should consider that sound is enabled
                _soundEnabled.postValue(true)
            }

            soundSelectionControl = manager.getSoundSelectionControlOp(feature)
            soundSelectionControl?.apply {
                _soundTypes = allSoundCollections.map { SoundType(it.id, it.name) }
                registerSoundSelectionChangeListener(soundSelectionChangeListener)
                soundSelectionCtrlPresence = true
            }

            soundVolumeControl = manager.getVolumeControlOp(feature)
            soundVolumeControl?.apply {
                minVolume = minVolumeIndex
                maxVolume = maxVolumeIndex
                registerVolumeChangeListener(volumeChangeListener)
                volumeCtrlPresence = true
            }

            temporaryMuteControl = manager.getMuteControlOp(feature)
            temporaryMuteControl?.apply {
                registerMuteChangeListener(muteChangeListener)
                temporaryMuteCtrlPresence = true
            }
        }

        val oseFeature = manager.features.find { it.name == AudioFeature.OSE }
        oseFeature?.let { feature ->
            oseActivationControl = manager.getActivationControlOp(feature)
            oseActivationControl?.apply {
                registerActivationChangeListener(oseActivationChangeListener)
                oseCtrlPresence = true
            }
        }

        val sonarFeaturePresent = sonarFeature?.let { true } ?: false
        val oseFeaturePresent = oseFeature?.let { true } ?: false
        infoLog("sound", "get", "sonarFeaturePresent", sonarFeaturePresent)
        infoLog("sound", "get", "audioActivationPresent", audioActivationCtrlPresence)
        infoLog("sound", "get", "soundSelectionPresent", soundSelectionCtrlPresence)
        infoLog("sound", "get", "volumeControlPresent", volumeCtrlPresence)
        infoLog("sound", "get", "muteControlPresent", temporaryMuteCtrlPresence)
        infoLog("sound", "get", "oseFeaturePresent", oseFeaturePresent)
        infoLog("sound", "get", "oseControlPresent", oseCtrlPresence)
    }

    override fun enableSound(enable: Boolean) {
        soundActivationControl?.apply {
            enable(enable)
            infoLog("sound", "send", " enable", enable)
        }
            ?: errorLog("sound", "try to enable sound while not available")
    }

    override fun setSoundType(id: Int) {
        soundSelectionControl?.apply {
            setSoundCollection(id, true)
            infoLog("sound", "send", " type", id)
        }
            ?: errorLog("sound", "try to set sound type while not available")
    }

    override fun setVolume(volume: Int) {
        soundVolumeControl?.apply {
            setVolumeIndex(volume, true)
            infoLog("sound", "send", " volume", volume)
        }
            ?: errorLog("sound", "try to set volume while not available")
    }

    override fun enableOse(enable: Boolean) {
        oseActivationControl?.apply {
            enable(enable)
            infoLog("sound", "send", " enableOse", enable)
        }
            ?: errorLog("sound", "try to enable ose while not available")
    }

    override fun mute(muted: Boolean) {
        temporaryMuteControl?.apply {
            mute(muted)
            infoLog("sound", "send", " mute", muted)
        }
            ?: errorLog("sound", "try to mute sound while not available")
    }
}