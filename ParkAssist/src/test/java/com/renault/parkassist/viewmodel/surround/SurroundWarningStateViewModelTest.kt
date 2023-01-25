package com.renault.parkassist.viewmodel.surround

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundViewRepository
import com.renault.parkassist.repository.surroundview.UserAcknowledgement
import com.renault.parkassist.repository.surroundview.WarningState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@SmallTest
class SurroundWarningStateViewModelTest : TestBase() {

    private val surroundViewRepository: SurroundViewRepository = mockk(relaxed = true)
    private lateinit var surroundWarningMapper: SurroundWarningMapper
    private lateinit var surroundWarningStateViewModel: SurroundWarningStateViewModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private val mockedWarningState = MutableLiveData<Int>()
    override val koinModule = module {
        single<IExtSurroundViewRepository>(override = true) { surroundViewRepository }
        single { surroundWarningMapper }
    }

    @Before
    override fun setup() {
        super.setup()
        surroundWarningMapper = mockk(relaxed = true)

        every {
            surroundViewRepository.warningState
        } returns mockedWarningState

        lifecycleOwner = TestUtils.mockLifecycleOwner()
        surroundWarningStateViewModel = SurroundWarningStateViewModel(mockk())
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `Should map warning and send to view  when surroundViewRepository warning change`() {
        var warningTypeReceived = 0
        surroundWarningStateViewModel.warning.observe(lifecycleOwner, Observer {
            warningTypeReceived = it
        })
        every {
            surroundWarningMapper.mapSurroundWarningType(WarningState.WARNING_STATE_CAMERA_SOILED)
        } returns SurroundWarningType.CAMERA_SOILED
        mockedWarningState.value = WarningState.WARNING_STATE_CAMERA_SOILED
        verify(exactly = 1) {
            surroundWarningMapper.mapSurroundWarningType(WarningState.WARNING_STATE_CAMERA_SOILED)
        }
        Assert.assertEquals(SurroundWarningType.CAMERA_SOILED, warningTypeReceived)
    }

    @Test
    fun `Should map warning and send ack to surround repo when user ack`() {
        surroundWarningStateViewModel.acknowledge()
        verify(exactly = 1) {
            surroundViewRepository.acknowledgeWarning(UserAcknowledgement.ACK_OK)
        }
    }
}