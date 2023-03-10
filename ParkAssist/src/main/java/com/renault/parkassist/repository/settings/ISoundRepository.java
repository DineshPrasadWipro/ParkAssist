/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/repository/sound-repository.ts'.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.repository.settings;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.lifecycle.LiveData;
import java.util.List;

/** Exposes sound service as livedatas. */
public interface ISoundRepository {
	/** Is sound enabled ? */
	@NonNull
	LiveData<Boolean> getSoundEnabled();

	/** Occupant Seat Exit enabled ? */
	@NonNull
	LiveData<Boolean> getOseEnabled();

	/** Selected sound type */
	@NonNull
	LiveData<Integer> getSoundType();

	/** Current volume */
	@NonNull
	LiveData<Integer> getVolume();

	/** Is sound muted ? */
	@NonNull
	LiveData<Boolean> getMuted();

	/** Sonar audio activation control feature present */
	boolean getSoundActivationControlPresence();

	/** Sonar sound type selection control feature present */
	boolean getSoundSelectionControlPresence();

	/** Available sound types */
	@NonNull
	List<SoundType> getSoundTypes();

	/** Sonar volume control feature present */
	boolean getVolumeControlPresence();

	/** Minimum volume */
	int getMinVolume();

	/** Maximum volume */
	int getMaxVolume();

	/** Sonar mute control feature present */
	boolean getTemporaryMuteControlPresence();

	/** Occupant Seat Exit feature present */
	boolean getOseControlPresence();

	/**
	 * Enable/disable sound.
	 *
	 * @param enable if true enable the sound, disable it else.
	 */
	public void enableSound(boolean enable);

	/**
	 * Set sound type.
	 *
	 * @param id identifier of the targeted sound type
	 */
	public void setSoundType(int id);

	/**
	 * Set volume.
	 *
	 * @param volume value to set.
	 */
	public void setVolume(int volume);

	/**
	 * Mute/unmute sound.
	 *
	 * @param muted if true mute the group, unmute it else.
	 */
	public void mute(boolean muted);

	/**
	 * Enable/disable Occupant Seat Exit feature.
	 *
	 * @param enable if true enable the group, disable it else.
	 */
	public void enableOse(boolean enable);
}
