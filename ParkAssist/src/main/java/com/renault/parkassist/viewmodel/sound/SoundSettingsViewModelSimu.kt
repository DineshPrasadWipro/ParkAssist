package com.renault.parkassist.viewmodel.sound

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.renault.parkassist.viewmodel.sound.mock.SoundSettingsViewModelMock

class SoundSettingsViewModelSimu(application: Application) :
    SoundSettingsViewModelMock(application),
    SharedPreferences.OnSharedPreferenceChangeListener {
    companion object {
        private const val SOUND_ENABLED_PREF = "soundEnabled"
        private const val SOUND_TYPE_PREF = "soundType"
        private const val SOUND_VOLUME_PREF = "volume"
    }

    private val sharedPreferences =
        application.getSharedPreferences("soundPreferences", Context.MODE_PRIVATE)

    init {
        minVolume = 1
        maxVolume = 5
        sounds = listOf(
                SoundDescriptor("BIP", 1),
                SoundDescriptor("CORDE", 2),
                SoundDescriptor("SONAR", 3)
            )
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        applyEnabled()
        applySoundType()
        applyVolume()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            SOUND_ENABLED_PREF -> applyEnabled()
            SOUND_TYPE_PREF -> applySoundType()
            SOUND_VOLUME_PREF -> applyVolume()
        }
    }

    private fun applyVolume() {
        volume.value = sharedPreferences?.getInt(SOUND_VOLUME_PREF, 50) ?: 50
    }

    private fun applySoundType() {
        soundId.value = sharedPreferences?.getInt(SOUND_TYPE_PREF, 1) ?: 1
    }

    private fun applyEnabled() {
        soundEnabled.value = sharedPreferences?.getBoolean(SOUND_ENABLED_PREF, false) ?: false
    }

    override fun enableSound(enable: Boolean) {
        sharedPreferences.edit().putBoolean(SOUND_ENABLED_PREF, enable).apply()
    }

    override fun setSound(type: Int) {
        sharedPreferences.edit().putInt(SOUND_TYPE_PREF, type).apply()
    }

    override fun setVolume(volume: Int) {
        sharedPreferences.edit().putInt(SOUND_VOLUME_PREF, volume).apply()
    }
}