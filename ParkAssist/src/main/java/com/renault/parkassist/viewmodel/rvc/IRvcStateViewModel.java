/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/rvc-state.ts'.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.rvc;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.lifecycle.LiveData;
import java.util.List;

/** Rear View Camera (RVC) video display current state */
public interface IRvcStateViewModel {
	/** Settings camera view action visible */
	@NonNull
	LiveData<Boolean> getSettingsVisible();

	/** Whether toolbar is enabled or not */
	@NonNull
	LiveData<Boolean> getToolbarEnabled();

	/** Requests Surround View service to navigate to RVC settings */
	public void navigateToSettings();

	/** Closes the Rvc screen */
	public void close();
}
