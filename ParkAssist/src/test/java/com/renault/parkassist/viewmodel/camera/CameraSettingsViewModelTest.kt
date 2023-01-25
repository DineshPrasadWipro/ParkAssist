package com.renault.parkassist.viewmodel.camera

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.SurroundViewRepository
import com.renault.parkassist.repository.surroundview.View
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class CameraSettingsViewModelTest : TestBase() {

    private val surroundRepository: SurroundViewRepository = mockk(relaxed = true)
    private lateinit var settingsViewModel: CameraSettingsViewModel
    override val koinModule = module {
        single<IExtSurroundViewRepository> { surroundRepository }
    }

    private val staticGuidelinesActivation: MutableLiveData<Boolean> = MutableLiveData()
    private val trailerGuidelinesActivation: MutableLiveData<Boolean> = MutableLiveData()
    private val autoZoomRearViewActivation: MutableLiveData<Boolean> = MutableLiveData()
    private val dynamicGuidelinesActivation: MutableLiveData<Boolean> = MutableLiveData()
    private val color: MutableLiveData<Int> = MutableLiveData()
    private val brightness: MutableLiveData<Int> = MutableLiveData()
    private val contrast: MutableLiveData<Int> = MutableLiveData()
    private val mockedSurroundState: MutableLiveData<SurroundState> = MutableLiveData()

    @Before
    override fun setup() {
        super.setup()

        every { surroundRepository.brightnessMin } returns 0
        every { surroundRepository.brightnessMax } returns 100
        every { surroundRepository.contrastMin } returns 0
        every { surroundRepository.contrastMax } returns 100
        every { surroundRepository.colorMin } returns 0
        every { surroundRepository.colorMax } returns 100

        every { surroundRepository.staticGuidelinesActivation } returns staticGuidelinesActivation
        every { surroundRepository.trailerGuidelinesActivation } returns trailerGuidelinesActivation
        every { surroundRepository.dynamicGuidelinesActivation } returns dynamicGuidelinesActivation
        every { surroundRepository.autoZoomRearViewActivation } returns autoZoomRearViewActivation
        every { surroundRepository.color } returns color
        every { surroundRepository.brightness } returns brightness
        every { surroundRepository.contrast } returns contrast
        every { surroundRepository.surroundState } returns mockedSurroundState

        settingsViewModel = CameraSettingsViewModel(mockk())
    }

    @Test
    fun `Should update static guidelines activation status when it changes`() { // ktlint-disable max-line-length
        var actualValue = false
        var expectedValue = true
        val observer = Observer<Boolean> { value -> actualValue = value }
        settingsViewModel.isStaticGuidelinesActive.observeForever(observer)
        staticGuidelinesActivation.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        expectedValue = false
        staticGuidelinesActivation.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        settingsViewModel.isStaticGuidelinesActive.removeObserver(observer)
    }

    @Test
    fun `Should update dynamic guidelines activation status when it changes`() { // ktlint-disable max-line-length
        var actualValue = false
        var expectedValue = true
        val observer = Observer<Boolean> { value ->
            actualValue = value
        }
        settingsViewModel.isDynamicGuidelinesActive.observeForever(observer)

        dynamicGuidelinesActivation.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        expectedValue = false
        dynamicGuidelinesActivation.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        settingsViewModel.isDynamicGuidelinesActive.removeObserver(observer)
    }

    @Test
    fun `Should update trailer guidelines activation status when it changes`() { // ktlint-disable max-line-length
        var actualValue = false
        var expectedValue = true
        val observer = Observer<Boolean> { value -> actualValue = value }
        settingsViewModel.isTrailerGuidelinesActive.observeForever(observer)

        trailerGuidelinesActivation.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        expectedValue = false
        trailerGuidelinesActivation.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        settingsViewModel.isTrailerGuidelinesActive.removeObserver(observer)
    }

    @Test
    fun `Should update auto-zoom activation status when it changes`() { // ktlint-disable max-line-length
        var actualValue = false
        var expectedValue = true
        val observer = Observer<Boolean> { value -> actualValue = value }
        settingsViewModel.isAutoZoomActive.observeForever(observer)

        autoZoomRearViewActivation.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        expectedValue = false
        autoZoomRearViewActivation.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        settingsViewModel.isAutoZoomActive.removeObserver(observer)
    }

    @Test
    fun `Should update color status when it changes`() { // ktlint-disable max-line-length
        var actualValue = -1
        val expectedValue = 3
        val observer = Observer<Int> { value -> actualValue = value }
        settingsViewModel.color.observeForever(observer)

        color.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        settingsViewModel.color.removeObserver(observer)
    }

    @Test
    fun `Should update brightness status when it changes`() { // ktlint-disable max-line-length
        var actualValue = -1
        val expectedValue = 3
        val observer = Observer<Int> { value -> actualValue = value }
        settingsViewModel.brightness.observeForever(observer)

        brightness.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        settingsViewModel.brightness.removeObserver(observer)
    }

    @Test
    fun `Should update contrast status when it changes`() { // ktlint-disable max-line-length
        var actualValue = -1
        val expectedValue = 3
        val observer = Observer<Int> { value -> actualValue = value }
        settingsViewModel.contrast.observeForever(observer)

        contrast.postValue(expectedValue)
        Assert.assertEquals(expectedValue, actualValue)

        settingsViewModel.contrast.removeObserver(observer)
    }

    @Test
    fun `Should request static guidelines activation`() { // ktlint-disable max-line-length
        val expectedValue = true
        settingsViewModel.setStaticGuidelines(expectedValue)
        verify { surroundRepository.setStaticGuidelinesActivation(expectedValue) }
    }

    @Test
    fun `Should request dynamic guidelines activation`() { // ktlint-disable max-line-length
        val expectedValue = true
        settingsViewModel.setDynamicGuidelines(expectedValue)
        verify { surroundRepository.setDynamicGuidelinesActivation(expectedValue) }
    }

    @Test
    fun `Should request color value`() { // ktlint-disable max-line-length
        val expectedValue = 3
        settingsViewModel.setColor(expectedValue)
        verify { surroundRepository.setColor(expectedValue) }
    }

    @Test
    fun `Should request brightness value`() { // ktlint-disable max-line-length
        val expectedValue = 3
        settingsViewModel.setBrightness(expectedValue)
        verify { surroundRepository.setBrightness(expectedValue) }
    }

    @Test
    fun `Should request contrast value`() { // ktlint-disable max-line-length
        val expectedValue = 3
        settingsViewModel.setContrast(expectedValue)
        verify { surroundRepository.setContrast(expectedValue) }
    }

    @Test
    fun `toolbar should be clickable when view state received`() {
        var toolbarEnabled = false
        settingsViewModel.toolbarEnabled.observeForever {
            toolbarEnabled = it
        }
        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, false))
        assertTrue(toolbarEnabled)

        mockedSurroundState.postValue(SurroundState(View.REAR_VIEW, true))
        assertFalse(toolbarEnabled)
    }
}