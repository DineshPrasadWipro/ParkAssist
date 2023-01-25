package com.renault.parkassist.viewmodel.apa.fapk

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.SurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module

class FapkSettingsViewModelTest : TestBase() {

    private val surroundViewRepository: SurroundViewRepository = mockk(relaxed = true)
    private lateinit var fapkSettingsViewModel: FapkSettingsViewModel
    private val surroundViewRepositorySurroundStateLiveData =
        MutableLiveData<SurroundState>()
    private lateinit var lifecycleOwner: LifecycleOwner

    override val koinModule: Module = module {
        single<IExtSurroundViewRepository>(override = true) { surroundViewRepository }
    }

    @Before
    override fun setup() {
        super.setup()

        every {
            surroundViewRepository.surroundState
        } returns surroundViewRepositorySurroundStateLiveData

        lifecycleOwner = TestUtils.mockLifecycleOwner()

        fapkSettingsViewModel = FapkSettingsViewModel()
    }

    @Test
    fun should_return_exit_settings_true_if_surround_request_is_apa_rear_view() {
        var exitSettings = false
        fapkSettingsViewModel.shouldExitSettings.observe(lifecycleOwner, Observer {
            exitSettings = it
        })

        surroundViewRepositorySurroundStateLiveData.postValue(
            SurroundState(
                View.APA_REAR_VIEW,
                true
            )
        )

        assertEquals(true, exitSettings)
    }

    @Test
    fun should_return_exit_settings_false_if_surround_state_is_not_apa_rear_view() {
        var exitSettings = true
        fapkSettingsViewModel.shouldExitSettings.observe(lifecycleOwner, Observer {
            exitSettings = it
        })

        surroundViewRepositorySurroundStateLiveData.postValue(
            SurroundState(
                View.APA_FRONT_VIEW,
                true
            )
        )

        assertEquals(false, exitSettings)
    }
}