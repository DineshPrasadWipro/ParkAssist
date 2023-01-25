package com.renault.parkassist.viewmodel.rvc

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.SurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

class RvcViewModelTest : TestBase() {

    private val surroundViewRepository: SurroundViewRepository = mockk(relaxed = true)
    private lateinit var rvcStateViewModel: RvcStateViewModel

    private val surroundViewRepositorySurroundStateLiveData =
        MutableLiveData<SurroundState>()
    private val trailerPresenceLiveDate = MutableLiveData<Int>()

    private lateinit var lifecycleOwner: LifecycleOwner

    override val koinModule = module {
        single<IExtSurroundViewRepository>(override = true) { surroundViewRepository }
    }

    @Before
    override fun setup() {
        super.setup()
        every {
            surroundViewRepository.surroundState
        } returns surroundViewRepositorySurroundStateLiveData
        every {
            surroundViewRepository.trailerPresence
        } returns trailerPresenceLiveDate

        lifecycleOwner = TestUtils.mockLifecycleOwner()
        rvcStateViewModel = RvcStateViewModel(mockk())
    }

    @Test
    fun toolbar_should_be_clickable_when_camera_status_is_displaying() {
        var toolbarEnabled = false
        rvcStateViewModel.toolbarEnabled.observeForever {
            toolbarEnabled = it
        }
        surroundViewRepositorySurroundStateLiveData
            .postValue(SurroundState(View.REAR_VIEW, false))
        TestCase.assertTrue(toolbarEnabled)

        surroundViewRepositorySurroundStateLiveData
            .postValue(SurroundState(View.REAR_VIEW, true))
        TestCase.assertFalse(toolbarEnabled)
    }
}