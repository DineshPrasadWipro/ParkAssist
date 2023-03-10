/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/hfp-guidance-vehicle-center-cut.ts'.
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

import com.renault.parkassist.viewmodel.apa.hfp.HfpGuidanceVehicleCenterCutViewModelBase;

public class HfpGuidanceVehicleCenterCutViewModelMock extends HfpGuidanceVehicleCenterCutViewModelBase {
	public HfpGuidanceVehicleCenterCutViewModelMock(@NonNull Application application) {
		super(application);
		this.reset();
	}

	public void reset() {
		arrowCurveDownVisible.setValue(false);
		engageLeftActiveVisible.setValue(false);
		engageLeftNotActiveVisible.setValue(false);
		engageRightActiveVisible.setValue(false);
		engageRightNotActiveVisible.setValue(false);
	}

	public void postReset() {
		arrowCurveDownVisible.postValue(false);
		engageLeftActiveVisible.postValue(false);
		engageLeftNotActiveVisible.postValue(false);
		engageRightActiveVisible.postValue(false);
		engageRightNotActiveVisible.postValue(false);
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getArrowCurveDownVisible() {
		return arrowCurveDownVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getEngageLeftActiveVisible() {
		return engageLeftActiveVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getEngageLeftNotActiveVisible() {
		return engageLeftNotActiveVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getEngageRightActiveVisible() {
		return engageRightActiveVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getEngageRightNotActiveVisible() {
		return engageRightNotActiveVisible;
	}

	protected MutableLiveData<Boolean> arrowCurveDownVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> engageLeftActiveVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> engageLeftNotActiveVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> engageRightActiveVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> engageRightNotActiveVisible = new MutableLiveData<>();

	


}
