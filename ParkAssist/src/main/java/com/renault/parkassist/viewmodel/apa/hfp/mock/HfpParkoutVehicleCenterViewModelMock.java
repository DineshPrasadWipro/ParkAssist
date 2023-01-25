/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/hfp-parkout-vehicle-center.ts'.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.apa.hfp.mock;


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

import com.renault.parkassist.viewmodel.apa.hfp.HfpParkoutVehicleCenterViewModelBase;

public class HfpParkoutVehicleCenterViewModelMock extends HfpParkoutVehicleCenterViewModelBase {
	public HfpParkoutVehicleCenterViewModelMock(@NonNull Application application) {
		super(application);
		this.reset();
	}

	public void reset() {
		leftDoubleCurveVisible.setValue(false);
		rightDoubleCurveVisible.setValue(false);
		stopVisible.setValue(false);
		arrowRightSideVisible.setValue(false);
		arrowLeftSideVisible.setValue(false);
	}

	public void postReset() {
		leftDoubleCurveVisible.postValue(false);
		rightDoubleCurveVisible.postValue(false);
		stopVisible.postValue(false);
		arrowRightSideVisible.postValue(false);
		arrowLeftSideVisible.postValue(false);
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getLeftDoubleCurveVisible() {
		return leftDoubleCurveVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getRightDoubleCurveVisible() {
		return rightDoubleCurveVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getStopVisible() {
		return stopVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getArrowRightSideVisible() {
		return arrowRightSideVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getArrowLeftSideVisible() {
		return arrowLeftSideVisible;
	}

	protected MutableLiveData<Boolean> leftDoubleCurveVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> rightDoubleCurveVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> stopVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> arrowRightSideVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> arrowLeftSideVisible = new MutableLiveData<>();

	


}
