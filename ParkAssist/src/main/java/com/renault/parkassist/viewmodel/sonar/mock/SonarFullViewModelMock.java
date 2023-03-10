/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/sonar-full.ts'.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.sonar.mock;


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

import com.renault.parkassist.viewmodel.sonar.SonarFullViewModelBase;

public class SonarFullViewModelMock extends SonarFullViewModelBase {
	public SonarFullViewModelMock(@NonNull Application application) {
		super(application);
		this.reset();
	}

	public void reset() {
		sonarAlertVisibility.setValue(false);
		easyParkIndication.setValue(false);
	}

	public void postReset() {
		sonarAlertVisibility.postValue(false);
		easyParkIndication.postValue(false);
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getSonarAlertVisibility() {
		return sonarAlertVisibility;
	}

	@NonNull
	@Override
	public MutableLiveData<Boolean> getEasyParkIndication() {
		return easyParkIndication;
	}

	protected MutableLiveData<Boolean> sonarAlertVisibility = new MutableLiveData<>();
	protected MutableLiveData<Boolean> easyParkIndication = new MutableLiveData<>();

	

	@Override
	public void setSonarAlertVisibility(boolean visible) {}
}
