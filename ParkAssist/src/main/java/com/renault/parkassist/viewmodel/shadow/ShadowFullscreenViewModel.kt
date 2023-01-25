package com.renault.parkassist.viewmodel.shadow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.renault.parkassist.routing.IDisplayManager
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject

class ShadowFullscreenViewModel : ViewModel(), KoinComponent {

    private val displayManager: IDisplayManager by inject()
    private val disposable: Disposable

    val shadowRequested = MutableLiveData<Boolean>().apply {
        disposable = displayManager.shadowRequested.subscribe {
            this.postValue(it)
        }
    }

    init {
        displayManager.checkAlive()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}