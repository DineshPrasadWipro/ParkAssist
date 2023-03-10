/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/hfp-guidance-vehicle-center-back.ts'.
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

import com.renault.parkassist.viewmodel.apa.hfp.HfpGuidanceVehicleCenterBackViewModelBase;

public class HfpGuidanceVehicleCenterBackViewModelMock extends HfpGuidanceVehicleCenterBackViewModelBase {
	public HfpGuidanceVehicleCenterBackViewModelMock(@NonNull Application application) {
		super(application);
		this.reset();
	}

	public void reset() {
		engageBackActiveVisible.setValue(false);
		engageBackNotActiveVisible.setValue(false);
		engageBackVisible.setValue(false);
		stopBackVisible.setValue(false);
	}

	public void postReset() {
		engageBackActiveVisible.postValue(false);
		engageBackNotActiveVisible.postValue(false);
		engageBackVisible.postValue(false);
		stopBackVisible.postValue(false);
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getEngageBackActiveVisible() {
		return engageBackActiveVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getEngageBackNotActiveVisible() {
		return engageBackNotActiveVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getEngageBackVisible() {
		return engageBackVisible;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getStopBackVisible() {
		return stopBackVisible;
	}

	protected MutableLiveData<Boolean> engageBackActiveVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> engageBackNotActiveVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> engageBackVisible = new MutableLiveData<>();
	protected MutableLiveData<Boolean> stopBackVisible = new MutableLiveData<>();

	


}
