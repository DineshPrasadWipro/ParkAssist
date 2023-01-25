package com.renault.parkassist.viewmodel.sonar

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.sonar.FkpDisplayRequestType
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.sonar.SensorState
import com.renault.parkassist.repository.sonar.UpaDisplayRequestType
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

class SonarStateViewModelTest : TestBase() {

    private lateinit var sonarRepository: ISonarRepository
    private lateinit var sonarStateViewModel: SonarStateViewModelBase
    private lateinit var lifecycleOwner: LifecycleOwner
    private val upaDisplayRequest = MutableLiveData<@UpaDisplayRequestType Int>()
    private val fkpDisplayRequest = MutableLiveData<@FkpDisplayRequestType Int>()
    private val upaFrontSensor = MutableLiveData<SensorState>()
    private val upaRearSensor = MutableLiveData<SensorState>()
    private val fkpSensor = MutableLiveData<SensorState>()
    private val upaFrontLeftSensor = MutableLiveData<SensorState>()

    override val koinModule = module {
        single { sonarRepository }
    }

    @Before
    override fun setup() {
        super.setup()
        sonarRepository = mockk(relaxed = true)
        every { sonarRepository.upaDisplayRequest } returns upaDisplayRequest
        every { sonarRepository.fkpDisplayRequest } returns fkpDisplayRequest
        every { sonarRepository.frontCenter } returns upaFrontSensor
        every { sonarRepository.frontLeft } returns upaFrontLeftSensor
        every { sonarRepository.rearCenter } returns upaRearSensor
        every { sonarRepository.rightFrontCenter } returns fkpSensor
        lifecycleOwner = TestUtils.mockLifecycleOwner()
        sonarStateViewModel = SonarStateViewModel(mockk())
    }

    @Test
    fun should_map_correctly_upa_sensor_level() {
        var parkingSensor: ParkingSensor? = null
        sonarStateViewModel.frontCenter.observe(
            lifecycleOwner,
            Observer { parkingSensor = it })

        // Set request so sensors are set to be displayed
        upaDisplayRequest.postValue(UpaDisplayRequestType.FRONT)

        upaFrontSensor.postValue(SensorState(true, true, true, 0))
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
        upaFrontSensor.postValue(SensorState(true, true, true, 1))
        assertEquals(SensorLevel.VERY_FAR, parkingSensor?.level)
        upaFrontSensor.postValue(SensorState(true, true, true, 2))
        assertEquals(SensorLevel.FAR, parkingSensor?.level)
        upaFrontSensor.postValue(SensorState(true, true, true, 3))
        assertEquals(SensorLevel.MEDIUM, parkingSensor?.level)
        upaFrontSensor.postValue(SensorState(true, true, true, 4))
        assertEquals(SensorLevel.CLOSE, parkingSensor?.level)
        upaFrontSensor.postValue(SensorState(true, true, true, 5))
        assertEquals(SensorLevel.VERY_CLOSE, parkingSensor?.level)
    }

    @Test
    fun should_map_correctly_not_center_upa_sensor_level() {
        var parkingSensor: ParkingSensor? = null
        sonarStateViewModel.frontLeft.observe(
            lifecycleOwner,
            Observer { parkingSensor = it })

        // Set request so sensors are set to be displayed
        upaDisplayRequest.postValue(UpaDisplayRequestType.FRONT)

        upaFrontLeftSensor.postValue(SensorState(true, true, true, 0))
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
        upaFrontLeftSensor.postValue(SensorState(true, true, true, 1))
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
        upaFrontLeftSensor.postValue(SensorState(true, true, true, 2))
        assertEquals(SensorLevel.FAR, parkingSensor?.level)
        upaFrontLeftSensor.postValue(SensorState(true, true, true, 3))
        assertEquals(SensorLevel.MEDIUM, parkingSensor?.level)
        upaFrontLeftSensor.postValue(SensorState(true, true, true, 4))
        assertEquals(SensorLevel.CLOSE, parkingSensor?.level)
        upaFrontLeftSensor.postValue(SensorState(true, true, true, 5))
        assertEquals(SensorLevel.VERY_CLOSE, parkingSensor?.level)
    }

    @Test
    fun should_map_correctly_fkp_sensor_level() {
        var parkingSensor: ParkingSensor? = null
        sonarStateViewModel.rightFrontCenter.observe(
            lifecycleOwner,
            Observer { parkingSensor = it })

        // Set request so sensors are set to be displayed
        fkpDisplayRequest.postValue(FkpDisplayRequestType.FLANK)

        fkpSensor.postValue(SensorState(true, true, true, 0))
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
        fkpSensor.postValue(SensorState(true, true, true, 1))
        assertEquals(SensorLevel.FAR, parkingSensor?.level)
        fkpSensor.postValue(SensorState(true, true, true, 2))
        assertEquals(SensorLevel.MEDIUM, parkingSensor?.level)
        fkpSensor.postValue(SensorState(true, true, true, 3))
        assertEquals(SensorLevel.VERY_CLOSE, parkingSensor?.level)
        fkpSensor.postValue(SensorState(true, true, true, 4))
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
        fkpSensor.postValue(SensorState(true, true, true, 5))
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
    }

    @Test
    fun should_map_correctly_upa_hatched_value() {
        var parkingSensor: ParkingSensor? = null
        sonarStateViewModel.frontCenter.observe(
            lifecycleOwner,
            Observer { parkingSensor = it })

        // Set request so sensors are set to be displayed
        upaDisplayRequest.postValue(UpaDisplayRequestType.FRONT)

        upaFrontSensor.postValue(SensorState(true, true, true, 2))
        assertEquals(true, parkingSensor?.hatched)
        upaFrontSensor.postValue(SensorState(true, false, true, 2))
        assertEquals(false, parkingSensor?.hatched)
    }

    @Test
    fun should_only_display_front_sensors_when_display_request_received() {
        var parkingSensor: ParkingSensor? = null
        sonarStateViewModel.frontCenter.observe(
            lifecycleOwner,
            Observer { parkingSensor = it })

        upaFrontSensor.postValue(SensorState(true, true, true, 0))

        upaDisplayRequest.postValue(UpaDisplayRequestType.NO_DISPLAY)
        assertEquals(SensorLevel.INVISIBLE, parkingSensor?.level)
        upaDisplayRequest.postValue(UpaDisplayRequestType.FRONT)
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
        upaDisplayRequest.postValue(UpaDisplayRequestType.REAR)
        assertEquals(SensorLevel.INVISIBLE, parkingSensor?.level)
        upaDisplayRequest.postValue(UpaDisplayRequestType.REAR_FRONT)
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
    }

    @Test
    fun should_only_display_rear_sensors_when_display_request_received() {
        var parkingSensor: ParkingSensor? = null
        sonarStateViewModel.rearCenter.observe(
            lifecycleOwner,
            Observer { parkingSensor = it })

        upaRearSensor.postValue(SensorState(true, true, true, 0))

        upaDisplayRequest.postValue(UpaDisplayRequestType.NO_DISPLAY)
        assertEquals(SensorLevel.INVISIBLE, parkingSensor?.level)
        upaDisplayRequest.postValue(UpaDisplayRequestType.REAR)
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
        upaDisplayRequest.postValue(UpaDisplayRequestType.FRONT)
        assertEquals(SensorLevel.INVISIBLE, parkingSensor?.level)
        upaDisplayRequest.postValue(UpaDisplayRequestType.REAR_FRONT)
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
    }

    @Test
    fun should_only_display_fkp_sensors_when_display_request_received() {
        var parkingSensor: ParkingSensor? = null
        sonarStateViewModel.rightFrontCenter.observe(
            lifecycleOwner,
            Observer { parkingSensor = it })

        fkpSensor.postValue(SensorState(true, true, true, 0))

        fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
        assertEquals(SensorLevel.INVISIBLE, parkingSensor?.level)
        fkpDisplayRequest.postValue(FkpDisplayRequestType.FLANK)
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
    }

    @Test
    fun should_only_display_fkp_sensors_if_scanned() {
        var parkingSensor: ParkingSensor? = null
        sonarStateViewModel.rightFrontCenter.observe(
            lifecycleOwner,
            Observer { parkingSensor = it })

        fkpDisplayRequest.postValue(FkpDisplayRequestType.FLANK)

        fkpSensor.postValue(SensorState(true, true, false, 0))
        assertEquals(SensorLevel.INVISIBLE, parkingSensor?.level)
        fkpSensor.postValue(SensorState(true, true, true, 0))
        assertEquals(SensorLevel.GREYED, parkingSensor?.level)
    }
}