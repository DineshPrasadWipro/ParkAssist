package com.renault.parkassist.viewmodel.sonar

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.settings.ISoundRepository
import com.renault.parkassist.repository.sonar.*
import com.renault.parkassist.repository.surroundview.FeatureConfig
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.View
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class SonarMuteStateViewModelTest : TestBase() {

    private val sonarRepository = mockk<ISonarRepository>()
    private val soundRepository = mockk<ISoundRepository>()
    private val surroundViewRepository = mockk<IExtSurroundViewRepository>()
    private lateinit var sonarMuteStateViewModel: SonarMuteStateViewModel

    private val volume = MutableLiveData(0)
    private val enabled = MutableLiveData(false)

    private val surroundState = MutableLiveData(SurroundState())
    private var featureConfig = FeatureConfig.AVM
    private val displayType = MutableLiveData(DisplayType.NONE)
    private val upaDisplayRequestType = MutableLiveData(UpaDisplayRequestType.NO_DISPLAY)
    private val fkpDisplayRequestType = MutableLiveData(FkpDisplayRequestType.NO_DISPLAY)
    private val frontState = MutableLiveData(GroupState.DISABLED)
    private val rearState = MutableLiveData(GroupState.DISABLED)
    private val flankState = MutableLiveData(GroupState.DISABLED)

    private val frontLeftSensor = MutableLiveData(SensorState())
    private val frontCenterSensor = MutableLiveData(SensorState())
    private val frontRightSensor = MutableLiveData(SensorState())
    private val rightFrontSensor = MutableLiveData(SensorState())
    private val rightFrontCenterSensor = MutableLiveData(SensorState())
    private val rightRearCenterSensor = MutableLiveData(SensorState())
    private val rightRearSensor = MutableLiveData(SensorState())
    private val rearRightSensor = MutableLiveData(SensorState())
    private val rearCenterSensor = MutableLiveData(SensorState())
    private val rearLeftSensor = MutableLiveData(SensorState())
    private val leftRearSensor = MutableLiveData(SensorState())
    private val leftRearCenterSensor = MutableLiveData(SensorState())
    private val leftFrontCenterSensor = MutableLiveData(SensorState())
    private val leftFrontSensor = MutableLiveData(SensorState())

    private lateinit var lifecycleOwner: LifecycleOwner

    override val koinModule = module {
        single { sonarRepository }
        single { soundRepository }
        single { surroundViewRepository }
    }

    @Before
    override fun setup() {
        super.setup()
        every { soundRepository.volume } returns volume
        every { soundRepository.soundEnabled } returns enabled
        every { soundRepository.minVolume } returns 10
        every { soundRepository.maxVolume } returns 50

        every { sonarRepository.displayRequest } returns displayType
        every { sonarRepository.upaDisplayRequest } returns upaDisplayRequestType
        every { sonarRepository.fkpDisplayRequest } returns fkpDisplayRequestType
        every { sonarRepository.frontState } returns frontState
        every { sonarRepository.rearState } returns rearState
        every { sonarRepository.flankState } returns flankState

        every { sonarRepository.frontLeft } returns frontLeftSensor
        every { sonarRepository.frontCenter } returns frontCenterSensor
        every { sonarRepository.frontRight } returns frontRightSensor
        every { sonarRepository.rightFront } returns rightFrontSensor
        every { sonarRepository.rightFrontCenter } returns rightFrontCenterSensor
        every { sonarRepository.rightRearCenter } returns rightRearCenterSensor
        every { sonarRepository.rightRear } returns rightRearSensor
        every { sonarRepository.rearRight } returns rearRightSensor
        every { sonarRepository.rearCenter } returns rearCenterSensor
        every { sonarRepository.rearLeft } returns rearLeftSensor
        every { sonarRepository.leftRear } returns leftRearSensor
        every { sonarRepository.leftRearCenter } returns leftRearCenterSensor
        every { sonarRepository.leftFrontCenter } returns leftFrontCenterSensor
        every { sonarRepository.leftFront } returns leftFrontSensor

        every { surroundViewRepository.surroundState } returns surroundState
        every { surroundViewRepository.featureConfig } returns featureConfig

        lifecycleOwner = TestUtils.mockLifecycleOwner()
        sonarMuteStateViewModel = SonarMuteStateViewModel(mockk())
    }

    @Test
    fun `Should hide mute button when UPA temporary mute not active and UPA activate`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns false

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)
        surroundState.postValue(SurroundState(View.REAR_VIEW))

        frontState.postValue(GroupState.DISABLED)
        rearState.postValue(GroupState.DISABLED)
        flankState.postValue(GroupState.DISABLED)
        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(false, buttonVisible)
    }

    @Test
    fun `Should display mute button when UPA temporary mute active and sound enabled`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns true
        enabled.postValue(true)

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)
        surroundState.postValue(SurroundState(View.REAR_VIEW))

        frontState.postValue(GroupState.ENABLED)
        rearState.postValue(GroupState.ENABLED)
        flankState.postValue(GroupState.ENABLED)

        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(true, buttonVisible)
    }

    @Test
    fun `Should hide mute button when UPA temporary mute active and sound disabled`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns true
        enabled.postValue(false)

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)
        surroundState.postValue(SurroundState(View.REAR_VIEW))

        frontState.postValue(GroupState.ENABLED)
        rearState.postValue(GroupState.ENABLED)
        flankState.postValue(GroupState.ENABLED)

        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(false, buttonVisible)
    }

    @Test
    fun `Should hide mute button when UPA and FKP groups disabled`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns true
        enabled.postValue(true)

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)
        surroundState.postValue(SurroundState(View.REAR_VIEW))

        frontState.postValue(GroupState.DISABLED)
        rearState.postValue(GroupState.DISABLED)
        flankState.postValue(GroupState.DISABLED)

        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(false, buttonVisible)

        frontState.postValue(GroupState.ENABLED)
        assertEquals(true, buttonVisible)
    }

    @Test
    fun `Should only show mute button in AVM Pano front when obstacles detected`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns true
        enabled.postValue(true)

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)
        surroundState.postValue(SurroundState(View.PANORAMIC_FRONT_VIEW))

        frontState.postValue(GroupState.ENABLED)
        rearState.postValue(GroupState.ENABLED)
        flankState.postValue(GroupState.ENABLED)

        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(false, buttonVisible)

        frontCenterSensor.postValue(SensorState(true, false, true, SensorLevel.CLOSE))
        assertEquals(true, buttonVisible)
    }

    @Test
    fun `Should only show mute button in AVM Pano rear when obstacles detected`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns true
        enabled.postValue(true)

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)
        surroundState.postValue(SurroundState(View.PANORAMIC_REAR_VIEW))

        frontState.postValue(GroupState.ENABLED)
        rearState.postValue(GroupState.ENABLED)
        flankState.postValue(GroupState.ENABLED)

        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(false, buttonVisible)

        frontCenterSensor.postValue(SensorState(true, false, true, SensorLevel.CLOSE))
        assertEquals(true, buttonVisible)
    }

    @Test
    fun `Should only show mute button in AVM 3D when obstacles detected`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns true
        enabled.postValue(true)

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)
        surroundState.postValue(SurroundState(View.THREE_DIMENSION_VIEW))

        frontState.postValue(GroupState.ENABLED)
        rearState.postValue(GroupState.ENABLED)
        flankState.postValue(GroupState.ENABLED)

        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(false, buttonVisible)

        frontCenterSensor.postValue(SensorState(true, false, true, SensorLevel.CLOSE))
        assertEquals(true, buttonVisible)
    }

    @Test
    fun `Should not show mute button in AVM Pano and 3D if sensors are deactivated`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns true
        enabled.postValue(true)

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)
        surroundState.postValue(SurroundState(View.PANORAMIC_REAR_VIEW))

        frontState.postValue(GroupState.DISABLED)
        rearState.postValue(GroupState.ENABLED)
        flankState.postValue(GroupState.ENABLED)

        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(false, buttonVisible)

        // Front sensors are disabled, this should stay false
        frontCenterSensor.postValue(SensorState(true, false, true, SensorLevel.CLOSE))
        assertEquals(false, buttonVisible)

        rearCenterSensor.postValue(SensorState(true, false, true, SensorLevel.CLOSE))
        assertEquals(true, buttonVisible)
    }

    @Test
    fun `Should hide mute button switching to AVM Pano or 3D from a different view with no obstacles`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns true
        enabled.postValue(true)

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)
        surroundState.postValue(SurroundState(View.FRONT_VIEW))

        frontState.postValue(GroupState.DISABLED)
        rearState.postValue(GroupState.ENABLED)
        flankState.postValue(GroupState.ENABLED)

        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(true, buttonVisible)

        surroundState.postValue(SurroundState(View.PANORAMIC_FRONT_VIEW))
        assertEquals(false, buttonVisible)

        surroundState.postValue(SurroundState(View.THREE_DIMENSION_VIEW))
        assertEquals(false, buttonVisible)
    }

    @Test
    fun `Mute button should be displayed properly when surround feature not available`() { // ktlint-disable max-line-length
        every { soundRepository.temporaryMuteControlPresence } returns true
        enabled.postValue(false)
        featureConfig = FeatureConfig.NONE

        displayType.postValue(DisplayType.FULLSCREEN)
        upaDisplayRequestType.postValue(UpaDisplayRequestType.REAR_FRONT)

        frontState.postValue(GroupState.DISABLED)
        rearState.postValue(GroupState.ENABLED)
        flankState.postValue(GroupState.ENABLED)

        var buttonVisible: Boolean? = null
        sonarMuteStateViewModel.visible.observe(
            lifecycleOwner,
            Observer { buttonVisible = it }
        )
        assertEquals(false, buttonVisible)
        enabled.postValue(true)
        assertEquals(true, buttonVisible)
    }
}