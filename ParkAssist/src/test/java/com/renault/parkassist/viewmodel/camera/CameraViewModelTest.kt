package com.renault.parkassist.viewmodel.camera

import alliance.car.sonar.AllianceCarSonarManager
import android.graphics.SurfaceTexture
import androidx.lifecycle.*
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.camera.CameraManager
import com.renault.parkassist.repository.apa.ApaRepository
import com.renault.parkassist.repository.apa.DisplayState
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.ViewMask
import com.renault.parkassist.repository.sonar.DisplayType
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.sonar.SonarRepository
import com.renault.parkassist.repository.surroundview.*
import com.renault.parkassist.repository.surroundview.View.*
import com.renault.parkassist.viewmodel.avm.Finger
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.lang.Boolean.TRUE
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlin.test.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class CameraViewModelTest : TestBase() {

    private lateinit var cameraViewModel: CameraViewModel
    private val surroundViewRepository = mockk<SurroundViewRepository>(relaxed = true)
    private val sonarRepository = mockk<SonarRepository>(relaxed = true)
    private val apaRepository = mockk<ApaRepository>(relaxed = true)
    private val mockedSurroundState = MutableLiveData<SurroundState>()
    private val mockedErrorState = MutableLiveData<Int>(ErrorState.ERROR_STATE_NO_ERROR)
    private val mockedDisplayRequest = MutableLiveData<Int>(DisplayType.NONE)
    private val mockedAutomaticManeuverState = MutableLiveData<Boolean>()
    private val mockedTrunkState = MutableLiveData<Int>()
    private val apaDisplayState = MutableLiveData<Int>(0)
    private val apaViewMask = MutableLiveData<Int>()
    private val cameraStateMachine: CameraManager = mockk(relaxed = true)
    private val sonarRepositoryRaebEnabled = MutableLiveData(java.lang.Boolean.FALSE)
    private val sonarRepositoryRaebFeaturePresent = TRUE
    private val raebAlertLevel = MutableLiveData(AllianceCarSonarManager.RAEB_NO_ALERT)

    private lateinit var lifecycleOwner: LifecycleOwner

    override val koinModule = module {
        single<IExtSurroundViewRepository>(override = true) { surroundViewRepository }
        single<ISonarRepository>(override = true) { sonarRepository }
        single<IApaRepository>(override = true) { apaRepository }
        factory { CameraIndicationMapper() }
        factory { cameraStateMachine }
    }

    @Before
    override fun setup() {
        super.setup()
        lifecycleOwner = mockLifecycleOwner()
        every { surroundViewRepository.surroundState } returns mockedSurroundState
        every { surroundViewRepository.errorState } returns mockedErrorState
        every { sonarRepository.displayRequest } returns mockedDisplayRequest
        every { apaRepository.automaticManeuver } returns mockedAutomaticManeuverState
        every { apaRepository.displayState } returns apaDisplayState
        every { apaRepository.viewMask } returns apaViewMask
        every { surroundViewRepository.trunkState } returns mockedTrunkState
        every { sonarRepository.raebAlertEnabled } returns sonarRepositoryRaebEnabled
        every { sonarRepository.raebFeaturePresent } returns sonarRepositoryRaebFeaturePresent
        every { sonarRepository.raebAlertState } returns raebAlertLevel

        cameraViewModel = CameraViewModel(mockk())
    }

    @Test
    fun `Should return cameraIndication FRONT when surroundViewRepository view  with FRONT_VIEW`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.FRONT
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                FRONT_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Should return cameraIndication FRONT when surroundViewRepository view  with PANORAMIC_FRONT_VIEW`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.FRONT
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                PANORAMIC_FRONT_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Should return cameraIndication FRONT when surroundViewRepository view  with SETTINGS_FRONT_VIEW`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.FRONT
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                SETTINGS_FRONT_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Should return cameraIndication FRONT when surroundViewRepository view  with APA_FRONT_VIEW`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.FRONT
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                APA_FRONT_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Should return cameraIndication REAR when surroundViewRepository view  with REAR_VIEW`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.REAR
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                REAR_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Should return cameraIndication REAR when surroundViewRepository view  with PANORAMIC_REAR_VIEW`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.REAR
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                PANORAMIC_REAR_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Should return cameraIndication REAR when surroundViewRepository view  with SETTINGS_REAR_VIEW`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.REAR
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                SETTINGS_REAR_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Should return cameraIndication REAR when surroundViewRepository view  with APA_REAR_VIEW`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.REAR
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                APA_REAR_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Should return cameraIndication NONE when surroundViewRepository view  different from standard or panoramic view`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.NONE
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                DEALER_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Should return cameraIndication TRAILER when surroundViewRepository view TRAILER_VIEW`() { // ktlint-disable max-line-length
        val expectedValue: Int = CameraIndication.TRAILER
        var actualValue: Int = -1
        cameraViewModel.cameraIndication.observe(
            lifecycleOwner,
            Observer { ind: Int -> actualValue = ind })
        mockedSurroundState.postValue(
            SurroundState(
                TRAILER_VIEW,
                false
            )
        )
        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Camera error should be false when surround error state changes to no error`() { // ktlint-disable max-line-length
        var cameraError = true
        cameraViewModel.cameraError.observe(
            lifecycleOwner,
            Observer { cameraError = it })
        mockedErrorState.postValue(
            ErrorState.ERROR_STATE_NO_ERROR
        )
        assertFalse(cameraError)
    }

    @Test
    fun `Should not call the repository screenPress method if on other views than 3D`() { // ktlint-disable max-line-length
        mockedSurroundState.postValue(
            SurroundState(
                FRONT_VIEW,
                false
            )
        )
        // We need an observer for the livedata to fire
        cameraViewModel.screenPress(Finger.FIRST, 12f, 10f)
        verify(exactly = 0) { surroundViewRepository.screenPress(any(), any(), any()) }
    }

    @Test
    fun `Should not call the repository screenRelease method if on other views than 3D`() { // ktlint-disable max-line-length
        mockedSurroundState.postValue(
            SurroundState(
                FRONT_VIEW,
                false
            )
        )
        // We need an observer for the livedata to fire
        cameraViewModel.screenRelease(Finger.SECOND)
        verify(exactly = 0) { surroundViewRepository.screenRelease(any()) }
    }

    @Test
    fun `Should call the repository screenRelease method when view is 3D`() {
        mockedSurroundState.value =
            SurroundState(THREE_DIMENSION_VIEW, false)
        // We need an observer for the livedata to fire

        cameraViewModel.screenRelease(Finger.SECOND)
        verify { surroundViewRepository.screenRelease(Finger.SECOND) }
    }

    @Test
    fun `Should call the repository screenPress method converting positions to percentages and reverted y-axis when view is 3D`() { // ktlint-disable max-line-length
        mockedSurroundState.value =
            SurroundState(THREE_DIMENSION_VIEW, false)
        // We need an observer for the livedata to fire
        cameraViewModel.screenPress(Finger.FIRST, 625f, 83.4f)
        // We check 90% Y-axis because it is 10% reversed
        verify { surroundViewRepository.screenPress(Finger.FIRST, 625f, 83.4f) }
        cameraViewModel.screenPress(Finger.FIRST, 50f, 90f)
        // We check 90% Y-axis because it is 10% reversed
        verify { surroundViewRepository.screenPress(Finger.FIRST, 50f, 90f) }
    }

    @Test
    fun `Camera showTailgate should be true when displaying rear camera and camera is on trunk and trunk is open`() { // ktlint-disable max-line-length
        var show = false
        cameraViewModel.showTailgateOpenedWarning.observe(lifecycleOwner, Observer { show = it })
        every { surroundViewRepository.isCameraOnTrunk } returns true
        mockedSurroundState.postValue(SurroundState(TRAILER_VIEW, false))
        mockedTrunkState.postValue(TrunkState.TRUNK_DOOR_OPENED)
        Assert.assertTrue(show)

        mockedSurroundState.postValue(SurroundState(PANORAMIC_REAR_VIEW, false))
        Assert.assertTrue(show)
    }

    @Test
    fun `Camera showTailgate should be false when NOT (displaying rear camera and camera is on trunk and trunk is open)`() { // ktlint-disable max-line-length
        var show = false
        cameraViewModel.showTailgateOpenedWarning.observe(lifecycleOwner, Observer { show = it })
        every { surroundViewRepository.isCameraOnTrunk } returns false
        mockedSurroundState.postValue(SurroundState(TRAILER_VIEW, false))
        mockedTrunkState.postValue(TrunkState.TRUNK_DOOR_OPENED)
        Assert.assertFalse(show)

        every { surroundViewRepository.isCameraOnTrunk } returns true
        mockedSurroundState.postValue(SurroundState(FRONT_VIEW, false))
        mockedTrunkState.postValue(TrunkState.TRUNK_DOOR_OPENED)
        Assert.assertFalse(show)

        every { surroundViewRepository.isCameraOnTrunk } returns true
        mockedSurroundState.postValue(SurroundState(TRAILER_VIEW, false))
        mockedTrunkState.postValue(TrunkState.TRUNK_DOOR_CLOSED)
        Assert.assertFalse(show)
    }

    @Test
    fun `Camera should be visible when camera status is displaying`() {

        val surface: SurfaceTexture = mockk(relaxed = true)
        var cameraVisible = false
        cameraViewModel.cameraVisible.observe(lifecycleOwner) {
            cameraVisible = it
        }

        cameraViewModel.provideSurface(surface)
        mockedSurroundState.postValue(SurroundState(REAR_VIEW, false))
        assertTrue(cameraVisible)

        mockedSurroundState.postValue(SurroundState(REAR_VIEW, true))
        assertFalse(cameraVisible)
    }

    @Test
    fun `Camera should always be visible when apa state is scanning or parkout`() {

        val surface: SurfaceTexture = mockk(relaxed = true)
        var cameraVisible = false
        cameraViewModel.cameraVisible.observe(lifecycleOwner) {
            cameraVisible = it
        }
        cameraViewModel.provideSurface(surface)

        apaDisplayState.value = DisplayState.DISPLAY_SCANNING

        mockedSurroundState.postValue(SurroundState(REAR_VIEW, false))
        assertTrue(cameraVisible)

        mockedSurroundState.postValue(SurroundState(REAR_VIEW, true))
        assertTrue(cameraVisible)

        apaDisplayState.value = DisplayState.DISPLAY_PARKOUT_CONFIRMATION

        mockedSurroundState.postValue(SurroundState(REAR_VIEW, false))
        assertTrue(cameraVisible)

        mockedSurroundState.postValue(SurroundState(REAR_VIEW, true))
        assertTrue(cameraVisible)
    }

    @Test
    fun camera_mask_should_be_visible_only_when_display_state_scanning_and_view_mask_requested() { // ktlint-disable max-line-length
        var cameraMaskVisible: Boolean?

        cameraViewModel.cameraMaskVisible.observe(lifecycleOwner, Observer {
            cameraMaskVisible = it
        })

        cameraMaskVisible = null

        apaDisplayState.postValue(DisplayState.DISPLAY_SCANNING)

        apaViewMask.postValue(ViewMask.REQUESTED)
        assertEquals(true, cameraMaskVisible)

        apaViewMask.postValue(ViewMask.UNAVAILABLE)
        assertEquals(false, cameraMaskVisible)

        apaDisplayState.postValue(DisplayState.DISPLAY_GUIDANCE)

        apaViewMask.postValue(ViewMask.REQUESTED)
        assertEquals(false, cameraMaskVisible)

        apaViewMask.postValue(ViewMask.UNAVAILABLE)
        assertEquals(false, cameraMaskVisible)
    }

    @Test
    fun `easy_park_picto_should_be_visible_only_when_display_state_guidance_and_maneuver_on`() {
        var pictoVisible: Boolean? = false
        cameraViewModel.easyParkIndication.observe(lifecycleOwner, Observer {
            pictoVisible = it
        })

        mockedAutomaticManeuverState.postValue(true)
        apaDisplayState.postValue(DisplayState.DISPLAY_GUIDANCE)
        assertEquals(true, pictoVisible)

        apaDisplayState.postValue(DisplayState.DISPLAY_SCANNING)
        assertEquals(false, pictoVisible)
    }
    @Test
    fun `Should return RAEB OFF visible true when surround state view is rear view and raeb disabled and raeb feature present`() { // ktlint-disable max-line-length
        var actual = false
        var expected = true
        cameraViewModel.raebOffVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        mockedSurroundState.postValue(
            SurroundState(
                REAR_VIEW,
                false
            )
        )
        sonarRepositoryRaebEnabled.postValue(java.lang.Boolean.FALSE)

        raebAlertLevel.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        Assert.assertEquals(expected, actual)

        raebAlertLevel.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB OFF visible true when surround state view is rear view and raeb not operational`() { // ktlint-disable max-line-length
        var actual = false
        var expected = true
        cameraViewModel.raebOffVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        mockedSurroundState.postValue(
            SurroundState(
                REAR_VIEW,
                false
            )
        )
        sonarRepositoryRaebEnabled.postValue(java.lang.Boolean.TRUE)
        raebAlertLevel.postValue(AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB OFF visible false when surround state view is front view`() { // ktlint-disable max-line-length
        var actual = true
        var expected = false
        cameraViewModel.raebOffVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        mockedSurroundState.postValue(
            SurroundState(
                FRONT_VIEW,
                false
            )
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB OFF visible false when surround state view is rear view and raeb enabled`() { // ktlint-disable max-line-length
        var actual = true
        var expected = false
        cameraViewModel.raebOffVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        mockedSurroundState.postValue(
            SurroundState(
                REAR_VIEW,
                false
            )
        )
        sonarRepositoryRaebEnabled.postValue(java.lang.Boolean.TRUE)

        raebAlertLevel.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        Assert.assertEquals(expected, actual)

        raebAlertLevel.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        Assert.assertEquals(expected, actual)
    }

    private fun mockLifecycleOwner(): LifecycleOwner {
        val owner = mockk<LifecycleOwner>()
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        every { owner.lifecycle } returns lifecycle
        return owner
    }
}