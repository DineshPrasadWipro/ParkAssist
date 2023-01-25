/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/sound-settings.ts'.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.sound.mock;


import android.app.Application;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.renault.parkassist.viewmodel.sound.SoundSettingsViewModelBase;

import com.renault.parkassist.viewmodel.sound.SoundDescriptor;

public class SoundSettingsViewModelMock extends SoundSettingsViewModelBase {
	public SoundSettingsViewModelMock(@NonNull Application application) {
		super(application);
		this.reset();
	}

	public void reset() {
		soundEnabled.setValue(false);
		soundId.setValue(0);
		volume.setValue(0);
		soundSwitchVisible = false;
		volumeVisible = false;
		soundTypeVisible = false;
		minVolume = 0;
		maxVolume = 0;
		sounds = new ArrayList<SoundDescriptor>();
	}

	public void postReset() {
		soundEnabled.postValue(false);
		soundId.postValue(0);
		volume.postValue(0);
		soundSwitchVisible = false;
		volumeVisible = false;
		soundTypeVisible = false;
		minVolume = 0;
		maxVolume = 0;
		sounds = new ArrayList<SoundDescriptor>();
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getSoundEnabled() {
		return soundEnabled;
	}

	@NonNull
	@Override
	public MutableLiveData<Integer> getSoundId() {
		return soundId;
	}

	@NonNull
	@Override
	public MutableLiveData<Integer> getVolume() {
		return volume;
	}

	@Override
	public boolean getSoundSwitchVisible() {
		return soundSwitchVisible;
	}

	@Override
	public boolean getVolumeVisible() {
		return volumeVisible;
	}

	@Override
	public boolean getSoundTypeVisible() {
		return soundTypeVisible;
	}

	@Override
	public int getMinVolume() {
		return minVolume;
	}

	@Override
	public int getMaxVolume() {
		return maxVolume;
	}

	@NonNull
	@Override
	public List<SoundDescriptor> getSounds() {
		return sounds;
	}

	protected MutableLiveData<Boolean> soundEnabled = new MutableLiveData<>();
	protected MutableLiveData<Integer> soundId = new MutableLiveData<>();
	protected MutableLiveData<Integer> volume = new MutableLiveData<>();
	public boolean soundSwitchVisible;
	public boolean volumeVisible;
	public boolean soundTypeVisible;
	public int minVolume;
	public int maxVolume;
	public List<SoundDescriptor> sounds;

	

	@Override
	public void enableSound(boolean enable) {}

	@Override
	public void setSound(int id) {}

	@Override
	public void setVolume(int volume) {}
}
