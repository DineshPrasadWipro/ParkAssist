package com.renault.parkassist.viewmodel.trailer

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.TrailerPresence
import org.koin.core.KoinComponent
import org.koin.core.inject

class TrailerViewModel(context: Application) : TrailerViewModelBase(context),
    KoinComponent {

    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val trailerPresence: LiveData<Boolean> =
        Transformations.map(surroundRepository.trailerPresence) {
            it == TrailerPresence.TRAILER_PRESENCE_DETECTED
        }

    override fun getPresence() = trailerPresence
}