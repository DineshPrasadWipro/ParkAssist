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
package com.renault.parkassist.viewmodel.rvc.mock;


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

import com.renault.parkassist.viewmodel.rvc.RvcStateViewModelBase;

public class RvcStateViewModelMock extends RvcStateViewModelBase {
	public RvcStateViewModelMock(@NonNull Application application) {
		super(application);
		this.reset();
	}

	public void reset() {
		settingsVisible.setValue(false);
		toolbarEnabled.setValue(false);
	}

	public void postReset() {
		settingsVisible.postValue(false);
		toolbarEnabled.postValue(false);
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getSettingsVisible() {
		return settingsVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getToolbarEnabled() {
		return toolbarEnabled;
	}

	protected MutableLiveData<Boolean> settingsVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> toolbarEnabled = new MutableLiveData<>();

	

	@Override
	public void navigateToSettings() {}

	@Override
	public void close() {}
}