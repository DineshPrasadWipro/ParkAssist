package com.renault.parkassist.viewmodel.sonar

import alliance.car.sonar.AllianceCarSonarManager.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.View
import io.mockk.every
import io.mockk.mockk
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import org.junit.*
import org.koin.dsl.module

@SmallTest
class SonarAlertsViewmodelTest : TestBase() {

    private val sonarRepository: ISonarRepository = mockk()
    private val surroundRepository: IExtSurroundViewRepository = mockk()
    private lateinit var sonarAlertsViewModel: SonarAlertsViewModel
    private val sonarRepositoryRctaSide = MutableLiveData(NO_ALERT)
    private val sonarRepositoryRctaLevel = MutableLiveData<Int>()
    private val raebAlertLevel = MutableLiveData(RAEB_NO_ALERT)
    private val sonarRepositoryRctaEnabled = MutableLiveData(FALSE)
    private val sonarRepositoryRaebEnabled = MutableLiveData(FALSE)
    private val surroundState = MutableLiveData(SurroundState(View.REAR_VIEW))
    private lateinit var lifecycleOwner: LifecycleOwner

    override val koinModule = module {
        single(override = true) { sonarRepository }
        single(override = true) { surroundRepository }
    }

    @Before
    override fun setup() {
        super.setup()
        every { sonarRepository.collisionAlertSide } returns sonarRepositoryRctaSide
        every { sonarRepository.collisionAlertLevel } returns sonarRepositoryRctaLevel
        every { sonarRepository.raebAlertState } returns raebAlertLevel
        every { sonarRepository.collisionAlertEnabled } returns sonarRepositoryRctaEnabled
        every { sonarRepository.raebAlertEnabled } returns sonarRepositoryRaebEnabled
        every { surroundRepository.surroundState } returns surroundState
        lifecycleOwner = TestUtils.mockLifecycleOwner()
        sonarAlertsViewModel =
            SonarAlertsViewModel(mockk())
    }

    @Test
    fun `Should return RAEB not visible false when RAEB signals no alert`() { // ktlint-disable max-line-length
        var actual = false
        var expected = false
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        raebAlertLevel.postValue(RAEB_NO_ALERT)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB not visible false when RAEB disabled`() { // ktlint-disable max-line-length
        var actual = false
        var expected = false
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        sonarRepositoryRaebEnabled.postValue(FALSE)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB visible true when RAEB enabled and signals a level 1 alert`() { // ktlint-disable max-line-length
        var actual = false
        var expected = true
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner, Observer { visible: Boolean -> actual = visible })
        raebAlertLevel.postValue(RAEB_ALERT_1)
        sonarRepositoryRaebEnabled.postValue(TRUE)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB not visible true when RAEB disabled and signals a level 1 alert`() { // ktlint-disable max-line-length
        var actual = false
        var expected = false
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        raebAlertLevel.postValue(RAEB_NO_ALERT)
        sonarRepositoryRaebEnabled.postValue(FALSE)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB visible true when RAEB enabled and signals a level 2 alert`() { // ktlint-disable max-line-length
        var actual = false
        var expected = true
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner, Observer { visible: Boolean -> actual = visible })
        raebAlertLevel.postValue(RAEB_ALERT_2)
        sonarRepositoryRaebEnabled.postValue(TRUE)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB not visible true when RAEB disabled and signals a level 2 alert`() { // ktlint-disable max-line-length
        var actual = false
        var expected = false
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        raebAlertLevel.postValue(RAEB_ALERT_2)
        sonarRepositoryRaebEnabled.postValue(FALSE)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB visible false when RAEB enabled and signals not operational alert`() { // ktlint-disable max-line-length
        var actual = true
        var expected = false
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner, Observer { visible: Boolean -> actual = visible })
        raebAlertLevel.postValue(RAEB_NOT_OPERATIONAL)
        sonarRepositoryRaebEnabled.postValue(TRUE)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB visible false when RAEB disabled and signals not operational alert`() { // ktlint-disable max-line-length
        var actual = true
        var expected = false
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        raebAlertLevel.postValue(RAEB_NOT_OPERATIONAL)
        sonarRepositoryRaebEnabled.postValue(FALSE)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB visible true when surround state view is rear view`() { // ktlint-disable max-line-length
        var actual = false
        var expected = true
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        surroundState.postValue(SurroundState(View.REAR_VIEW))
        raebAlertLevel.postValue(RAEB_ALERT_2)
        sonarRepositoryRaebEnabled.postValue(TRUE)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return RAEB visible false when surround state view is front view`() { // ktlint-disable max-line-length
        var actual = true
        var expected = false
        sonarAlertsViewModel.raebVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> actual = visible })
        raebAlertLevel.postValue(RAEB_ALERT_2)
        sonarRepositoryRaebEnabled.postValue(TRUE)
        surroundState.postValue(SurroundState(View.FRONT_VIEW))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Should return left RCTA flag visible true when RCTA enabled and signals left collision side`() { // ktlint-disable max-line-length
        var testedVisibility: Boolean = false
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        sonarRepositoryRctaSide.postValue(SIDE_LEFT_ALERT)
        sonarRepositoryRctaEnabled.postValue(true)
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return left RCTA flag not visible when RCTA disabled and signals left collision side`() { // ktlint-disable max-line-length
        var testedVisibility: Boolean = false
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        sonarRepositoryRctaSide.postValue(SIDE_LEFT_ALERT)
        sonarRepositoryRctaEnabled.postValue(false)
        Assert.assertEquals(false, testedVisibility)
    }

    @Test
    fun `Should return right RCTA flag visible true when RCTA enabled and signals right collision side`() { // ktlint-disable max-line-length
        var testedVisibility: Boolean = false
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        sonarRepositoryRctaSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepositoryRctaEnabled.postValue(true)
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return right RCTA flag not visible when RCTA disabled and signals right collision side`() { // ktlint-disable max-line-length
        var testedVisibility: Boolean = false
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        sonarRepositoryRctaSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepositoryRctaEnabled.postValue(false)
        Assert.assertEquals(false, testedVisibility)
    }

    @Test
    fun `Should return right and right RCTA flags visible true when RCTA enabled and signals left and right collision side`() { // ktlint-disable max-line-length
        var testedLeftVisibility: Boolean = false
        var testedRightVisibility: Boolean = false
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedRightVisibility = visible })
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedLeftVisibility = visible })
        sonarRepositoryRctaSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepositoryRctaEnabled.postValue(true)
        Assert.assertEquals(true, testedLeftVisibility)
        Assert.assertEquals(true, testedRightVisibility)
    }

    @Test
    fun `Should return right RCTA flag not visible when sourround view is front view`() { // ktlint-disable max-line-length
        var testedVisibility: Boolean = true
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        sonarRepositoryRctaSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepositoryRctaEnabled.postValue(true)
        listOf(
            View.FRONT_VIEW,
            View.TRAILER_VIEW,
            View.SETTINGS_FRONT_VIEW,
            View.APA_FRONT_VIEW,
            View.PANORAMIC_FRONT_VIEW,
            View.THREE_DIMENSION_VIEW
        ).forEach {
            surroundState.postValue(SurroundState(it))
            Assert.assertEquals(false, testedVisibility)
        }
    }

    @Test
    fun `Should return left RCTA flag not visible when surround view is front view`() { // ktlint-disable max-line-length
        var testedVisibility: Boolean = true
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        sonarRepositoryRctaSide.postValue(SIDE_LEFT_ALERT)
        sonarRepositoryRctaEnabled.postValue(true)
        surroundState.postValue(SurroundState(View.FRONT_VIEW))
        Assert.assertEquals(false, testedVisibility)
    }

    @Test
    fun `Should return right and left RCTA flags not visible when surround view is front view`() { // ktlint-disable max-line-length
        var testedLeftVisibility: Boolean = true
        var testedRightVisibility: Boolean = true
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedRightVisibility = visible })
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedLeftVisibility = visible })
        sonarRepositoryRctaSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepositoryRctaEnabled.postValue(true)
        sonarRepositoryRctaEnabled.postValue(true)
        surroundState.postValue(SurroundState(View.FRONT_VIEW))
        Assert.assertEquals(false, testedLeftVisibility)
        Assert.assertEquals(false, testedRightVisibility)
    }

    @Test
    fun `Should return left and right RCTA flags not visible when RCTA disabled and signals left and right collision side`() { // ktlint-disable max-line-length
        var testedLeftVisibility: Boolean = false
        var testedRightVisibility: Boolean = false
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedRightVisibility = visible })
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedLeftVisibility = visible })
        sonarRepositoryRctaSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepositoryRctaEnabled.postValue(false)
        Assert.assertEquals(false, testedLeftVisibility)
        Assert.assertEquals(false, testedRightVisibility)
    }

    @Test
    fun `Should return right and right RCTA flags not visible when RCTA signals no collision side`() { // ktlint-disable max-line-length
        var testedLeftVisibility: Boolean = true
        var testedRightVisibility: Boolean = true
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedRightVisibility = visible })
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedLeftVisibility = visible })
        sonarRepositoryRctaSide.postValue(NO_ALERT)
        Assert.assertEquals(false, testedLeftVisibility)
        Assert.assertEquals(false, testedRightVisibility)
    }

    @Test
    fun `Should return right and right RCTA flags not visible when RCTA disabled`() { // ktlint-disable max-line-length
        var testedLeftVisibility: Boolean = true
        var testedRightVisibility: Boolean = true
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedRightVisibility = visible })
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedLeftVisibility = visible })
        sonarRepositoryRctaEnabled.postValue(false)
        Assert.assertEquals(false, testedLeftVisibility)
        Assert.assertEquals(false, testedRightVisibility)
    }

    @Test
    fun `Should return right and right RCTA flags not visible when RCTA enabled and signals unexpected collision side`() { // ktlint-disable max-line-length
        var testedLeftVisibility: Boolean = true
        var testedRightVisibility: Boolean = true
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedRightVisibility = visible })
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedLeftVisibility = visible })
        sonarRepositoryRctaSide.postValue(98745)
        sonarRepositoryRctaEnabled.postValue(true)
        Assert.assertEquals(false, testedLeftVisibility)
        Assert.assertEquals(false, testedRightVisibility)
    }

    @Test
    fun `Should return right and right RCTA flags not visible when RCTA disabled and signals unexpected collision side`() { // ktlint-disable max-line-length
        var testedLeftVisibility: Boolean = true
        var testedRightVisibility: Boolean = true
        sonarAlertsViewModel.rctaRightVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedRightVisibility = visible })
        sonarAlertsViewModel.rctaLeftVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedLeftVisibility = visible })
        sonarRepositoryRctaSide.postValue(98745)
        sonarRepositoryRctaEnabled.postValue(false)
        Assert.assertEquals(false, testedLeftVisibility)
        Assert.assertEquals(false, testedRightVisibility)
    }
}