package com.renault.parkassist.viewmodel.mvc;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public abstract class MvcStateViewModelBase extends AndroidViewModel implements IMvcStateViewModel {
    public MvcStateViewModelBase(@NonNull Application application) {
        super(application);
    }
}