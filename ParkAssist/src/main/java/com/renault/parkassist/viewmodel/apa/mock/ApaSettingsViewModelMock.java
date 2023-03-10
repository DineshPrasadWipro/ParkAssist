/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/apa-settings.ts'.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.apa.mock;


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

import com.renault.parkassist.viewmodel.apa.ApaSettingsViewModelBase;

import com.renault.parkassist.viewmodel.apa.Maneuver;

public class ApaSettingsViewModelMock extends ApaSettingsViewModelBase {
	public ApaSettingsViewModelMock(@NonNull Application application) {
		super(application);
		this.reset();
	}

	public void reset() {
		maneuverSelectorVisible = false;
		maneuvers = new ArrayList<Integer>();
		defaultManeuver.setValue(Maneuver.PARALLEL);
	}

	public void postReset() {
		maneuverSelectorVisible = false;
		maneuvers = new ArrayList<Integer>();
		defaultManeuver.postValue(Maneuver.PARALLEL);
	}

	@Override
	public boolean getManeuverSelectorVisible() {
		return maneuverSelectorVisible;
	}

	@NonNull
	@Override
	public List<Integer> getManeuvers() {
		return maneuvers;
	}

	@NonNull
	@Override
	public MutableLiveData<Integer> getDefaultManeuver() {
		return defaultManeuver;
	}

	public boolean maneuverSelectorVisible;
	public List<Integer> maneuvers;
	protected MutableLiveData<Integer> defaultManeuver = new MutableLiveData<>();

	

	@Override
	public void setDefaultManeuver(@Maneuver int mode) {}
}
