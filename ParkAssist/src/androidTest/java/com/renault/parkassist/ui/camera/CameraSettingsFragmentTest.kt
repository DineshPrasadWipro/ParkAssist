package com.renault.parkassist.ui.camera

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.renault.parkassist.R
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.camera.EvsSurfaceTexture
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.dynamicLinesButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.staticLinesButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.trailerButtonTag
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment.Companion.zoomAutoButtonTag
import com.renault.parkassist.utils.EspressoTestUtils.assertRenaultSliderValue
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagIsChecked
import com.renault.parkassist.utils.EspressoTestUtils.assertViewWithTagIsNotSelected
import com.renault.parkassist.utils.EspressoTestUtils.setCheck
import com.renault.parkassist.utils.EspressoTestUtils.setPercentageProgress
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
import io.mockk.verify
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@MediumTest
class CameraSettingsFragmentTest : OverlayActivityTest(){

    companion object {
        private const val DELIGHTFUL_TIME_MS = 50L
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CameraSettingsViewModelMock

    @Before
    fun setup() {
        launchFullScreen()
        viewModel = spyk(CameraSettingsViewModelMock(mockk()))
        getKoin().loadModules(
            listOf(
                module {
                    viewModel<CameraSettingsViewModelBase>(override = true) { viewModel }
                    viewModel<SonarAlertsViewModelBase>(override = true) {
                        SonarAlertsViewModelMock(mockk())
                    }
                    viewModel<SonarMuteStateViewModelBase>(override = true) {
                        SonarMuteStateViewModelMock(mockk())
                    }
                    viewModel<ExtCameraViewModelBase>(override = true) {
                        ExtCameraViewModelMock(mockk(), CameraViewModelMock(mockk()))
                    }
                    viewModel<SonarFullViewModelBase>(override = true) {
                        SonarFullViewModelMock(mockk())
                    }
                    single<EvsSurfaceTexture>(override = true) { EvsSurfaceTexture() }
                }
            )
        )
        navigateFullscreen(R.id.avmSettings)
    }

    @Test
    fun should_toggle_fixed_guidelines_state_when_switch_button_clicked() { // ktlint-disable max-line-length
        viewModel.isStaticGuidelinesActive.postValue(false)
        assertViewWithTagIsNotSelected(staticLinesButtonTag)
        onView(withTagValue(`is`(staticLinesButtonTag))).perform(setCheck(true))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { viewModel.setStaticGuidelines(true) }

        viewModel.isStaticGuidelinesActive.postValue(true)
        assertViewWithTagIsChecked(staticLinesButtonTag)
        onView(withTagValue(`is`(staticLinesButtonTag))).perform(setCheck(false))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { viewModel.setStaticGuidelines(false) }
    }

    @Test
    fun should_toggle_dynamic_guidelines_state_when_switch_button_clicked() { // ktlint-disable max-line-length
        viewModel.isDynamicGuidelinesActive.postValue(false)
        assertViewWithTagIsNotSelected(dynamicLinesButtonTag)
        onView(withTagValue(`is`(dynamicLinesButtonTag))).perform(setCheck(true))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { viewModel.setDynamicGuidelines(true) }

        viewModel.isDynamicGuidelinesActive.postValue(true)
        assertViewWithTagIsChecked(dynamicLinesButtonTag)
        onView(withTagValue(`is`(dynamicLinesButtonTag))).perform(setCheck(false))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { viewModel.setDynamicGuidelines(false) }
    }

    @Test
    fun should_toggle_trailer_guidelines_state_when_switch_button_clicked() { // ktlint-disable max-line-length
        viewModel.isTrailerGuidelinesActive.postValue(false)
        assertViewWithTagIsNotSelected(trailerButtonTag)
        onView(withTagValue(`is`(trailerButtonTag))).perform(setCheck(true))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { viewModel.setTrailerGuidelines(true) }

        viewModel.isTrailerGuidelinesActive.postValue(true)
        assertViewWithTagIsChecked(trailerButtonTag)
        onView(withTagValue(`is`(trailerButtonTag))).perform(setCheck(false))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { viewModel.setTrailerGuidelines(false) }
    }

    @Test
    fun should_toggle_autozoom_state_when_switch_button_clicked() { // ktlint-disable max-line-length
        viewModel.isAutoZoomActive.postValue(false)
        assertViewWithTagIsNotSelected(zoomAutoButtonTag)
        onView(withTagValue(`is`(zoomAutoButtonTag))).perform(setCheck(true))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { viewModel.setAutoZoom(true) }

        viewModel.isAutoZoomActive.postValue(true)
        assertViewWithTagIsChecked(zoomAutoButtonTag)
        onView(withTagValue(`is`(zoomAutoButtonTag))).perform(setCheck(false))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        verify { viewModel.setAutoZoom(false) }
    }

    @Test
    fun should_move_color_seekbar_to_10_when_color_viewmodel_is_set_to_10() { // ktlint-disable max-line-length
        viewModel.color.postValue(10)
        assertRenaultSliderValue(R.id.hue_seek_bar, 10)
    }

    @Test
    fun should_move_color_viewmodel_value_to_10_when_color_seekbar_clicked_on_10() { // ktlint-disable max-line-length
        setPercentageSeekBar(R.id.hue_seek_bar, 0)
        setPercentageSeekBar(R.id.hue_seek_bar, 10)
        verify { viewModel.setColor(10) }
    }

    @Test
    fun should_move_color_seekbar_to_20_when_color_viewmodel_is_set_to_20() { // ktlint-disable max-line-length
        viewModel.color.postValue(20)
        assertRenaultSliderValue(R.id.hue_seek_bar, 20)
    }

    @Test
    fun should_move_color_viewmodel_value_to_20_when_color_seekbar_clicked_on_20() { // ktlint-disable max-line-length
        setPercentageSeekBar(R.id.hue_seek_bar, 20)
        verify { viewModel.setColor(20) }
    }

    @Test
    fun should_move_brightness_seekbar_to_40_when_brightness_viewmodel_is_set_to_40() { // ktlint-disable max-line-length
        viewModel.brightness.postValue(40)
        assertRenaultSliderValue(R.id.luminosity_seek_bar, 40)
    }

    @Test
    fun should_move_brightness_viewmodel_value_to_40_when_brightness_seekbar_clicked_on_40() { // ktlint-disable max-line-length
        setPercentageSeekBar(R.id.luminosity_seek_bar, 0)
        setPercentageSeekBar(R.id.luminosity_seek_bar, 40)
        verify { viewModel.setBrightness(40) }
    }

    @Test
    fun should_move_brightness_seekbar_to_80_when_brightness_viewmodel_is_set_to_80() { // ktlint-disable max-line-length
        viewModel.brightness.postValue(80)
        assertRenaultSliderValue(R.id.luminosity_seek_bar, 80)
    }

    @Test
    fun should_move_brightness_viewmodel_value_to_80_when_brightness_seekbar_clicked_on_80() { // ktlint-disable max-line-length
        setPercentageSeekBar(R.id.luminosity_seek_bar, 0)
        setPercentageSeekBar(R.id.luminosity_seek_bar, 80)
        verify { viewModel.setBrightness(80) }
    }

    @Test
    fun should_move_contrast_seekbar_to_0_when_contrast_viewmodel_is_set_to_0() { // ktlint-disable max-line-length
        viewModel.contrast.postValue(0)
        assertRenaultSliderValue(R.id.contrast_seek_bar, 0)
    }

    @Test
    fun should_move_contrast_viewmodel_value_to_0_when_contrast_seekbar_clicked_on_0() { // ktlint-disable max-line-length
        setPercentageSeekBar(R.id.contrast_seek_bar, 100)
        setPercentageSeekBar(R.id.contrast_seek_bar, 0)
        verify { viewModel.setContrast(0) }
    }

    @Test
    fun should_move_contrast_seekbar_to_100_when_contrast_viewmodel_is_set_to_100() { // ktlint-disable max-line-length
        viewModel.contrast.postValue(100)
        assertRenaultSliderValue(R.id.contrast_seek_bar, 100)
    }

    @Test
    fun should_move_contrast_viewmodel_value_to_100_when_contrast_seekbar_clicked_on_100() { // ktlint-disable max-line-length
        setPercentageSeekBar(R.id.contrast_seek_bar, 100)
        verify { viewModel.setContrast(100) }
    }

    private fun setPercentageSeekBar(id: Int, progress: Int) {
        onView(withId(id)).perform(setPercentageProgress(progress))
        Thread.sleep(SEEKBAR_DEBOUNCE_DURATION + DELIGHTFUL_TIME_MS)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    }
}