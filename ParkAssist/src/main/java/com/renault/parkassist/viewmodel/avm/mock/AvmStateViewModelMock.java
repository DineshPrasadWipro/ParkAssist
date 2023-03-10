/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/avm-state.ts'.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.avm.mock;


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

import com.renault.parkassist.viewmodel.avm.AvmStateViewModelBase;

import com.renault.parkassist.viewmodel.avm.AvmModeRequest;
import com.renault.parkassist.viewmodel.avm.AvmModeSelected;

public class AvmStateViewModelMock extends AvmStateViewModelBase {
	public AvmStateViewModelMock(@NonNull Application application) {
		super(application);
		this.reset();
	}

	public void reset() {
		modeSelected.setValue(AvmModeSelected.STANDARD);
		closeVisible.setValue(false);
		easyparkShortcutVisible = false;
		maneuverVisible.setValue(false);
		settingsVisible.setValue(false);
		trailerVisible.setValue(false);
		birdSideCameraMargin.setValue(false);
		selectStandardViewVisible.setValue(false);
		backButtonVisible.setValue(false);
		selectPanoramicViewVisible.setValue(false);
		selectSidesViewVisible.setValue(false);
		selectThreeDimensionViewVisible.setValue(false);
		threeDimensionInfoVisible.setValue(false);
		buttonsEnabled.setValue(false);
	}

	public void postReset() {
		modeSelected.postValue(AvmModeSelected.STANDARD);
		closeVisible.postValue(false);
		easyparkShortcutVisible = false;
		maneuverVisible.postValue(false);
		settingsVisible.postValue(false);
		trailerVisible.postValue(false);
		birdSideCameraMargin.postValue(false);
		selectStandardViewVisible.postValue(false);
		backButtonVisible.postValue(false);
		selectPanoramicViewVisible.postValue(false);
		selectSidesViewVisible.postValue(false);
		selectThreeDimensionViewVisible.postValue(false);
		threeDimensionInfoVisible.postValue(false);
		buttonsEnabled.postValue(false);
	}

	@NonNull
	@Override
	public MutableLiveData<Integer> getModeSelected() {
		return modeSelected;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getCloseVisible() {
		return closeVisible;
	}

	@Override
	public boolean getEasyparkShortcutVisible() {
		return easyparkShortcutVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getManeuverVisible() {
		return maneuverVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getSettingsVisible() {
		return settingsVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getTrailerVisible() {
		return trailerVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getBirdSideCameraMargin() {
		return birdSideCameraMargin;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getSelectStandardViewVisible() {
		return selectStandardViewVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getBackButtonVisible() {
		return backButtonVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getSelectPanoramicViewVisible() {
		return selectPanoramicViewVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getSelectSidesViewVisible() {
		return selectSidesViewVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getSelectThreeDimensionViewVisible() {
		return selectThreeDimensionViewVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getThreeDimensionInfoVisible() {
		return threeDimensionInfoVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getButtonsEnabled() {
		return buttonsEnabled;
	}

	protected MutableLiveData<Integer> modeSelected = new MutableLiveData<>();
	protected MutableLiveData<Boolean> closeVisible = new MutableLiveData<>();
	public boolean easyparkShortcutVisible;
	protected MutableLiveData<Boolean> maneuverVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> settingsVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> trailerVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> birdSideCameraMargin = new MutableLiveData<>();
	protected MutableLiveData<Boolean> selectStandardViewVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> backButtonVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> selectPanoramicViewVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> selectSidesViewVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> selectThreeDimensionViewVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> threeDimensionInfoVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> buttonsEnabled = new MutableLiveData<>();

	

	@Override
	public void requestViewMode(@AvmModeRequest int request) {}

	@Override
	public void requestView() {}

	@Override
	public void closeView() {}
}
