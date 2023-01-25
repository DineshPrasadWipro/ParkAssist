package com.renault.parkassist.ui.camera

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import com.renault.parkassist.R
import com.renault.parkassist.utils.EspressoTestUtils
import com.renault.parkassist.viewmodel.camera.CameraSettingsViewModelBase
import com.renault.parkassist.viewmodel.camera.ExtCameraViewModelBase
import com.renault.parkassist.viewmodel.camera.mock.CameraSettingsViewModelMock
import com.renault.parkassist.viewmodel.camera.mock.CameraViewModelMock
import com.renault.parkassist.viewmodel.camera.mock.ExtCameraViewModelMock
import com.renault.parkassist.viewmodel.sonar.SonarAlertsViewModelBase
import com.renault.parkassist.viewmodel.sonar.SonarFullViewModelBase
import com.renault.parkassist.viewmodel.sonar.SonarMuteStateViewModelBase
import com.renault.parkassist.viewmodel.sonar.mock.SonarAlertsViewModelMock
import com.renault.parkassist.viewmodel.sonar.mock.SonarFullViewModelMock
import com.renault.parkassist.viewmodel.sonar.mock.SonarMuteStateViewModelMock
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinComponent
import org.koin.dsl.module

class CameraOverlayFragmentTest : KoinComponent {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CameraSettingsViewModelMock

    @Before
    fun setup() {
        viewModel = spyk(CameraSettingsViewModelMock(mockk()))

        getKoin().loadModules(
            listOf(
                module {
                    viewModel<ExtCameraViewModelBase>(override = true) {
                        ExtCameraViewModelMock(mockk(), CameraViewModelMock(mockk()))
                    }
                    viewModel<CameraSettingsViewModelBase>(override = true) { viewModel }
                    viewModel<SonarAlertsViewModelBase>(override = true) {
                        SonarAlertsViewModelMock(mockk())
                    }
                    viewModel<SonarMuteStateViewModelBase>(override = true) {
                        SonarMuteStateViewModelMock(mockk())
                    }
                    viewModel<SonarFullViewModelBase>(override = true) {
                        SonarFullViewModelMock(mockk())
                    }
                }
            )
        )
    }

    @Test
    fun should_set_avm_std_overlay() { // ktlint-disable max-line-length
        launchFragmentInContainer<CameraOverlayFragment>(fragmentArgs = Bundle().apply {
            putInt(
                CameraOverlayFragment.LAYOUT_OVERLAY_ID,
                CameraOverlayFragment.LayoutOverlay.AVM_STANDARD.layoutResId
            )
        })
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_std_fragment)
    }

    @Test
    fun should_have_same_layout_after_recreate() { // ktlint-disable max-line-length
        val fragmentScenario = launchFragmentInContainer<CameraOverlayFragment>(
            fragmentArgs = Bundle().apply {
                putInt(
                    CameraOverlayFragment.LAYOUT_OVERLAY_ID,
                    CameraOverlayFragment.LayoutOverlay.AVM_STANDARD.layoutResId
                )
            })
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_std_fragment)
        fragmentScenario.recreate()
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.camera_ovl_avm_std_fragment)
    }
}