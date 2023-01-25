/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.sonar;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/** Parking sensor state */
public class ParkingSensor {
	private boolean hatched;
	private @SensorLevel int level;

	/**
	 * @param hatched Hatched state for a single FKP Sensor.
	 * @param level sensor level.
	 */
	public ParkingSensor(
		boolean hatched,
		@SensorLevel int level
	) {
		this.hatched = hatched;
		this.level = level;
	}

	/** @param hatched Hatched state for a single FKP Sensor. */
	public ParkingSensor(boolean hatched) {
		this(hatched, SensorLevel.INVISIBLE);
	}

	/** Constructs an instance with each member initialized to its default value */
	public ParkingSensor() {
		this(false, SensorLevel.INVISIBLE);
	}

	/** @return Hatched state for a single FKP Sensor. */
	public final boolean getHatched() {
		return this.hatched;
	}

	/** @return sensor level. */
	@SensorLevel
	@NonNull
	public final int getLevel() {
		return this.level;
	}

	@Override
	@NonNull
	public String toString() {
		return "ParkingSensor(" +
			"hatched=" + this.hatched + ", " +
			"level=" + this.level + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			this.hatched,
			this.level);
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj) {
			return true;
		} else {
			if (obj instanceof ParkingSensor) {
				ParkingSensor other = (ParkingSensor) obj;
				return
					Objects.equals(this.hatched, other.hatched) &&
					Objects.equals(this.level, other.level);
			}
			return false;
		}
	}
}

