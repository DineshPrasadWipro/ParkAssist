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
package com.renault.parkassist.repository.settings;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/** Sound type */
public class SoundType {
	private int id;
	private @NonNull String name;

	/**
	 * @param id Sound identifier
	 * @param name Sound name
	 */
	public SoundType(
		int id,
		@NonNull String name
	) {
		this.id = id;
		this.name = name;
	}

	/** @param id Sound identifier */
	public SoundType(int id) {
		this(id, "");
	}

	/** Constructs an instance with each member initialized to its default value */
	public SoundType() {
		this(0, "");
	}

	/** @return Sound identifier */
	public final int getId() {
		return this.id;
	}

	/** @return Sound name */
	@NonNull
	public final String getName() {
		return this.name;
	}

	@Override
	@NonNull
	public String toString() {
		return "SoundType(" +
			"id=" + this.id + ", " +
			"name=" + this.name + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			this.id,
			this.name);
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj) {
			return true;
		} else {
			if (obj instanceof SoundType) {
				SoundType other = (SoundType) obj;
				return
					Objects.equals(this.id, other.id) &&
					Objects.equals(this.name, other.name);
			}
			return false;
		}
	}
}
